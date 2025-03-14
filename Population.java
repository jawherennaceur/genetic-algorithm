
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Population<T extends Generation<?>> implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<T> generations = new ArrayList<T>();
    private NavigableMap<Integer, Integer> recensement = new TreeMap<Integer, Integer>();

    public void addGeneration(T generation) {
        this.generations.add(generation);
    }

    public int nbGenaerations() {
        return this.generations.size();
    }

    public void recenser(T generation) {
        this.recensement.put(generation.getNumero(), generation.getSumFitness());
    }

    public boolean isPopulationDecroit() {
        int nbGenerationDecroissante = 0;
        if (recensement.size() < 10)
            for (int i = 0; i < 5; i++) {
                if (recensement.descendingMap().get(i) < recensement.descendingMap().get(i + 1)) {
                    nbGenerationDecroissante++;
                }
            }
        return (nbGenerationDecroissante > 5);
    }
}
