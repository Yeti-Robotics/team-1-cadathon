package frc.robot.subsystem.elevator;

public enum ElevatorPosition {
    HOME(0),
    HOVER(1),
    LOW_GOAL(5),
    MIDDLE_GOAL(10),
    HIGH_GOAL(15);


    private final double target;

    ElevatorPosition(double target) {
        this.target = target;
    }

    public double getTarget() {
        return target;
    }
}
