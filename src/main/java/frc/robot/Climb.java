package frc.robot;
import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Servo;

public class Climb {

    Encoder encoder;
    WPI_TalonSRX armMoter;
    WPI_TalonSRX climbMoter;
    Servo lockservo;

    Climb(Encoder encoder, WPI_TalonSRX armMoter, WPI_TalonSRX climbMoter, Servo lockservo) {
        this.encoder = encoder;
        this.armMoter = armMoter;
        this.climbMoter = climbMoter;
        this.lockservo = lockservo;
    }

    
    public void apllyState(State state){
    }
    public void ArmAdvanced(double speed){
    }
    public void ClimbAdvanced(double speed){
    }
    public void lockedservo(){
        
    }
    public double getDistancePerPulse(){
        return encoder.getDistance();
    }
}
