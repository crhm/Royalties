package gui.importfiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import importing.ImportFactory;

/**ActionListener for user clicking "Import File" in the File menu.
 * Opens a file chooser and tries to import the files chosen by the user, by calling 
 * ImportFactory.ImportData on each.
 * @author crhm
 */
public class ImportListener implements ActionListener{

	final JFileChooser fc = new JFileChooser();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		fc.setMultiSelectionEnabled(true);
		
		int returnVal = fc.showOpenDialog(null);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			 File[] files = fc.getSelectedFiles();
			 for (File file : files) {
				 ImportFactory.ImportData(file.getPath());
			 }			
		}
	}

}
