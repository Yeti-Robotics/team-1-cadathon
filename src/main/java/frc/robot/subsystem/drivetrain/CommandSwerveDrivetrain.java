package frc.robot.subsystem.drivetrain;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants;

public class CommandSwerveDrivetrain extends SwerveDrivetrain {
    public static final double SUPPLY_CURRENT_LIMIT = 60;
    public static final boolean SUPPLY_CURRENT_LIMIT_ENABLE = true;
    public static final double SUPPLY_CURRENT_LIMIT_CURRENT_THRESHOLD = 65;
    public static final double SUPPLY_CURRENT_LIMIT_TIME_THRESHOLD = 0.1;

    public static final double PEAK_FORWARD_VOLTAGE = 12.0;
    public static final double PEAK_REVERSE_VOLTAGE = -12.0;

    public CommandSwerveDrivetrain(SwerveDrivetrainConstants driveTrainConstants, SwerveModuleConstants... modules) {
        super(driveTrainConstants, modules);
        setElectricalLimits();
    }

    private void setElectricalLimits() {
        CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs();
        VoltageConfigs voltageConfigs = new VoltageConfigs();

        currentLimitsConfigs.SupplyCurrentLimit = SUPPLY_CURRENT_LIMIT;
        currentLimitsConfigs.SupplyCurrentLimitEnable = SUPPLY_CURRENT_LIMIT_ENABLE;
        currentLimitsConfigs.SupplyCurrentThreshold = SUPPLY_CURRENT_LIMIT_CURRENT_THRESHOLD;
        currentLimitsConfigs.SupplyTimeThreshold = SUPPLY_CURRENT_LIMIT_TIME_THRESHOLD;

        voltageConfigs.PeakForwardVoltage = PEAK_FORWARD_VOLTAGE;
        voltageConfigs.PeakReverseVoltage = PEAK_REVERSE_VOLTAGE;

        for (SwerveModule module : Modules) {
            TalonFXConfigurator driveConfig = module.getDriveMotor().getConfigurator();
            TalonFXConfigurator azimuthConfig = module.getSteerMotor().getConfigurator();

            driveConfig.apply(currentLimitsConfigs);
            azimuthConfig.apply(currentLimitsConfigs);
            driveConfig.apply(voltageConfigs);
            azimuthConfig.apply(voltageConfigs);
        }
    }
}
