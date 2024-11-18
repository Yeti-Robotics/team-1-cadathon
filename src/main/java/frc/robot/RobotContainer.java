// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.auto.AutonomousManager;
import frc.robot.constants.Constants;
import frc.robot.subsystem.drivetrain.CommandSwerveDrivetrain;
import frc.robot.subsystem.drivetrain.TunerConstants;
import frc.robot.subsystem.elevator.ElevatorPosition;
import frc.robot.subsystem.elevator.ElevatorSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.roller.RollerSubsystem;
import frc.robot.subsystem.vision.limelight.LimelightSystem;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
@Singleton
public class RobotContainer {

    private final CommandXboxController xboxController;

    private final ElevatorSubsystem elevatorSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    private final RollerSubsystem rollerSubsystem;
    private final LimelightSystem limelightSubsystem;
    private final CommandSwerveDrivetrain commandSwerveDrivetrain;
    private final AutonomousManager autonomousManager;
    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    @Inject
    public RobotContainer(ElevatorSubsystem elevatorSubsystem, IntakeSubsystem intakeSubsystem, RollerSubsystem rollerSubsystem, LimelightSystem limelightSubsystem, CommandSwerveDrivetrain commandSwerveDrivetrain, AutonomousManager autonomousManager) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.rollerSubsystem = rollerSubsystem;
        this.limelightSubsystem = limelightSubsystem;
        this.commandSwerveDrivetrain = commandSwerveDrivetrain;
        this.autonomousManager = autonomousManager;
        this.xboxController = new CommandXboxController(Constants.XBOX_CONTROLLER_PORT);
        configureBindings();
    }


    /**
     * Use this method to define your trigger->command mappings. Triggers can be created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
     * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */
    public Command successfulIntakeRumble() {
        return Commands.startEnd( //Starts the command and waits for ending condition
                        () -> xboxController.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 1.0), //Set both rumble motors to max
                        () -> xboxController.getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0.0)) // Turn off rumble
                .raceWith(Commands.waitSeconds(0.5)); //Condition to end method
    }

    private void configureBindings() {
        xboxController.rightBumper().whileTrue(intakeSubsystem.intake().andThen(successfulIntakeRumble()));
        xboxController.leftBumper().whileTrue(intakeSubsystem.deposit());

        xboxController.x().whileTrue(rollerSubsystem.suck());
        xboxController.y().whileTrue(rollerSubsystem.dump());

        xboxController.povUpLeft().onTrue(elevatorSubsystem.toPosition(ElevatorPosition.LOW_GOAL));
        xboxController.povUpRight().onTrue(elevatorSubsystem.toPosition(ElevatorPosition.HIGH_GOAL));
        xboxController.povUp().onTrue(elevatorSubsystem.toPosition(ElevatorPosition.MIDDLE_GOAL));

        commandSwerveDrivetrain.setDefaultCommand(
                commandSwerveDrivetrain.runRequest( () -> new SwerveRequest.FieldCentric()
                        .withVelocityX(-xboxController.getLeftY() * TunerConstants.kSpeedAt12VoltsMps)
                        .withVelocityY(-xboxController.getLeftY() * TunerConstants.kSpeedAt12VoltsMps)
                        .withRotationalRate(-xboxController.getRightY() * TunerConstants.kSpeedAt12VoltsMps * TunerConstants.kAngularRate))
        );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return null;
    }
}
