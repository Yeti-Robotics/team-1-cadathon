package frc.robot.subsystem.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class IntakeSubsystem extends SubsystemBase {
    private static final int INTAKE_MOTOR_FRONT_ID = 5;
    private static final int INTAKE_MOTOR_REAR_ID = 6;
    private static final int INTAKE_BEAM_BREAK_ID = 0;

    private final TalonFX intakeMotorFront = new TalonFX(INTAKE_MOTOR_FRONT_ID, Constants.CANIVORE_BUS);
    private final TalonFX intakeMotorRear = new TalonFX(INTAKE_MOTOR_REAR_ID, Constants.CANIVORE_BUS);

    private final DigitalInput intakeBeamBreak = new DigitalInput(INTAKE_BEAM_BREAK_ID);

    public IntakeSubsystem() {
        TalonFXConfigurator intakeMotorFrontConfig = intakeMotorFront.getConfigurator();
        TalonFXConfiguration intakeMotorConfigs = new TalonFXConfiguration();

        intakeMotorConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        intakeMotorConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        intakeMotorConfigs.FutureProofConfigs = true;

        intakeMotorFrontConfig.apply(intakeMotorConfigs);
        intakeMotorRear.setControl(new Follower(INTAKE_MOTOR_FRONT_ID, true));
    }

    private Command roll(double speed) {
        return runEnd(() -> intakeMotorFront.set(speed), () -> intakeMotorRear.set(0));
    }

    public Command rollIn(double speed) {
        return roll(Math.abs(speed));
    }

    public Command rollOut(double speed) {
        return roll(-Math.abs(speed));
    }
}


