
package frc.robot;

//import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.SpeedController;
//import edu.wpi.first.wpilibj.PIDController;
//import edu.wpi.first.wpilibj.Talon;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

public class Canon{
        
    //宣言
    SensorCollection Encoder;   //エンコーダー
    WPI_TalonSRX Motor;      //モーター
    
    SensorCollection MaxUp;         //角度上限(Fwd)
    SensorCollection MaxDown;       //角度下限（0度であり基本状態）(Rev)


    //コンストラクター
    Canon(WPI_TalonSRX CanonMotor, SensorCollection CanonEncoder,
          SensorCollection MaxUpSwitch, SensorCollection MaxDownSwitch){        
        
        this.Motor = CanonMotor;
        this.Encoder = CanonEncoder;

        this.MaxUp = MaxUpSwitch;
        this.MaxDown = MaxUpSwitch;

    }

    //----------------------------------------------------------------------------------------------------
    //現在の砲台の状態を確認（行き過ぎを防ぐ）(DownがRev、UpがVel)（最も上を向いているときが0度、最も下が135度）
    void CanonCheck(State state){

        if(MaxUp.isFwdLimitSwitchClosed()){
            //角度上限スイッチがONの時、砲台は基本状態である
            state.canonState = State.CanonState.k_Maxup; 
            
            //現在角度は0度
            state.CanonNowAngle = 0; 
        }
        else if(MaxDown.isRevLimitSwitchClosed()){
            //角度下限スイッチがONの時、砲台は最も上を向いている
            state.canonState = State.CanonState.k_Basic; 

            //現在角度は135度
            state.CanonNowAngle = Const.CanonMaxAngle;
        }
        else{
            //角度上限スイッチも下限スイッチも押されていない状態
            state.canonState = State.CanonState.k_Aaiming;

            //現在角度にエンコーダーからの値を計算して代入
            state.CanonNowAngle = getCanonNow(Encoder.getAnalogInRaw());
        }
    }


    //---------------------------------------------------------------------------------------------------
    //出力処理
    void applyState(State state){
        //まず、最初に砲台の状態を確認
        CanonCheck(state);
        
        switch(state.canonOutState){
            //---------------------------------------------------------------
            //砲台の角度をゼロに
            case k_ChangeBasic:



            break;            
            //---------------------------------------------------------------
            //砲台の角度をセル発射用に
            case k_Shoot:



            break;
            //---------------------------------------------------------------
            //砲台の角度をパネル回転用に
            case k_Panel:



            break;
            //---------------------------------------------------------------
            //砲台の角度を微調整 
            case k_LittleAaim:
                CanonAiming(state);
            break;
            //---------------------------------------------------------------
            //砲台の角度を地面と平行に（使う？） 
            case k_Parallel:

            break;
            //---------------------------------------------------------------
            //何もしない
            case k_DoNothing:
            
            break;

        }
    }

    //--------------------------------------------------------------------------------
    //砲台の角度を微調整する（PID無し）
    void CanonAiming(State state){

        //砲台の状態に合わせて微妙に動く
        switch(state.canonState){
        //------------------------------------------
        //角度が初期状態（0度）
        case k_Basic:
            if(state.CanonRotateSpeed > 0){
                CanonMove(state.CanonRotateSpeed * Const.CanonMagni);
                break;
            }
        break;
        //------------------------------------------
        //角度が最も上（135度）
        case k_Maxup:
            if(state.CanonRotateSpeed < 0){
                CanonMove(state.CanonRotateSpeed * Const.CanonMagni);
                break;
            }
        break;
        //------------------------------------------
        //角度が0度～135度
        case k_Aaiming:            
            CanonMove(state.CanonRotateSpeed * Const.CanonMagni);            
        break;
        //------------------------------------------
        }
    }

    //--------------------------------------------------------------------------------
    //砲台のモーターを回す制御(速度をsetSpeedで決める)（）
    void CanonMove(double setSpeed){     
        Motor.set(setSpeed);
    }
    
    
    //砲台のモーターを回すPID制御(位置をSetPoint()で決める・重力オフセットをSetFeedForward()で決める)
    void CanonPIDMove(int TargetAngle){    

      Motor.set(ControlMode.Position, SetPoint(TargetAngle), 
                DemandType.ArbitraryFeedForward, SetFeedForward(TargetAngle));

  }

    //--------------------------------------------------------------------------------
    //砲台の位置を管理

    /*
    //砲台を初期状態にするコマンド    
    void CanonChangeBasic(){
        while(!MaxDown.isFwdLimitSwitchClosed()){               
            //角度下限認識スイッチが反応したら関数を抜ける

            //角度下限認識スイッチが反応してなかったら、回す
            CanonMove(Const.CanonBasicSpeed_M);            
        }
    }
    */

  //---------------------------------------------------------------------
  //砲台角度制御に関する計算式
  
    //現在の砲台の角度を計算 
    //(角度の最大最小差分) ÷（エンコーダー値の最大最小差分) × (エンコーダー現在値 - 最小値) + (角度の最小値)
    double getCanonNow(int CanonNowPoint){
      
      double NowAngle;
      NowAngle = Const.CanonAngleError / Const.CanonPointError * 
                (CanonNowPoint - Const.CanonMinPoint) + Const.CanonMinAngle;

      return NowAngle;
    }

    //目標角度に合わせたPIDの目標値を計算
    //(目標角度 - 最小角度) ×（エンコーダー値の最大最小差分) ÷ (角度の最大最小差分) + (0からの差分)
    double SetPoint(int TargetAngle){
      
      double Targetpoint;
      Targetpoint = (TargetAngle - Const.CanonMinAngle) * Const.CanonMaxPoint / 
                    Const.CanonMaxAngle + Const.CanonMinPoint;

      return  Targetpoint;
    }

    //目標角度に合わせた重力オフセットを計算
    //(地面と水平な時の重力オフセット) × (cos目標角度)
    double SetFeedForward(int TargetAngle){
      
      double FeedForward;
      FeedForward = Const.CanonMaxOffset * Math.cos(Math.toRadians(TargetAngle));

      return FeedForward;
    }

  //--------------------------------------------------------------------------------------


}
