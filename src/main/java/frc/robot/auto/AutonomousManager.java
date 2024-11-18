package frc.robot.auto;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystem.drivetrain.CommandSwerveDrivetrain;
import frc.robot.subsystem.drivetrain.TunerConstants;
import frc.robot.subsystem.elevator.ElevatorPosition;
import frc.robot.subsystem.elevator.ElevatorSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AutonomousManager {

    private final IntakeSubsystem intakeSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;

    @Inject
    public AutonomousManager(CommandSwerveDrivetrain drivetrain, IntakeSubsystem intakeSubsystem, ElevatorSubsystem elevatorSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        this.elevatorSubsystem = elevatorSubsystem;

        configureNamedCommands();

        AutoBuilder.configureHolonomic(
                () -> drivetrain.getState().Pose,
                drivetrain::seedFieldRelative,
                drivetrain::getChassisSpeeds,
                (drivetrain::setSpeeds),
                new HolonomicPathFollowerConfig(
                        new PIDConstants(10, 0, 0),
                        new PIDConstants(10, 0, 0),
                        TunerConstants.kSpeedAt12VoltsMps,
                        0
                        , new ReplanningConfig()
                ),
                () -> DriverStation.getAlliance().filter(value -> value == DriverStation.Alliance.Red).isPresent(),
                drivetrain
        );
    }

    public void configureNamedCommands() {
        NamedCommands.registerCommand("intakeBall", intakeSubsystem.intake());
        NamedCommands.registerCommand("depositBall", intakeSubsystem.deposit());
        NamedCommands.registerCommand("elevatorLow", elevatorSubsystem.toPosition(ElevatorPosition.LOW_GOAL));
        NamedCommands.registerCommand("elevatorMiddle", elevatorSubsystem.toPosition(ElevatorPosition.MIDDLE_GOAL));
        NamedCommands.registerCommand("elevatorHigh", elevatorSubsystem.toPosition(ElevatorPosition.HIGH_GOAL));
        NamedCommands.registerCommand("elevatorDown", elevatorSubsystem.toPosition(ElevatorPosition.HOME));
    }
}
