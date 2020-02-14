
package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.PIDController;
//import edu.wpi.first.wpilibj.Talon;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Canon{
        
    //宣言
    Encoder Encoder;            //エンコーダー（多分使わない）
    SpeedController Motor;      //モーター
    
    DigitalInput MaxUp;         //角度上限
    DigitalInput MaxDown;       //角度下限（0度であり基本状態）
    
    PIDController canonPID;     //PID制御（今後使う）


    //コンストラクター
    Canon(SpeedController CanonMotor, 
          DigitalInput MaxUpSwitch, DigitalInput MaxDownSwitch){        
        
        this.Motor = CanonMotor;
        
        this.MaxUp = MaxUpSwitch;
        this.MaxDown = MaxUpSwitch;

        //砲台のPID制御宣言
        //canonPID = new PIDController(Const.canonkp, Const.CanonKi, Const.CanonKd, Encoder, Motor)

        //PID制御は基本オフ
        canonPID.disable();
    }

    //現在の砲台の状態を確認
    void CanonCheck(State state){

        if(MaxDown.get()){
            //角度下限スイッチがONの時、砲台は基本状態である
            state.canonState = State.CanonState.k_Basic;           
        }
        else if(MaxUp.get()){
            //角度上限スイッチがONの時、砲台は最も上を向いている
            state.canonState = State.CanonState.k_Maxup; 
        }
        else{
            //角度上限スイッチも下限スイッチも押されていない状態
            state.canonState = State.CanonState.k_Aaiming;

        }

    }

    //出力処理
    void applyState(State state){
        
        switch(state.canonState){

            //---------------------------------------------------------------
            //砲台が最も下を向いている
            case k_Basic:

                //砲台の角度を変える（上のみ）
                if(state.CanonRotateSpeed > 0){
                    CanonMove(state.CanonRotateSpeed);
                }

            break;

            //---------------------------------------------------------------
            //砲台が最も上を向いている
            case k_Maxup:
                
                //砲台の角度を初期化
                if(state.is_CanonChangeBasic == true){
                    CanonChangeBasic();
                    break;
                }

                //砲台の角度を変える（下のみ）
                if(state.CanonRotateSpeed < 0){
                    CanonMove(state.CanonRotateSpeed);
                }

            break;

            //---------------------------------------------------------------
            //砲台の角度を自由に動かせる
            case k_Aaiming:
                
                //砲台の角度を初期化
                if(state.is_CanonChangeBasic == true){
                    CanonChangeBasic();
                    break;
                }

                //砲台の角度を変える
                CanonMove(state.CanonRotateSpeed);

            break;

            //---------------------------------------------------------------
            //砲台が地面と平行
            case k_Parallel:


            break;
        }


    }

    //--------------------------------------------------------------------------------
    //砲台のモーターを回す(速さはsetspeedで決める)
    void CanonMove(double setspeed){     
        Motor.set(setspeed);
    }


    //--------------------------------------------------------------------------------
    //砲台の位置を管理

    //砲台を初期状態にするコマンド
    void CanonChangeBasic(){
        while(!MaxDown.get()){               
            //角度下限認識スイッチが反応したら関数を抜ける

            //角度下限認識スイッチが反応してなかったら、回す
            CanonMove(Const.CanonBasicSpeed_M);            
        }
    }

    //砲台を地面と平行にするコマンド
    void CanonChangeParallel(){


    }

    //--------------------------------------------------------------------------------

    //エンコーダー管理




    //--------------------------------------------------------------------------------

    /*
    //PID管理

    //PIDの目標値を設定
    public void PIDSetPoint(double setPoint){
        canonPID.setSetpoint(setPoint);
    }
    
    //PIDコントローラーをオン
    public void PIDEnable(){
        canonPID.enable();
    }
    
    //PIDコントローラーをオフ
    public void PIDDisable(){
        canonPID.disable();
    }
    
    */
    

}
