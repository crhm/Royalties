package gui.importfile.newformatpanels;

import gui.importfile.NewImportFormatDialog;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class DateFormatDetailsPanel extends FormatDetailsPanel {
	private JTextField tfOldDateFormat;
	
	public DateFormatDetailsPanel(NewImportFormatDialog overallDialog) {
		this.overallDialog = overallDialog;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JTextArea txtrFormatExplanation = new JTextArea();
		txtrFormatExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrFormatExplanation.setEnabled(false);
		txtrFormatExplanation.setEditable(false);
		txtrFormatExplanation.setForeground(new Color(0, 0, 0));
		txtrFormatExplanation.setBackground(new Color(238, 238, 238));
		txtrFormatExplanation.setLineWrap(true);
		txtrFormatExplanation.setWrapStyleWord(true);
		txtrFormatExplanation.setText("Please indicate the format that the date provided in the csv follows. "
				+ "Date formats are indicated by different combinations of the letters 'd' (for days), 'M' (for months) and 'y' (for years). "
				+ "Please note that capitalisation is important.\nSome examples:\n"
				+ "20/03/2017 -> dd/MM/yyyy\n"
				+ "03-20-17 -> MM-dd-yy\n"
				+ "20170320 -> yyyyMMdd\n"
				+ "Mar 2017 -> MMM yyyy\n");
		GridBagConstraints gbc_txtrFormatExplanation = new GridBagConstraints();
		gbc_txtrFormatExplanation.insets = new Insets(5, 5, 5, 5);
		gbc_txtrFormatExplanation.fill = GridBagConstraints.BOTH;
		gbc_txtrFormatExplanation.gridx = 0;
		gbc_txtrFormatExplanation.gridy = 0;
		add(txtrFormatExplanation, gbc_txtrFormatExplanation);
		
		tfOldDateFormat = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		add(tfOldDateFormat, gbc_textField);
		tfOldDateFormat.setColumns(10);
	}

	@Override
	public Boolean validateUserInput() {
		Boolean success = false;
		if (!tfOldDateFormat.getText().isEmpty()) {
			/*Cannot use String.matches() because that matches the entire string. So we use pattern to compile the regex and a matcher
			 * to find any matches.
			 * The regex given here matches any letter (regardless of capitalisation) or number except d, y and M.
			 */
			final Pattern p = Pattern.compile("(?![dyM])([a-zA-Z0-9])");
			final Matcher m = p.matcher(tfOldDateFormat.getText());
			if (!m.find()){ //If no illicit letters/numbers have been found
				try {
					new SimpleDateFormat(tfOldDateFormat.getText());				
					success = true;
				} catch (Exception e) {
					System.out.println("User provided incorrect date format.");
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	@Override
	public void saveUserInput() {
		this.overallDialog.setOldDateFormat(new SimpleDateFormat(tfOldDateFormat.getText()));	
	}

}
