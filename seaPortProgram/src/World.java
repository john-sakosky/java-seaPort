/** 
 * World.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: World
 * 
 * 
 */

package seaPortProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.tree.DefaultMutableTreeNode;

import seaPortProgram.SeaPortProgram.UpdatableTableModel;

import java.util.Scanner;

public class World extends Thing {

	ArrayList<SeaPort> ports = new ArrayList<SeaPort>();
	ArrayList<Ship> ships = new ArrayList<Ship>();
	ArrayList<Person> persons = new ArrayList<Person>();
	ArrayList<Job> activeJobs = new ArrayList<Job>();
	Map map = new Map();
	UpdatableTableModel model;
	int jobCounter = 0;
	int jobActCounter = 0;
	boolean running = false;

	public World(String inputFile) {
		super(new Scanner("World 0 0"), new HashMap<Integer, Thing>(), null);
		readFile(inputFile);
		buildLocalArrays();
		for (SeaPort port : ports) {
			Collections.sort(port.ships, Ship.compByNameAsc());
		}
		print("output.txt");
		// start();
	}

	void readFile(String inputFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				process(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void print(String outputFile) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

			bw.write(toStr());

			// no need to close it.
			// bw.close();

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Processes one line of text and creates objects as well as assigns them to
	 * their parent
	 */
	void process(String st) {
		if (st.startsWith("//"))
			return;
		Scanner sc = new Scanner(st);

		if (sc.hasNext()) {
			switch (sc.next()) {
			case "port":
				addPort(sc);
				break;
			case "dock":
				addDock(new Dock(sc, map, this));
				break;
			case "ship":
				addShip(new Ship(sc, map, this));
				break;
			case "cship":
				addShip(new CargoShip(sc, map, this));
				break;
			case "pship":
				addShip(new PassengerShip(sc, map, this));
				break;
			case "person":
				addPerson(new Person(sc, map, this));
				break;
			case "job":
				addJob(new Job(sc, map, this));
				break;
			}
		}
		sc.close();

	}

	public String toStr() {
		String content = "";

		for (SeaPort msp : ports) {
			content += msp.toStr(map);
		}
		return content;
	}

	void addDock(Dock dock) {
		SeaPort port = getSeaPortByIndex(dock.parent);
		if (port != null) {
			port.docks.add(dock);
			dock.myPort = port;
		}

		else
			; // !! Add error message here//
	}

	void addJob(Job job) {
		Ship ship = getShipByIndex(job.parent);
		if (ship != null) {
			ship.jobs.add(job);
			job.myShip = ship;
		} else
			System.out.println(
					"Error:  Failed to add job " + job.name + ": parent (" + job.parent + ") is not an existing ship");
	}

	void addPerson(Person person) {
		if (getSeaPortByIndex(person.parent) != null)
			getSeaPortByIndex(person.parent).persons.add(person);
		else
			; // !! Add error message here//
	}

	void addPort(Scanner sc) {
		// SeaPort port = ;
		this.ports.add(new SeaPort(sc, map, this));
	}

	void addShip(Ship ship) {
		Dock dock = getDockByIndex(ship.parent);
		SeaPort port;
		if (dock != null) {
			dock.dockShip(ship);
			port = getSeaPortByIndex(dock.parent);
		} else {
			port = getSeaPortByIndex(ship.parent);
			port.que.add(ship);
		}
		if (port != null) {
			port.ships.add(ship);
			ship.myPort = port;
		}

		else
			; // !! Add error message here//
	}

	void buildLocalArrays() {
		for (Integer ship : map.Ships.keySet()) {
			ships.add((Ship) map.Ships.get(ship));
		}
		for (Integer person : map.Persons.keySet()) {
			persons.add((Person) map.Persons.get(person));
		}
	}

	// Finding a ship by index
	Ship getShipByIndex(int x) {
		if (map.Ships.containsKey(x))
			return (Ship) map.Ships.get(x);
		return null;
	}

	// Finding a dock by index
	Dock getDockByIndex(int x) {
		if (map.Docks.containsKey(x))
			return (Dock) map.Docks.get(x);
		return null;
	}

	// Finding a person by index
	Person getPersonByIndex(int x) {
		if (map.Persons.containsKey(x))
			return (Person) map.Persons.get(x);
		return null;
	}

	// Finding a Sea Port by index
	SeaPort getSeaPortByIndex(int x) {

		if (map.SeaPorts.containsKey(x))
			return (SeaPort) map.SeaPorts.get(x);
		return null;
	}

	/**
	 * Search all elements by name. Return any element who's name matches or
	 * includes the search term
	 */
	public String searchByName(String queryS) {
		return searchByName(queryS, Ordering.Ascending);
	}

	public String searchByName(String queryS, Ordering ord) {
		ArrayList<Thing> results = new ArrayList<Thing>();

		for (Entry<Integer, Thing> e : map.SeaPorts.entrySet()) {
			if (e.getValue().name.contains(queryS))
				results.add(map.SeaPorts.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Docks.entrySet()) {
			if (e.getValue().name.contains(queryS))
				results.add(map.Docks.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Ships.entrySet()) {
			if (e.getValue().name.contains(queryS))
				results.add(map.Ships.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Persons.entrySet()) {
			if (e.getValue().name.contains(queryS))
				results.add(map.Persons.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Jobs.entrySet()) {
			if (e.getValue().name.contains(queryS))
				results.add(map.Jobs.get(e.getKey()));
		}

		if (ord == Ordering.Descending) {
			Collections.sort(results, Thing.compByNameDes());
		} else
			Collections.sort(results, Thing.compByNameAsc());

		return searchResultstoStr(results);
	}

	/**
	 * Search all elements by index. Return any element who's index matches or
	 * includes the search term
	 */
	public String searchByIndex(int query) {
		ArrayList<Thing> results = new ArrayList<Thing>();
		String queryS = new String(String.valueOf(query));

		for (Entry<Integer, Thing> e : map.SeaPorts.entrySet()) {
			if (e.getKey() == query || String.valueOf(e.getKey()).contains(queryS))
				results.add(map.SeaPorts.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Docks.entrySet()) {
			if (e.getKey() == query || String.valueOf(e.getKey()).contains(queryS))
				results.add(map.Docks.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Ships.entrySet()) {
			if (e.getKey() == query || String.valueOf(e.getKey()).contains(queryS))
				results.add(map.Ships.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Persons.entrySet()) {
			if (e.getKey() == query || String.valueOf(e.getKey()).contains(queryS))
				results.add(map.Persons.get(e.getKey()));
		}
		for (Entry<Integer, Thing> e : map.Jobs.entrySet()) {
			if (e.getKey() == query || String.valueOf(e.getKey()).contains(queryS))
				results.add(map.Jobs.get(e.getKey()));
		}

		return searchResultstoStr(results);
	}

	/**
	 * Search all persons by skill. Return any person who's skill matches or
	 * includes the search term
	 */
	public String searchBySkill(String queryS) {
		ArrayList<Thing> results = new ArrayList<Thing>();

		for (SeaPort msp : ports) {
			for (Person ds : msp.persons) {
				if (String.valueOf(ds.skill).contains(queryS))
					results.add(ds);
			}
		}

		return searchResultstoStr(results);
	}

	/** Generate a string output of all elements passed */
	public String searchResultstoStr(ArrayList<Thing> results) {
		String output = new String("");

		if (!results.isEmpty()) {
			output += "Results";
			for (Thing thing : results) {
				output += "\n" + thing.name + ":  " + thing.type + " " + thing.index;
			}
		} else
			output = "No Results";

		return output;
	}

	public void start() {
		for (SeaPort s : ports) {
			for (Dock d : s.docks) {
				d.ship.startJobs();
			}
		}
	}

	public DefaultMutableTreeNode treeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				new TreeNode(name, NodeType.World, this, descriptionFormatted()));
		DefaultMutableTreeNode nodeB = null;

		nodeB = new DefaultMutableTreeNode(new TreeNode("Ports", NodeType.Ports, this));
		if (ports == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("World has no Ports", NodeType.Empty, null)));
		}
		for (SeaPort port : ports) {
			nodeB.add(port.treeNode());
		}
		node.add(nodeB);

		nodeB = new DefaultMutableTreeNode(new TreeNode("Ships", NodeType.Ships, this, true));
		if (ships == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("World has no Ships", NodeType.Empty, null)));
		}
		for (Ship ship : ships) {
			nodeB.add(ship.treeNode());
		}
		node.add(nodeB);

		nodeB = new DefaultMutableTreeNode(new TreeNode("Persons", NodeType.Persons, this, true));
		if (persons == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("World has no Persons", NodeType.Empty, null)));
		}
		for (Person person : persons) {
			nodeB.add(person.treeNode());
		}
		node.add(nodeB);

		nodeB = new DefaultMutableTreeNode(new TreeNode("Jobs", NodeType.Jobs, this, true));
		if (map.Jobs == null) {
			nodeB.add(new DefaultMutableTreeNode(new TreeNode("World has no Jobs", NodeType.Empty, null)));
		}
		for (Integer job : map.Jobs.keySet()) {
			nodeB.add(map.Jobs.get(job).treeNode());
		}
		node.add(nodeB);

		return node;
	}

	public void updateCounterInc() {
		synchronized (this) {
			jobCounter++;
			jobActCounter++;
		}
	}

	public void updateCounterDec() {
		synchronized (this) {
			jobCounter--;
		}
	}

	public String descriptionFormatted() {

		return "Description";
	}

}
