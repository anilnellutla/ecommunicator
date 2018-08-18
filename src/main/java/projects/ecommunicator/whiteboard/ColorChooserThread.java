package projects.ecommunicator.whiteboard;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import projects.ecommunicator.internalframes.WhiteBoard;

public class ColorChooserThread implements Runnable, ChangeListener {
    WhiteBoard whiteBoard;
    JColorChooser colorChooser;

    public ColorChooserThread(WhiteBoard whiteBoard) {
        this.whiteBoard = whiteBoard;
    }

    public void run() {

		colorChooser = new JColorChooser(Color.black);
		colorChooser.setPreviewPanel(new JPanel());

		JDialog colorChooserDialog = JColorChooser.createDialog(whiteBoard,"Color",true,colorChooser,null,null);
		colorChooser.getSelectionModel().addChangeListener(this);
		JDialog.setDefaultLookAndFeelDecorated(true);
		colorChooserDialog.setSize(460,310);
		colorChooserDialog.show();
    }

    public void stateChanged(ChangeEvent evt) {
        whiteBoard.getCanvas().setColor(colorChooser.getColor());
    }

}