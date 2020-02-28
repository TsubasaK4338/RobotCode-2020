
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
                state.armPID_ON = false;
                ArmChangeBasic();
                break;
            //---------------------------------------------------------------
            //砲台の角度をセル発射用に(PID)
            case k_Shoot:
                state.armPID_ON = true;
                state.SetarmAngle = Const.ArmShootAngle;
                break;
            //---------------------------------------------------------------
            //砲台の角度をパネル回転用に(PID)
            case k_Panel:
                state.armPID_ON = true;
                state.SetarmAngle = Const.ArmPanelAngle;
                break;
            //---------------------------------------------------------------
            //砲台の角度を微調整 
            case k_LittleAaim:
                state.armPID_ON = false;
                ArmAiming(state);
                break;
            //---------------------------------------------------------------
            //砲台の角度を地面と平行に(PID) 
            case k_Parallel:
                state.armPID_ON = true;
                state.SetarmAngle = Const.ArmParallelAngle;
                break;
            //---------------------------------------------------------------
            //何もしない
            case k_DoNothing:
                state.armPID_ON = false;
                break;
            //---------------------------------------------------------------
        }
        
        //PIDがONの時、設定した角度までPID制御
        if(state.armPID_ON == true){
            ArmPIDMove(state.SetarmAngle, state.NowarmAngle);
        }
    }

    //--------------------------------------------------------------------------------
    //砲台の角度を微調整する（PID無し）
    void ArmAiming(State state){

        if(armSensor.getArmFrontSensor()){
            //------------------------------------------
            //角度が初期状態（-30度）
            if(state.armMotorSpeed > 0){
               ArmMove(state.armMotorSpeed * Const.ArmMagni);
            }
        }else if(armSensor.getArmBackSensor()){
            //------------------------------------------
            //角度が最も上（80度）
            if(state.armMotorSpeed < 0){
                ArmMove(state.armMotorSpeed * Const.ArmMagni);
            }
        }else{
            //------------------------------------------
            //角度が-30度～80度
                ArmMove(state.armMotorSpeed * Const.ArmMagni);           
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
    
    //砲台を初期状態にする     
    void ArmChangeBasic(){
        while(!armSensor.getArmFrontSensor()){               
            //角度下限認識スイッチが反応したら関数を抜ける
            //角度下限認識スイッチが反応してなかったら、回す
            ArmMove(-Const.armBasicSpeed);
        }
    }
    
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
