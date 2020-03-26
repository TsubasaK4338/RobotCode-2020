package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subClass.*;

public class PanelRotationMode {
    Drive drive;
    Panel panel;
    Arm arm;
    XboxController driver, operator;

    PanelRotationMode(Drive drive, Panel panel, Arm arm, Controller controller) {
        this.drive = drive;
        this.panel = panel;
        this.arm = arm;
        this.driver   = controller.driver;
        this.operator = controller.operator;
    }

    public void applyMode(State state) {
        if (state.controlState == State.ControlState.m_PanelRotation) {

                if (operator.getXButton()) {
                    //赤に合わせる
                    state.panelState = State.PanelState.p_toRed;
                } else if (operator.getYButton()) {
                    //緑に合わせる
                    state.panelState = State.PanelState.p_toGreen;
                } else if (operator.getAButton()) {
                    //青に合わせる
                    state.panelState = State.PanelState.p_toBlue;
                } else if (operator.getBButton()) {
                    //黄色に合わせる
                    state.panelState = State.PanelState.p_toYellow;
                } else if (Util.deadbandCheck(operator.getTriggerAxis(GenericHID.Hand.kLeft))) {
                    //手動右回転
                    state.panelState = State.PanelState.p_ManualRot;
                    state.panelManualSpeed = -operator.getTriggerAxis(GenericHID.Hand.kLeft);
                } else if (Util.deadbandCheck(operator.getTriggerAxis(GenericHID.Hand.kRight))) {
                    //手動左回転
                    state.panelState = State.PanelState.p_ManualRot;
                    state.panelManualSpeed = operator.getTriggerAxis(GenericHID.Hand.kRight);
                } else if(operator.getBumper(GenericHID.Hand.kRight)){
                    //砲台をパネルに合わせた角度へ
                    state.armOutState = State.ArmOutState.k_Panel;
                }
          
            drive.applyState(state);
            //panel.applyState(state);
            arm.applyState(state);
        }
    }

}
