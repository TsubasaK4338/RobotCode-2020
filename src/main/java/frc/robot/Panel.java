package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.revrobotics.ColorSensorV3;

public class Panel{

  //ports n such (>const?)
  final static I2C.Port i2cPort = I2C.Port.kOnboard;
  final static ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  //construction
    WPI_TalonSRX MotorLeft;
    WPI_TalonSRX MotorRight;
    XboxController operator;
 
    Panel(WPI_TalonSRX ShootMotorLeft, WPI_TalonSRX ShootMotorRight, XboxController operator){
        this.MotorLeft = ShootMotorLeft;
        this.MotorRight = ShootMotorRight;
        this.operator = operator;
    }

  //!
  switch (State.PanelState) {
    case p_ManualRot:
      MotorLeft.set(Util.deadbandProcessing(operator.getTriggerAxis(kLeft)-operator.getTriggerAxis(kRight)));
      MotorRight.set(Util.deadbandProcessing(operator.getTriggerAxis(kLeft)-operator.getTriggerAxis(kRight)));
      break;

    case p_AlignTo:
    if(operator.getXButton()){
      AlignPanelTo(ColorCode.red);
    }
    if(operator.getYButton()){
      AlignPanelTo(ColorCode.green);
    }
    if(operator.getBButton()){
      AlignPanelTo(ColorCode.blue);
    }
    if(operator.getAButton()){
      AlignPanelTo(ColorCode.yellow);
    }
        
      }
      break;

    case p_silent:
      MotorLeft.set(0);
      MotorRight.set(0);
  }
  //
  public enum ColorCode {
    yellow,
    red,
    green,
    blue,
    inRange,
    outOfRange
  };
  ColorCode colorOutput = ColorCode.inRange;

  //!
  private ColorCode DetectedColor() {
    int p = m_colorSensor.getProximity();
    Color detectedColor = m_colorSensor.getColor();
    double r = detectedColor.red;
    double g = detectedColor.green;
    double b = detectedColor.blue;
    if (p < 80){
      return ColorCode.outOfRange;
    }
    if ((0.2 <= r && r < 0.4) && (0.45 <= g) && (b < 0.2)){
      return ColorCode.yellow;
    }
    if ((0.3 <= r) && (0.2 <= g && g < 0.48) && (b < 0.3)){
      return ColorCode.red;
    }
    if ((r < 0.3) && (0.4 <= g) && (0.2 <=b && b < 0.27)){
      return ColorCode.green;
    }
    if ((r < 0.25) && (0.4 <= g) && (0.27 <= b)){
      return ColorCode.blue;
    }
    return ColorCode.inRange;
  }

  private void AlignPanelTo(Color c){

    if(DetectedColor()==c){
      MotorLeft.set(0);
      MotorRight.set(0);
    } else {
      MotorLeft.set(0.3);
      MotorRight.set(0.3);
    }
    
  }


}