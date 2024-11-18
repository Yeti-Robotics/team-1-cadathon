package frc.robot.subsystem.elevator;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

class ElevatorConfig {
    public static final int LEFT_ELEVATOR_MOTOR_ID = 2;
    public static final int RIGHT_ELEVATOR_MOTOR_ID = 12;
    public static final int LEFT_CANCODER_ID = 7;
    public static final int RIGHT_CANCODER_ID = 10;
    public static final double LEFT_CANCODER_MAGNET_OFFSET = 0.42;
    public static final double RIGHT_CANCODER_MAGNET_OFFSET = 0.48;
    public static final int ELEVATOR_BOTTOM_SWITCH_ID = 0;

    public static final String ELEVATOR_POSITION_LOG_NAME = "ElevatorPosition";

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

    private static final HardwareLimitSwitchConfigs hardwareLimitSwitchConfigs = new HardwareLimitSwitchConfigs()
            .withReverseLimitEnable(true)
            .withReverseLimitAutosetPositionEnable(true)
            .withReverseLimitAutosetPositionValue(ElevatorPosition.HOVER.getTarget());

    public static final TalonFXConfiguration elevatorTalonConfig = new TalonFXConfiguration()
            .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake))
            .withFeedback(new FeedbackConfigs()
                    .withSensorToMechanismRatio(1)
                    .withRotorToSensorRatio(1)
            )
            .withCurrentLimits(currentLimitsConfigs)
            .withSlot0(slot0Configs)
            .withHardwareLimitSwitch(hardwareLimitSwitchConfigs)
            .withMotionMagic(motionMagicConfigs);
}
