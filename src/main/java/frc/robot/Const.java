package frc.robot;

public class Const {

    //ControllerPort(コントローラーのポート)
	public static final int JoystickPort = 0;
	public static final int DriveControllerPort = 0;
    public static final int OperateControllerPort = 1;

	//マイコンポート宣言（ロボリオ）
        
        //Drive-motor-port
	    public static final int DriveRightFrontPort = 0;
	    public static final int DriveRightBackPort = 5;
        public static final int DriveLeftFrontPort = 1;
        public static final int DriveLeftBackPort = 6;

        //Arm-Motor-port & Encoder & Servo
        public static final int CanonMotorPort = 0;
        public static final int CanonEncoderPort_A = 0;
        public static final int CanonEncoderPort_B = 0;

        public static final int ShootMotorRightPort = 0;
        public static final int ShootMotorLeftPort  = 0;
        public static final int ShootEncoderRightPort_A = 0;
        public static final int ShootEncoderRightPort_B = 0;
        public static final int ShootEncoderLeftPort_A  = 0;
        public static final int ShootEncoderLeftPort_B  = 0;

        public static final int IntakeMotorPort = 0;
        
        public static final int BeltMotorFrontPort = 0;
        public static final int BeltMotorBackPort  = 0;

        public static final int HangingMotorPort = 0;
        public static final int HangingEncoderPort_A = 0;
        public static final int HangingEncoderPort_B = 0;

        public static final int climbservoMotorPort = 0;
        public static final int climbSlidePort = 0;

    
        
    //定速で回したい時の速度宣言
        public static final double CanonBasicSpeed_P = 0.4; 
        public static final double CanonBasicSpeed_M = -0.4;


    //microSwitch-port
        public static final int MaxUpSwitchPort   = 0;
        public static final int MaxDownSwitchPort = 0;

    
        
    //PIDのゲイン宣言
    //砲台のモーターのゲイン宣言
        public static final double CanonKp = 0;
	    public static final double CanonKi = 0;
        public static final double CanonKd = 0;


	//台形加速時間宣言
        //drive
        public static final double DriveFullSpeedTime = 0.5;

        //Arm
        public static final double CanonFullSpeedTime = 0.3;


    //Seosor


    //その他
    public static final double Deadband = 0.2;
    // 
}
