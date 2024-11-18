package frc.robot.subsystem.roller;

import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.subsystem.drivetrain.CommandSwerveDrivetrain;
import frc.robot.subsystem.vision.limelight.LimelightSystem;
import frc.robot.util.device.Motor;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.function.Supplier;

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
    private final RollerLogging rollerLogging = new RollerLogging();

    private final LimelightSystem limelightSystem;
    private final CommandSwerveDrivetrain drivetrain;

    @Inject
    public RollerSubsystem(LimelightSystem limelightSystem, CommandSwerveDrivetrain drivetrain) {
        this.limelightSystem = limelightSystem;
        this.drivetrain = drivetrain;
        rollerLogging.initLog();
    }

    public Command suck() {
        return runEnd(() -> lowerRoller.setControl(lowRollerRequest), lowerRoller::stopMotor);
    }

    public Command dump() {
        return runEnd(() -> highRoller.setControl(highRollerRequest), lowerRoller::stopMotor);
    }

    public Command visionAlignDump(Supplier<Double> xVel, Supplier<Double> yVel) {
        SwerveRequest.FieldCentricFacingAngle facingAngle = new SwerveRequest.FieldCentricFacingAngle();

        return runEnd(() -> {
            Optional<Pose2d> tagPose = limelightSystem.getTagHeading(13);
            Pose2d currentPose = tagPose.orElse(drivetrain.getState().Pose);

            drivetrain.setControl(facingAngle.withTargetDirection(
                    currentPose.relativeTo(DUNK_TANK).getRotation()
            ).withVelocityX(xVel.get() * 1.6).withVelocityY(yVel.get() * 1.6));
        }, highRoller::stopMotor);
    }

    @Override
    public void periodic() {
        rollerLogging.update(lowerRoller.getVelocity().refresh().getValue(), highRoller.getVelocity().refresh().getValue());
    }
}
