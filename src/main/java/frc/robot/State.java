
package frc.robot;

public class State {

    //ロボットの状態管理State

        //根本的な制御モード（ドライブ、セル発射、コンパネぐるぐる、ぶら下がり）
        public enum ControlState{
            m_Drive,
            m_Firingball,
            m_Panelrotation,
            m_Hanging

        }

        //Canonの状態を確認
        public enum CanonState{
            k_Basic,           //基本状態（最も下を向いている）
            k_Aaiming,         //砲台の照準を合わせている状態
            k_Maxup            //最も上を向いている状態
        }

    //ロボットの出力管理State

        //Driveのモード定義
        public enum DriveState{
            kManual,
            kAuto,
            kTest
        }

        //Canonの処理を分ける
        public enum CanonOutState{
            k_DoNothing,        //動かない
            k_ChangeBasic,      //砲台を基本状態にする
            k_Panel,            //コントロールパネル回す角度
            k_Parallel,         //地面と平行な角度(使う?)
            k_Shoot,            //セル発射する角度
            k_LittleAaim        //角度を微調整
        }

        //Shooterの処理を分ける
        public enum ShooterState{
            s_DoNothing,        //動かない
            s_Intake,           //ボールを取り込む（ドライブモード）
            s_Shoot,            //ボールを発射する（セル発射モード）
            s_Panelrotate       //コンパネを回す　（パネル回転モード）
        }

        //Intake,IntakeBeltの処理を分ける
        public enum IntakeState{
            i_DoNothing,        //動かない
            i_Intake,           //ボール回収
            i_AllShoot,         //ボール一気に発射
            i_OneShoot,         //ボール1玉発射
            i_Loading           //ボールを1玉発射するための部分に装填
        }

        //Panel
        public enum PanelState{
            p_DoNothing,        //stop
            p_ManualRot,        //手動
            p_toBlue,           //色合わせ(大会のパネル側のセンサーの色なのでカラーセンサーが読み取るのは二つずれた値。青<->赤、黄<->緑）
            p_toYellow,
            p_toRed,
            p_toGreen

        }


    //State宣言
    public ControlState  controlState;
    public CanonState canonState;

    public DriveState driveState;
    public CanonOutState canonOutState;
    public ShooterState shooterState;
    public IntakeState  intakeState;
    public PanelState panelState;


    //MotorのSpeed＆PID宣言
    public double driveStraightSpeed, driveRotateSpeed;  //Driveの速度
    public double driveStraightSetpoint, driveRotateSetpoint;    // PID制御の目標値
    public boolean is_drivePIDOn;    // PID制御しているかどうか

    public double CanonRotateSpeed;  //砲台の回る速度
    public double CanonRotateSetPoint;  // PID制御の目標値
    public boolean is_canonPIDOn;  // PID制御しているかどうか

    
    public double panelManualSpeed;    //パネル回す速度（手動）



    //入力の認識変数宣言
    //public boolean is_CanonChangeBasic; //砲台の角度初期化するかどうか

    //現在の砲台の角度
    public double CanonNowAngle;


    State(){
        stateInit();
    }



    public void stateInit(){

        //基本制御モードはドライブモード
        controlState = ControlState.m_Drive;

        //DriveのStateを初期化
        driveState = DriveState.kManual;
        driveStraightSetpoint = 0;
        driveRotateSpeed = 0;
        is_drivePIDOn = false;

        //ArmのStateを初期化
        canonState = CanonState.k_Basic;
        canonOutState = CanonOutState.k_ChangeBasic;
        CanonRotateSpeed = 0;
        CanonRotateSetPoint = 0;
        is_canonPIDOn = false;

        panelManualSpeed = 0;

        //shooterState初期化
        shooterState = ShooterState.s_Intake;

        //IntakeState初期化
        intakeState  = IntakeState.i_Intake;

        //panelstate初期化
        panelState   = PanelState.p_DoNothing;

        //入力の認識変数の初期化
        //is_CanonChangeBasic = false;

        //砲台の角度の初期化
        CanonNowAngle = 0;

    }

    /*
    CanonStateやControlStateは、ロボットの状態を見るStateです。
    まずこれらのStateがあって、その上でコントローラーからの入力を受け付けるわけです。

    対して、DriverStateやShooterStateやIntakeStateは、
    コントローラーからの入力に合わせて変化するStateです。
    このStateをApplyすることで、ShooterやIntakeのモーターを回す処理に繋げるのです。

    */

}
