package frc.robot.util.device;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

public class Motor extends DeviceBuilder<TalonFX, TalonFXConfiguration, Motor> {
    private Motor(TalonFX motor) {
        super(motor);
    }

    protected TalonFXConfiguration getDefaultConfig() {
        return new TalonFXConfiguration();
    }

    public static Motor configure(int deviceID, String canbus) {
        DeviceIDManager.validateDeviceID(deviceID, canbus);
        return new Motor(new TalonFX(deviceID, canbus));
    }

    public Motor withCANCoder(CANcoder canCoder) {
        getConfig().Feedback.withFusedCANcoder(canCoder);
        return this;
    }

    @Override
    public Motor syncConfigs() {
        getDevice().getConfigurator().apply(getConfig());
        return this;
    }

    public Motor follow(TalonFX master) {
        return followWithRequest(new Follower(master.getDeviceID(), false));
    }

    public Motor oppose(TalonFX master) {
        return followWithRequest(new Follower(master.getDeviceID(), true));
    }

    public Motor followWithRequest(ControlRequest req) {
        getDevice().setControl(req);
        return this;
    }

    @Override
    protected Motor getDeviceBuilderClass() {
        return this;
    }
}
