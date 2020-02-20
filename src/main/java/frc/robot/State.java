package frc.robot;

public class State {

    
    //根本的な制御モード（ドライブ、セル発射、コンパネぐるぐる、ぶら下がり）
    public enum ControlState{
        m_Drive,
        m_Firingball,
        m_Panelrotation,
        m_Hanging
    }
    
    //ドライブモード
    public enum DriveState{
        kManual,
        kAuto,
        kTest
    }

    public enum ClimbState{
        doNothing,
        climbExtend,
        climbShrink,
        climbLock,
        climbRightSlide,
        climbLeftSlide
    }

    //Canonの状態を確認
    public enum CanonState{
        k_Basic,           //基本状態（最も下を向いている）
        k_Parallel,        //地面と平行な状態
        k_Aaiming,         //砲台の照準を合わせている状態
        k_Maxup            //最も上を向いている状態
    }

    // public enum ClimbAngle {
    //     climbNormal,
    //     climbMaxUp,
    //     climbFreeMove,
    // }

    //State宣言
    public ControlState  controlState;
    public DriveState driveState;
    public CanonState canonState;
    public ClimbState climbState;

    public double driveStraightSpeed, driveRotateSpeed;  //Driveの速度
    public double driveStraightSetpoint, driveRotateSetpoint;    // PID制御の目標値
    public boolean is_drivePIDOn;    // PID制御するかどうか

    public double hangingMotorSpeed;
    public double canonMotorSpeed;
    public double hangingServoAngle;
    public double climbSlideMotorSpeed;

    public double CanonRotateSpeed;  //砲台の回る速度
    public double CanonRotateSetPoint;  // PID制御の目標値
    public boolean is_canonPIDOn;  // PID制御しているかどうか

    //入力の認識変数宣言
    public boolean is_CanonChangeBasic; //砲台の角度初期化するかどうか

// 

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

        // Climb
        hangingMotorSpeed = 0;
        canonMotorSpeed = 0;
        hangingServoAngle = 0;
        climbSlideMotorSpeed = 0;

        //ArmのStateを初期化
        canonState = CanonState.k_Basic;
        CanonRotateSpeed = 0;
        CanonRotateSetPoint = 0;
        is_canonPIDOn = false;
    }

}
