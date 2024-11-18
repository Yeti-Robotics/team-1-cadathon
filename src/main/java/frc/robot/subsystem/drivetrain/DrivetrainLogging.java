package frc.robot.subsystem.drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivetrainLogging {

    private final Field2d field2d = new Field2d();
    private final StructPublisher<Pose2d> robotPose = NetworkTableInstance.getDefault()
            .getStructTopic("Robot Pose", Pose2d.struct).publish();

    public void initLog() {
        SmartDashboard.putData("Field", field2d);
    }

    public void updatePose(Pose2d pose) {
        field2d.setRobotPose(pose);
        robotPose.set(pose);
    }
}
