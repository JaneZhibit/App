package panels.upgrData;

public enum Broom {
    BASIC("Обычная", 0, 0.025, 0.35, 0, 35),
    NIMBUS("Нимбус 2000", 5000, 0.045, 0.45, 0, 45),
    FIREBOLT("Молния", 15000, 0.055, 0.5, 1, 50);

    public final String name;
    public final int cost;
    public final double angleStep;
    public final double maxAngle;
    public final int baseSpeed;
    public final double speedMultiplier;

    Broom(String name, int cost, double angleStep, double maxAngle, int baseSpeed, double speedMultiplier) {
        this.name = name;
        this.cost = cost;
        this.angleStep = angleStep;
        this.maxAngle = maxAngle;
        this.baseSpeed = baseSpeed;
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public String toString() {
        return name + " (" + cost + " очков)";
    }
}