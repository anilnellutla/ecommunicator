package projects.ecommunicator.graph;

public class StackCell {

	public Token now;
	public StackCell next;

	public StackCell(Token a, StackCell b) {
		now = a;
		next = b;
	}
}
