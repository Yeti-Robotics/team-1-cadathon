package frc.robot.subsystem.elevator;


import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class ElevatorSubsystem extends SubsystemBase {
    private static final int LEFT_ELEVATOR_MOTOR_ID = 2;
    private static final int RIGHT_ELEVATOR_MOTOR_ID = 10;

    private final TalonFX leftElevatorMotor = new TalonFX(LEFT_ELEVATOR_MOTOR_ID, Constants.CANIVORE_BUS);
    private final TalonFX rightElevatorMotor = new TalonFX(RIGHT_ELEVATOR_MOTOR_ID, Constants.CANIVORE_BUS);

    private static final int LEFT_CANCODER_ID = 2;
    private static final int RIGHT_CANCODER_ID = 10;
    private static final double LEFT_CANCODER_MAGNET_OFFSET = 0.42;
    private static final double RIGHT_CANCODER_MAGNET_OFFSET = 0.48;

    private static final Slot0Configs slot0Configs = new Slot0Configs()
            .withKP(45)
            .withKI(0)
            .withKD(0)
            .withKG(0)
            .withKA(0.05)
            .withGravityType(GravityTypeValue.Elevator_Static);

    private static final CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs()
            .withSupplyCurrentLimitEnable(true)
            .withSupplyCurrentThreshold(65)
            .withSupplyCurrentLimit(75)
            .withSupplyTimeThreshold(1)
            .withStatorCurrentLimitEnable(true)
            .withStatorCurrentLimit(75);

    private static final MotionMagicConfigs motionMagicConfigs = new MotionMagicConfigs()
            .withMotionMagicExpo_kV(0.2)
            .withMotionMagicExpo_kA(0.5);

    private final CANcoder leftCANcoder = new CANcoder(LEFT_CANCODER_ID, Constants.CANIVORE_BUS);
    private final CANcoder rightCANcoder = new CANcoder(RIGHT_CANCODER_ID, Constants.CANIVORE_BUS);

    public ElevatorSubsystem() {
        TalonFXConfigurator leftMotorConfigurator = leftElevatorMotor.getConfigurator();
        TalonFXConfigurator rightMotorConfigurator = rightElevatorMotor.getConfigurator();

        TalonFXConfiguration leftTalonFXConfiguration = new TalonFXConfiguration();

        leftTalonFXConfiguration.Feedback.FeedbackRemoteSensorID = LEFT_CANCODER_ID;
        leftTalonFXConfiguration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
        leftTalonFXConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        leftTalonFXConfiguration.FutureProofConfigs = true;
        leftTalonFXConfiguration.Feedback.SensorToMechanismRatio = 1;
        leftTalonFXConfiguration.Feedback.RotorToSensorRatio = 1;
        leftTalonFXConfiguration.CurrentLimits = currentLimitsConfigs;
        leftTalonFXConfiguration.Slot0 = slot0Configs;
        leftTalonFXConfiguration.MotionMagic = motionMagicConfigs;

        leftMotorConfigurator.apply(leftTalonFXConfiguration);
        rightMotorConfigurator.apply(leftTalonFXConfiguration);

        rightElevatorMotor.setControl(new Follower(LEFT_ELEVATOR_MOTOR_ID, true));

        MagnetSensorConfigs leftMagnetConfigs = new MagnetSensorConfigs();
        MagnetSensorConfigs rightMagnetConfigs = new MagnetSensorConfigs();

        leftMagnetConfigs.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        leftMagnetConfigs.MagnetOffset = LEFT_CANCODER_MAGNET_OFFSET;
        leftMagnetConfigs.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;

        rightMagnetConfigs.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        rightMagnetConfigs.MagnetOffset = RIGHT_CANCODER_MAGNET_OFFSET;
        rightMagnetConfigs.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;

        leftCANcoder.getConfigurator().apply(leftMagnetConfigs);
        rightCANcoder.getConfigurator().apply(rightMagnetConfigs);
    }
}
