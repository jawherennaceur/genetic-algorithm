import java.util.function.BiConsumer;

public class Selecteur <T extends Individu<?>> {
    private BiConsumer<T,Integer> selecteurIndividu;

    private Integer seuil;
    public void selectionner(T individu){
        this.selecteurIndividu.accept(individu ,this.seuil);
    }
    
}
