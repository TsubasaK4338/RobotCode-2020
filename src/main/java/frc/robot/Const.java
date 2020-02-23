package frc.robot;


public final class Const {

    //コントローラーのポート宣言（PC）
        public static final int driverControllerPort = 0;
        public static final int operatoeControllerPort = 1;
        //public static final int joystickControllerPort = 2;


    //マイコンポート宣言（CAN接続）
        
        //Drive-motor-port
	    public static final int DriveRightFrontPort = 2;
	    public static final int DriveRightBackPort = 12;
	    public static final int DriveLeftFrontPort = 6;
        public static final int DriveLeftBackPort = 13;

        //Arm - Motor-port & Encoder - port
        public static final int CanonMotorPort = 3;
        //public static final int CanonEncoderPort_A = 0;
        //public static final int CanonEncoderPort_B = 0;

        public static final int ShootMotorRightPort = 4;
        public static final int ShootMotorLeftPort  = 5;
        //public static final int ShootEncoderRightPort_A = 0;
        //public static final int ShootEncoderRightPort_B = 0;
        //public static final int ShootEncoderLeftPort_A  = 0;
        //public static final int ShootEncoderLeftPort_B  = 0;

        public static final int IntakeMotorPort = 14;
        
        public static final int BeltMotorFrontPort = 11;
        public static final int BeltMotorBackPort  = 15;

        public static final int HangingMotorPort = 7;
        //public static final int HangingEncoderPort_A = 0;
        //public static final int HangingEncoderPort_B = 0;

        //microSwitch-port
        public static final int MaxUpSwitchPort   = 0;
        public static final int MaxDownSwitchPort = 0;

    
    //マイコンポート宣言（ロボリオ本体）
        //インテイクしたボールの位置を確認するポート
        public static final int BallCheckSensorPort = 0;


    //---------------------------------------------------------------------------------------------
    
    //定速で回したい時の速度宣言
        public static final double CanonBasicSpeed_P = 0.05; 
        public static final double CanonBasicSpeed_M = -0.05;

        public static final double ShooterBasicSpeed_P = 1.0;
        public static final double ShooterBasicSpeed_M = -1.0;

        public static final double IntakeBasicSpeed_P = 0.5;
        public static final double IntakeBasicSpeed_M = -0.5;


    //PIDのゲイン宣言
        //砲台のモーターのゲイン宣言
        public static final double CanonKp = 0;
	    public static final double CanonKi = 0;
        public static final double CanonKd = 0;

        //発射モーターのゲイン宣言
        public static final double ShootKp = 0;
	    public static final double ShootKi = 0;
        public static final double ShootKd = 0;

    //台形加速時間
        //drive
        public static final double DriveFullSpeedTime = 0.5;

        //Arm
        public static final double CanonFullSpeedTime = 0.2;

        //Shooter
        public static final double ShooterFullSpeedTime = 0.01;

    //不感帯
        public static final double Deadband = 0.2;

    //スティックの傾きに対するモーターの速さの倍率
        public static final double CanonMagni = 0.005;

    //アームの可動域の角度＆エンコーダーからの値の最大
        public static final double CanonMaxAngle = 135;
        public static final double CanonMaxPoint = 383;


}
