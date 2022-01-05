package frc.auton;

import frc.robot.Dashboard;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutonSelector {
	public static final String ForwardAuto = "Forward";
	public static final String LeftPosition = "L";
	public static final String CenterPosition = "C";
	public static final String RightPosition = "R";
	//fields
	private String autonChooser;
	private String position;
	private static AutonSelector autonSelector;
	
	
	public static AutonSelector getInstance() {
		if(autonSelector == null) {
			System.out.println("Creating new AutonSelector");
			autonSelector = new AutonSelector();
		}
		return autonSelector;
	}
	
	private AutonSelector() {		
	}
	
	public SequentialCommandGroup chooseAuton() {
        // TODO: Connect auton chooser to dashboard
		//position = Dashboard.getInstance().getPosition(); 
		//autonChooser = Dashboard.getInstance().getAutonChooser(); 
		
		System.out.println("pos:"); //+ position
		System.out.println("auto:"); //+ autonChooser
		
		switch (autonChooser) {
			case ForwardAuto:
				return new DriveAuton();
			default:
				return new DriveAuton();
		}
	}
}
