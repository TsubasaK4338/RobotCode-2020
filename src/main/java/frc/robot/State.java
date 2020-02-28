package frc.robot.subClass;

public class State {


    public double driveStraightSpeed, driveRotateSpeed;  //Driveの速度;
    public double shooterLeftSpeed, shooterRightSpeed, shooterPIDSpeed;
    public double hangingMotorSpeed;
    public double armMotorSpeed;
    public double SetarmAngle;
    public double NowarmAngle;
    public double armAngle;
    public double climbSlideMotorSpeed;
    public double panelManualSpeed;
    public double shooterAngle;
    public double armRotateSpeed;
    public ShooterState shooterState;
    public IntakeState intakeState;
    public IntakeBeltState intakeBeltState;
    public DriveState driveState;
    public ClimbState climbState;
    public ArmOutState armOutState;
    public ControlState controlState;
    public PanelState panelState;

    public State() {
        stateInit();
    }

    public void stateInit() {

        //DriveのStateを初期化
        driveState = DriveState.kManual;
        driveRotateSpeed = 0;

        //Shooter
        shooterState = ShooterState.doNothing;
        shooterLeftSpeed = 0;
        shooterRightSpeed = 0;
        shooterPIDSpeed = 0;

        //Intake
        intakeState = IntakeState.doNothing;

        //IntakeBeltState
        intakeBeltState = IntakeBeltState.doNothing;

        //Climb
        climbState = ClimbState.doNothing;
        hangingMotorSpeed = 0;
        armMotorSpeed = 0;
        armAngle = 0;
        climbSlideMotorSpeed = 0;

        //Arm
        armOutState = ArmOutState.k_DoNothing;
        armMotorSpeed = 0;
        SetarmAngle = 0;
        NowarmAngle = 0;

        panelState = PanelState.p_DoNothing;
        //ControlMode

        controlState = ControlState.m_Drive;

        shooterAngle = 0;


    }

    public void changeState() {

        //DriveのStateを初期化
        driveState = DriveState.kManual;
        driveRotateSpeed = 0;

        //Shooter
        shooterState = ShooterState.doNothing;
        shooterLeftSpeed = 0;
        shooterRightSpeed = 0;
        shooterPIDSpeed = 0;

        //Intake
        intakeState = IntakeState.doNothing;

        //IntakeBeltState
        intakeBeltState = IntakeBeltState.doNothing;
        //Climb
        climbState = ClimbState.doNothing;
        hangingMotorSpeed = 0;
        armMotorSpeed = 0;
        armAngle = 0;
        climbSlideMotorSpeed = 0;
        armOutState = ArmOutState.k_LittleAaim;
        panelState  = PanelState.p_DoNothing;


    }
    public enum ControlState {
        m_ShootingBall, m_PanelRotation, m_Climb, m_Drive

    }
    public enum DriveState {
        kManual,
        kLow,
        kdoNothing
    }
    public enum ShooterState {
        kshoot,
        kintake,
        kmanual,
        doNothing,
        kouttake
    }
    public enum IntakeState {
        kIntake,
        kouttake,
        doNothing
    }
    public enum IntakeBeltState {
        kIntake,
        kouttake,
        doNothing
    }

    public enum ClimbState {
        doNothing,
        climbExtend,
        climbShrink,
        climbLock,
        climbRightSlide,
        climbLeftSlide
    }

    public enum ArmOutState {
        k_ChangeBasic,
        k_Shoot,
        k_Panel,
        k_LittleAaim,
        k_Parallel,
        k_DoNothing
    }

    public enum PanelState {
        p_DoNothing,        //stop
        p_ManualRot,        //手動
        p_toBlue,           //色合わせ(大会のパネル側のセンサーの色なのでカラーセンサーが読み取るのは二つずれた値。青<->赤、黄<->緑）
        p_toYellow,
        p_toRed,
        p_toGreen

    }

}