import java.util.function.BiConsumer;

public class Selecteur <T extends Individu<?>> {
    private BiConsumer<T,Integer> selecteurIndividu;

    private Integer seuil;
    public Selecteur(BiConsumer<T,Integer> fonctionSelectriceIndividu, int i) {
        this.selecteurIndividu = fonctionSelectriceIndividu;
        this.seuil = i;
    }
    public void selectionner(T individu){
        this.selecteurIndividu.accept(individu ,this.seuil);
    }
    public void setSeuil(Integer fitnessSelection) {
        this.seuil = fitnessSelection;
    }
    
}
