package projects.ecommunicator.pptconverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import projects.ecommunicator.actions.PPtAction;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.PropertyFileNames;

public class PPtConverter implements Runnable {
	WhiteBoardDesktopPane whiteBoardDesktopPane;
	PPtAction pptAction;
	int pptPageNo;

	public PPtConverter(
		WhiteBoardDesktopPane whiteBoardDesktopPane,
		PPtAction pptAction,
		int pptPageNo) {
		this.whiteBoardDesktopPane = whiteBoardDesktopPane;
		this.pptAction = pptAction;
		this.pptPageNo = pptPageNo;
	}

	public void run() {
		convertPPtFilesToJPG();
	}

	private void convertPPtFilesToJPG() {
		String fileName;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(
			new File(
				Property.getString(PropertyFileNames.SCHOOL, "current_dir")));

		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				String fname = f.getName().toLowerCase();
				return fname.endsWith(".ppt");
			}
			public String getDescription() {
				return "Power Point Files";
			}
		});
		int r = chooser.showOpenDialog(whiteBoardDesktopPane);
		if (r == JFileChooser.APPROVE_OPTION) {
			fileName = chooser.getSelectedFile().getPath();
			if (fileName.endsWith(".ppt")) {

				// convert ppt file to images
				String[] TYPE_OF_SLIDE =
					{ "PowerPoint | Largest", "PowerPoint | Quality" };

				ArrayList slidePaths = null;
				String tempDirPath = null;
				try {
					String userHomeDir = System.getProperty("user.home");
					/*File tempDir =
						new File(
							userHomeDir
								+ File.separator
								+ "slides"
								+ File.separator
								+ pptPageNo);
					tempDir.deleteOnExit();
					*/
					File tempFile = File.createTempFile("slides", null);
					String tempFileName = tempFile.getName();
					tempFile.delete();

					File tempDir =
						new File(userHomeDir + File.separator + tempFileName);
					tempDir.deleteOnExit();
					tempDir.mkdirs();
					tempDirPath = tempDir.getCanonicalPath();
					if (!tempDirPath.endsWith(File.separator)) {
						tempDirPath = tempDirPath + File.separator;
					}
					System.out.println("temp dir path:" + tempDirPath);
				} catch (IOException io) {
				}
				try {
					slidePaths =
						convert(
							Property.getString(
								PropertyFileNames.SCHOOL,
								"ppt_converter_filePath"),
							tempDirPath,
							fileName,
							TYPE_OF_SLIDE[0]);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				pptAction.createNewPPtPages(slidePaths);

			} else {
				JOptionPane.showMessageDialog(
					whiteBoardDesktopPane,
					"You can only select Power Point files.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
				convertPPtFilesToJPG();
			}
		}
	}

	private ArrayList convert(
		String converterFilePath,
		String tempDirPath,
		String path,
		String arg)
		throws IOException {

		String absPathDst = converterFilePath;
		absPathDst = absPathDst.concat("pptconverter.exe");
		System.out.println("absPathDst:" + absPathDst);

		Runtime rt = Runtime.getRuntime();

		String command[] =
			{
				absPathDst,
				"\"" + tempDirPath + "\"",
				"\"" + path + "\"",
				"\"" + arg + "\"" };
		try {
			Process prcs = rt.exec(command);
			prcs.waitFor();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		ArrayList slidePaths = moveCurrentFiles(tempDirPath);
		return slidePaths;
	}

	private ArrayList moveCurrentFiles(String sourceDir) throws IOException {

		File[] fileNames = (new File(sourceDir)).listFiles();
		ArrayList list = null;
		if (fileNames != null) {
			list = new ArrayList();
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i].isFile()) {
					String name = fileNames[i].getName();
					if (name.endsWith(".jpg") || name.endsWith(".png")) {
						/*File tempFile =
							File.createTempFile("SLIDE", ".png", null);
						tempFile.deleteOnExit();
						ImageIO.write(
							ImageIO.read(fileNames[i]),
							"png",
							tempFile);
						list.add(tempFile.getCanonicalPath());
						fileNames[i].delete();
						*/
						list.add(fileNames[i].getCanonicalPath());
					} else {
						fileNames[i].delete();
					}
				}
			}
		}
		return list;
	}

}