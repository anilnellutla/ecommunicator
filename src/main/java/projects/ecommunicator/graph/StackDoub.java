package projects.ecommunicator.graph;

public class StackDoub {
	java.util.Stack stack;

		StackDoub() {
			stack = new java.util.Stack();
		}

		public boolean isEmpty() {
			return stack.empty();
		}

		public void push(double s) {
			stack.push(new Double(s));
		}

		public double pop() {
			return ((Double) stack.pop()).doubleValue();
		}

		public double peek() {
			return ((Double) stack.peek()).doubleValue();
		}


}
