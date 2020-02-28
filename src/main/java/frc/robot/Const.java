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

        //Arm-Motor-port & Encoder
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

	
	//台形加速時間宣言
        //drive
        public static final double DriveFullSpeedTime = 0.5;

        //Arm
        public static final double CanonFullSpeedTime = 0.3;
	
	
	//アームの可動域の角度＆エンコーダーからの値の最大
      	public static final double CanonMaxAngle   = 80;
      	public static final double CanonMinAngle   = -30;
      	public static final double CanonMaxPoint   = 496;   
      	public static final double CanonMinPoint   = 162;
  
      	public static final double CanonPointError = CanonMaxAngle - CanonMinAngle;
      	public static final double CanonAngleError = CanonMaxPoint - CanonMinPoint;
  
   	 //アームの重力オフセット最大値（角度が地面と平行であり、Cos = 1の時）
      	public static final double CanonMaxOffset = -0.13;





    //その他
    public static final double Deadband = 0.2;
}
