package frc.robot.subsystem.drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Logger;

public class DrivetrainLogging implements Logger {
    private final Field2d field2d = new Field2d();
    private final StructPublisher<Pose2d> robotPose = NetworkTableInstance.getDefault()
            .getStructTopic("Robot Pose", Pose2d.struct).publish();

    private final StructArrayPublisher<SwerveModuleState> moduleStatesPublisher =  NetworkTableInstance.getDefault()
            .getStructArrayTopic("Module States", SwerveModuleState.struct).publish();

    public void initLog() {
        SmartDashboard.putData("Field", field2d);
    }

    public void update(Pose2d pose, SwerveModuleState[] state) {
        field2d.setRobotPose(pose);
        robotPose.set(pose);
        moduleStatesPublisher.set(state);
    }
}
