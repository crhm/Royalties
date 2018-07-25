package gui.importfile.newformatpanels;

import javax.swing.JPanel;

import gui.importfile.NewImportFormatDialog;

@SuppressWarnings("serial")
public abstract class FormatDetailsPanel extends JPanel {
	
	protected NewImportFormatDialog overallDialog;

	public abstract Boolean validateUserInput();
	
	public abstract void saveUserInput();
}
