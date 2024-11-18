package frc.robot.subsystem.drivetrain;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.mechanisms.swerve.*;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.util.Loggable;
import jakarta.inject.Singleton;

import java.util.function.Supplier;

@Singleton
public class CommandSwerveDrivetrain extends SwerveDrivetrain implements Subsystem {
    public static final double SUPPLY_CURRENT_LIMIT = 60;
    public static final boolean SUPPLY_CURRENT_LIMIT_ENABLE = true;
    public static final double SUPPLY_CURRENT_LIMIT_CURRENT_THRESHOLD = 65;
    public static final double SUPPLY_CURRENT_LIMIT_TIME_THRESHOLD = 0.1;

    public static final double PEAK_FORWARD_VOLTAGE = 12.0;
    public static final double PEAK_REVERSE_VOLTAGE = -12.0;

    private final SwerveRequest.ApplyChassisSpeeds chassisSpeedRequest = new SwerveRequest.ApplyChassisSpeeds();
    private final DrivetrainLogging logging = new DrivetrainLogging();

    public CommandSwerveDrivetrain(SwerveDrivetrainConstants driveTrainConstants, SwerveModuleConstants... modules) {
        super(driveTrainConstants, modules);
        setElectricalLimits();
        logging.initLog();
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

    public Command runRequest(Supplier<SwerveRequest> requestSupplier) {
        return run(() -> setControl(requestSupplier.get()));
    }

    public void setSpeeds(ChassisSpeeds chassisSpeeds) {
        setControl(chassisSpeedRequest.withSpeeds(chassisSpeeds));
    }

    public SwerveDrivePoseEstimator getPoseEstimator() {
        return m_odometry;
    }

    public ChassisSpeeds getChassisSpeeds() {
        return m_kinematics.toChassisSpeeds(getState().ModuleStates);
    }
    private static final double kSimLoopPeriod = 0.005; // 5 ms
    private Notifier m_simNotifier = null;
    private double m_lastSimTime;

    @Override
    public void periodic() {
        Pose2d robotPose = this.getState().Pose;
        logging.updatePose(robotPose);
    }

    @Override
    public void simulationPeriodic() {
        m_lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        m_simNotifier = new Notifier(() -> {
            final double currentTime = Utils.getCurrentTimeSeconds();
            double deltaTime = currentTime - m_lastSimTime;
            m_lastSimTime = currentTime;

            /* use the measured time delta, get battery voltage from WPILib */
            updateSimState(deltaTime, RobotController.getBatteryVoltage());
        });
        m_simNotifier.startPeriodic(kSimLoopPeriod);
    }
}
