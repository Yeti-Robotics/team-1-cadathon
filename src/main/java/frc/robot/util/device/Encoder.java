package frc.robot.util.device;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.robot.constants.Constants;

public class Encoder extends DeviceBuilder<CANcoder, CANcoderConfiguration, Encoder> {
    private Encoder(CANcoder device) {
        super(device);
    }

    public static Encoder configure(int deviceID, String canBus) {
        DeviceIDManager.validateDeviceID(deviceID, canBus);
        return new Encoder(new CANcoder(deviceID, canBus));
    }

    @Override
    public Encoder syncConfigs() {
        getDevice().getConfigurator().apply(getConfig());
        return this;
    }

    protected CANcoderConfiguration getDefaultConfig() {
        return Constants.BASE_CANCODER_CONFIGS;
    }

    @Override
    protected Encoder getDeviceBuilderClass() {
        return this;
    }
}
