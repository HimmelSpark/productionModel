package details;

/**
 * Деталь типа А
 */
public class DetailA extends Detail {

    final private double dT = 168; // 2.8 минут;

    final private double delayMin = 5; // секунд

    final private double delayMax = 15; // секунд

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
