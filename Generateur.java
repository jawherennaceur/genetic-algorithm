import java.util.function.Supplier;

public class Generateur<T extends Individu<?>> {
    private Supplier<T> fabriqueIndividu;

    // All-args constructor
    public Generateur(Supplier<T> fabriqueIndividu) {
        this.fabriqueIndividu = fabriqueIndividu;
    }

    // Setter method
    public void setFabriqueIndividu(Supplier<T> fabriqueIndividu) {
        this.fabriqueIndividu = fabriqueIndividu;
    }

    public Generation<T> getGeneration(int nbIndividu) {
        Generation<T> generationInitiale = new Generation<T>(0);
        for (int i = 0; i < nbIndividu; i++) {
            generationInitiale.addIndividu(fabriqueIndividu.get());
        }
        return generationInitiale;
    }
}
