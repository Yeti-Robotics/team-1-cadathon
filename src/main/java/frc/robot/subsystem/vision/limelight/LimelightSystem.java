package frc.robot.subsystem.vision.limelight;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystem.drivetrain.CommandSwerveDrivetrain;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import static frc.robot.subsystem.vision.limelight.LimelightConfig.LIMELIGHT_NAME;

@Singleton
public class LimelightSystem extends SubsystemBase {

    private final CommandSwerveDrivetrain drivetrain;

    @Inject
    public LimelightSystem(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public Command blink() {
        return runOnce(() ->
            LimelightHelpers.setLEDMode_ForceBlink(LIMELIGHT_NAME)
        ).andThen(Commands.waitSeconds(4)).andThen(runOnce(() ->
            LimelightHelpers.setLEDMode_PipelineControl(LIMELIGHT_NAME)
        ));
    }

    @Override
    public void periodic() {
        double gyroRate = drivetrain.getPigeon2().getRate();
        LimelightHelpers.SetRobotOrientation(LIMELIGHT_NAME, drivetrain.getPoseEstimator().getEstimatedPosition().getRotation().getDegrees(), gyroRate, 0, 0, 0, 0);
        LimelightHelpers.PoseEstimate poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(LIMELIGHT_NAME);

        if (poseEstimate != null && !((Math.abs(gyroRate) > 720) || poseEstimate.tagCount == 0)) {
            drivetrain.addVisionMeasurement(poseEstimate.pose, poseEstimate.timestampSeconds);
        }
    }
}
