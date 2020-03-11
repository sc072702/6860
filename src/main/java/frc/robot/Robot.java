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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private SendableChooser<String> m_chooser = new SendableChooser<>();

  DifferentialDrive drive;
  /** variable for driving as a whole */
  XboxController controller_1, controller_2;
  /** variable for the xbox controller */
  /**
   * controller_1 is used to drive intake and dump balls controller_2 is used for
   * all climb mechanism and the color wheel
   */
  DoubleSolenoid s0 = new DoubleSolenoid(0, 1);
  /** variable for the two solenoid valves */

  Spark leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;

  /** variable for the 4 cims */
  Spark dump, // variable fo the dump
      intake, // variable for the intake
      Winch1, Winch2; // variabel for the 2 winches

  PWMSparkMax wheel; // variabel for the color sheel spinner
  Spark Deploy; // varabel to bring up the climb
  SpeedControllerGroup leftSide, rightSide;
  /** variable for the two sides of the drivetrain */
  SpeedControllerGroup Winch; // variable for the 2 winch motor
  Compressor airCompressor;
  private Timer timer;

  @Override
  public void robotInit() {
    m_chooser = new SendableChooser<>();
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    leftFrontMotor = new Spark(4); /** pwm slot 3 on the roborio */
    leftBackMotor = new Spark(5); /** pwm slot 2 */
    rightFrontMotor = new Spark(2); /** pwm slot 1 */
    rightBackMotor = new Spark(3); /** pwm slot 0 */

    dump = new Spark(6);
    intake = new Spark(7);
    Winch1 = new Spark(1);
    Winch2 = new Spark(0);
    Deploy = new Spark(9);

    Winch = new SpeedControllerGroup(Winch1, Winch2);
    wheel = new PWMSparkMax(8);

    controller_1 = new XboxController(0); // used to drive, intake and dump
    controller_2 = new XboxController(1); // Used for all climb function and the color wheel
    timer = new Timer();

    airCompressor = new Compressor(0);

    // You don't need to manually control compressor

    leftFrontMotor.enableDeadbandElimination(true);
    leftBackMotor.enableDeadbandElimination(true);
    rightFrontMotor.enableDeadbandElimination(true);
    rightBackMotor.enableDeadbandElimination(true);
    rightSide = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);
    leftSide = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
    /** sets up both sides of the drive train to be controlled with tank drive */
    drive = new DifferentialDrive(rightSide, leftSide);
    drive.setSafetyEnabled(false);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Axis. Output", controller_1.getY(Hand.kLeft));
    SmartDashboard.putNumber("Rigth Axis Output", controller_1.getY(Hand.kRight));
  }

  @Override
  public void autonomousInit() {

    timer.start();// start the timer at 0
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
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
      case 'B':
        // Blue case code
        break;
      case 'G':
        // Green case code
        break;
      case 'R':
        // Red case code
        break;
      case 'Y':
        // Yellow case code
        break;
      default:
        // This is corrupt data
        break;
      }
    } else {
      // Code for no data received yet
    }

    // this code makes the robot move

    // Note: Y-axis would yield negative values when you push upward and positive
    // when you push downard
    drive.tankDrive(-(controller_1.getY(Hand.kLeft) * -.65), -(controller_1.getY(Hand.kRight) * -.65));

    // *Sanic mode(a mode to deliver a higher maxinum speed)*/

    if (controller_1.getPOV() != -1) { // When DPAD is pressed -- You need this because otherwise the code inside will
                                       // override whatever you had on line 168

      switch (controller_1.getPOV()) {
      case 0:
        drive.tankDrive(.95, .95);
        break;
      case 45:
        drive.tankDrive(.7, 0);
        break;
      case 90:
        drive.tankDrive(.7, -.7);
        break;
      case 135:
        drive.tankDrive(-.7, 0);
        break;
      case 180:
        drive.tankDrive(-.95, -.95);
        break;
      case 225:
        drive.tankDrive(0, -.7);
        break;
      case 270:
        drive.tankDrive(-.7, .7);
        break;
      case 315:
        drive.tankDrive(-.7, 0);
        break;
      default:
        break;
      }
    }

    SmartDashboard.putNumber("front left motor percent output", leftFrontMotor.get());
    SmartDashboard.putNumber("back left motor percent output", leftBackMotor.get());
    SmartDashboard.putNumber("front right motor percent output", rightFrontMotor.get());
    SmartDashboard.putNumber("back right motor percent output", rightBackMotor.get());

    // Syntax: (boolean? value A : value B). If boolean is true, then output would
    // be value A, otherwise it would be value B.

    dump.set((controller_1.getRawButton(1) ? 1 : 0) - (controller_1.getRawButton(2) ? 1 : 0));

    // FIXME: I could not read what you have below.
    // Do you mind pm me the key map? Like X button does function A, RT does funtion
    // B, etc.

    intake.set(controller_1.getTriggerAxis(Hand.kLeft) - controller_1.getTriggerAxis(Hand.kRight));

    Winch.set(controller_2.getTriggerAxis(Hand.kLeft) - controller_2.getTriggerAxis(Hand.kRight));

    Deploy.set((controller_2.getRawButton(5) ? 1 : 0) - (controller_2.getRawButton(6) ? 1 : 0));

    // brings the piston up
    if (controller_1.getRawButton(5)) {
      s0.set(Value.kForward);
    }

    // brings the piston down
    if (controller_2.getRawButton(6)) {
      s0.set(Value.kReverse);
    }

  }
}
