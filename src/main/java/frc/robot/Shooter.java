
package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.PIDController;

public class Shooter{

    //モーター＆エンコーダー＆PID宣言
    SpeedController MotorLeft, MotorRight;
    Encoder EncoderLeft, EncoderRight;      //多分使わない

    PIDController LeftPID, RightPID;

    //コンストラクター
    Shooter(SpeedController ShootMotorLeft, SpeedController ShootMotorRight){
       
        this.MotorLeft = ShootMotorLeft;
        this.MotorRight = ShootMotorRight;

        //PID宣言
        this.LeftPID  = new PIDController(Const.ShootKp, Const.ShootKi, Const.ShootKd, EncoderLeft, MotorLeft);
        this.RightPID = new PIDController(Const.ShootKp, Const.ShootKi, Const.ShootKd, EncoderRight, MotorRight);

        //PIDは基本オフ
        LeftPID.disable();
        RightPID.disable();
    }

    //出力処理
    void applyState(State state){

    


    }


}