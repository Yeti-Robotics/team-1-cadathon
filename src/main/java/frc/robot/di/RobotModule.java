package frc.robot.di;

import dagger.Component;
import frc.robot.RobotContainer;
import jakarta.inject.Singleton;

@Singleton
@Component(modules = {RobotSystems.class})
public interface RobotModule {
    RobotContainer robotContainer();

    // Create the component
    @Component.Builder
    interface Builder {
        RobotModule build();
    }
}
