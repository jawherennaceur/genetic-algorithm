import java.io.Serializable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Generation<T extends Individu<?>> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final LogManager logManager = LogManager.getLogManager();

    static {
        try {
            logManager.readConfiguration(new FileInputStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e);
        }
    }
    private List<T> individus = new ArrayList<T>();
    private Integer numero;
    public Generation(int numero) {
        this.numero = numero;
    }
    public Integer getNumero() {
        return this.numero;
    }

    public void addIndividu(T individu) {
        this.individus.add(individu);
    }

    public void addGroupIndividus(List<T> individus) {
        if(!individus.isEmpty() || ! individus.contains(null))
        this.individus.addAll(individus);
    }
    public void removeIndividu(T individu) {
        this.individus.remove(individu);
    }
    public Integer nbIndividus(){
        return this.individus.size();
    }
    public void removeNotSelectedIndividus() {
        List<T> individus = new ArrayList<T>();
        for (T individu : this.individus) {
            if (! individu.isSelected()) {
                individus.remove(individu);
            }
        }
    }
    public Integer getSumFitness(){
        int sum =0;
        for(T individu : this.individus){
            sum += individu.getFitness();
        }
        return sum;
    }



private static final Logger logger = Logger.getLogger(Generation.class.getName());


    public Integer getFitnessSelection(){
        int maxFitness = 0;
        for (T individu : this.individus) {
            if (individu.isSelected() && individu.getFitness() > maxFitness) 
                maxFitness = (int) individu.getFitness();
            }
        int[] repartitionFitness = new int[maxFitness + 1];
        for (T individu : this.individus) {
            repartitionFitness[individu.getFitness()]++;
            }
        int nbIndividus =0;
        int sommeponderee = 0;

        for(int i =1;i< repartitionFitness.length;i++){
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Répartition :");
                nbIndividus += repartitionFitness[i];
                sommeponderee += repartitionFitness[i] * i +1;
            }
        }
        return (int) sommeponderee / nbIndividus;
    }

    public boolean containsSolution() {
        for (T individu : this.individus) {
            if (individu.isSolution()) {
                return true;
            }
        }
        return false;
    }
    
    public T getBestIndividu() {
        T bestIndividu = null;
        for (T individu : this.individus) {
            if (bestIndividu == null || individu.getFitness() > bestIndividu.getFitness()) {
                bestIndividu = individu;
            }
        }
        return bestIndividu;
    }
}
