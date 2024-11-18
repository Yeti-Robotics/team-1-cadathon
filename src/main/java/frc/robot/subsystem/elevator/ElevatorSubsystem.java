package frc.robot.subsystem.elevator;


import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.Constants;
import frc.robot.util.device.Encoder;
import frc.robot.util.device.Motor;
import frc.robot.util.device.Switch;
import jakarta.inject.Singleton;

import static frc.robot.subsystem.elevator.ElevatorConfig.*;

@Singleton
public class ElevatorSubsystem extends SubsystemBase {
    private final CANcoder leftCANCoder = Encoder.configure(LEFT_CANCODER_ID, Constants.CANIVORE_BUS)
            .extend(config -> config.MagnetSensor.MagnetOffset = LEFT_CANCODER_MAGNET_OFFSET)
            .syncConfigs()
            .getDevice();

    private final CANcoder rightCANCoder = Encoder.configure(RIGHT_CANCODER_ID, Constants.CANIVORE_BUS)
            .extend(config -> config.MagnetSensor.MagnetOffset = RIGHT_CANCODER_MAGNET_OFFSET)
            .syncConfigs()
            .getDevice();

    private final DigitalInput elevatorLimitSwitch = Switch.digitalInput(ELEVATOR_BOTTOM_SWITCH_ID);

    private final TalonFX leftElevatorMotor = Motor.configure(LEFT_ELEVATOR_MOTOR_ID, Constants.CANIVORE_BUS)
            .using(elevatorTalonConfig)
            .withCANCoder(leftCANCoder)
            .syncConfigs()
            .getDevice();

    private final TalonFX rightElevatorMotor = Motor.configure(RIGHT_ELEVATOR_MOTOR_ID, Constants.CANIVORE_BUS)
            .using(elevatorTalonConfig)
            .withCANCoder(rightCANCoder)
            .follow(leftElevatorMotor)
            .syncConfigs()
            .getDevice();

    private final MotionMagicVoltage motionMagicVoltageReq = new MotionMagicVoltage(ElevatorPosition.HOME.getTarget())
            .withLimitReverseMotion(getLimitSwitch());


    public ElevatorSubsystem() {
        new Trigger(this::getLimitSwitch).onTrue(toPosition(ElevatorPosition.HOVER));
    }

    private boolean getLimitSwitch() {
        return elevatorLimitSwitch.get();
    }

    private void setElevatorToPosition(ElevatorPosition position) {
        leftElevatorMotor.setControl(motionMagicVoltageReq.withPosition(position.getTarget()));
    }

    public Command toPosition(ElevatorPosition position) {
        return runOnce(() -> setElevatorToPosition(position));
    }
}
