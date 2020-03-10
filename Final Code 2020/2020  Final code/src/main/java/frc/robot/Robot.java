/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot; 
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private SendableChooser<String> m_chooser = new SendableChooser<>();
  	
  DifferentialDrive drive; 	/**variable for driving as a whole*/
	Joystick controller_1, controller_2 ; /**variable for the xbox controller*/
/** controller_1 is used to drive intake and dump balls 
 * controller_2 is used for all climb mechanism and the color wheel 
 */
DoubleSolenoid s0 = new DoubleSolenoid(0,1); /**variable for the two solenoid valves*/

	
  Spark leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;	 /**variable for the 4 cims */
  Spark dump; //variable fo the dump
  Spark intake; //variable for the intake
  Spark Winch1, Winch2; // variabel for the 2 winches
  PWMSparkMax wheel; // variabel for the color sheel spinner
 Spark  Deploy; // varabel to bring up the climb
 SpeedControllerGroup leftSide, rightSide;	/**variable for the two sides of the drivetrain*/
 SpeedControllerGroup Winch; // variable for the 2 winch motor
 Compressor airCompressor;
  private Timer timer;
  

  @Override
  public void robotInit() {
    m_chooser = new SendableChooser<>();
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
	SmartDashboard.putData("Auto choices", m_chooser);
	
    
		leftFrontMotor = new Spark(4); /**pwm slot 3 on the roborio*/
		leftBackMotor = new Spark(5); /**pwm slot 2*/
		rightFrontMotor = new Spark(2); /**pwm slot 1*/
		rightBackMotor = new Spark(3); 	/**pwm slot 0*/
		dump = new Spark(6);
		intake = new Spark(7);
		Winch1 = new Spark(1);
    Winch2 = new Spark(0);
    Deploy = new Spark(9);
    Winch = new SpeedControllerGroup(Winch1, Winch2);
    wheel = new PWMSparkMax(8);
    controller_1 = new Joystick(1);  // used to drive, intake and dump
		controller_2 = new Joystick(2); // Used for all climb function and the color wheel
    timer= new Timer();
    airCompressor = new Compressor(0);
    airCompressor.start();
    airCompressor.setClosedLoopControl(true);
    airCompressor.setClosedLoopControl(false);
    leftFrontMotor.enableDeadbandElimination(true);
		leftBackMotor.enableDeadbandElimination(true);
		rightFrontMotor.enableDeadbandElimination(true);
		rightBackMotor.enableDeadbandElimination(true);
     rightSide = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);
     leftSide = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
		/**sets up both sides of the drive train to be controlled with tank drive*/
		drive = new DifferentialDrive(rightSide, leftSide);
		drive.setSafetyEnabled(false);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Axis. Output", controller_1.getRawAxis(1));
		SmartDashboard.putNumber("Rigth Axis Output", controller_1.getRawAxis(5));
	  
	{
   
  }
}

 
  @Override
  public void autonomousInit() {
  timer.reset(); // resets the timer at 0
  timer.start();//start the timer at 0
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {

	
  
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }


  

  
  @Override
  public void teleopPeriodic() {

	String gameData;
gameData = DriverStation.getInstance().getGameSpecificMessage();
if(gameData.length() > 0)
{
  switch (gameData.charAt(0))
  {
    case 'B' :
      //Blue case code
      break;
    case 'G' :
      //Green case code
      break;
    case 'R' :
      //Red case code
      break;
    case 'Y' :
      //Yellow case code
      break;
    default :
      //This is corrupt data
      break;
  }
} else {
  //Code for no data received yet
}
   
		
		// this code makes the robot move 
drive.tankDrive((controller_1.getRawAxis(1)*-.65), (controller_1.getRawAxis(5)*-.65));


		//*Sanic mode(a mode to deliver a higher maxinum speed)*/
		if (controller_1.getPOV() == 0) //*Up*/
			{
				drive.tankDrive(.95, .95);
			}
		if (controller_1.getPOV() == 90) //*Right*/
			{
				drive.tankDrive(.7, -.7);
			}
		if (controller_1.getPOV() == 180) //*Down*/
			{
				drive.tankDrive(-.95, -.95);
			}
		if (controller_1.getPOV() == 270) //*Left*/
			{
				drive.tankDrive(-.7, .7);
			}
		if (controller_1.getPOV() == 45) //*Up-Rightx*/
			{
				drive.tankDrive(.7, 0);
			}
		if (controller_1.getPOV() == 315) //*Up-Left*/
			{
				drive.tankDrive(0, .7);
			}
		if (controller_1.getPOV() == 225) //*Down-Left*/
			{
				drive.tankDrive(0, -.7);
			}
		if (controller_1.getPOV() == 135) //*Down-Right*/
			{
				drive.tankDrive(-.7, 0);
			}

	
		SmartDashboard.putNumber("front left motor percent output", leftFrontMotor.get());
		SmartDashboard.putNumber("back left motor percent output", leftBackMotor.get());
		SmartDashboard.putNumber("front right motor percent output", rightFrontMotor.get());
		SmartDashboard.putNumber("back right motor percent output", rightBackMotor.get());
		
	

	

    if(controller_1.getRawButton(1) ){
    dump.set(1);
	} 
      else{
      dump.set(0);
    }
    
	 if(controller_1.getRawButton(2)){
 
		dump.set(-1); }
          else{
          dump.set(0);
      }
      
     intake.set(controller_1.getRawAxis(2));//intakes ball
     intake.set(controller_1.getRawAxis(3)*-1);//spits ball

     Winch.set(controller_2.getRawAxis(2));//brings  bot up
     Winch.set(controller_2.getRawAxis(3)*-1);//brings bot down
     
		 if(controller_2.getRawButton(5)){
			 Deploy.set(1);}
			 else{
         Deploy.set(0);
           }
           //brings the climber down
			 if(controller_2.getRawButton(6)){
				Deploy.set(-1);}
	  			else{
					Deploy.set(0);
					}
		
					
        //brings the piston up
        if (controller_1.getRawButton(5))
		{
			s0.set(Value.kForward);
		}

//brings the piston down
		if (controller_2.getRawButton(6))
		{
			s0.set(Value.kReverse);
		}
				
      }
    }

		 





          
		
		

