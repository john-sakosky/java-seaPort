/** 
 * CargoShip.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: CargoShip
 * 
 * 
 */

package seaPortProgram;

import java.util.Scanner;

public class CargoShip extends Ship {
	String type = "Cargo Ship";
	double cargoValue, cargoVolume, cargoWeight;

	public CargoShip(Scanner sc, Map m, World w) {
		super(sc, "CShip", m, w);
		if (sc.hasNextInt())
			cargoWeight = sc.nextInt();
		if (sc.hasNextInt())
			cargoVolume = sc.nextInt();
		if (sc.hasNextInt())
			cargoValue = sc.nextInt();
	}

	public String toStr(Map m) {
		String st = "Cargo ship: " + super.toStr(m);
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
