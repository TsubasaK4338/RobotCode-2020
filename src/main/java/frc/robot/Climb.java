
package frc.robot;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.State.ControlState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Climb {

    //クライム用のモーター&エンコーダー
    private WPI_TalonSRX ClimbMotor;
    private WPI_TalonSRX CanonMotor;
    private Servo Servo;
    private WPI_TalonSRX SlideMotor;
    private DigitalInput MaxUp;         //角度上限
    private DigitalInput MaxDown; 

    private Timer lockTimer;

    Climb(WPI_TalonSRX hangingMotor, WPI_TalonSRX canonMotor, Servo hangingServo, WPI_TalonSRX climbSlideMotor, Timer climbTimer, DigitalInput maxUp, DigitalInput maxDown) {
        this.ClimbMotor = hangingMotor;
        this.CanonMotor = canonMotor;
        this.Servo = hangingServo;
        this.SlideMotor = climbSlideMotor;
        this.lockTimer = climbTimer;
        this.MaxUp = maxUp;
        this.MaxDown = maxDown;
    }


    // クライムを伸ばす
    private void climbAdvanced(){
        ClimbMotor.set(0.30);
        CanonMotor.set(0.15);
    }

    // クライムを縮める
    private void climbShrinked(){
        ClimbMotor.set(-0.30);
        CanonMotor.set(-0.15);
    }

    // クライムをアンロックする
    private void unlockServo(){
        Servo.setAngle(30);
    }

    // クライムをロックする
    private void lockServo(){
        Servo.setAngle(0);
    }

    // ジェネレーター上で右に動く
    private void rightSlide() {

        SlideMotor.set(0.30);

    }

    // ジェネレーター上で左に動く
    private void leftSlide() {

        SlideMotor.set(-0.30);        

    }


    // private void climbCheck (State state) {
        
    // }
    
    public void apllyState(State state){

        if(state.controlState == ControlState.m_Hanging){
            ClimbSequence(state);

        }else{

        }

    }

    private void ClimbSequence(State state){

        switch(state.climbState){

            case doNothing:

            case climbExtend:

            unlockServo();

            climbAdvanced();

            break;

            case climbShrink:

            unlockServo();
            
            lockTimer.reset();
            lockTimer.start();

            if(lockTimer.get() > 0.3) {

                climbShrinked();

            }
            

            climbShrinked();

            break;

            case climbLock:

            lockServo();

            break;

            case climbRightSlide:

            rightSlide();

            break;

            case climbLeftSlide:

            leftSlide();

            break;

        }

    }

}
