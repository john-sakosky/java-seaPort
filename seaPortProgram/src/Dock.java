/** 
 * Dock.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: Dock
 * 
 * 
 */

package seaPortProgram;

import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;

public class Dock extends Thing {
	String type = "Dock";
	SeaPort myPort = null;
	Ship ship;
	boolean available = true;

	public Dock(Scanner sc, Map m, World w) {
		super(sc, "Dock", m.Docks, w);
		status = Status.AVAILABLE;
	}

	public String toStr(Map m) {
		String st = "\n Dock: " + super.toStr(m);
		if (!available)
			st += "\n   Ship: " + ship.toStr(m);
		else
			st += "\n  Empty ";
		return st;
	} // end method toStr

	public boolean available() {
		return available;
	}

	public boolean dockShip(Ship arrivingShip) {
		if (available) {
			ship = arrivingShip;
			// ship.setParent(this.index);
			ship.myDock = this;
			available = false;
			ship.startJobs();
			System.out.println("Docked Ship");
			status = Status.OCCUPIED;
			return true;
		} else
			return false;
	}

	// Remove current ship and tell Port to dock next ship in que
	public void undockShip() {
		ship.myDock = null;
		ship = null;
		available = true;
		status = Status.AVAILABLE;
		System.out.println("UnDocked Ship");
		myPort.lock.lock();
		myPort.dockShip();
		myPort.lock.unlock();
	}

	public DefaultMutableTreeNode treeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				new TreeNode("Dock: " + name, NodeType.Dock, this, descriptionFormatted()));
		if (ship == null) {
			node.add(new DefaultMutableTreeNode(new TreeNode("Empty", NodeType.Empty, null)));
		} else
			node.add(ship.treeNode());
		return node;
	}

	public String descriptionFormatted() {

		return "Description";
	}
}
