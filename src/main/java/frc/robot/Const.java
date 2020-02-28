package frc.robot.subClass;

public class Const {

    //ControllerPort(コントローラーのポート)
    public static final int JoystickPort = 0;
    public static final int DriveControllerPort = 0;
    public static final int OperateControllerPort = 1;

    public static final double deadband = 0.2;


    //Drive-motor-port
    public static final int DriveRightFrontPort = 2;
    public static final int DriveRightBackPort = 12;
    public static final int DriveLeftFrontPort = 6;
    public static final int DriveLeftBackPort = 13;

    //Arm - Motor-port & Encoder - port
    public static final int ArmMotorPort = 3;


    public static final int IntakeMotorPort = 14;
    public static final int IntakeBeltSensorFrontPort = 0;
    public static final int IntakeBeltSensorBackPort = 1;

    public static final int HangingMotorPort = 7;


    //Drive
    public static final double DriveFullSpeedTime = 0.5;

    //Sensor


    //その他
    public static final double Deadband = 0.2;


    //Shooter
    public static final int shooterLeftMotor = 4;
    public static final int shooterRightMotor = 5;
    public static final int shooterMotorMaxOutput = 100000;
    public static final int kSlotIdx = 0;
    public static final int kPIDLoopIdx = 0;
    public static final int kTimeoutMs = 30;
    public final static Gains kGains_Velocit = new Gains(0.01, 0.000005, 0, 1023.0 / 7200.0, 300, 1.00, 20000000);
    public final static double shooterOutTakeSpeed = -0.2;
    public final static double shooterIntakeSpeed = 0.18;


    //Intake
    public final static double intakeSpeed = 0.8;
    public final static double outtakeSpeed = -0.6;
    public final static int intakeBeltFrontMotor = 11;
    public final static int intakeBeltBackMotor = 15;


    //Climb
    public static final double climbMotorAdvanceSpeed = 0.30;
    public static final double armMotorAdvanceSpeed = 0.15;
    public static final double climbMotorShrinkSpeed = -0.30;
    public static final double armMotorShrinkSpeed = -0.15;
    public static final double unLockAngle = 30;
    public static final double lockAngle = 0;
    public static final double slideMotorRight = 0.30;
    public static final double slideMotorLeft = -0.30;


    //ARM
    public static final int armMotor = 3;
    public static final double armBasicSpeed = 0.1;
    //スティックの傾きに対するモーターの速さの倍率
    public static final double ArmMagni = 0.1;
    //アームの可動域の角度＆エンコーダーからの値の最大
    public static final double ArmMaxAngle = 80;
    public static final double ArmMinAngle = -30;
    public static final double ArmMaxPoint = 496;
    public static final double ArmMinPoint = 167;

    public static final double ArmAngleDifference = ArmMaxAngle - ArmMinAngle;
    public static final double ArmPointDifference = ArmMaxPoint - ArmMinPoint;

    public static final double ArmMaxOffset = 0.13;

    public final static Gains kGains_ArmPosition = new Gains(10, 0, 25, 0, 0, 1.00, 0);


    //目標角度（現在不明）
    public static final double ArmShootAngle = 0;
    public static final double ArmParallelAngle = 0;
    public static final double ArmPanelAngle = 0;


    public static final double shooterPanelSpeed = 0.2;
}
