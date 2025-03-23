
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Mediateur<T extends Individu<?> ,R> implements Runnable{
    private static final Logger log = Logger.getLogger(Mediateur.class.getName());

    static {
        try{
            LogManager.getLogManager().readConfiguration(new FileInputStream("genericConfig.properties"));
        }catch (IOException exception){
            log.log(Level.SEVERE,"Cannot read config file",exception);
        }
    }
    private int longueurDeMot;
    private int nbIndividuParGeneration;
    private long dureeTraitement;

    private Generateur<T> gs;
    private Generation<T> g;
    private Selecteur<T> selecteurIndividu;
    private Entrecroiseur<T> entrecroiseur;
    private Population<Generation<T>> population =  new Population<Generation<T>>();
    private LocalDateTime startAt;

    private Supplier<R> fonctionGeneratriceMotMystereAleatoire;
    private Supplier<T> fonctionGeneratriceIndividuAleatoire;
    private BiConsumer<T,R> fonctionEvaluatriceIndividu;
    private Evaluateur<T,R> evaluateurIndividu;
    private BiConsumer<T,Integer> fonctionSelectriceIndividu;
    private BiFunction<T,T,List<T>> fonctionFabriqueDeDescendants;
    
    public void setFontionGeneratriceMotMystereAleatoire(Supplier<R> fonctionGeneratriceMotMystereAleatoire) {
        this.fonctionGeneratriceMotMystereAleatoire = fonctionGeneratriceMotMystereAleatoire;
    }

    public void setFonctionGeneratriceIndividuAleatoire(Supplier<T> fonctionGeneratriceIndividuAleatoire) {
        this.fonctionGeneratriceIndividuAleatoire = fonctionGeneratriceIndividuAleatoire;
    }

    public void setFonctionEvaluatriceIndividu(BiConsumer<T, R> fonctionEvaluatriceIndividu) {
        this.fonctionEvaluatriceIndividu = fonctionEvaluatriceIndividu;
    }

    public void setEvaluateurIndividu(Evaluateur<T, R> evaluateurIndividu) {
        this.evaluateurIndividu = evaluateurIndividu;
    }

    public void setFonctionSelectriceIndividu(BiConsumer<T, Integer> fonctionSelectriceIndividu) {
        this.fonctionSelectriceIndividu = fonctionSelectriceIndividu;
    }

    public void setFonctionFabriqueDeDescendants(BiFunction<T, T, List<T>> fonctionFabriqueDeDescendants) {
        this.fonctionFabriqueDeDescendants = fonctionFabriqueDeDescendants;
    }

    private void init(){
        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("genericConfig.properties"));
            this.longueurDeMot = Integer.valueOf(properties.getProperty("longueurDeMot"));
            this.nbIndividuParGeneration = Integer.valueOf(properties.getProperty("nbIndividuParGeneration"));
            this.dureeTraitement = Integer.valueOf(properties.getProperty("dureeTraitement"));
        }catch(IOException e){
            if (log.isLoggable(Level.SEVERE)){
                log.log(Level.SEVERE, "Erreur a l'execution", e.getMessage());
                log.log(Level.SEVERE,"Cause:",e.getCause());
            }
            throw new RuntimeException(e);
        }
        this.selecteurIndividu = new Selecteur<T>(fonctionSelectriceIndividu,0);
        this.entrecroiseur = new Entrecroiseur<T>(fonctionFabriqueDeDescendants);
        this.gs = new Generateur<T>(fonctionGeneratriceIndividuAleatoire);
        this.startAt = LocalDateTime.now();
    }
    public Mediateur (
        Supplier<R> fonctionGeneratriceMotMystereAleatoire,
        Supplier<T> fonctionGeneratriceIndividuAleatoire,
        BiConsumer<T,R> fonctionEvaluatriceIndividu,
        Evaluateur<T,R> evaluateurIndividu,
        BiConsumer<T,Integer> fonctionSelectriceIndividu,
        BiFunction<T,T,List<T>> fonctionFabriqueDeDescendants
    ){
        setFontionGeneratriceMotMystereAleatoire(fonctionGeneratriceMotMystereAleatoire);
        setFonctionGeneratriceIndividuAleatoire(fonctionGeneratriceIndividuAleatoire);
        setFonctionEvaluatriceIndividu(fonctionEvaluatriceIndividu);
        setEvaluateurIndividu(evaluateurIndividu);
        setFonctionSelectriceIndividu(fonctionSelectriceIndividu);
        setFonctionFabriqueDeDescendants(fonctionFabriqueDeDescendants);
    }
    
    public void run(){
        init();
        g = gs.getGeneration(nbIndividuParGeneration);

        for(T individu : g.getIndividus()){
            evaluateurIndividu.evaluer(individu);
        }
        if (log.isLoggable(Level.FINE)){
            log.log(Level.FINE,"Generation initial evaluée");
            g.getIndividus().forEach(System.out::println);
        }

        while(Duration.between(startAt,LocalDateTime.now()).toMinutes() < dureeTraitement && !g.containsSolution() && !population.isPopulationDecroit()){
            if(log.isLoggable(Level.INFO)){
                log.log(Level.INFO,"Generation {0} en cours de traitement : " + g.getNumero());

                if(log.isLoggable(Level.FINE)){
                    log.log(Level.FINE,"Duree de traitement : {0} minutes",Duration.between(startAt,LocalDateTime.now()).toMinutes());
                    log.log(Level.FINE, "MotMystere : {0}", evaluateurIndividu.getMotMystere());
                    log.log(Level.FINE,"score cumulé : {0}",g.getSumFitness());
                    log.log(Level.FINE,"score de selection : {0}",g.getFitnessSelection());
                }
                log.log(Level.INFO,"Meilleur solution : {0}",g.getBestIndividu());
            }


            selecteurIndividu.setSeuil(g.getFitnessSelection());
            for(T individu : g.getIndividus()){
                selecteurIndividu.selectionner(individu);
            }

            g.removeNotSelectedIndividus();

            g = entrecroiseur.getGeneration(g);

            if(g.nbIndividus() < nbIndividuParGeneration){
                g.addGroupIndividus(gs.getGeneration(50 - g.nbIndividus()).getIndividus());
            }

            for(T individu : g.getIndividus()){
                evaluateurIndividu.evaluer(individu);
            }

            population.recenser(g);

            if (log.isLoggable(Level.INFO)){
                log.log(Level.INFO,"END");
                log.log(Level.INFO,"le mot mystere est : {0}",evaluateurIndividu.getMotMystere());
                log.log(Level.INFO,"la meilleure solution est : {0}",g.getBestIndividu());
                log.log(Level.INFO,"Population decroit ?: {0}",population.isPopulationDecroit());
                log.log(Level.INFO,"Temps ecoulé : {0} minutes",Duration.between(startAt,LocalDateTime.now()).toMinutes());
            }
        }
    }

}