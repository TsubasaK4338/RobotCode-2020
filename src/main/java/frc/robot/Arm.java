
package frc.robot.subClass;

//import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.SpeedController;
//import edu.wpi.first.wpilibj.PIDController;
//import edu.wpi.first.wpilibj.Talon;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.DemandType;

public class Arm{

    //宣言
    TalonSRX Motor;            //モーター
    SensorCollection Encoder;  //角度測る半固定抵抗

    ArmSensor armSensor;


    //コンストラクター
    public Arm(TalonSRX ArmMotor, SensorCollection ArmEncoder, ArmSensor armSensor){
        this.Motor = ArmMotor; 
        this.Encoder = ArmEncoder;
        
        this.armSensor = armSensor;
    }

    //---------------------------------------------------------------------------------------------------
    //出力処理
    public void applyState(State state){
        
        state.NowarmAngle = getArmNow(Encoder.getAnalogInRaw());

        switch(state.armOutState){
            //---------------------------------------------------------------
            //砲台の角度を基本状態に
            case k_ChangeBasic:
                state.SetarmAngle = Const.ArmMinAngle;
                break;
            //---------------------------------------------------------------
            //砲台の角度をセル発射用に
            case k_Shoot:
                state.SetarmAngle = Const.ArmShootAngle;
                break;
            //---------------------------------------------------------------
            //砲台の角度をパネル回転用に
            case k_Panel:
                state.SetarmAngle = Const.ArmPanelAngle;
                break;
            //---------------------------------------------------------------
            //砲台の角度を微調整 
            case k_LittleAaim:
                ArmAiming(state);
                break;
            //---------------------------------------------------------------
            //砲台の角度を地面と平行に（使う？） 
            case k_Parallel:
                state.SetarmAngle = Const.ArmParallelAngle;
                break;
            //---------------------------------------------------------------
            //何もしない
            case k_DoNothing:
                state.SetarmAngle = state.NowarmAngle;
                break;
            //---------------------------------------------------------------
        }
        
        ArmPIDMove(state.SetarmAngle, state.NowarmAngle);
    }

    //--------------------------------------------------------------------------------
    //砲台の角度を微調整する（PID無し）
    void ArmAiming(State state){

        if(armSensor.getArmFrontSensor()){
            //------------------------------------------
            //角度が初期状態（0度）
            if(state.armRotateSpeed < 0){
               ArmMove(state.armRotateSpeed * Const.ArmMagni);
            }
        }else if(armSensor.getArmBackSensor()){
            //------------------------------------------
            //角度が最も上（135度）
            if(state.armRotateSpeed > 0){
                ArmMove(state.armRotateSpeed * Const.ArmMagni);
            }
        }else{
            //------------------------------------------
            //角度が0度～135度
                ArmMove(state.armRotateSpeed * Const.ArmMagni);           
        }

    }

    //--------------------------------------------------------------------------------
    //砲台のモーターを回す制御(速度をsetSpeedで決める)（）
    public void ArmMove(double setSpeed){
        Motor.set(ControlMode.PercentOutput, setSpeed);
    }

    //砲台のモーターを回すPID制御(位置をSetPoint()で決める・重力オフセットをSetFeedForward()で決める)
    public void ArmPIDMove(double TargetAngle, double NowAngle){

        Motor.set(ControlMode.Position, SetPoint(TargetAngle),
                  DemandType.ArbitraryFeedForward, SetFeedForward(NowAngle));

    }

    //--------------------------------------------------------------------------------
    /*
    //砲台の位置を管理
    //砲台を初期状態にするコマンド       
    void ArmChangeBasic(){
        while(armSensor.getArmFrontSensor){               
            //角度下限認識スイッチが反応したら関数を抜ける
            //角度下限認識スイッチが反応してなかったら、回す
            ArmMove(Const.armBasicspeed);
        }
    }
    
    */
    //---------------------------------------------------------------------
    //砲台角度制御に関する計算式

    //現在の砲台の角度を計算(この関数上手く動いてくれない)
    //(角度の最大最小差分) ÷（エンコーダー値の最大最小差分) × (エンコーダー現在値 - 最小値) + (角度の最小値)
    private double getArmNow(int ArmNowPoint){

        double NowAngle;
        NowAngle = Const.ArmAngleDifference / Const.ArmPointDifference *
                (ArmNowPoint - Const.ArmMinPoint) + Const.ArmMinAngle;

        return NowAngle;
    }

    //目標角度に合わせたPIDの目標値を計算
    //(目標角度 - 最小角度) ×（エンコーダー値の最大最小差分) ÷ (角度の最大最小差分) + (0からの差分)
    private double SetPoint(double TargetAngle){

        double Targetpoint;
        Targetpoint = (TargetAngle - Const.ArmMinAngle) * Const.ArmPointDifference /
                       Const.ArmAngleDifference + Const.ArmMinPoint;

        return  Targetpoint;
    }

    //目標角度に合わせた重力オフセットを計算
    //(地面と水平な時の重力オフセット) × (cos目標角度)
    private double SetFeedForward(double TargetAngle){

        double FeedForward;
        FeedForward = Const.ArmMaxOffset * Math.cos(Math.toRadians(TargetAngle));

        return FeedForward;
    }

    //--------------------------------------------------------------------------------------


}
