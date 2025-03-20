

public class Mediateur<T extends Individu<?> ,R> implements Runnable{
    private static final LogManager LogManager = LogManager.getLogManager();
    static {
        try{
            LogManager.readConfiguration( new FileInputStream("genericConfig.properties"));
        }
    }

}