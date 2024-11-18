package frc.robot.subsystem.roller;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Logger;

import static frc.robot.subsystem.roller.RollerConfig.DEPOSIT_SPEED_LOG_NAME;
import static frc.robot.subsystem.roller.RollerConfig.SUCTION_SPEED_LOG_NAME;

public class RollerLogging implements Logger {
    private final DoublePublisher lowRollerPublisher = NetworkTableInstance.getDefault()
            .getDoubleTopic(SUCTION_SPEED_LOG_NAME).publish();
    private final DoublePublisher highRollerPublisher = NetworkTableInstance.getDefault()
            .getDoubleTopic(DEPOSIT_SPEED_LOG_NAME).publish();

    @Override
    public void initLog() {
        SmartDashboard.putNumber(SUCTION_SPEED_LOG_NAME, 0);
        SmartDashboard.putNumber(DEPOSIT_SPEED_LOG_NAME, 0);
    }

    public void update(double low, double high) {
        SmartDashboard.putNumber(SUCTION_SPEED_LOG_NAME, low);
        lowRollerPublisher.set(low);
        SmartDashboard.putNumber(DEPOSIT_SPEED_LOG_NAME, high);
        highRollerPublisher.set(high);
    }
}
