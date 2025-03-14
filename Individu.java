
import java.io.Serializable;

public interface Individu <T> extends Serializable{
    T getValue();
    void setValue(T value);
    void setFitness(double fitness);
    Integer getFitness();
    boolean isSolution();
    boolean isSelected();
    boolean isChild();
}