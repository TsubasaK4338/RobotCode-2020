/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.State.ClimbState;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;

//TalonSRX&VictorSPXのライブラリー
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {

  //コントローラー
  //とりあえず、Xbox2つ
  XboxController driver, operator;

  //モーター(全12個)
    
    //DriveMotor
    WPI_TalonSRX  driveRightFront,driveLeftFront;
    VictorSPX driveRightBack,driveLeftBack;

    //アームの角度変更モーター＆エンコーダー
    WPI_TalonSRX CanonMotor;
    Encoder CanonEncoder;

    //発射部分のモーター＆エンコーダー（コンパネぐるぐるの時も使う）
    WPI_TalonSRX ShootMotorRight , ShootMotorLeft;
    Encoder ShootEncoderRight , ShootEncoderLeft;

    //インテイクモーター（ロボット先端のアレ）
    VictorSPX IntakeMotor;
    
    //アーム内部のベルトモーター
    VictorSPX BeltMotorFront , BeltMotorback;

    //マイクロスイッチ(砲台の角度上限＆下限)
    DigitalInput MaxUpSwitch;
    DigitalInput MaxDownSwitch;

    //クライム用のモーター&エンコーダー&サーボ
    WPI_TalonSRX hangingMotor;
    Encoder hangingEncoder;
    Servo hangingServo;
    WPI_TalonSRX climbSlideMotor;

    // タイマー
    Timer climbTimer;
  
    //センサー
    ADXRS450_Gyro gyro;

    //SubClass
    Drive drive;
    Climb climb;
    Canon canon;
    State state;

  @Override
  public void robotInit() {

    //コントローラーの初期化
    driver = new XboxController(Const.DriveControllerPort);
    operator = new XboxController(Const.OperateControllerPort);

    //gyroの初期化
    gyro = new ADXRS450_Gyro();

    //----------------------------------------------------------------
    //DriveMotor
    //ドライブモーターの初期化
    driveRightFront = new WPI_TalonSRX(Const.DriveRightFrontPort);
    driveRightBack = new VictorSPX(Const.DriveRightBackPort);
    driveLeftFront = new WPI_TalonSRX(Const.DriveLeftFrontPort);
    driveLeftBack = new VictorSPX(Const.DriveLeftBackPort);
    //ドライブモーターの台形加速&フォローの設定
    driveLeftFront.configOpenloopRamp(Const.DriveFullSpeedTime);
    driveLeftBack.follow(driveLeftFront);
    driveRightFront.configOpenloopRamp(Const.DriveFullSpeedTime);
    driveRightBack.follow(driveRightFront);
    
    //----------------------------------------------------------------
    //ArmMotor
    
      //アームの角度変更モーター&エンコーダーの初期化
      CanonMotor = new WPI_TalonSRX(Const.CanonMotorPort);
      CanonEncoder = new Encoder(Const.CanonEncoderPort_A , Const.CanonEncoderPort_B);
      //アームの角度変更の台形加速設定
      CanonMotor.configOpenloopRamp(Const.CanonFullSpeedTime);

      //発射部分のモーター＆エンコーダーの初期化
      ShootMotorLeft  = new WPI_TalonSRX(Const.ShootMotorLeftPort);
      ShootMotorRight = new WPI_TalonSRX(Const.ShootMotorRightPort);
      ShootEncoderLeft  = new Encoder(Const.ShootEncoderLeftPort_A , Const.ShootEncoderLeftPort_B);
      ShootEncoderRight = new Encoder(Const.ShootEncoderRightPort_A , Const.ShootEncoderRightPort_B);

      //インテイクモーターの初期化
      IntakeMotor = new VictorSPX(Const.IntakeMotorPort);

      //アーム内部のベルトモーターの初期化
      BeltMotorFront = new VictorSPX(Const.BeltMotorFrontPort);
      BeltMotorback  = new VictorSPX(Const.BeltMotorBackPort);

      //ぶら下がり用モーター＆エンコーダーの初期化
      hangingMotor = new WPI_TalonSRX(Const.HangingMotorPort);
      hangingEncoder = new Encoder(Const.HangingEncoderPort_A , Const.HangingEncoderPort_B);
      hangingServo = new Servo(Const.climbservoMotorPort);
      climbSlideMotor = new WPI_TalonSRX(Const.climbSlidePort);

      climbTimer = new Timer();


    //----------------------------------------------------------------

    //マイクロスイッチの初期化
    MaxUpSwitch   = new DigitalInput(Const.MaxUpSwitchPort);
    MaxDownSwitch = new DigitalInput(Const.MaxDownSwitchPort);


    drive = new Drive(driveLeftFront, driveRightFront, gyro);
    climb = new Climb(hangingMotor, CanonMotor, hangingServo, climbSlideMotor, climbTimer, MaxUpSwitch, MaxDownSwitch);
    canon = new Canon(CanonMotor, MaxUpSwitch, MaxDownSwitch);
    state = new State();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {

    //この関数はずっとループして呼ばれるので、ループの最初にモード確認するお！    
    //根本的な制御モードの区別、そして入力確認
    switch(state.controlState) {

      //--------------------------------------------------------------------------------

      //【ドライブモード】
      case m_Drive:

        //OperatorのLBボタンが押されたら、セル発射モードへ切り替え
        if(operator.getBumper(Hand.kLeft)){
          state.controlState = State.ControlState.m_Firingball;
          break;
        }

        //OperatorのBackボタンが押されたら、パネル回転モードへ切り替え
        if(operator.getBackButton()){
          state.controlState = State.ControlState.m_Panelrotation;
          break;
        }

        //driverのstartボタンがおされたら、ぶら下がりモードへ切り替え
        if(driver.getStartButtonPressed()){
          state.controlState = State.ControlState.m_Hanging;
          break;
        }

        /********** Drive ***********/
          state.driveState = State.DriveState.kManual;
          state.driveStraightSpeed = Util.deadbandProcessing(driver.getY(Hand.kLeft));
          state.driveRotateSpeed = Util.deadbandProcessing(driver.getX(Hand.kRight));


       //ここはてらゆうにまかせようじゃあないか！！
        break;

     //---------------------------------------------------------------------------------
     //【セル発射モード】
     case m_Firingball:

        //OperatorのLBボタンが離されたら、ドライブモードへ切り替え
        if(operator.getBumperReleased(Hand.kLeft)){
          state.controlState = State.ControlState.m_Drive;
          break;
        }


        /********** CanonRotate ***********/
        state.CanonRotateSpeed = Util.deadbandProcessing(operator.getY(Hand.kLeft));


          //ここはぼくががんばる
        break;

     //---------------------------------------------------------------------------------

     //【コンパネぐるぐるモード】
     case m_Panelrotation:

        //OperatorのBackボタンが離されたら、ドライブモードへ切り替え
        if(operator.getBackButtonReleased()){
          state.controlState = State.ControlState.m_Drive;
          break;
        }

          //ここをしゅｎｙに任せようじゃあないか！！

        break;

     //---------------------------------------------------------------------------------

     //【ぶら下がりモード】
     case m_Hanging:

        // ワイヤー伸ばす
        if(driver.getYButton()) {
          
          state.climbState = ClimbState.climbExtend;

          break;
        }

        // ワイヤー巻く
        if(driver.getBButton()) {

          state.climbState = ClimbState.climbShrink;

          break;
        }

        // ワイヤーの爪初期化
        if(driver.getBackButton()) {

          state.climbState = ClimbState.climbLock;

          break;
        }

        // 登った後、左右に動く
        if(driver.getBumper(Hand.kRight)) {

          state.climbState = ClimbState.climbRightSlide;

          break;
        } 
        else if(driver.getBumper(Hand.kLeft)) {

          state.climbState = ClimbState.climbLeftSlide;

          break;
        }

        //driverのBackボタンがおされたら、ドライブモードへ切り替え
        if(driver.getBackButtonPressed()) {

          state.controlState = State.ControlState.m_Drive;
          break;

        }

      break;

    }
    //---------------------------------------------------------------------------------

    //入力確認が終わったら最後にStateを確認（apply）しよう

    drive.apllyState(state);
    climb.apllyState(state);

  }

  @Override
  public void testPeriodic() {
  }
}
