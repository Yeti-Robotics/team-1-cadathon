package frc.robot.util.device;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.constants.Constants;

public class Switch {
    public static DigitalInput digitalInput(int port) {
        DeviceIDManager.validateDeviceID(port, Constants.DIGITAL_PORT);
        return new DigitalInput(port);
    }
}
