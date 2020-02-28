package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subClass.*;

public class ShootingBallMode {
    Drive drive;
    Shooter shooter;
    IntakeBelt intakeBelt;
    Arm arm;
    XboxController driver, operator;

    ShootingBallMode(Drive drive, Shooter shooter, Arm arm, IntakeBelt intakeBelt, Controller controller) {
        this.drive = drive;
        this.shooter = shooter;
        this.arm = arm;
        this.driver = controller.driver;
        this.operator = controller.operator;
        this.intakeBelt = intakeBelt;
    }

    public void applyMode(State state) {
        if (state.controlState == State.ControlState.m_ShootingBall) {
            //もう一度バンパーが押されたら、ドライブモードへ切り替え
            if (operator.getBumper(GenericHID.Hand.kLeft)) {
                if (Util.deadbandCheck(operator.getTriggerAxis(GenericHID.Hand.kRight))) {
                    //ボールを飛ばす
                    state.shooterState = State.ShooterState.kshoot;
                    state.shooterPIDSpeed = operator.getTriggerAxis(GenericHID.Hand.kRight);
                    state.driveState = State.DriveState.kdoNothing;
                    state.intakeBeltState = State.IntakeBeltState.kouttake;
                } else if (Util.deadbandCheck(operator.getX(GenericHID.Hand.kLeft))) {
                    //ドライブを少し動かす
                    state.shooterState = State.ShooterState.doNothing;
                    state.driveState = State.DriveState.kLow;
                    state.driveRotateSpeed = operator.getX(GenericHID.Hand.kLeft);
                    state.driveStraightSpeed = operator.getY(GenericHID.Hand.kRight);
                } else if (Util.deadbandCheck(operator.getTriggerAxis(GenericHID.Hand.kLeft))) {
                    //砲台の角度をゴールへ調節する
                    state.driveState = State.DriveState.kdoNothing;
                    state.shooterState = State.ShooterState.doNothing;
                    state.armOutState = State.ArmOutState.k_Shoot;
                    state.shooterAngle = 0;
                } else if (Util.deadbandCheck(operator.getX(GenericHID.Hand.kLeft))) {
                    //砲台の角度を手動で調節
                    state.driveState = State.DriveState.kdoNothing;
                    state.shooterState = State.ShooterState.doNothing;
                    state.armOutState = State.ArmOutState.k_LittleAaim;
                    state.armMotorSpeed = operator.getX(GenericHID.Hand.kLeft);
                } else {
                    //何もしない
                    state.shooterState = State.ShooterState.doNothing;
                    state.driveState = State.DriveState.kdoNothing;
                    state.intakeBeltState = State.IntakeBeltState.doNothing;
                    state.armOutState = State.ArmOutState.k_DoNothing;
                }
                /*if(Util.deadbandCheck(operator.getTriggerAxis(GenericHID.Hand.kRight))&&Util.deadbandCheck(operator.getTriggerAxis(GenericHID.Hand.kLeft))){
                    state.intakeBeltState = State.IntakeBeltState.kouttake;
                }*/
            } else {
                //ドライブモードへ切り替え
                state.controlState = State.ControlState.m_Drive;
                state.driveState = State.DriveState.kManual;
                state.shooterState = State.ShooterState.doNothing;
                state.armOutState = State.ArmOutState.k_ChangeBasic;
                Util.sendConsole("Mode", "DriveMode");
            }

            drive.applyState(state);
            shooter.applyState(state);
            arm.applyState(state);
            intakeBelt.applyState(state);

        }
    }
}
