package frc.robot;
import edu.wpi.first.wpilibj.util.Color;

public class Util {

    //不感帯処理
    public static double deadbandProcessing(double value) {
        return Math.abs(value) > Const.Deadband ? value : 0 ;
    }

    
    //カラーセンサー

public enum ColorCode {
    yellow,
    red,
    green,
    blue,
    inRange,
    outOfRange
  };
 ColorCode colorOutput = ColorCode.inRange;
  private static ColorCode selectColorAction(Color DCImport,int p){
    double r = DCImport.red;
    double g = DCImport.green;
    double b = DCImport.blue;
    if (p < 80){
      return ColorCode.outOfRange;
    }
    if ((0.2 <= r && r < 0.4) && (0.45 <= g) && (b < 0.2)){
      return ColorCode.yellow;
    }
    if ((0.3 <= r) && (0.2 <= g && g < 0.48) && (b < 0.3)){
      return ColorCode.red;
    }
    if ((r < 0.25) && (0.4 <= g) && (0.2 <=b && b < 0.27)){
      return ColorCode.green;
    }
    if ((r < 0.25) && (0.4 <= g) && (0.27 <= b)){
      return ColorCode.blue;
    }
    return ColorCode.inRange;
  }
}
