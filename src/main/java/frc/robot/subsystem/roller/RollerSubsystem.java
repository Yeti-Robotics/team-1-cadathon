package frc.robot.subsystem.roller;

import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.subsystem.vision.limelight.LimelightSystem;
import frc.robot.util.device.Motor;
import jakarta.inject.Singleton;

import static frc.robot.subsystem.roller.RollerConfig.*;

@Singleton
public class RollerSubsystem extends SubsystemBase {
    private final TalonFX lowerRoller = Motor.configure(ROLLER_LOWER_MOTOR_ID, Constants.CANIVORE_BUS)
            .using(rollerMotorConfig)
            .getDevice();

    private final TalonFX highRoller = Motor.configure(ROLLER_HIGHER_MOTOR_ID, Constants.CANIVORE_BUS)
            .using(rollerMotorConfig)
            .getDevice();

    private final MotionMagicVelocityVoltage lowRollerRequest = new MotionMagicVelocityVoltage(DEFAULT_SUCTION_SPEED);
    private final MotionMagicVelocityVoltage highRollerRequest = new MotionMagicVelocityVoltage(DEFAULT_OUTPUT_SPEED);

    private final LimelightSystem limelightSystem;

    public RollerSubsystem(LimelightSystem limelightSystem) {
        this.limelightSystem = limelightSystem;
    }

    public Command suck() {
        return runEnd(() -> lowerRoller.setControl(lowRollerRequest), lowerRoller::stopMotor);
    }

    public Command dump() {
        return runEnd(() -> highRoller.setControl(highRollerRequest), lowerRoller::stopMotor);
    }

    public Command visionAlignDump() {
        return runOnce(() -> {});
    }
}
