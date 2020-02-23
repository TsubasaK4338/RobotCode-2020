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
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.State.PanelState;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

//TalonSRX&VictorSPXのライブラリー
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

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
    VictorSPX IntakeMotor;
    
    //アーム内部のベルトモーター
    VictorSPX BeltMotorFront , BeltMotorback;

    //ぶら下がり用のモーター&エンコーダー
    WPI_TalonSRX HangingMotor;
    Encoder HangingEncoder;
  

  //センサー
  ADXRS450_Gyro gyro;

  //SubClass
  Drive drive;
  State state;
  Panel panel;

  @Override
  public void robotInit() {


    //コントローラーの初期化
    driver = new XboxController(Const.driverControllerPort);
    operator = new XboxController(Const.operatorControllerPort);

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
      //アームの角度変更の台形加速設定
      CanonMotor.configOpenloopRamp(Const.CanonFullSpeedTime);

      //発射部分のモーター＆エンコーダーの初期化
      ShootMotorLeft  = new WPI_TalonSRX(Const.ShootMotorLeftPort);
      ShootMotorRight = new WPI_TalonSRX(Const.ShootMotorRightPort);

      //インテイクモーターの初期化
      IntakeMotor = new VictorSPX(Const.IntakeMotorPort);

      //アーム内部のベルトモーターの初期化
      BeltMotorFront = new VictorSPX(Const.BeltMotorFrontPort);
      BeltMotorback  = new VictorSPX(Const.BeltMotorBackPort);

      //ぶら下がり用モーター＆エンコーダーの初期化
      HangingMotor = new WPI_TalonSRX(Const.HangingMotorPort);

    //----------------------------------------------------------------


    drive = new Drive(driveLeftFront, driveRightFront, gyro);
    panel = new Panel(ShootMotorLeft, ShootMotorRight);
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
      //syuny here rollin

      if((Util.deadbandProcessing(operator.getTriggerAxis(Hand.kLeft)) - 
      Util.deadbandProcessing(operator.getTriggerAxis(Hand.kRight)) > 0){
        state.panelState = State.PanelState.p_ManualRot;
      }
      else if (operator.getXButton()||operator.getYButton()||operator.getAButton()||operator.getBButton()){
        state.panelState = State.PanelState.p_AlignTo;
      }
      

      }
      

     //---------------------------------------------------------------------------------
      
     //【ぶら下がりモード】
     case m_Hanging:

        //driverのBackボタンがおされたら、ドライブモードへ切り替え
        if(driver.getBackButtonPressed()){
          state.controlState = State.ControlState.m_Drive;
          break;
        }

          //ぶら下がりの仕様がよくわからん…てらゆう、お前知ってるんだって？じゃぁお前担当な！（もしくは女王）

        break;
    }
    //---------------------------------------------------------------------------------
    
    //入力確認が終わったら最後にStateを確認（apply）しよう

    drive.apllyState(state);
    canon.applyState(state);
    shooter.applyState(state);
    panel.applyState(state);

    
    
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledInit() {

  }
}
