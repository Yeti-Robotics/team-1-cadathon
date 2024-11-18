package frc.robot.subsystem.intake;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

class IntakeConfig {
    public static final int INTAKE_MOTOR_FRONT_ID = 5;
    public static final int INTAKE_BEAM_BREAK_ID = 2;

    public static final double DEFAULT_SUCTION_SPEED = 500; //in rps, how fast to move velocity motors
    public static final double DEFAULT_HOOP_SPEED = 200;

    public static final String INTAKE_SPEED_LOG_NAME = "IntakeSpeed";

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

    public static final TalonFXConfiguration intakeMotorConfig = new TalonFXConfiguration()
            .withMotorOutput(motorOutputConfigs)
            .withSlot0(slot0Configs)
            .withCurrentLimits(currentLimitsConfigs)
            .withMotionMagic(motionMagicConfigs);
}
