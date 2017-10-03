/** 
 * Thing.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: Thing
 * 
 * Base Class for most other classes in this program.  Contains common attributes and core methods.
 */

package seaPortProgram;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;

public class Thing implements Comparable<Thing> {
	String type;
	int index;
	int parent;
	String name;
	World myWorld;
	Status status = null;

	public Thing(Scanner sc, String type, HashMap<Integer, Thing> hm, World w) {
		this(sc, hm, w);
		this.type = type;
	}

	public Thing(Scanner sc, HashMap<Integer, Thing> hm, World w) {
		if (sc.hasNext())
			name = sc.next();
		if (sc.hasNextInt()) {
			index = sc.nextInt();
			hm.put(index, this);
		}
		if (sc.hasNextInt())
			parent = sc.nextInt();
		type = "Thing";
		myWorld = w;
	}

	@Override
	public int compareTo(Thing arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String toStr(Map m) {
		String st = name + " " + index;
		return st;
	} // end method toString

	public void setParent(int newParent) {
		parent = newParent;
	}

	public static Comparator<Thing> compByNameAsc() {
		Comparator<Thing> compNameA = new Comparator<Thing>() {
			public int compare(Thing t1, Thing t2) {
				return t1.name.compareTo(t2.name);
			}
		};
		return compNameA;
	}

	public static Comparator<Thing> compByNameDes() {
		Comparator<Thing> compNameD = new Comparator<Thing>() {
			public int compare(Thing t1, Thing t2) {
				return t2.name.compareTo(t1.name);
			}
		};
		return compNameD;
	}

	public DefaultMutableTreeNode treeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeNode("Thing: " + name, NodeType.Thing, this));
		return node;
	}
}
