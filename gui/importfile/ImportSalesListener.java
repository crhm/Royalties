package gui.importfile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;

import gui.RoyaltiesApp;
import importing.ImportFactory;
import main.SalesHistory;

/**ActionListener for user clicking "Import File" in the File menu.
 * Opens a file chooser and tries to import the files chosen by the user, by calling 
 * ImportFactory.ImportSales on each.
 * @author crhm
 */
public class ImportSalesListener implements ActionListener{

	final JFileChooser fc = new JFileChooser();

	private RoyaltiesApp appToUpdate;

	/**Necessary so that ImportSalesListener can notify RoyaltiesApp that its state has changed after the import 
	 * so that it updates the current panel.
	 * @param appToUpdate
	 */
	public void setAppToUpdate(RoyaltiesApp appToUpdate) {
		this.appToUpdate = appToUpdate;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new CustomFileFilter());

		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			for (File file : files) {
				if (SalesHistory.get().getImportedFiles().contains(file.getName())) {
					int userChoice = JOptionPane.showConfirmDialog(this.appToUpdate, "Warning! A file with this "
							+ "name has been imported before:\n" + file.getName() 
							+ "\nAre you sure you want to import this file anyway?"
							+ "\nIt may result in duplicate sales.", "Warning!", JOptionPane.YES_NO_OPTION , JOptionPane.WARNING_MESSAGE);
					if (userChoice == JOptionPane.YES_OPTION) {
						ImportFactory.ImportSales(file.getPath());
					}
				} else {
					ImportFactory.ImportSales(file.getPath());
				}
			}
			appToUpdate.stateChanged(new ChangeEvent("Files Imported"));
		}
	}

}

class CustomFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		Boolean isCorrectFileExtension = false;
        String s = f.getName();
        if (s.endsWith(".csv") || s.endsWith(".txt")) {
        	isCorrectFileExtension = true;
        }
		return isCorrectFileExtension;
	}

	@Override
	public String getDescription() {
		return "Only csv and txt files";
	}
	
}
