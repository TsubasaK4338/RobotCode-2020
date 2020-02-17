
package frc.robot;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.State.ControlState;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Climb {

    //クライム用のモーター&エンコーダー
    WPI_TalonSRX ClimbMotor;
    WPI_TalonSRX CanonMotor;
    Servo Servo;
    WPI_TalonSRX SlideMotor;

    
    Climb(WPI_TalonSRX hangingMotor, WPI_TalonSRX canonMotor, Servo hangingServo, WPI_TalonSRX climbSlideMotor) {
        this.ClimbMotor = hangingMotor;
        this.CanonMotor = canonMotor;
        this.Servo = hangingServo;
        this.SlideMotor = climbSlideMotor;
    }

    void climbAdvanced(){
        ClimbMotor.set(0.30);
        CanonMotor.set(0.15);
    }

    void climbShrinked(){
        ClimbMotor.set(-0.30);
        CanonMotor.set(-0.15);
    }

    void unlockServo(){
        Servo.setAngle(30);
    }

    void lockServo(){
        Servo.setAngle(0);
    }

    void rightSlide() {

        SlideMotor.set(0.30);

    }

    void leftSlide() {

        SlideMotor.set(-0.30);        

    }
    
    public void apllyState(State state){
        
        if(state.controlState == ControlState.m_Hanging){
            ClimbSequence(state);

        }else{

        }

    }

    void ClimbSequence(State state){

        switch(state.climbState){

            case doNothing:

            break;

            case climbExtend:

            unlockServo();

            climbAdvanced();

            break;

            case climbShrink:

            unlockServo();

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
