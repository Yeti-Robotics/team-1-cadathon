package frc.robot.di;

import dagger.Module;
import dagger.Provides;
import frc.robot.auto.AutonomousManager;
import frc.robot.subsystem.drivetrain.CommandSwerveDrivetrain;
import frc.robot.subsystem.drivetrain.TunerConstants;
import frc.robot.subsystem.elevator.ElevatorSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.roller.RollerSubsystem;
import frc.robot.subsystem.vision.limelight.LimelightSystem;
import jakarta.inject.Singleton;

@Module
public class RobotSystems {
    @Provides
    @Singleton
    LimelightSystem provideLimelightSystem(CommandSwerveDrivetrain commandSwerveDrivetrain) {
        return new LimelightSystem(commandSwerveDrivetrain);
    }

    @Provides
    @Singleton
    IntakeSubsystem provideIntakeSystem(LimelightSystem limelightSystem) {
        return new IntakeSubsystem(limelightSystem);
    }

    @Provides
    @Singleton
    ElevatorSubsystem provideElevatorSubsystem() {
        return new ElevatorSubsystem();
    }

    @Provides
    @Singleton
    RollerSubsystem provideRollerSubsystem(LimelightSystem limelightSystem) {
        return new RollerSubsystem(limelightSystem);
    }

    @Provides
    @Singleton
    CommandSwerveDrivetrain provideDrivetrainSystem() {
        return TunerConstants.DriveTrain;
    }

    @Provides
    @Singleton
    AutonomousManager provideAutoManager(
            CommandSwerveDrivetrain commandSwerveDrivetrain,
            IntakeSubsystem intakeSubsystem,
            ElevatorSubsystem elevatorSubsystem
    ) {
        return new AutonomousManager(commandSwerveDrivetrain, intakeSubsystem, elevatorSubsystem);
    }
}
