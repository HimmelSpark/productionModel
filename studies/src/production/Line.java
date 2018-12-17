package production;

import details.Detail;

/**
 * Данный класс представляет линию производства
 */
public class Line {

    private boolean killFlag = false;

    private Detail detail;

    /**
     * Будет передан в поток для генерации деталей
     */
    private Runnable runner;

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public boolean isKillFlag() {
        return killFlag;
    }

    public void setKillFlag(boolean killFlag) {
        this.killFlag = killFlag;
    }

    public void setRunner(Runnable runner) {
        this.runner = runner;
    }

    public Runnable getRunner() {
        return runner;
    }

    public void run() {
        this.runner.run();
    }
}
