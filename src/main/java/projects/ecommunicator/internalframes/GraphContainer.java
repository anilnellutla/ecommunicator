package projects.ecommunicator.internalframes;

import javax.swing.JDesktopPane;

public class GraphContainer extends JDesktopPane {

	
	public GraphContainer()
	{
		Graph g = new Graph();
		add(g);
	}
	public static void main(String args[])
	{
		GraphContainer gc= new GraphContainer();
		gc.setVisible(true);
	}
	
}
