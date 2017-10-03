package seaPortProgram;

public enum Status {
	INITIALIZING("Initializing"), 
	ACQUIRING("Acquiring"), 
	AVAILABLE("Available"), 
	ERROR("Error"), 
	OCCUPIED("Occupied"), 
	RUNNING("Running"), 
	SUSPENDED("Suspended"), 
	WAITING("Waiting"), 
	WORKING("Working"), 
	DONE("Done");

	private Status(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
