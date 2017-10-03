/** 
 * Person.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: Person
 * 
 * 
 */

package seaPortProgram;

import java.util.Scanner;
import java.util.concurrent.locks.*;

import javax.swing.tree.DefaultMutableTreeNode;

public class Person extends Thing {
	// boolean available = true;
	Job assignment = null;
	String type = "Person";
	String skill;
	Lock lock = new ReentrantLock();

	public Person(Scanner sc, Map m, World w) {
		super(sc, "Person", m.Persons, w);
		if (sc.hasNext())
			skill = sc.next();
		status = Status.AVAILABLE;
	}

	public DefaultMutableTreeNode treeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				new TreeNode(type + ": " + name, NodeType.Person, this, descriptionFormatted()));
		return node;
	}

	public String descriptionFormatted() {

		return "Description";
	}
	public void acquire(Job job){
		assignment = job;
		status = Status.WORKING;
	}
	public void release(){
		assignment = null;
		status = Status.AVAILABLE;
	}
}
