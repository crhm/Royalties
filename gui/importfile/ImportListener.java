package gui.importfile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import gui.RoyaltiesApp;
import importing.DataAlreadyImportedException;
import importing.ImportFactory;

/**ActionListener for user clicking "Import File" in the File menu.
 * Opens a file chooser and tries to import the files chosen by the user, by calling 
 * ImportFactory.ImportData on each.
 * @author crhm
 */
public class ImportListener implements ActionListener{

	final JFileChooser fc = new JFileChooser();

	private RoyaltiesApp appToUpdate;

	/**Necessary so that ImportListener can notify RoyaltiesApp that its state has changed after the import 
	 * so that it updates the current panel.
	 * @param appToUpdate
	 */
	public void setAppToUpdate(RoyaltiesApp appToUpdate) {
		this.appToUpdate = appToUpdate;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fc.setMultiSelectionEnabled(true);

		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			for (File file : files) {
				try {
					ImportFactory.ImportData(file.getPath());
				} catch (DataAlreadyImportedException ex) {
					String template = "<html><body><p style='width: 200px;'>%s</p></body></html>";
					String message = "Please note: " + file.getName() + " was not imported because "
							+ "data for this channel and this date were already present in the app.";
					String toDisplay = String.format(template, message);
					JOptionPane.showMessageDialog(null, toDisplay, "Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
			appToUpdate.stateChanged(new ChangeEvent("Files Imported"));
		}
	}

}
