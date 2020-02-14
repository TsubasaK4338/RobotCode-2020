/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

//TalonSRX&VictorSPXのライブラリー
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


public class Robot extends TimedRobot {

  //コントローラー
  //とりあえず、Xbox2つ
 XboxController driver, operator;

  //モーター(全11個)
    
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
    WPI_VictorSPX IntakeMotor;
    
    //アーム内部のベルトモーター
    WPI_VictorSPX BeltMotorFront , BeltMotorback;

    //ぶら下がり用のモーター&エンコーダー
    WPI_TalonSRX HangingMotor;
    Encoder HangingEncoder;
  
  
  //センサー
  ADXRS450_Gyro gyro;

  //マイクロスイッチ(砲台の角度上限＆下限)
  DigitalInput MaxUpSwitch;
  DigitalInput MaxDownSwitch;
 
  //SubClass
  Drive drive;
  Canon canon;
  Shooter shooter;
  State state;


//ロボットに搭載するハードウェアの宣言（初期設定）
  @Override
  public void robotInit() {
    
    //コントローラーの初期化
    driver = new XboxController(0);
    operator = new XboxController(1);

    //gyroの初期化
    gyro = new ADXRS450_Gyro();

    //---------------------------------------------------------------
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
      //CanonMotor.getSelectedSensorPosition();
      //アームの角度変更の台形加速設定
      CanonMotor.configOpenloopRamp(Const.CanonFullSpeedTime);

      //発射部分のモーター＆エンコーダーの初期化
      ShootMotorLeft  = new WPI_TalonSRX(Const.ShootMotorLeftPort);
      ShootMotorRight = new WPI_TalonSRX(Const.ShootMotorRightPort);
      ShootEncoderLeft  = new Encoder(Const.ShootEncoderLeftPort_A , Const.ShootEncoderLeftPort_B);
      ShootEncoderRight = new Encoder(Const.ShootEncoderRightPort_A , Const.ShootEncoderRightPort_B);

      //インテイクモーターの初期化
      IntakeMotor = new WPI_VictorSPX(Const.IntakeMotorPort);

      //アーム内部のベルトモーターの初期化
      BeltMotorFront = new WPI_VictorSPX(Const.BeltMotorFrontPort);
      BeltMotorback  = new WPI_VictorSPX(Const.BeltMotorBackPort);

      //ぶら下がり用モーター＆エンコーダーの初期化
      HangingMotor = new WPI_TalonSRX(Const.HangingMotorPort);
      HangingEncoder = new Encoder(Const.HangingEncoderPort_A , Const.HangingEncoderPort_B);

    //----------------------------------------------------------------

    //マイクロスイッチの初期化
    MaxUpSwitch   = new DigitalInput(Const.MaxUpSwitchPort);
    MaxDownSwitch = new DigitalInput(Const.MaxDownSwitchPort);


    //サブクラスの初期化
    state = new State();
    drive = new Drive(driveLeftFront, driveRightFront, gyro);
    canon = new Canon(CanonMotor, MaxUpSwitch, MaxDownSwitch);
    shooter = new Shooter(ShootMotorLeft, ShootMotorRight);
    
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


  //メインループ
  @Override
  public void teleopPeriodic() {

    //まず、最初に砲台の状態を確認
    canon.CanonCheck(state);

    //根本的な制御モードの区別、そして入力確認
    switch(state.controlState) {
      
      //--------------------------------------------------------------------------------
      
      //【ドライブモード】
      case m_Drive:
        
        //OperatorのLBボタンが押されている間、セル発射モードへ切り替え
        if(operator.getBumper(Hand.kLeft)){
          state.controlState = State.ControlState.m_Firingball;
          break;
        }
        
        //OperatorのBackボタンが押されている間、パネル回転モードへ切り替え
        if(operator.getBackButton()){
          state.controlState = State.ControlState.m_Panelrotation;
          break;
        }  

        //driverのstartボタンがおされたら、ぶら下がりモードへ切り替え
        if(driver.getStartButtonPressed()){
          state.controlState = State.ControlState.m_Hanging;
          break;
        }

        //driverのAボタンが押されたら、砲台の角度を初期化
        state.is_CanonChangeBasic = operator.getAButton();


        /********** Drive ***********/
        state.driveState = State.DriveState.kManual;
        state.driveStraightSpeed = Util.deadbandProcessing(driver.getY(Hand.kLeft));
        state.driveRotateSpeed = Util.deadbandProcessing(driver.getX(Hand.kRight));

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


          
        break;

      //---------------------------------------------------------------------------------
      
      //【コンパネぐるぐるモード】
      case m_Panelrotation:

        //OperatorのBackボタンが離されたら、ドライブモードへ切り替え
        if(operator.getBackButtonReleased()){
          state.controlState = State.ControlState.m_Drive;
          break;
        }

        break;

      //---------------------------------------------------------------------------------
      
      //【ぶら下がりモード】
      case m_Hanging:

        //driverのBackボタンがおされたら、ドライブモードへ切り替え
        if(driver.getBackButtonPressed()){
          state.controlState = State.ControlState.m_Drive;
          break;
        }

        break;
     //---------------------------------------------------------------------------------
    }

    //入力確認が終わったら最後にStateを適用（apply）して出力処理
    drive.apllyState(state);
    canon.applyState(state);
    shooter.applyState(state);

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

  }
  
}
