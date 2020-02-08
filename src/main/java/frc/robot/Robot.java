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
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

//TalonSRX&VictorSPXのライブラリー
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {

  //コントローラー
  //とりあえず、Xbox2つ
  XboxController driver, operator;

  //DriveMotor
  WPI_TalonSRX  driveRightFront,driveLeftFront;
  VictorSPX driveRightBack,driveLeftBack;

  //センサー
  ADXRS450_Gyro gyro;

  //SubClass
  Drive drive;
  State state;

  @Override
  public void robotInit() {

    //コントローラーの初期化
    driver = new XboxController(Const.DriveControllerPort);
    operator = new XboxController(Const.OperateControllerPort);

    //gyroの初期化
    gyro = new ADXRS450_Gyro();

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


    drive = new Drive(driveLeftFront, driveRightFront, gyro);
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

          //ここをしゅｎｙに任せようじゃあないか！！

        break;

     //---------------------------------------------------------------------------------
      
     //【ぶら下がりモード】
     case m_Hanging:

        //driverのBackボタンがおされたら、ドライブモードへ切り替え
        if(driver.getBackButtonPressed()){
          state.controlState = State.ControlState.m_Drive;
          break;
        }

          //ここは設計完了して細かい仕様が分かり次第検討…たぶん女王ががんばるのか？

        break;
    }
    //---------------------------------------------------------------------------------
    
    //入力確認が終わったら最後にStateを確認（apply）しよう

   　drive.apllyState(state);
    
    
  }

  @Override
  public void testPeriodic() {
  }
}
