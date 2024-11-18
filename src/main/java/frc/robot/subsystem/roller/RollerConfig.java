package frc.robot.subsystem.roller;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class RollerConfig {
    public static int ROLLER_LOWER_MOTOR_ID = 8;
    public static int ROLLER_HIGHER_MOTOR_ID = 9;

    public static double DEFAULT_SUCTION_SPEED = 600;
    public static double DEFAULT_OUTPUT_SPEED = 400;

    private static final MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs()
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.Clockwise_Positive);

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
            .withMotionMagicExpo_kA(0.5)
            .withMotionMagicAcceleration(400)
            .withMotionMagicJerk(200);

    public static final TalonFXConfiguration rollerMotorConfig = new TalonFXConfiguration()
            .withMotorOutput(motorOutputConfigs)
            .withSlot0(slot0Configs)
            .withCurrentLimits(currentLimitsConfigs)
            .withMotionMagic(motionMagicConfigs);

}
