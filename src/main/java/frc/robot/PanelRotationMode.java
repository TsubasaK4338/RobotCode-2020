package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subClass.*;

public class PanelRotationMode {
    Drive drive;
    Panel panel;
    Arm   arm;
    XboxController driver, operator;

    PanelRotationMode(Drive drive, Panel panel, Arm arm, Controller controller) {
        this.drive = drive;
        this.panel = panel;
        this.arm   = arm;
        this.driver = controller.driver;
        this.operator = controller.operator;
    }

    public void applyMode(State state) {
        drive.applyState(state);
        panel.applyState(state);
        arm.applyState(state);
    }

}
