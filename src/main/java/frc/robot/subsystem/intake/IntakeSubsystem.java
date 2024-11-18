package frc.robot.subsystem.intake;

import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.Constants;
import frc.robot.subsystem.vision.limelight.LimelightSystem;
import frc.robot.util.device.Motor;
import frc.robot.util.device.Switch;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import static frc.robot.subsystem.intake.IntakeConfig.*;

@Singleton
public class IntakeSubsystem extends SubsystemBase {
    private final TalonFX intakeMotor = Motor
            .configure(INTAKE_MOTOR_FRONT_ID, Constants.CANIVORE_BUS)
            .using(intakeMotorConfig)
            .syncConfigs()
            .getDevice();

    private final DigitalInput intakeBeamBreak = Switch.digitalInput(INTAKE_BEAM_BREAK_ID);
    private final MotionMagicVelocityVoltage intakeMotorRequest = new MotionMagicVelocityVoltage(DEFAULT_SUCTION_SPEED);

    private final LimelightSystem limelightSystem;

    @Inject
    public IntakeSubsystem(LimelightSystem limelightSystem) {
        this.limelightSystem = limelightSystem;

        new Trigger(this::getIntakeBeamBreak).onTrue(limelightSystem.blink());
    }

    private void runIntake(boolean reversed) {
        intakeMotorRequest.withVelocity(reversed ? DEFAULT_SUCTION_SPEED : -DEFAULT_HOOP_SPEED);
        intakeMotor.setControl(intakeMotorRequest);
    }

    private boolean getIntakeBeamBreak() {
        return intakeBeamBreak.get();
    }

    public Command intake() {
        return runEnd(() -> runIntake(false), intakeMotor::stopMotor)
                .until(this::getIntakeBeamBreak);
    }

    public Command deposit() {
        return runEnd(() -> runIntake(true), intakeMotor::stopMotor)
                .until(() -> !getIntakeBeamBreak());
    }
}


