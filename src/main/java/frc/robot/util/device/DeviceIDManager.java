package frc.robot.util.device;

import java.util.HashSet;

public class DeviceIDManager {
    private static final HashSet<String> devices = new HashSet<>();
    private static final String SEPARATOR = " ";

    private DeviceIDManager() {
        throw new AssertionError("Device ID manager cannot be instantiated");
    }

    // note that swerve drivetrain doesn't need validation because it uses a separate can bus
    public static void validateDeviceID(int deviceID, String bus) {
        String el = bus + SEPARATOR + deviceID;

        if (devices.contains(el)) {
            throw new IllegalArgumentException("Device ID " + deviceID + " is already in use in bus " + bus);
        }

        devices.add(el);
    }
}
