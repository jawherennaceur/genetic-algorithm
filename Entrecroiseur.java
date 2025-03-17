import java.util.function.BiFunction;

public class Entrecroiseur<T extends Individu<?>> {
    private BiFunction<T, T, Liste<T>> facbriqueDescsendants;

    public Generation<T> getGeneration(Generation<T> generationParent){
        Generation<T> nouvelleGeneration = new Generation<T>(generationParent.getNumero()+1);
        if(generationParent.nbIndivdu() < genertaionParent.getNumero)
    }

}
