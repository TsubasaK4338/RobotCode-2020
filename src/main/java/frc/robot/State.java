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
        DoNothing,
        climbExtend,
        climbShrink,
        climbLock,
        climbSlide
    }

    //State宣言
    public ControlState  controlState;
    public DriveState driveState;
    public ClimbState climbState;

    public double driveStraightSpeed, driveRotateSpeed;  //Driveの速度
    public double driveStraightSetpoint, driveRotateSetpoint;    // PID制御の目標値
    public boolean is_drivePIDOn;    // PID制御するかどうか

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

    }

}
