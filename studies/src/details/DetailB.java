package details;

/**
 * Деталь типа В
 */
public class DetailB extends Detail {

    final private double dT = 84; //1.4 минут

    final private double delayMin = 5; // секунд

    final private double delayMax = 20;// секунд

    private int position = 0;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public double generateDelay() {
        return dT + Math.abs(Math.random() * (delayMax + delayMin) - delayMin);
    }
}
