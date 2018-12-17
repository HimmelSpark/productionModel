package production;

/**
 * Данный класс представляет позицию в конвейере сборки деталей
 */
public class Position {

    public static enum DetailType { A, B, C }


    private DetailType previousDetailType = null;

    private int positionNumber;


    private Runnable runner;

    public Position(int positionNumber) {
        this.positionNumber = positionNumber;
    }

    public int getPositionNumber() {
        return positionNumber;
    }
    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }

    public Runnable getRunner() {
        return runner;
    }
    public void setRunner(Runnable runner) {
        this.runner = runner;
    }

    public DetailType getPreviousDetailType() {
        return previousDetailType;
    }
    public void setPreviousDetailType(DetailType previousDetailType) {
        this.previousDetailType = previousDetailType;
    }
}
