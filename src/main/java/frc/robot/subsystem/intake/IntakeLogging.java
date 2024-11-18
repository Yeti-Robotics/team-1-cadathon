package frc.robot.subsystem.intake;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Logger;

import static frc.robot.subsystem.intake.IntakeConfig.INTAKE_SPEED_LOG_NAME;

public class IntakeLogging implements Logger {
    private final DoublePublisher intakeSpeedPublisher = NetworkTableInstance.getDefault()
            .getDoubleTopic(INTAKE_SPEED_LOG_NAME).publish();

    @Override
    public void initLog() {
        SmartDashboard.putNumber(INTAKE_SPEED_LOG_NAME, 0);
    }

    public void update(double pos) {
        SmartDashboard.putNumber(INTAKE_SPEED_LOG_NAME, pos);
        intakeSpeedPublisher.set(pos);
    }
}
