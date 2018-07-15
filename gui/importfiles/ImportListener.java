package gui.importfiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import importing.ImportFactory;

public class ImportListener implements ActionListener{

	final JFileChooser fc = new JFileChooser();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		int returnVal = fc.showOpenDialog(null);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			 File file = fc.getSelectedFile();
			 ImportFactory.ImportData(file.getPath());
		}
	}

}
