package projects.ecommunicator.internalframes;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import projects.ecommunicator.actions.SnapGraphAction;
import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.graph.ComponentPanel;
import projects.ecommunicator.graph.GraphPanel;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

public class Graph extends JInternalFrame implements ActionListener {

	private GraphPanel graph;
	private ComponentPanel component;

	private JButton zoomIn;
	private JButton zoomOut;
	private JButton moveAround;
	private JButton marker;
	private JButton snapButton;

	private Dimension size;
	private Cursor zoomInCursor;
	private Cursor zoomOutCursor;
	private WhiteBoardDesktopPane whiteBoardDesktopPane;

	public static void main(String argsp[])
	{
		
	}
	
	
	public Graph() {

		super("Graph Calculator");	
		setFocusable(true);	
		//this.whiteBoardDesktopPane = whiteBoardDesktopPane;

		setSize(800, 600);
		getContentPane().setLayout(new BorderLayout());

		graph = new GraphPanel();
		component = new ComponentPanel(this);
		graph.setComponentPanel(component);
		component.setGraphPanel(graph);

		JPanel graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		graphPanel.setBorder(ScoolConstants.ETCHED_BORDER);

		JPanel centerPanel = new JPanel();
		centerPanel.setSize(graph.getPreferredSize());
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(
			(EtchedBorder) BorderFactory.createEtchedBorder());
		centerPanel.add(graph, BorderLayout.CENTER);

		graphPanel.add(centerPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBorder(
			(EtchedBorder) BorderFactory.createEtchedBorder());

		zoomIn =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "GF_ZoomIn_Gif"))));
		zoomIn.setMargin(new Insets(0, 0, 0, 0));
		zoomIn.addActionListener(this);
		zoomIn.setToolTipText("Zoom In ");

		zoomOut =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "GF_ZoomOut_Gif"))));
		zoomOut.setMargin(new Insets(0, 0, 0, 0));
		zoomOut.addActionListener(this);
		zoomOut.setToolTipText("Zoom Out ");

		moveAround =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "GF_Move_Gif"))));
		moveAround.setMargin(new Insets(0, 0, 0, 0));
		moveAround.addActionListener(this);
		moveAround.setToolTipText("Move Around");

		marker =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "GF_Marker_Gif"))));
		marker.setMargin(new Insets(0, 0, 0, 0));
		marker.addActionListener(this);
		marker.setToolTipText("Marker");

		snapButton =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "GF_SnapGraph_Gif"))));
		snapButton.setMargin(new Insets(0, 0, 0, 0));
		snapButton.addActionListener(this);
		snapButton.setToolTipText("Snap Graph");
		Action snapGraphAction = new SnapGraphAction(this);
		snapButton.addActionListener(snapGraphAction);

		size = Toolkit.getDefaultToolkit().getBestCursorSize(32, 32);
		if (size.width == 32 && size.height == 32) {
			Point hotspot = new Point(9, 5);
			ImageIcon zoomInIcon =
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "GF_ZoomIn_Cursor_Gif")));
			ImageIcon zoomOutIcon =
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "GF_ZoomOut_Cursor_Gif")));

			zoomInCursor =
				Toolkit.getDefaultToolkit().createCustomCursor(
					zoomInIcon.getImage(),
					hotspot,
					"zoomIn");
			zoomOutCursor =
				Toolkit.getDefaultToolkit().createCustomCursor(
					zoomOutIcon.getImage(),
					hotspot,
					"zoomOut");
		}

		buttonPanel.add(zoomIn);
		buttonPanel.add(zoomOut);
		buttonPanel.add(moveAround);
		buttonPanel.add(marker);
		buttonPanel.add(snapButton);
		graphPanel.add(buttonPanel, BorderLayout.SOUTH);

		JPanel componentPanel = new JPanel();
		componentPanel.setLayout(new BorderLayout());
		componentPanel.setBorder(
			(EtchedBorder) BorderFactory.createEtchedBorder());
		componentPanel.add(component, BorderLayout.NORTH);

		// add panels to the GraphPanel
		 ((JComponent) getContentPane()).add(graphPanel, BorderLayout.CENTER);
		((JComponent) getContentPane()).add(componentPanel, BorderLayout.EAST);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension graphSize = getSize();
		setLocation(
			(screenSize.width - graphSize.width) / 2,
			(screenSize.height - graphSize.height) / 2);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == zoomIn) {
			graph.setCursor(zoomInCursor);
			graph.setMoveArround(false);
			graph.setPointer(false);
			if (graph.getZoomOut()) {
				graph.setZoomOut(false);
			}
			graph.setZoomIn(true);

		} else if (evt.getSource() == zoomOut) {
			graph.setCursor(zoomOutCursor);
			graph.setMoveArround(false);
			graph.setPointer(false);
			if (graph.getZoomIn()) {
				graph.setZoomIn(false);
			}
			graph.setZoomOut(true);

		} else if (evt.getSource() == moveAround) {
			graph.resetZoom();
			graph.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			graph.setPointer(false);
			if (graph.getMoveArround() == false) {
				graph.setMoveArround(true);
			}

		} else if (evt.getSource() == marker) {
			graph.resetZoom();
			graph.setMoveArround(false);
			if (graph.getPointer() == false) {
				graph.setPointer(true);
			}
			graph.setCursor(
				Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}

	public WhiteBoardFrame getWhiteBoardFrame() {
		return whiteBoardDesktopPane.getWhiteBoardPanel().getWhiteBoardFrame();
	}

	public GraphPanel getGraphPanel() {
		return graph;
	}

}
