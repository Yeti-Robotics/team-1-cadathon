package frc.robot.subsystem.elevator;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Logger;

import static frc.robot.subsystem.elevator.ElevatorConfig.ELEVATOR_POSITION_LOG_NAME;

public class ElevatorLogging implements Logger {

    private final DoublePublisher elevatorPositionPublisher = NetworkTableInstance.getDefault()
            .getDoubleTopic(ELEVATOR_POSITION_LOG_NAME).publish();

    @Override
    public void initLog() {
        SmartDashboard.putNumber(ELEVATOR_POSITION_LOG_NAME, 0);
    }

    public void update(double pos) {
        SmartDashboard.putNumber(ELEVATOR_POSITION_LOG_NAME, pos);
        elevatorPositionPublisher.set(pos);
    }
}
