package projects.ecommunicator.graph;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import projects.ecommunicator.internalframes.Graph;
import projects.ecommunicator.utility.ScoolConstants;

public class ComponentPanel
	extends JPanel
	implements ActionListener, ItemListener {

	private JComboBox scaleCombo;

	private JComboBox functionCombo;

	private JButton functionButton;
	private JTextField xMin;
	private JTextField xMax;
	private JTextField xScale;
	private JTextField yMin;
	private JTextField yMax;
	private JTextField yScale;


	private GraphPanel graphPanel;

	private double tempMidX;
	private double tempMidY;

	private ArrayList coOrdinateList;

	private String expression;

	private Graph graph;
	
	
	public static void main(String args[])
	{
		
		
	}

	public ComponentPanel(Graph graph) {
		this.graph = graph;
		coOrdinateList = new ArrayList();

		setLayout(new BorderLayout());
		EtchedBorder etched = ScoolConstants.ETCHED_BORDER;

		Font verdana = new Font("Verdana", Font.PLAIN, 11);
		Font verdanaBold = new Font("Verdana", Font.BOLD, 11);

		//default panel
		JPanel defaultPanel = new JPanel();
		defaultPanel.setBorder(etched);
		defaultPanel.setLayout(new GridLayout(1, 2));

		JLabel defaultLabel = new JLabel("Default Scale : ");
		defaultLabel.setFont(verdanaBold);
		JPanel defaultLabelPanel = new JPanel();
		defaultLabelPanel.setLayout(new BorderLayout());
		defaultLabelPanel.add(defaultLabel, BorderLayout.CENTER);

		String[] scale = { "default scale", "center scale" };
		scaleCombo = new JComboBox(scale);
		scaleCombo.addActionListener(this);
		scaleCombo.setFont(verdana);
		scaleCombo.setBackground(Color.WHITE);
		JPanel scaleComboPanel = new JPanel();
		scaleComboPanel.setLayout(new BorderLayout());
		scaleComboPanel.add(scaleCombo, BorderLayout.WEST);

		defaultPanel.add(defaultLabelPanel);
		defaultPanel.add(scaleComboPanel);

		//function Panel
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new BorderLayout());
		TitledBorder titledFunction =
			BorderFactory.createTitledBorder(
				etched,
				"Enter the Function",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				verdana);
		functionPanel.setBorder(titledFunction);

		JPanel functionTempPanel = new JPanel();
		functionTempPanel.setLayout(new BorderLayout());

		JLabel functionLabel = new JLabel(" Y : ");
		functionLabel.setFont(verdanaBold);
		functionLabel.setForeground(Color.RED);
		functionCombo = new JComboBox();
		functionCombo.setPreferredSize(new Dimension(20,15));
		functionCombo.setEditable(true);
		functionCombo.setFont(verdana);
		functionCombo.setBackground(Color.WHITE);
		functionCombo.addItemListener(this);

		functionButton = new JButton(" X : ");
		functionButton.setMargin(new Insets(0, 0, 0, 0));
		functionButton.setFont(verdanaBold);
		functionButton.addActionListener(this);

		functionTempPanel.add(functionButton, BorderLayout.WEST);
		JPanel functionCenterPanel = new JPanel();
		functionCenterPanel.setLayout(new BorderLayout());
		functionCenterPanel.add(functionLabel, BorderLayout.WEST);
		functionCenterPanel.add(functionCombo, BorderLayout.CENTER);
		functionTempPanel.add(functionCenterPanel, BorderLayout.CENTER);

		functionPanel.add(functionTempPanel, BorderLayout.CENTER);

		//xRangePanel
		JPanel xRangePanel = new JPanel();
		xRangePanel.setLayout(new GridLayout(3, 1));
		TitledBorder titledXRange =
			BorderFactory.createTitledBorder(
				etched,
				"X Range",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				verdana);
		xRangePanel.setBorder(titledXRange);

		JLabel xMinLabel = new JLabel("min   x : ");
		xMinLabel.setFont(verdanaBold);
		xMin = new JTextField("-8.00", 10);
		xMin.addActionListener(this);
		xMin.setFont(verdana);

		JLabel xMaxLabel = new JLabel("max  x : ");
		xMaxLabel.setFont(verdanaBold);
		xMax = new JTextField("8.00", 10);
		xMax.addActionListener(this);
		xMax.setFont(verdana);

		JLabel xScaleLabel = new JLabel("scale x : ");
		xScaleLabel.setFont(verdanaBold);
		xScale = new JTextField("1.0", 10);
		xScale.addActionListener(this);
		xScale.setFont(verdana);

		JPanel xMinPanel = new JPanel();
		xMinPanel.setLayout(new BorderLayout());
		xMinPanel.add(xMinLabel, BorderLayout.WEST);
		xMinPanel.add(xMin, BorderLayout.CENTER);
		xRangePanel.add(xMinPanel);

		JPanel xMaxPanel = new JPanel();
		xMaxPanel.setLayout(new BorderLayout());
		xMaxPanel.add(xMaxLabel, BorderLayout.WEST);
		xMaxPanel.add(xMax, BorderLayout.CENTER);
		xRangePanel.add(xMaxPanel);

		JPanel xScalePanel = new JPanel();
		xScalePanel.setLayout(new BorderLayout());
		xScalePanel.add(xScaleLabel, BorderLayout.WEST);
		xScalePanel.add(xScale, BorderLayout.CENTER);
		xRangePanel.add(xScalePanel);

		//yRangePanel
		JPanel yRangePanel = new JPanel();
		yRangePanel.setLayout(new GridLayout(3, 1));
		TitledBorder titledYRange =
			BorderFactory.createTitledBorder(
				etched,
				"Y Range",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				verdana);
		yRangePanel.setBorder(titledYRange);

		JLabel yMinLabel = new JLabel("min   y : ");
		yMinLabel.setFont(verdanaBold);
		yMin = new JTextField("-8.00", 10);
		yMin.addActionListener(this);
		yMin.setFont(verdana);

		JLabel yMaxLabel = new JLabel("max  y : ");
		yMaxLabel.setFont(verdanaBold);
		yMax = new JTextField("8.00", 10);
		yMax.addActionListener(this);
		yMax.setFont(verdana);

		JLabel yScaleLabel = new JLabel("scale y : ");
		yScaleLabel.setFont(verdanaBold);
		yScale = new JTextField("1.0", 10);
		yScale.addActionListener(this);
		yScale.setFont(verdana);

		JPanel yMinPanel = new JPanel();
		yMinPanel.setLayout(new BorderLayout());
		yMinPanel.add(yMinLabel, BorderLayout.WEST);
		yMinPanel.add(yMin, BorderLayout.CENTER);
		yRangePanel.add(yMinPanel);

		JPanel yMaxPanel = new JPanel();
		yMaxPanel.setLayout(new BorderLayout());
		yMaxPanel.add(yMaxLabel, BorderLayout.WEST);
		yMaxPanel.add(yMax, BorderLayout.CENTER);
		yRangePanel.add(yMaxPanel);

		JPanel yScalePanel = new JPanel();
		yScalePanel.setLayout(new BorderLayout());
		yScalePanel.add(yScaleLabel, BorderLayout.WEST);
		yScalePanel.add(yScale, BorderLayout.CENTER);
		yRangePanel.add(yScalePanel);

		//add panels internally

		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new BorderLayout());
		firstPanel.add(defaultPanel, BorderLayout.NORTH);

		add(firstPanel, BorderLayout.CENTER);

		JPanel secondPanel = new JPanel();
		secondPanel.setLayout(new BorderLayout());
		secondPanel.add(functionPanel, BorderLayout.NORTH);
		firstPanel.add(secondPanel, BorderLayout.CENTER);

		JPanel thirdPanel = new JPanel();
		thirdPanel.setLayout(new BorderLayout());
		thirdPanel.add(xRangePanel, BorderLayout.NORTH);
		secondPanel.add(thirdPanel, BorderLayout.CENTER);

		JPanel fourthPanel = new JPanel();
		fourthPanel.setLayout(new BorderLayout());
		fourthPanel.add(yRangePanel, BorderLayout.NORTH);
		thirdPanel.add(fourthPanel, BorderLayout.CENTER);

		//pack();



	}

	public void actionPerformed(ActionEvent evt) {
		FunctionDialog functionDialog = null;

		if (evt.getSource() == functionButton) {
			if (functionDialog == null) {
				functionDialog = new FunctionDialog(this);
			}
			functionDialog.show();
		}

		if (evt.getSource() == scaleCombo) {
			JComboBox combo = (JComboBox) evt.getSource();
			String selectedItem = (String) combo.getSelectedItem();
			if (selectedItem.equals("default scale")) {

				graphPanel.setMidX(tempMidX);
				graphPanel.setMidY(tempMidY);
				graphPanel.setRanges();
				drawFunction();

			} else if (selectedItem.equals("center scale")) {

				double panelWidth = graphPanel.getPanelWidth();
				double panelHeight = graphPanel.getPanelHeight();

				tempMidX = graphPanel.getMidX();
				tempMidY = graphPanel.getMidY();

				graphPanel.setMidX((panelWidth / 2));
				graphPanel.setMidY((panelHeight / 2));
				graphPanel.setRanges();
				drawFunction();
			}
		}
		if (evt.getSource() == xMin
			|| evt.getSource() == xMax
			|| evt.getSource() == yMin
			|| evt.getSource() == yMax) {

			if (compareX() && compareY()) {
				setXRange();
				setYRange();
				moveMidPoints();
			}
		}

		if (evt.getSource() == xScale || evt.getSource() == yScale) {

			if (compareX() && compareY()) {
				drawFunction();
			}
		}
	}

	public void itemStateChanged(ItemEvent evt) {

		if (evt.getStateChange() == ItemEvent.SELECTED) {
			JComboBox comboBox = (JComboBox) evt.getSource();
			String selectedItem = (String) comboBox.getSelectedItem();
			int itemCount = comboBox.getItemCount();
			boolean addItem = true;

			if (!(selectedItem.equals(""))) {
				if (itemCount > 0) {
					for (int i = 0; i <= itemCount; i++) {
						String oldItem = (String) comboBox.getItemAt(i);
						if (selectedItem.equals(oldItem)) {
							addItem = false;
							break;
						}
					}
				}
				if (addItem) {
					comboBox.addItem(selectedItem);
				}
				expression = selectedItem;
				drawFunction();
			}
		}
	}

	private boolean compareX() {

		double xMinRange = Double.parseDouble(xMin.getText());
		double xMaxRange = Double.parseDouble(xMax.getText());

		if (xMinRange > xMaxRange || xMinRange == xMaxRange) {
			JOptionPane.showMessageDialog(
				this,
				"MinX  should always be less than MaxX.",
				"Invalid X Ranges",
				JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean compareY() {

		double yMinRange = Double.parseDouble((yMin.getText()));
		double yMaxRange = Double.parseDouble(yMax.getText());

		if (yMinRange > yMaxRange || yMinRange == yMaxRange) {
			JOptionPane.showMessageDialog(
				this,
				"MinY  should always be less than MaxY.",
				"Invalid Y Ranges",
				JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public void setXRange() {

		graphPanel.setXMinRange(Double.parseDouble(xMin.getText()));
		graphPanel.setXMaxRange(Double.parseDouble(xMax.getText()));
		graphPanel.setXScale(Double.parseDouble(xScale.getText()));

		double verticalLines =
			(graphPanel.getXMaxRange() - graphPanel.getXMinRange())
				/ graphPanel.getXScale();

		graphPanel.setGridWidth(graphPanel.getPanelWidth() / verticalLines);

	}

	public void setYRange() {

		graphPanel.setYMinRange(Double.parseDouble(yMin.getText()));
		graphPanel.setYMaxRange(Double.parseDouble(yMax.getText()));
		graphPanel.setYScale(Double.parseDouble(yScale.getText()));

		double horizontalLines =
			(graphPanel.getYMaxRange() - graphPanel.getYMinRange())
				/ graphPanel.getYScale();

		graphPanel.setGridHeight(graphPanel.getPanelHeight() / horizontalLines);

	}

	private void moveMidPoints() {

		double xMinRange = graphPanel.getXMinRange();
		double gridWidth = graphPanel.getGridWidth();
		double xScale = graphPanel.getXScale();

		double yMinRange = graphPanel.getYMinRange();
		double panelHeight = graphPanel.getPanelHeight();
		double gridHeight = graphPanel.getGridHeight();
		double yScale = graphPanel.getYScale();

		xMinRange = 0 - xMinRange;
		double midX = xMinRange * (gridWidth / xScale);

		graphPanel.setMidX(midX);

		yMinRange = 0 - yMinRange;
		double midY = panelHeight - (yMinRange * (gridHeight / yScale));

		graphPanel.setMidY(midY);

	}

	public void drawFunction() {
		if (compareX() && compareY()) {
			try {
				setXRange();
				setYRange();
				if (!expression.equals("")) {
					calculateCoOrdinates();
					graphPanel.repaint();
				}
			} catch (Exception exc) {
			}

		}
	}

	public void calculateCoOrdinates() {

		double xMinRange = graphPanel.getXMinRange();
		double xMaxRange = graphPanel.getXMaxRange();

		coOrdinateList.clear();

		int index = 0;

		for (double xValue = xMinRange; xValue <= xMaxRange; xValue += 0.05) {

			CoOrdinates coOrdinates = new CoOrdinates();
			coOrdinates.xValue = new Double(xValue);
			coOrdinateList.add(index, coOrdinates);
			index++;

		}

		EvaluateExpression evaluateExpression =
			new EvaluateExpression(expression);
		evaluateExpression.evaluateY(coOrdinateList);

	}

	public void setGraphPanel(GraphPanel graphPanel) {
		this.graphPanel = graphPanel;
	}

	public JTextField getXMin() {
		return xMin;
	}

	public JTextField getXMax() {
		return xMax;
	}

	public JTextField getYMin() {
		return yMin;
	}

	public JTextField getYMax() {
		return yMax;
	}

	public String getExpression() {
		return expression;
	}

	public ArrayList getCoOrdinateList() {
		return coOrdinateList;
	}

	public Graph getGraph() {
		return graph;
	}

	public JComboBox getFunctionCombo(){
		return functionCombo;
	}

}