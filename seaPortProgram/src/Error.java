package seaPortProgram;

public enum Error {
	E200("E200: Requirement cannot be met at current port");

	private Error(String name) {
		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
