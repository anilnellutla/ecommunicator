package projects.ecommunicator.graph;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class EvaluateExpression {
	//private double x = 0;
	private Stack pf;
	private String infixStr;
	private boolean isConstant;

	public EvaluateExpression() {
		this("");
	}

	protected EvaluateExpression(Stack s) {
		pf = s;
	}

	public EvaluateExpression(String input) {

		infixStr = input;
		Stack infix = new Stack();
		String eqn = input.trim();
		boolean okay = true;
		isConstant = true;

		Token tk = null;

		while (eqn.length() != 0 && okay) {
			tk = new Token(eqn);
			if (tk.isValid()) {
				isConstant = isConstant && !tk.is("x");

				//String content = tk.getContent();
				int num = tk.length();
				eqn = eqn.substring(num).trim();

				infix.push(tk);
			} else {
				infix.clear();
				okay = false;
			}
		}

		pf = infix2postfix(infix.flip());

		if (isMalformed()) {
			pf = new Stack();
		}
	}

	private boolean isMalformed() {
		boolean b = false;

		char[] ch = infixStr.toCharArray();
		int count = 0;

		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == '(')
				count++;
			if (ch[i] == ')')
				count--;

			if (count < 0)
				return true;
		}

		try {
			b = (count != 0 || evaluate(0).doubleValue() == Double.MIN_VALUE);
		} catch (EmptyStackException e) {
			return true;
		}

		return b;
	}

	private Stack infix2postfix(Stack infix) {
		Stack pf = new Stack();
		Stack ops = new Stack();
		//Stack temp = new Stack();

		Token lastTk = new Token("+");

		infix.showAll();

		while (!infix.isEmpty()) {
			pf.showAll();

			Token tk = infix.pop();

			/*NEGATION CODE
				If conditions are right, "-" can mean the negative
				of a number and not subtraction.
			*/
			if (tk.is("-")) {
				if (pf.isEmpty()) {
					tk = new Token("neg");
				} else if (lastTk.isOperation() && !lastTk.is(")")) {
					tk = new Token("neg");
				} else if (lastTk.isNumber()) {
					tk = new Token("-");
				}
			}

			/*POSITIVE CODE
				If conditions are right, "+" can mean the positive
				of a number and not addition.
			*/
			if (tk.is("+")) {
				if (pf.isEmpty()) {
					tk = new Token("");
				} else if (lastTk.isOperation() && !lastTk.is(")")) {
					tk = new Token("");
				} else if (lastTk.isNumber()) {
					tk = new Token("+");
				}
			}

			float precedence = tk.precedence();

			// IMPLIED MULTIPLICATION CODE
			if (!infix.isEmpty()) {
				Token nt = infix.peek();
				if ((tk.is(")") && nt.is("("))
					|| (tk.isNumber() && nt.is("("))
					|| (tk.is(")") && nt.isNumber())
					|| (tk.is(")") && nt.precedence() == 1)
					|| (tk.isNumber() && nt.precedence() == 1)
					|| (tk.isNumber() && nt.isNumber()))
					infix.push(new Token("*"));
			}

			if (tk.isNumber()) {
				pf.push(tk);
			} else if (tk.isOperation()) {
				if (ops.isEmpty() || tk.is("(")) {
					ops.push(tk);
				} else if (tk.is(")")) {
					while (!ops.peek().is("(")) {
						pf.push(ops.pop());
						if (ops.isEmpty())
							break;
					}
					if (!ops.isEmpty())
						ops.pop();
				} else if (tk.isBinary()) {
					while (ops.peek().precedence() <= precedence) {
						pf.push(ops.pop());
						if (ops.isEmpty())
							break;
					}
					ops.push(tk);
				} else {
					while (ops.peek().precedence() < precedence) {
						pf.push(ops.pop());
						if (ops.isEmpty())
							break;
					}
					ops.push(tk);
				}
			}

			if (!tk.is(""))
				lastTk = tk;
		}

		while (!ops.isEmpty())
			pf.push(ops.pop());

		Stack t = new Stack();
		while (!pf.isEmpty()) {

			Token tk = pf.pop();
			if (tk.is("ddx")) {
				Stack d = derivative(sub(pf));
				while (d != null && !d.isEmpty()) {
					t.push(d.pop());
				}
			} else {
				t.push(tk);
			}
		}

		while (!t.isEmpty())
			pf.push(t.pop());

		return pf;
	}

	static Stack add(Stack a, Stack b) // r = a+b
	{
		Stack r = new Stack();

		if (a.isZero() && b.isZero()) {
			r.push("0");
		} else if (a.isZero()) {
			r.push(b);
		} else if (b.isZero()) {
			r.push(a);
		} else {
			r.push(a);
			r.push(b);
			r.push("+");
		}

		return r;
	}

	static Stack subtract(Stack a, Stack b) // r = a-b
	{
		Stack r = new Stack();

		if (a.isZero() && b.isZero()) {
			r.push("0");
		} else if (a.isZero()) {
			r.push(b);
			r.push("neg");
		} else if (b.isZero()) {
			r.push(a);
		} else {
			r.push(a);
			r.push(b);
			r.push("-");
		}

		return r;
	}

	static Stack multiply(Stack a, Stack b) // r = a*b
	{
		Stack r = new Stack();

		if (a.isZero() || b.isZero()) {
			r.push("0");
		} else if (a.isOne()) {
			r.push(b);
		} else if (b.isOne()) {
			r.push(a);
		} else {
			r.push(a);
			r.push(b);
			r.push("*");
		}

		return r;
	}

	static Stack divide(Stack a, Stack b) // r = a/b
	{
		Stack r = new Stack();

		if (b.isOne()) {
			r.push(a);
		} else {
			r.push(a);
			r.push(b);
			r.push("/");
		}

		return r;
	}

	static Stack pow(Stack a, Stack b) // r = a^b
	{
		Stack r = new Stack();

		if (a.isOne()) {
			r.push("1");
		} else if (b.isZero() && !a.isZero()) {
			r.push("1");
		} else if (b.isOne()) {
			r.push(a);
		} else {
			r.push(a);
			r.push(b);
			r.push("^");
		}

		return r;
	}

	static Stack ln(Stack a) // r = ln a
	{
		Stack r = new Stack();

		if (a.isOne()) {
			r.push("0");
		} else {
			r.push(a);
			r.push("ln");
		}

		return r;
	}

	static Stack inverse(Stack a) // r = 1/a
	{
		Stack r = new Stack();

		if (a.isOne()) {
			r.push("1");
		} else {
			r.push("1");
			r.push(a);
			r.push("/");
		}

		return r;
	}

	static Stack negate(Stack a) // r = -a
	{
		Stack r = new Stack();

		if (a.isZero()) {
			r.push("0");
		} else {
			r.push(a);
			r.push("neg");
		}

		return r;
	}

	static Stack square(Stack a) // r = a^2
	{
		Stack r = new Stack();

		if (a.isZero()) {
			r.push("0");
		} else if (a.isOne()) {
			r.push("1");
		} else {
			r.push(a);
			r.push("2");
			r.push("^");
		}

		return r;
	}

	static Stack sqrt(Stack a) // r = sqrt a
	{
		Stack r = new Stack();

		if (a.isOne()) {
			r.push("1");
		} else {
			r.push(a);
			r.push("sqrt");
		}

		return r;
	}

	static Stack abs(Stack a) // r = sqrt a
	{
		Stack r = new Stack();

		r.push(a);
		r.push("abs");

		return r;
	}

	public EvaluateExpression derivative() {
		Stack rec = this.pf.copy();

		Stack der = derivative(rec);

		return new EvaluateExpression(der);
	}

	private static Stack derivative(Stack rec) {
		if (rec.isEmpty())
			return null;

		Stack der = new Stack();
		Token tk = rec.pop();

		if (tk.isNumber()) {
			if (tk.is("x")) {
				der.push("1");
			} else {
				der.push("0");
			}
		} else {
			if (tk.is("+")) {
				Stack r = derivative(rec);
				Stack l = derivative(rec);
				der.push(add(l, r));
			} else if (tk.is("-")) {
				Stack r = derivative(rec);
				Stack l = derivative(rec);

				der.push(subtract(l, r));
			} else if (tk.is("*")) {
				Stack v = sub(rec);
				Stack dv = derivative(v.copy());
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(add(multiply(v, du), multiply(u, dv)));

			} else if (tk.is("/")) {
				Stack v = sub(rec);
				Stack dv = derivative(v.copy());
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack two = new Stack();
				two.push("2");

				der.push(
					divide(
						subtract(multiply(v, du), multiply(u, dv)),
						pow(v, two)));
			} else if (tk.is("^")) {
				Stack h = sub(rec);
				Stack dh = derivative(h.copy());
				Stack g = sub(rec);
				Stack dg = derivative(g.copy());

				if (!h.isConstant()) {
					der.push(
						multiply(
							add(
								multiply(h, divide(dg, g)),
								multiply(dh, ln(g))),
							pow(g, h)));
				} else {
					EvaluateExpression p = new EvaluateExpression(h);
					Stack h2 = new Stack();
					h2.push("" + Math.abs(p.evaluate(0).doubleValue() - 1.0));
					if (p.evaluate(0).doubleValue() - 1.0 < 0)
						h2.push("neg");
					der.push(multiply(multiply(h, pow(g, h2)), dg));
				}
			} else if (tk.is("neg")) {
				Stack d = derivative(sub(rec).copy());

				der.push(d);
				der.push("neg");
			} else if (tk.is("abs")) {
				Stack h = sub(rec);
				Stack dh = derivative(h.copy());

				der.push(multiply(divide(abs(h), h), dh));
			} else if (tk.is("exp")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				if (!u.isZero()) {
					der.push(u);
					der.push("exp");
					der = multiply(der, du);
				} else {
					der.push("1");
				}
			} else if (tk.is("ln")) {
				Stack h = sub(rec);
				Stack dh = derivative(h.copy());

				der.push(divide(dh, h));
			} else if (tk.is("log")) {
				Stack h = sub(rec);
				Stack dh = derivative(h.copy());

				Stack ln10 = new Stack();
				ln10.push("10");
				ln10.push("ln");

				der.push(divide(dh, multiply(ln10, h)));
			} else if (tk.is("sin")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack p = new Stack();
				p.push(u);
				p.push("cos");

				der.push(multiply(du, p));
			} else if (tk.is("cos")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack p = new Stack();
				p.push(u);
				p.push("sin");
				p.push("neg");

				der.push(multiply(du, p));
			} else if (tk.is("tan")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("sec");
				der.push("2");
				der.push("^");

				der = multiply(du, der);
			} else if (tk.is("sec")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("sec");
				der.push(u);
				der.push("tan");
				der.push("*");

				der = multiply(du, der);
			} else if (tk.is("csc")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("csc");
				der.push(u);
				der.push("cot");
				der.push("*");
				der.push("neg");

				der = multiply(du, der);
			} else if (tk.is("cot")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("csc");
				der.push("2");
				der.push("^");
				der.push("neg");

				der = multiply(du, der);
			} else if (tk.is("sqrt")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(".5");
				der.push(sqrt(u));
				der.push("/");

				der = multiply(du, der);
			} else if (tk.is("asin")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(multiply(du, inverse(sqrt(subtract(one, square(u))))));
			} else if (tk.is("acos")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(
					negate(
						multiply(du, inverse(sqrt(subtract(one, square(u)))))));
			} else if (tk.is("atan")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(multiply(du, inverse(add(one, square(u)))));
			} else if (tk.is("acot")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(negate(multiply(du, inverse(add(one, square(u))))));
			} else if (tk.is("asec")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(
					multiply(
						du,
						inverse(
							multiply(
								square(u),
								sqrt(subtract(one, inverse(square(u))))))));
			} else if (tk.is("acsc")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(
					negate(
						multiply(
							du,
							inverse(
								multiply(
									square(u),
									sqrt(
										subtract(one, inverse(square(u)))))))));
			} else if (tk.is("sinh")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("cosh");

				der = multiply(du, der);
			} else if (tk.is("cosh")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("sinh");

				der = multiply(du, der);
			} else if (tk.is("tanh")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(u);
				der.push("tanh");

				der = multiply(du, subtract(one, square(der)));
			} else if (tk.is("coth")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(u);
				der.push("coth");

				der = multiply(du, subtract(one, square(der)));
			} else if (tk.is("csch")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("csch");
				der.push("neg");
				der.push(u);
				der.push("coth");
				der.push("*");

				der = multiply(der, du);
			} else if (tk.is("sech")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				der.push(u);
				der.push("sech");
				der.push("neg");
				der.push(u);
				der.push("tanh");
				der.push("*");

				der = multiply(der, du);
			} else if (tk.is("asinh")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(multiply(du, inverse(sqrt(add(one, square(u))))));
			} else if (tk.is("acosh")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(
					multiply(
						du,
						inverse(
							multiply(
								sqrt(subtract(u, one)),
								sqrt(add(u, one))))));
			} else if (tk.is("atanh")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(multiply(du, inverse(subtract(one, square(u)))));
			} else if (tk.is("acoth")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(multiply(du, inverse(subtract(one, square(u)))));
			} else if (tk.is("asech")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(
					negate(
						multiply(
							du,
							inverse(
								multiply(
									square(u),
									sqrt(
										subtract(inverse(square(u)), one)))))));
			} else if (tk.is("acsch")) {
				Stack u = sub(rec);
				Stack du = derivative(u.copy());

				Stack one = new Stack();
				one.push("1");

				der.push(
					negate(
						multiply(
							du,
							inverse(
								multiply(
									square(u),
									sqrt(add(inverse(square(u)), one)))))));
			} else if (tk.is("ddx")) {
				Stack d2u = derivative(derivative(sub(rec).copy()));

				der.push(d2u);
			} else {
				der.push("?");
			}

		}

		return der;

	}

	public static Stack sub(Stack s) {
		Stack ret = new Stack();

		if (!s.isEmpty()) {
			Token tk = s.pop();

			if (tk.isNumber()) {
				ret.push(tk);
			} else if (
				tk.is("+")
					|| tk.is("-")
					|| tk.is("*")
					|| tk.is("/")
					|| tk.is("^")) {
				//Binary Operations

				Stack oper2 = sub(s);
				Stack oper1 = sub(s);
				ret.push(oper1);
				ret.push(oper2);
				ret.push(tk);
			} else if (
				tk.is("sin")
					|| tk.is("cos")
					|| tk.is("tan")
					|| tk.is("sec")
					|| tk.is("csc")
					|| tk.is("cot")
					|| tk.is("asin")
					|| tk.is("acos")
					|| tk.is("atan")
					|| tk.is("asec")
					|| tk.is("acsc")
					|| tk.is("acot")
					|| tk.is("sinh")
					|| tk.is("cosh")
					|| tk.is("tanh")
					|| tk.is("sech")
					|| tk.is("csch")
					|| tk.is("coth")
					|| tk.is("asinh")
					|| tk.is("acosh")
					|| tk.is("atanh")
					|| tk.is("asech")
					|| tk.is("acsch")
					|| tk.is("acoth")
					|| tk.is("neg")
					|| tk.is("abs")
					|| tk.is("sqrt")
					|| tk.is("log")
					|| tk.is("ln")
					|| tk.is("exp")
					|| tk.is("ddx")) {
				//Unary Operations

				Stack oper1 = sub(s);
				ret.push(oper1);
				ret.push(tk);
			}
		}

		return ret;
	}

	public void evaluateY(ArrayList coOrdinateList) {
		for (int i = 0; i < coOrdinateList.size(); i++) {
			CoOrdinates coOrdinates = (CoOrdinates) coOrdinateList.get(i);
			coOrdinates.yValue = evaluate((coOrdinates.xValue).doubleValue());
			//System.out.println("x is : "+coOrdinates.xValue+"  y is : "+coOrdinates.yValue);
		}
	}

	public Double evaluate(double x) {
		double a = 0;
		Stack rpf = new Stack();
		StackDoub ws = new StackDoub();
		double oper1, oper2;

		rpf = pf.flip();

		try //to catches malformed inputs.
			{
			while (!rpf.isEmpty()) {
				Token tk = rpf.pop();

				if (tk.isNumber()) {
					if (tk.is("x"))
						ws.push(x);
					else if (tk.is("E"))
						ws.push(Math.E);
					else if (tk.is("PI"))
						ws.push(Math.PI);
					else if (tk.is("rnd"))
						ws.push(Math.random());
					else
						ws.push(Double.valueOf(tk.getContent()).doubleValue());
				} else if (tk.is("+")) {
					oper2 = ws.pop();
					oper1 = ws.pop();
					ws.push(oper1 + oper2);
				} else if (tk.is("-")) {
					oper2 = ws.pop();
					oper1 = ws.pop();
					ws.push(oper1 - oper2);
				} else if (tk.is("*")) {
					oper2 = ws.pop();
					oper1 = ws.pop();
					ws.push(oper1 * oper2);
				} else if (tk.is("/")) {
					oper2 = ws.pop();
					oper1 = ws.pop();
					ws.push(oper1 / oper2);
				} else if (tk.is("^")) {
					oper2 = ws.pop();
					oper1 = ws.pop();
					ws.push(Math.pow(oper1, oper2));
				} else if (tk.is("sin"))
					ws.push(Math.sin(ws.pop()));
				else if (tk.is("cos"))
					ws.push(Math.cos(ws.pop()));
				else if (tk.is("tan"))
					ws.push(Math.tan(ws.pop()));
				else if (tk.is("csc"))
					ws.push(1.0 / Math.sin(ws.pop()));
				else if (tk.is("sec"))
					ws.push(1.0 / Math.cos(ws.pop()));
				else if (tk.is("cot"))
					ws.push(1.0 / Math.tan(ws.pop()));
				else if (tk.is("asinh")) {
					oper1 = ws.pop();
					ws.push(Math.log(Math.sqrt(oper1 * oper1 + 1) + oper1));
				} else if (tk.is("acosh")) {
					oper1 = ws.pop();
					ws.push(Math.log(Math.sqrt(oper1 * oper1 - 1) + oper1));
				} else if (tk.is("atanh")) {
					oper1 = ws.pop();
					ws.push(Math.log((1 + oper1) / (1 - oper1)) / 2);
				} else if (tk.is("acsch")) {
					oper1 = 1 / ws.pop();
					ws.push(Math.log(Math.sqrt(oper1 * oper1 + 1) + oper1));
				} else if (tk.is("asech")) {
					oper1 = 1 / ws.pop();
					ws.push(Math.log(Math.sqrt(oper1 * oper1 - 1) + oper1));
				} else if (tk.is("acoth")) {
					oper1 = 1 / ws.pop();
					ws.push(Math.log((1 + oper1) / (1 - oper1)) / 2);
				} else if (tk.is("sinh")) {
					oper1 = ws.pop();
					ws.push((Math.exp(oper1) - Math.exp(-oper1)) / 2);
				} else if (tk.is("cosh")) {
					oper1 = ws.pop();
					ws.push((Math.exp(oper1) + Math.exp(-oper1)) / 2);
				} else if (tk.is("tanh")) {
					oper1 = ws.pop();
					ws.push(
						(Math.exp(oper1) - Math.exp(-oper1))
							/ (Math.exp(oper1) + Math.exp(-oper1)));
				} else if (tk.is("csch")) {
					oper1 = ws.pop();
					ws.push(2.0 / (Math.exp(oper1) - Math.exp(-oper1)));
				} else if (tk.is("sech")) {
					oper1 = ws.pop();
					ws.push(2.0 / (Math.exp(oper1) + Math.exp(-oper1)));
				} else if (tk.is("coth")) {
					oper1 = ws.pop();
					ws.push(
						(Math.exp(oper1) + Math.exp(-oper1))
							/ (Math.exp(oper1) - Math.exp(-oper1)));
				} else if (tk.is("asin"))
					ws.push(Math.asin(ws.pop()));
				else if (tk.is("acos"))
					ws.push(Math.acos(ws.pop()));
				else if (tk.is("atan"))
					ws.push(Math.atan(ws.pop()));
				else if (tk.is("acsc"))
					ws.push(Math.asin(1 / ws.pop()));
				else if (tk.is("asec"))
					ws.push(Math.acos(1 / ws.pop()));
				else if (tk.is("acot"))
					ws.push(Math.PI / 2 - Math.atan(ws.pop()));
				else if (tk.is("neg"))
					ws.push(-ws.pop());
				else if (tk.is("abs"))
					ws.push(Math.abs(ws.pop()));
				else if (tk.is("sqrt"))
					ws.push(Math.sqrt(ws.pop()));
				else if (tk.is("log"))
					ws.push(Math.log(ws.pop()) / Math.log(10));
				else if (tk.is("ln"))
					ws.push(Math.log(ws.pop()));
				else if (tk.is("exp"))
					ws.push(Math.exp(ws.pop()));
				else if (tk.is("sign")) {
					{
						oper1 = ws.pop();
						if (oper1 > 0)
							ws.push(1);
						else if (oper1 < 0)
							ws.push(-1);
						else
							ws.push(0);
					}
				}
			}
		} catch (NullPointerException exception) {
			//Primative malformed input detection.
			ws = new StackDoub();
			ws.push(Double.MIN_VALUE);
		}

		if (!ws.isEmpty())
			a = ws.pop();
		else
			a = Double.NaN;
		return new Double(a);
	}

	public Stack getStack() {
		return pf.copy();
	}

	public boolean isConstant() {
		return isConstant;
	}

	public String infix() {
		if (pf != null)
			return infix(pf.copy());

		return "?:?";
	}

	private static String infix(Stack s) {
		String str = "";

		Token tk = s.pop();

		if (tk.isBinary()) {
			Stack b = sub(s);
			Stack a = sub(s);

			str = "(" + infix(a) + tk.getContent() + infix(b) + ")";
		} else if (tk.isNumber()) {
			str = tk.getContent();
		} else {
			Stack a = sub(s);
			str = "(" + tk.getContent() + " " + infix(a) + ")";
		}

		return str;

	}

}
