/** 
 * Job.java
 * 01/20/2017
 * j.sakosky
 * 
 * Class: Job
 * 
 * 
 */

package seaPortProgram;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;

public class Job extends Thing implements Runnable {
	String type = "Job";
	Status status = Status.INITIALIZING;
	Error error = null;
	String errorDetails = "        ";
	Ship myShip = null;
	int timeElapsed = 0;
	double duration;
	ArrayList<String> requirements = new ArrayList<String>();
	ArrayList<Person> personsOnJob = new ArrayList<Person>();
	boolean complete = false;

	public Job(Scanner sc, Map m, World w) {
		super(sc, "Job", m.Jobs, w);
		if (sc.hasNextDouble())
			duration = sc.nextDouble();

		while (sc.hasNext()) {
			requirements.add(sc.next());
		}
	}

	public boolean checkRequirements() {
		boolean pass = true;
		for (String r : requirements) {
			Person worker = null;
			while (worker == null) {

				for (Person p : myShip.myPort.persons) {
					if (!personsOnJob.contains(p)) {
						if (r.equalsIgnoreCase(p.skill)) {
							worker = p;
							personsOnJob.add(worker);
							break;
							// continue;
						}
					}
				}
				if (worker == null) {
					errorDetails += (r + ", ");
					pass = false;
					break;
				}
			}
		}
		if (!pass) {
			System.out.println("Requirements can't be met");
			status = Status.ERROR;
			error = Error.E200;
			errorDetails += (" not available at " + this.myShip.myPort.index + " " + this.myShip.myPort.name);
		}
		personsOnJob.clear();
		return pass;
	}

	public int compByDurationAsc(Ship s) {
		if (duration < s.draft)
			return -1;
		if (duration > s.draft)
			return 1;
		return 0;
	}

	public int compByDurationDes(Ship s) {
		if (duration < s.draft)
			return 1;
		if (duration > s.draft)
			return -1;
		return 0;
	}

	public DefaultMutableTreeNode treeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				new TreeNode(type + ": " + name, NodeType.Job, this, descriptionFormatted()));
		return node;
	}

	public String descriptionFormatted() {

		return "Description";
	}

	public String getDetailsFormatted() {
		String details = "";

		details += (this.name + "  ship: " + this.myShip.name + "\n" + "Duration: " + this.duration + "\n"
				+ "Status: ");
		if (this.status == Status.ERROR) {
			details += ("ERROR " + this.error.toString() + "\n" + this.errorDetails);
		} else {
			details += status.toString();
		}

		return details;
	}

	public float getProgress() {
		return (float) (timeElapsed / duration);
	}

	private boolean getWorkers() {
		personsOnJob.clear();
		int counter = 0;
		// myShip.myPort.lock();

		synchronized (myShip.myPort.persons) { // party since looking forward to
												// P4
			// requirements
			while (myShip.myPort.busyList) {
				// showStatus(Status.WAITING);
				try {
					myShip.myPort.persons.wait();
				} catch (InterruptedException e) {
				} // end try/catch block
			} // end while waiting for worker to be free
			myShip.myPort.busyList = true;
		} // end sychronized on worker

		status = Status.ACQUIRING;
		for (String r : requirements) {
			Person worker = null;
			while (worker == null) {
				for (Person p : myShip.myPort.persons) {
					if (!personsOnJob.contains(p)) {
						if (r.equalsIgnoreCase(p.skill)) {
							if (p.lock.tryLock()) {
								worker = p;
								// System.out.println("Locked Worker");
								personsOnJob.add(worker);
								p.acquire(this);
								break;
								// continue;
							}
						}
					}
				}
				if (worker == null) {
					status = Status.WAITING;
					counter++;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (counter >= 2) {
						if (!personsOnJob.isEmpty()) {
							for (Person p : personsOnJob) {
								p.release();
								p.lock.unlock();
							}
						}
						// myShip.myPort.unlock();
						synchronized (myShip.myPort.persons) {
							myShip.myPort.busyList = false;
							myShip.myPort.persons.notifyAll();
						}
						// System.out.println("Not enough workers");
						return false;
					}
				}
			}

		}

		synchronized (myShip.myPort.persons) {
			myShip.myPort.busyList = false;
			myShip.myPort.persons.notifyAll();
		}

		// myShip.myPort.unlock();
		return true;
	}

	public void run() {
		myWorld.activeJobs.add(this);
		myWorld.model.addJob(this);
		myWorld.updateCounterInc();
		status = Status.INITIALIZING;

		// ------------Testing only-----------
		// myShip.myPort.lock();
		// -----------------------------------

		if (checkRequirements()) {
			// Find persons for job
			if (!requirements.isEmpty()) {
				while (!getWorkers()) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			status = Status.RUNNING;
			// Run Job
			System.out.println("Starting Job...");
			for (int i = 0; i < duration; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timeElapsed++;
				myWorld.model.fireTableDataChanged();
			}

			System.out.println("Finished Job...");

			// Release workers from job

			synchronized (myShip.myPort.persons) { // party since looking
													// forward to P4
				// requirements
				while (myShip.myPort.busyList) {
					// showStatus(Status.WAITING);
					try {
						myShip.myPort.persons.wait();
					} catch (InterruptedException e) {
					} // end try/catch block
				} // end while waiting for worker to be free
				myShip.myPort.busyList = true;
			} // end synchronization on list

			while (!personsOnJob.isEmpty()) {
				Person p = personsOnJob.get(0);
				p.release();
				p.lock.unlock();
				personsOnJob.remove(p);
				// System.out.println("Released Worker");
			}

			synchronized (myShip.myPort.persons) {
				myShip.myPort.busyList = false;
				myShip.myPort.persons.notifyAll();
			}

			status = Status.DONE;
		}

		// ------------Testing only-----------
		// myShip.myPort.unlock();
		// -----------------------------------
		complete = true;

		myShip.depart();
		// myWorld.activeJobs.remove(this);
		myWorld.updateCounterDec();
		System.out.println(myWorld.jobCounter + " / " + myWorld.jobActCounter);
	}
}
