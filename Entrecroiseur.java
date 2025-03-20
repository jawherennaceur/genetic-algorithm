import java.util.function.BiFunction;
import java.util.Comparator;
import java.util.List;

public class Entrecroiseur<T extends Individu<?>> {
    private BiFunction<T, T, List<T>> facbriqueDescsendants;

    public Generation<T> getGeneration(Generation<T> generationParent) {
        Generation<T> nouvelleGeneration = new Generation<T>(generationParent.getNumero() + 1);
        if (generationParent.nbIndividus() < generationParent.getNumero() + 1) {
            nouvelleGeneration.addGroupIndividus(generationParent.getIndividus());
        }
        generationParent.getIndividus().sort(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.getFitness() - o2.getFitness();
            }
            
        });
        int index =0;
        while(nouvelleGeneration.nbIndividus() < 50 && index < generationParent.nbIndividus() -1 ){
            System.out.println("Croisement individu:" + "index" + generationParent.getIndividus().get(index));
            System.out.println("Avec individu "+ index + generationParent.getIndividus().get(index +1 ));
            nouvelleGeneration.addGroupIndividus(facbriqueDescsendants.apply(generationParent.getIndividus().get(index),generationParent.getIndividus().get(index+1)));     
            index +=2;        
            }
            return nouvelleGeneration;
    }
}
