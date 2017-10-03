/** 
 * Ship.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: Ship
 * 
 * 
 */

package seaPortProgram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;

public class Ship extends Thing {
	String type = "Ship";
	PortTime arrivalTime, dockTime;
	double draft, length, weight, width;
	ArrayList<Job> jobs = new ArrayList<Job>();
	Dock myDock = null;
	SeaPort myPort = null;

	public Ship(Scanner sc, String type, Map m, World w) {
		super(sc, type, m.Ships, w);
		if (sc.hasNextInt())
			weight = sc.nextInt();
		if (sc.hasNextInt())
			length = sc.nextInt();
		if (sc.hasNextInt())
			width = sc.nextInt();
		if (sc.hasNextInt())
			draft = sc.nextInt();
	}

	public Ship(Scanner sc, Map m, World w) {
		super(sc, "Ship", m.Ships, w);
		if (sc.hasNextInt())
			weight = sc.nextInt();
		if (sc.hasNextInt())
			length = sc.nextInt();
		if (sc.hasNextInt())
			width = sc.nextInt();
		if (sc.hasNextInt())
			draft = sc.nextInt();
	}

	/*
	 * public static Comparator<Ship> compare(String a){
	 * 
	 * switch (a) { case "draftA": Comparator<Ship> compDraftA = new
	 * Comparator<Ship>(){ public int compare(Ship s1, Ship s2){ if
	 * (s1.draft<s2.draft) return -1; if (s1.draft>s2.draft) return 1; return 0;
	 * } }; return compDraftA; case "draftD": Comparator<Ship> compDraftD = new
	 * Comparator<Ship>(){ public int compare(Ship s1, Ship s2){ if
	 * (s2.draft<s1.draft) return -1; if (s2.draft>s1.draft) return 1; return 0;
	 * } }; return compDraftD; case "lengthA": Comparator<Ship> compLengthA =
	 * new Comparator<Ship>(){ public int compare(Ship s1, Ship s2){ if
	 * (s1.length<s2.length) return -1; if (s1.length>s2.length) return 1;
	 * return 0; } }; return compLengthA; case "lengthD": Comparator<Ship>
	 * compLengthD = new Comparator<Ship>(){ public int compare(Ship s1, Ship
	 * s2){ if (s2.length<s1.length) return -1; if (s2.length>s1.length) return
	 * 1; return 0; } }; return compLengthD; case "weightA": Comparator<Ship>
	 * compWeightA = new Comparator<Ship>(){ public int compare(Ship s1, Ship
	 * s2){ if (s1.weight<s2.weight) return -1; if (s1.weight>s2.weight) return
	 * 1; return 0; } }; return compWeightA; case "weightD": Comparator<Ship>
	 * compWeightD = new Comparator<Ship>(){ public int compare(Ship s1, Ship
	 * s2){ if (s2.weight<s1.weight) return -1; if (s2.weight>s1.weight) return
	 * 1; return 0; } }; return compWeightD; case "widthA": Comparator<Ship>
	 * compWidthA = new Comparator<Ship>(){ public int compare(Ship s1, Ship
	 * s2){ if (s1.width<s2.width) return -1; if (s1.width>s2.width) return 1;
	 * return 0; } }; return compWidthA; case "widthD": Comparator<Ship>
	 * compWidthD = new Comparator<Ship>(){ public int compare(Ship s1, Ship
	 * s2){ if (s2.width<s1.width) return -1; if (s2.width>s1.width) return 1;
	 * return 0; } }; return compWidthD; }
	 * 
	 * 
	 * }
	 */

	public static Comparator<Ship> compByDraftAsc() {
		Comparator<Ship> compDraftA = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s1.draft < s2.draft)
					return -1;
				if (s1.draft > s2.draft)
					return 1;
				return 0;
			}
		};
		return compDraftA;
	}

	public static Comparator<Ship> compByDraftDes() {
		Comparator<Ship> compDraftD = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s2.draft < s1.draft)
					return -1;
				if (s2.draft > s1.draft)
					return 1;
				return 0;
			}
		};
		return compDraftD;
	}

	public static Comparator<Ship> compByLengthAsc() {
		Comparator<Ship> compLengthA = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s1.length < s2.length)
					return -1;
				if (s1.length > s2.length)
					return 1;
				return 0;
			}
		};
		return compLengthA;
	}

	public static Comparator<Ship> compByLengthDes() {
		Comparator<Ship> compLengthD = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s2.length < s1.length)
					return -1;
				if (s2.length > s1.length)
					return 1;
				return 0;
			}
		};
		return compLengthD;
	}

	public static Comparator<Ship> compByWeightAsc() {
		Comparator<Ship> compWeightA = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s1.weight < s2.weight)
					return -1;
				if (s1.weight > s2.weight)
					return 1;
				return 0;
			}
		};
		return compWeightA;
	}

	public static Comparator<Ship> compByWeightDes() {
		Comparator<Ship> compWeightD = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s2.weight < s1.weight)
					return -1;
				if (s2.weight > s1.weight)
					return 1;
				return 0;
			}
		};
		return compWeightD;
	}

	public static Comparator<Ship> compByWidthAsc() {
		Comparator<Ship> compWidthA = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s1.width < s2.width)
					return -1;
				if (s1.width > s2.width)
					return 1;
				return 0;
			}
		};
		return compWidthA;
	}

	public static Comparator<Ship> compByWidthDes() {
		Comparator<Ship> compWidthD = new Comparator<Ship>() {
			public int compare(Ship s1, Ship s2) {
				if (s2.width < s1.width)
					return -1;
				if (s2.width > s1.width)
					return 1;
				return 0;
			}
		};
		return compWidthD;
	}

	public void depart() {
		for (Job j : jobs) {
			if (!j.complete)
				return;
		}
		myDock.undockShip();
	}

	public void startJobs() {
		for (Job j : jobs) {
			new Thread(j).start();
			// j.run();
		}
	}

	public DefaultMutableTreeNode treeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				new TreeNode(type + ": " + name, NodeType.Ship, this, descriptionFormatted()));
		jobs.forEach((job) -> node.add(job.treeNode()));
		return node;
	}


	public String descriptionFormatted() {

		return "Description";
	}
}
