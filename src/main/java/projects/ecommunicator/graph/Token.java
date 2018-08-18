package projects.ecommunicator.graph;

public class Token {
	public static String[] validString =
			{
				"0",
				"1",
				"2",
				"3",
				"4",
				"5",
				"6",
				"7",
				"8",
				"9",
				".",
				"(",
				")",
				"^",
				"*",
				"/",
				"+",
				"-",
				"sinh",
				"cosh",
				"tanh",
				"csch",
				"sech",
				"coth",
				"asinh",
				"acosh",
				"atanh",
				"acsch",
				"asech",
				"acoth",
				"sin",
				"cos",
				"tan",
				"csc",
				"sec",
				"cot",
				"asin",
				"acos",
				"atan",
				"acsc",
				"asec",
				"acot",
				"sqrt",
				"neg",
				"log",
				"abs",
				"ln",
				"exp",
				"sign",
				"ddx",
				"rnd",
				"x",
				"PI",
				"E" };

		public int index = -1;
		public String content = "";
		boolean numerical;
		int length;

		Token(String str) {
			String t = str.trim().toLowerCase();
			numerical = false;

			for (int i = 0; i < validString.length && content.equals(""); i++) {
				if (t.startsWith(validString[i].toLowerCase())) {
					content = validString[i];
					length = content.length();

					index = i;

					if (i < 11) {
						content = getValue(t);
						//System.out.println("content ="+content);


						if (content.equals("?"))
							index = -1;
						numerical = (index != -1);
					} else {
						numerical =
							(content.equals("x")
								|| content.equals("PI")
								|| content.equals("E")
								|| content.equals("rnd"));
					}
				}
			}

			if (index >= 0 && index < 11)
				index = 0; //Makes sure all numerical values have index 0
		}

		public boolean is(String a) {
			return getContent().equalsIgnoreCase(a);
		}

		public boolean isValid() {
			return index != -1;
		}

		public boolean isNumber() {
			return (numerical);
		}

		public boolean isOperation() {
			return (precedence() > 0);
		}

		public boolean isBinary() {
			return (precedence() == 4 || precedence() == 3 || precedence() == 2);
		}

		public String getContent() {
			return content;
		}

		public float precedence() {
			float order = -1;
			Token tk = this;

			String tk_str = tk.getContent().toLowerCase();

			if (tk_str.equals("(")) {
				order = 5;
			} else if (tk_str.equals(")")) {
				order = 6;
			} else if (tk_str.equals("+")) {
				order = 4;
			} else if (tk_str.equals("-")) {
				order = 4;
			} else if (tk_str.equals("*")) {
				order = 3;
			} else if (tk_str.equals("/")) {
				order = 3;
			} else if (tk_str.equals("^")) {
				order = 2;
			} else if (
				tk_str.equals("sin")
				|| //Trig functions
			tk_str.equals("cos")
					|| tk_str.equals("tan")
					|| tk_str.equals("csc")
					|| tk_str.equals("sec")
					|| tk_str.equals("cot")
					|| tk_str.equals("asin")
				|| //Inverse trig functions
			tk_str.equals("acos")
					|| tk_str.equals("atan")
					|| tk_str.equals("acsc")
					|| tk_str.equals("asec")
					|| tk_str.equals("acot")
					|| tk_str.equals("sinh")
				|| //Hyperbolic trig functions
			tk_str.equals("cosh")
					|| tk_str.equals("tanh")
					|| tk_str.equals("csch")
					|| tk_str.equals("sech")
					|| tk_str.equals("coth")
					|| tk_str.equals("asinh")
				|| //Inverse hyperbolic trig functions
			tk_str.equals("acosh")
					|| tk_str.equals("atanh")
					|| tk_str.equals("acsch")
					|| tk_str.equals("asech")
					|| tk_str.equals("acoth")
					|| tk_str.equals("ddx")
				|| //Derivative
			tk_str.equals("neg")
				|| //Negation
			tk_str.equals("sqrt")
				|| //Square root
			tk_str.equals("exp")
				|| //Exponential base e
			tk_str.equals("ln")
				|| //Natural log
			tk_str.equals("log")
				|| //Common log
			tk_str.equals("abs")
				|| //Absolute value
			tk_str.equals("sign") //sign
			) {
				order = 1;
			} else if (tk.isNumber()) {
				order = 0;
			} else {
				order = -1;
			}

			return order;
		}

		public String getValue(String s) {
			String ret = "";
			char[] input = s.toLowerCase().toCharArray();
			int state = 1;
			int i = 0;

			StackDoub st = new StackDoub();

			while (state < 30) {
				st.push(state);
							//System.out.println(state+" "+input[i]);

				switch (state) {
					case 1 :
						if (Character.isDigit(input[i]))
							state = 2;
						else if (input[i] == '.')
							state = 6;
						else
							state = 99;
						break;

					case 2 :
						if (Character.isDigit(input[i]))
							state = 2;
						else if (input[i] == 'e')
							state = 3;
						else if (input[i] == '.')
							state = 7;
						else {
							i = i - 1;
							state = 99;
						}
						break;

					case 3 :
						if (Character.isDigit(input[i]))
							state = 5;
						else if (input[i] == 'e') {
							i = i - 2;
							st.pop();
							state = 99;
						} else if (input[i] == '.')
							state = 8;
						else if (input[i] == '+' || input[i] == '-')
							state = 4;
						else
							state = 99;
						break;

					case 4 :
						if (Character.isDigit(input[i]))
							state = 5;
						else if (input[i] == 'e') {
							i = i - 3;
							st.pop();
							st.pop();
							state = 99;
						} else if (input[i] == '.') {
							i = i - 3;
							st.pop();
							st.pop();
							state = 99;
						} else if (input[i] == '+' || input[i] == '-') {
							i = i - 3;
							st.pop();
							st.pop();
							state = 99;
						} else
							state = 99;
						break;

					case 5 :
						if (Character.isDigit(input[i]))
							state = 5;
						else if (input[i] == '.')
							state = 8;
						else {
							i = i - 1;
							state = 99;
						}
						break;

					case 6 :
						if (Character.isDigit(input[i]))
							state = 7;
						else if (input[i] == 'e')
							state = 8;
						else if (input[i] == '.')
							state = 8;
						else if (input[i] == '+' || input[i] == '-')
							state = 8;
						else
							state = 99;
						break;

					case 7 :
						if (Character.isDigit(input[i]))
							state = 7;
						else if (input[i] == 'e')
							state = 3;
						else if (input[i] == '.')
							state = 8;
						else {
							i = i - 1;
							state = 99;
						}
						break;

					case 8 :
						break;
				}

				if (i == (input.length - 1) || state == 99) {
					if (state == 99)
						state = (int) st.pop();

					if (state != 2 && state != 7 && state != 5)
						state = 8;
					else
						state = 99;
				}

				if (state == 8) {
					s = "?";
					i = 0;
					state = 99;
				}

				i++;
			}

			length = i;

			ret = s.substring(0, length);

			return ret;

		}

		public int length() {
			return length;
		}

}
