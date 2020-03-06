package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subClass.*;

public class ClimbMode {
    Arm arm;

    //クライム用のモーター&エンコーダー
    private TalonSRX climbMotor;
    private Servo climbServo;
    private TalonSRX slideMotor;
    private Timer lockTimer;
    private boolean is_LockTimerStart;

    private int n_extendReverse;

    ClimbMode(Arm arm, TalonSRX climbMotor, Servo climbServo, TalonSRX climbSlideMotor) {
        this.climbMotor = climbMotor;
        this.climbServo = climbServo;
        this.slideMotor = climbSlideMotor;
        this.lockTimer = new Timer();

        this.arm = arm;
    }

    public void changeState(State state) {
        System.out.println(state.climbState);

        switch (state.climbState) {
            case doNothing:
                lockServo();
                setClimbMotorSpeed(0);
                setSlideMotorSpeed(0);
                n_extendReverse = 0;
                is_LockTimerStart = false;
                break;
            case climbExtend:
                System.out.println("climbExtending");
                climbExtend(state);
                break;

            case climbShrink:
                climbShrink(state);
                break;

            case climbLock:
                lockServo();
                break;

            case climbSlide:
                //-で右 +で左
                lockServo();
                setSlideMotorSpeed(state.climbSlideMotorSpeed);
                break;

            case climbMotorOnlyExtend:
                if(!is_LockTimerStart) {lockTimer.reset(); lockTimer.start(); is_LockTimerStart = true;}
                if(lockTimer.get() > 0.5) {
                    unlockServo();
                    if (n_extendReverse > 3) {
                        System.out.println("climbextending:" + n_extendReverse);
                        setClimbMotorSpeed(Const.climbMotorExtendSpeed);
                    } else {
                        setClimbMotorSpeed(-1);
                        n_extendReverse++;
                    }
                } else {
                    unlockServo();
                }
                break;

            case climbMotorOnlyShrink:
                lockServo();
                setClimbMotorSpeed(-0.3);
        }
    }

    // クライムを伸ばす
    private void climbExtend(State state) {
        double armAngle = state.armAngle;

        if (armAngle <= -Const.armParallelAngleRange) {
            //Armの角度変更
            state.armState = State.ArmState.k_Parallel;
            n_extendReverse = 0;
            unlockServo();
        }
        System.out.println(armAngle);
        if (-Const.armParallelAngleRange < armAngle) {
            // Arｍ機構と合うようにスピードを調整
            state.armState = State.ArmState.k_Adjust;
            state.armMotorSpeed = arm.SetFeedForward(armAngle) + Const.climbArmExtendSpeed + state.climbExtendAdjustSpeed;
            System.out.println(state.armMotorSpeed);
            if(!is_LockTimerStart) {lockTimer.reset(); lockTimer.start(); is_LockTimerStart = true;}
            if(lockTimer.get() > 0.5) {
                unlockServo();
                if (n_extendReverse > 3) {
                    System.out.println("climbextending:" + n_extendReverse);
                    setClimbMotorSpeed(Const.climbMotorExtendSpeed);
                } else {
                    setClimbMotorSpeed(-1);
                    n_extendReverse++;
                }
            } else {
                unlockServo();
            }
        }
    }

    // クライムを縮める
    private void climbShrink(State state) {
        double armAngle = state.armAngle;
        if (armAngle > Const.armParallelAngleRange) {
            lockServo();
            //Armの角度変更
            state.armState = State.ArmState.k_Manual;
            state.armMotorSpeed = 0;
            setClimbMotorSpeed(Const.climbMotorShrinkSpeed);
        } else if (-Const.armParallelAngleRange <= armAngle && armAngle <= Const.armParallelAngleRange) {
            //アームの速さを任意でセットする関数をArmにつくる
            state.armState = State.ArmState.k_Conserve;
            setClimbMotorSpeed(0);
            lockServo();
        } else if (armAngle < Const.armParallelAngleRange) {
            //機構破壊防止のためClimbが下がりすぎたら上げる。


            lockServo();
            //アームの速さを任意でセットする関数をArmにつくる
            state.armState = State.ArmState.k_Manual;
            state.armMotorSpeed = Const.climbArmExtendSpeed;
            setClimbMotorSpeed(0);
        }


    }

    // クライムをアンロックする
    private void unlockServo() {
        setServoAngle(Const.unLockAngle);
    }

    // クライムをロックする
    private void lockServo() {
        setServoAngle(Const.lockAngle);
    }


    private void setSlideMotorSpeed(double speed) {
        slideMotor.set(ControlMode.PercentOutput, speed);
        if(Math.abs(slideMotor.getStatorCurrent()) > 25) {
            slideMotor.set(ControlMode.PercentOutput, 0);
        }
        System.out.println("slideMotorCurrent(Out):"+slideMotor.getStatorCurrent());
    }

    public void setClimbMotorSpeed(double speed) {
        climbMotor.set(ControlMode.PercentOutput, speed);
    }

    private void setServoAngle(double angle) {
        climbServo.set(angle);
    }
}
