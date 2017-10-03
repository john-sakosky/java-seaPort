/** 
 * PassenegerShip.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: PassenegerShip
 * 
 * 
 */

package seaPortProgram;

import java.util.Scanner;

public class PassengerShip extends Ship {
	String type = "Passenger Ship";
	int numberOfOccupiedRooms;
	int numberOfPassengers;
	int numberOfRooms;

	public PassengerShip(Scanner sc, Map m, World w) {
		super(sc, "PShip", m, w);
		if (sc.hasNextInt())
			numberOfPassengers = sc.nextInt();
		if (sc.hasNextInt())
			numberOfRooms = sc.nextInt();
		if (sc.hasNextInt())
			numberOfOccupiedRooms = sc.nextInt();
	}

	public String toStr(Map m) {
		String st = "Passenger ship: " + super.toStr(m);
		if (jobs.size() == 0)
			return st;
		for (Job mj : jobs)
			st += "\n       - " + mj.toStr(m);
		return st;
	} // end method toStr

	public String descriptionFormatted() {

		return "Description Cargo";
	}
}
