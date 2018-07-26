package gui.importfile.newformatpanels;

import gui.importfile.NewImportFormatDialog;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;

@SuppressWarnings("serial")
public class DateDetailsPanel extends FormatDetailsPanel implements ActionListener {
	private JTextField tfDateColumn;
	private JTextField tfDateRow;
	private JTextField tfDateOffset;
	private JCheckBox chckbxDateRow;
	private JCheckBox chckbxDateOffset;
	private JLabel lblDateColumn;
	private JLabel lblDateRow;
	private JLabel lblDateOffset;

	public DateDetailsPanel(NewImportFormatDialog overallDialog) {
		this.overallDialog = overallDialog;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 80, 0};
		gridBagLayout.rowHeights = new int[]{0, 45, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		chckbxDateRow = new JCheckBox("<html>Does the file <b>LACK</b> a dedicated column giving the date for each sale?</html>");
		GridBagConstraints gbc_chckbxDateRow = new GridBagConstraints();
		gbc_chckbxDateRow.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDateRow.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxDateRow.anchor = GridBagConstraints.WEST;
		gbc_chckbxDateRow.gridwidth = 2;
		gbc_chckbxDateRow.gridx = 0;
		gbc_chckbxDateRow.gridy = 0;
		chckbxDateRow.addActionListener(this);
		add(chckbxDateRow, gbc_chckbxDateRow);

		JTextArea txtrDateRowExplanation = new JTextArea();
		txtrDateRowExplanation.setEditable(false);
		txtrDateRowExplanation.setEnabled(false);
		txtrDateRowExplanation.setText("Some files may not have a column listing the date at each sale line, and will only provide the"
				+ " date in a single cell (at the top of the file for example). Please tick the box only if the date is provided just once in the file, "
				+ "rather than at each sale line. You would then be asked to provide not only the number of the column in which the date is to be "
				+ "found, but also the row where the unique instance is.");
		txtrDateRowExplanation.setBackground(new Color(238, 238, 238));
		txtrDateRowExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrDateRowExplanation.setWrapStyleWord(true);
		txtrDateRowExplanation.setLineWrap(true);
		GridBagConstraints gbc_txtrDateRowExplanation = new GridBagConstraints();
		gbc_txtrDateRowExplanation.gridwidth = 2;
		gbc_txtrDateRowExplanation.insets = new Insets(0, 5, 5, 0);
		gbc_txtrDateRowExplanation.fill = GridBagConstraints.BOTH;
		gbc_txtrDateRowExplanation.gridx = 0;
		gbc_txtrDateRowExplanation.gridy = 1;
		add(txtrDateRowExplanation, gbc_txtrDateRowExplanation);

		lblDateColumn = new JLabel("Date Column Number:");
		GridBagConstraints gbc_lblDateColumn = new GridBagConstraints();
		gbc_lblDateColumn.anchor = GridBagConstraints.WEST;
		gbc_lblDateColumn.insets = new Insets(0, 5, 5, 5);
		gbc_lblDateColumn.gridx = 0;
		gbc_lblDateColumn.gridy = 2;
		add(lblDateColumn, gbc_lblDateColumn);

		tfDateColumn = new JTextField();
		GridBagConstraints gbc_tfDateColumn = new GridBagConstraints();
		gbc_tfDateColumn.insets = new Insets(0, 0, 5, 0);
		gbc_tfDateColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfDateColumn.gridx = 1;
		gbc_tfDateColumn.gridy = 2;
		add(tfDateColumn, gbc_tfDateColumn);
		tfDateColumn.setColumns(10);

		lblDateRow = new JLabel("Date Row Number:");
		lblDateRow.setEnabled(false);
		GridBagConstraints gbc_lblDateRow = new GridBagConstraints();
		gbc_lblDateRow.anchor = GridBagConstraints.WEST;
		gbc_lblDateRow.insets = new Insets(0, 5, 5, 5);
		gbc_lblDateRow.gridx = 0;
		gbc_lblDateRow.gridy = 3;
		add(lblDateRow, gbc_lblDateRow);

		tfDateRow = new JTextField();
		tfDateRow.setEditable(false);
		tfDateRow.setEnabled(false);
		GridBagConstraints gbc_tfDateRow = new GridBagConstraints();
		gbc_tfDateRow.insets = new Insets(0, 0, 5, 0);
		gbc_tfDateRow.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfDateRow.gridx = 1;
		gbc_tfDateRow.gridy = 3;
		add(tfDateRow, gbc_tfDateRow);
		tfDateRow.setColumns(10);

		chckbxDateOffset = new JCheckBox("<html>" + "Date provided needs to be offset by a number of months to be correct?" + "</html>");
		GridBagConstraints gbc_chckbxDateOffset = new GridBagConstraints();
		gbc_chckbxDateOffset.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxDateOffset.anchor = GridBagConstraints.WEST;
		gbc_chckbxDateOffset.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDateOffset.gridwidth = 2;
		gbc_chckbxDateOffset.gridx = 0;
		gbc_chckbxDateOffset.gridy = 4;
		chckbxDateOffset.addActionListener(this);
		add(chckbxDateOffset, gbc_chckbxDateOffset);

		JTextArea txtrForSomeChannels = new JTextArea();
		txtrForSomeChannels.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrForSomeChannels.setText("For some channels, the date provided needs to be offset by a certain "
				+ "number of months to reflect the true transaction date. Check this box if it is the case here.");
		txtrForSomeChannels.setWrapStyleWord(true);
		txtrForSomeChannels.setLineWrap(true);
		txtrForSomeChannels.setEnabled(false);
		txtrForSomeChannels.setEditable(false);
		txtrForSomeChannels.setBackground(new Color(238, 238, 238));
		GridBagConstraints gbc_txtrForSomeChannels = new GridBagConstraints();
		gbc_txtrForSomeChannels.gridwidth = 2;
		gbc_txtrForSomeChannels.insets = new Insets(0, 5, 5, 5);
		gbc_txtrForSomeChannels.fill = GridBagConstraints.BOTH;
		gbc_txtrForSomeChannels.gridx = 0;
		gbc_txtrForSomeChannels.gridy = 5;
		add(txtrForSomeChannels, gbc_txtrForSomeChannels);

		lblDateOffset = new JLabel("Number of months to offset date by:");
		lblDateOffset.setEnabled(false);
		GridBagConstraints gbc_lblDateOffset = new GridBagConstraints();
		gbc_lblDateOffset.anchor = GridBagConstraints.WEST;
		gbc_lblDateOffset.insets = new Insets(0, 5, 0, 5);
		gbc_lblDateOffset.gridx = 0;
		gbc_lblDateOffset.gridy = 6;
		add(lblDateOffset, gbc_lblDateOffset);

		tfDateOffset = new JTextField();
		tfDateOffset.setEnabled(false);
		tfDateOffset.setEditable(false);
		GridBagConstraints gbc_tfDateOffset = new GridBagConstraints();
		gbc_tfDateOffset.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfDateOffset.gridx = 1;
		gbc_tfDateOffset.gridy = 6;
		add(tfDateOffset, gbc_tfDateOffset);
		tfDateOffset.setColumns(10);

	}
	
	private int getDateColumn() {
		int dateColumn = -1;
		try {
			dateColumn = Integer.parseInt(tfDateColumn.getText().trim());	
		} catch (NumberFormatException e) {
			System.out.println("String was not parsable as an integer. ");
			e.printStackTrace();
		}
		return dateColumn;
	}
	
	private int getDateRow() {
		int dateRow = -1;
		try {
			dateRow = Integer.parseInt(tfDateRow.getText().trim());	
		} catch (NumberFormatException e) {
			System.out.println("String was not parsable as an integer. ");
			e.printStackTrace();
		}
		return dateRow;
	}
	
	private int getMonthsOffset() {
		int monthsOffset = 0;
		try {
			monthsOffset = Integer.parseInt(tfDateOffset.getText().trim());	
		} catch (NumberFormatException e) {
			System.out.println("String was not parsable as an integer. ");
			e.printStackTrace();
		}
		return monthsOffset;
	}

	@Override
	public Boolean validateUserInput() {
		Boolean success = true;
		if (getDateColumn() == -1) {
			success = false;
		}
		if (chckbxDateRow.isSelected() && getDateRow() == -1) {
			success = false;
		}
		if (chckbxDateOffset.isSelected() && getMonthsOffset() == 0) {
			success = false;
		}
	
		return success;
	}

	@Override
	public void saveUserInput() {
		this.overallDialog.setDateColumnIndex(getDateColumn());
		if (chckbxDateRow.isSelected()) {
			this.overallDialog.setDateRowIndex(getDateRow());
		}
		if (chckbxDateOffset.isSelected()) {
			this.overallDialog.setMonthsFromDate(getMonthsOffset());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == chckbxDateRow) {
			if (chckbxDateRow.isSelected()) {
				tfDateRow.setEnabled(true);
				tfDateRow.setEditable(true);
				lblDateRow.setEnabled(true);
			} else {
				tfDateRow.setEnabled(false);
				tfDateRow.setEditable(false);
				lblDateRow.setEnabled(false);
			}

		}
		if (e.getSource() == chckbxDateOffset) {
			if (chckbxDateOffset.isSelected()) {
				lblDateOffset.setEnabled(true);
				tfDateOffset.setEnabled(true);
				tfDateOffset.setEditable(true);
			} else {
				lblDateOffset.setEnabled(false);
				tfDateOffset.setEnabled(false);
				tfDateOffset.setEditable(false);
			}

		}

	}

}
