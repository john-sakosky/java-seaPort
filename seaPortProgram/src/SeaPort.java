/** 
 * SeaPort.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: SeaPort
 * 
 * 
 */

package seaPortProgram;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.tree.DefaultMutableTreeNode;

public class SeaPort extends Thing {
	static String type = "SeaPort";
	ArrayList<Dock> docks = new ArrayList<Dock>();
	ArrayList<Ship> que = new ArrayList<Ship>();
	ArrayList<Ship> ships = new ArrayList<Ship>();
	// ArrayList<Ship> departingShips = new ArrayList<Ship>();
	ArrayList<Person> persons = new ArrayList<Person>();
	boolean busyList = false;
	Lock lock = new ReentrantLock();

	public SeaPort(Scanner sc, Map m, World w) {
		super(sc, "Port", m.SeaPorts, w);
	}

	public String toStr(Map m) {
		String st = "\n\nSeaPort: " + super.toStr(m);
		for (Dock md : docks)
			st += "\n" + md.toStr(m);
		st += "\n\n --- List of all ships in que:";
		for (Ship ms : que)
			st += "\n   > " + ms.toStr(m);
		st += "\n\n --- List of all ships:";
		for (Ship ms : ships)
			st += "\n   > " + ms.toStr(m);
		st += "\n\n --- List of all persons:";
		for (Person mp : persons)
			st += "\n   > " + mp.toStr(m);
		return st;
	} // end method toString

	public DefaultMutableTreeNode treeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				new TreeNode(type + ": " + name, NodeType.Thing, this, descriptionFormatted()));
		DefaultMutableTreeNode nodeB = null;

		nodeB = new DefaultMutableTreeNode(new TreeNode("Docks", NodeType.Docks, this));
		if (docks == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("Port has no Docks", NodeType.Empty, null)));
		}
		for (Dock dock : docks) {
			nodeB.add(dock.treeNode());
		}
		node.add(nodeB);

		nodeB = new DefaultMutableTreeNode(new TreeNode("Ships in Que", NodeType.Que, this));
		if (que == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("No Ships in Que", NodeType.Empty, null)));
		}
		for (Ship ship : que) {
			nodeB.add(ship.treeNode());
		}
		node.add(nodeB);

		nodeB = new DefaultMutableTreeNode(new TreeNode("All Ships", NodeType.Ships, this));
		if (ships == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("No Ships in Port", NodeType.Empty, null)));
		}
		for (Ship ship : ships) {
			nodeB.add(ship.treeNode());
		}
		node.add(nodeB);

		nodeB = new DefaultMutableTreeNode(new TreeNode("Persons", NodeType.Persons, this));
		if (persons == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("Empty", NodeType.Empty, null)));
		}
		for (Person person : persons) {
			nodeB.add(person.treeNode());
		}
		node.add(nodeB);

		return node;
	}

	public void findWorker() {

	}

	public void shipArrive(Ship arrivingShip) {
		arrivingShip.setParent(this.index);
		arrivingShip.myPort = this;
		que.add(arrivingShip);
		ships.add(arrivingShip);
		dockShip();
	}

	public boolean dockShip() {
		if (!que.isEmpty()) {
			for (Dock md : docks) {
				if (md.available) {
					md.dockShip(que.remove(0));
					return true;

				}

			}
		}
		return false;
	}

	public String descriptionFormatted() {

		return "Description";
	}

}
