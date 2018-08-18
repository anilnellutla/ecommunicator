package projects.ecommunicator.graph;

public class Stack {
	private StackCell top;

	public Stack() {
		top = null;
	}

	public void clear() {
		top = null;
	}

	public boolean isEmpty() {
		return top == null;
	}

	public void push(Token s) {
		StackCell newCell = new StackCell(s, top);
		top = newCell;
	}

	public void push(String s) {
		push(new Token(s));
	}

	public void push(Stack s) {
		Stack t = new Stack();
		if (s == null)
			return;
		while (!s.isEmpty())
			t.push(s.pop());

		while (!t.isEmpty()) {
			push(t.peek());
			s.push(t.pop());
		}
	}

	public Token pop() {
		if (top != null) {
			Token result = top.now;
			top = top.next;
			return result;
		}

		return new Token("?");
	}

	public Token peek() {
		return top.now;
	}
	/*
		public static void showAll(Stack s0)
		{
			Stack s1 = new Stack();
			while (! s0.isEmpty())
			{
				Token tk = s0.pop();
				s1.push(tk);
			}

			while (! s1.isEmpty())
			{
				s0.push(s1.pop());
			}
		}
	*/
	public String showAll() {
		Stack s1 = new Stack();
		String ret = "";
		while (!this.isEmpty()) {
			Token tk = this.pop();
			ret = tk.getContent() + " " + ret;
			s1.push(tk);
		}

		while (!s1.isEmpty()) {
			this.push(s1.pop());
		}

		return ret;
	}

	public Stack copy() {
		Stack s1 = new Stack();
		Stack s2 = new Stack();
		while (!this.isEmpty()) {
			s1.push(this.pop());
		}
		while (!s1.isEmpty()) {
			s2.push(new Token(s1.peek().getContent()));
			this.push(s1.pop());
		}

		return s2;
	}

	public Stack flip() {
		Stack s1 = new Stack();
		Stack s2 = new Stack();
		while (!this.isEmpty()) {
			s1.push(this.peek());

			s2.push(this.pop());

		}
		while (!s1.isEmpty()) {
			this.push(s1.pop());
		}

		return s2;
	}

	public boolean isConstant() {
		Stack s1 = new Stack();
		boolean isconstant = true;

		while (!isEmpty()) {
			Token tk = pop();
			s1.push(tk);

			if (tk.is("x"))
				isconstant = false;
		}

		while (!s1.isEmpty())
			push(s1.pop());

		return isconstant;
	}

	public boolean isZero() {
		EvaluateExpression p = new EvaluateExpression(this);
		return (
			this.isConstant()
				&& Math.abs(p.evaluate(10).doubleValue()) < 1e-20);
	}

	public boolean isOne() {
		EvaluateExpression p = new EvaluateExpression(this);
		return (
			this.isConstant()
				&& Math.abs(p.evaluate(10).doubleValue() - 1) < 1e-20);
	}

	public boolean isNegativeOne() {
		EvaluateExpression p = new EvaluateExpression(this);
		return (
			this.isConstant()
				&& Math.abs(p.evaluate(10).doubleValue() + 1) < 1e-20);
	}

}
