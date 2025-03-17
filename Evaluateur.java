import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("hiding")
public class Evaluateur<Individu,V> {
    private Supplier<V> fabriqueMotMystere;
    private V motMystere;

    private BiConsumer<Individu,V> evaluateurIndividu;

    public void setFabriqueMotMystere(Supplier<V> fabriqueMotMystere) {
        this.fabriqueMotMystere = fabriqueMotMystere;
    }

    public void setEvaluateurIndividu(BiConsumer<Individu, V> evaluateurIndividu) {
        this.evaluateurIndividu = evaluateurIndividu;
    }
    public Evaluateur(Supplier<V> fabriqueMotMyster,BiConsumer<Individu,V> evaluateurIndividu){
        this.motMystere = fabriqueMotMystere.get();
        setFabriqueMotMystere(fabriqueMotMystere);
        setEvaluateurIndividu(evaluateurIndividu);
    }
    public void evaluer(Individu individu){
        this.evaluateurIndividu.accept(individu,motMystere);
    }
}
