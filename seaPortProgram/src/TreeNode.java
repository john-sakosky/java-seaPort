package seaPortProgram;

import java.util.Comparator;

public class TreeNode {
	NodeType type;
	Boolean map;
	String name;
	String description = "";
	Thing object;

	public TreeNode(String name, NodeType type, Thing object) {
		this.name = name;
		this.type = type;
		this.object = object;
		this.map = false;
	}

	public TreeNode(String name, NodeType type, Thing object, Boolean bool) {
		this(name, type, object);
		this.map = bool;
	}

	public TreeNode(String name, NodeType type, Thing object, String description) {
		this(name, type, object);
		this.description = description;
	}

	public TreeNode(String name, NodeType type, Thing object, String description, Boolean bool) {
		this(name, type, object, description);
		this.map = bool;
	}

	public String toString() {
		return name;
	}

	public static Comparator<TreeNode> compTreeNode(boolean asc, SortBy sb) {
		Comparator<TreeNode> compNameA = new Comparator<TreeNode>() {
			public int compare(TreeNode t1, TreeNode t2) {
				int mult = -1;
				if (asc)
					mult = 1;

				switch (t1.type) {

				case Jobs:
				case Persons:
				case Ports:
				case Docks:
					if (asc)
						return t1.object.name.compareTo(t2.object.name);
					else
						return t2.object.name.compareTo(t1.object.name);
				case Que:
				case Ships:
					if (sb == SortBy.Name) {
						if (asc)
							return t1.object.name.compareTo(t2.object.name);
						else
							t2.object.name.compareTo(t1.object.name);
					}
					if (sb == SortBy.Weight) {
						if (((Ship) t1.object).weight < ((Ship) t2.object).weight)
							return -1 * mult;
						if (((Ship) t1.object).weight > ((Ship) t2.object).weight)
							return 1 * mult;
						return 0;
					}
					if (sb == SortBy.Length) {
						if (((Ship) t1.object).length < ((Ship) t2.object).length)
							return -1 * mult;
						if (((Ship) t1.object).length > ((Ship) t2.object).length)
							return 1 * mult;
						return 0;
					}
					if (sb == SortBy.Width) {
						if (((Ship) t1.object).width < ((Ship) t2.object).width)
							return -1 * mult;
						if (((Ship) t1.object).width > ((Ship) t2.object).width)
							return 1 * mult;
						return 0;
					}
					if (sb == SortBy.Draft) {
						if (((Ship) t1.object).draft < ((Ship) t2.object).draft)
							return -1 * mult;
						if (((Ship) t1.object).draft > ((Ship) t2.object).draft)
							return 1 * mult;
						return 0;
					}
					break;
				default:
					break;
				}

				return t1.name.compareTo(t2.name);
			}
		};
		return compNameA;
	}

}
