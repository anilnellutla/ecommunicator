package projects.ecommunicator.graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FunctionDialog extends JDialog implements ActionListener {

	private JLabel yResultLabel;
	private JTextField xField;
	private JTextField functionField;
	private JButton cancelButton;
	private String expression;

	public FunctionDialog(ComponentPanel componentPanel) {

		super(
			componentPanel.getGraph().getWhiteBoardFrame(),
			"Evaluate Function",
			true);

		Font verdana = new Font("Verdana", Font.PLAIN, 11);
		Font verdanaBold = new Font("Verdana", Font.BOLD, 11);

		setResizable(false);
		setSize(325, 150);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent evt) {
				dispose();
			}
		});
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dialogSize = getSize();
		setLocation(
			(screenSize.width - dialogSize.width) / 2,
			(screenSize.height - dialogSize.height) / 2);

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(3, 1));

		//evaluate label
		JLabel evaluateLabel = new JLabel("Function to Evaluate : ");
		evaluateLabel.setFont(verdanaBold);
		//function textField
		functionField = new JTextField("", 5);
		functionField.setFont(verdana);
		functionField.setText(
			(String) componentPanel.getFunctionCombo().getSelectedItem());
		functionField.addActionListener(this);
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new BorderLayout());
		functionPanel.add(functionField, BorderLayout.WEST);

		//xValue Label
		JLabel xValueLabel = new JLabel("Value of X :");
		xValueLabel.setFont(verdanaBold);
		//xField textField
		xField = new JTextField("", 5);
		xField.setFont(verdana);
		xField.addActionListener(this);

		//yValue Label
		JLabel yValueLabel = new JLabel("Value of Y : ");
		yValueLabel.setFont(verdanaBold);
		//yField textField
		yResultLabel = new JLabel("0", JLabel.CENTER);
		yResultLabel.setFont(verdanaBold);

		JPanel xPanel = new JPanel();
		xPanel.setLayout(new BorderLayout());
		xPanel.add(xField, BorderLayout.WEST);

		northPanel.add(evaluateLabel);
		northPanel.add(functionField);
		northPanel.add(xValueLabel);
		northPanel.add(xPanel);
		northPanel.add(yValueLabel);
		northPanel.add(yResultLabel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton markButton = new JButton("Mark");
		markButton.setFont(verdanaBold);
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(verdanaBold);
		cancelButton.addActionListener(this);

		buttonPanel.add(markButton);
		buttonPanel.add(cancelButton);

		getContentPane().add(northPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		setFocusable(true);

	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == functionField) {
			calculateY();
		}

		if (evt.getSource() == xField) {
			calculateY();

		}
		if (evt.getSource() == cancelButton) {
			setVisible(false);
		}
	}

	private void calculateY() {
		expression = functionField.getText();
		double xValue = Double.parseDouble(xField.getText());
		EvaluateExpression evaluateExpression =
			new EvaluateExpression(expression);
		Double yValue = evaluateExpression.evaluate(xValue);
		yResultLabel.setText(printYValue(yValue.toString()));

	}

	private static String printYValue(String yValue) {
		String input = yValue;
		String output = "";

		int index = input.indexOf('.');
		if (index == -1) {
			output = input + ".00";
		} else {
			try {
				output =
					input.substring(0, index)
						+ "."
						+ input.substring(index + 1, index + 3);
			} catch (Exception ex) {
				output = input;
			}
		}
		//System.out.println(output);
		return output;
	}
}
