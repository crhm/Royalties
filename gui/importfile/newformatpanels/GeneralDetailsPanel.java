package gui.importfile.newformatpanels;

import javax.swing.JLabel;
import javax.swing.JTextField;

import gui.importfile.NewImportFormatDialog;
import main.Channel;
import main.SalesHistory;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

@SuppressWarnings("serial")
public class GeneralDetailsPanel extends FormatDetailsPanel{

	private JTextField tfFirstLine;
	private JComboBox<Channel> cbbChannel;

	public GeneralDetailsPanel(NewImportFormatDialog overallDialog) {
		this.overallDialog = overallDialog;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{222, 223, 0};
		gridBagLayout.rowHeights = new int[]{26, 50, 41, 27, 50, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JLabel lblFirstLine = new JLabel("First Line Containing A Sale:");
		GridBagConstraints gbc_lblFirstLine = new GridBagConstraints();
		gbc_lblFirstLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFirstLine.insets = new Insets(0, 5, 5, 5);
		gbc_lblFirstLine.gridx = 0;
		gbc_lblFirstLine.gridy = 0;
		this.add(lblFirstLine, gbc_lblFirstLine);

		tfFirstLine = new JTextField();
		GridBagConstraints gbc_tfFirstLine = new GridBagConstraints();
		gbc_tfFirstLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfFirstLine.anchor = GridBagConstraints.NORTHWEST;
		gbc_tfFirstLine.insets = new Insets(0, 0, 5, 0);
		gbc_tfFirstLine.gridx = 1;
		gbc_tfFirstLine.gridy = 0;
		this.add(tfFirstLine, gbc_tfFirstLine);
		tfFirstLine.setColumns(10);

		JTextArea txtFirstLineExplanation = new JTextArea();
		txtFirstLineExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtFirstLineExplanation.setEnabled(false);
		txtFirstLineExplanation.setEditable(false);
		txtFirstLineExplanation.setBackground(new Color(238, 238, 238));
		txtFirstLineExplanation.setWrapStyleWord(true);
		txtFirstLineExplanation.setLineWrap(true);
		txtFirstLineExplanation.setText("Please enter the line number at which the first sale "
				+ "is to be found, so that the program does not try to process the column headers or initial empty lines as sales.");
		GridBagConstraints gbc_txtFirstLineExplanation = new GridBagConstraints();
		gbc_txtFirstLineExplanation.anchor = GridBagConstraints.WEST;
		gbc_txtFirstLineExplanation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFirstLineExplanation.insets = new Insets(0, 5, 5, 5);
		gbc_txtFirstLineExplanation.gridwidth = 2;
		gbc_txtFirstLineExplanation.gridx = 0;
		gbc_txtFirstLineExplanation.gridy = 1;
		this.add(txtFirstLineExplanation, gbc_txtFirstLineExplanation);

		JLabel lblChannel = new JLabel("Sales Channel:");
		GridBagConstraints gbc_lblChannel = new GridBagConstraints();
		gbc_lblChannel.anchor = GridBagConstraints.WEST;
		gbc_lblChannel.insets = new Insets(0, 5, 5, 5);
		gbc_lblChannel.gridx = 0;
		gbc_lblChannel.gridy = 3;
		this.add(lblChannel, gbc_lblChannel);

		cbbChannel = new JComboBox<Channel>();
		cbbChannel.setSelectedIndex(-1);
		cbbChannel.setEditable(false);

		GridBagConstraints gbc_cbbChannel = new GridBagConstraints();
		gbc_cbbChannel.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbChannel.anchor = GridBagConstraints.NORTHWEST;
		gbc_cbbChannel.insets = new Insets(0, 0, 5, 0);
		gbc_cbbChannel.gridx = 1;
		gbc_cbbChannel.gridy = 3;
		this.add(cbbChannel, gbc_cbbChannel);

		JTextArea txtChannelExplanation = new JTextArea();
		txtChannelExplanation.setBackground(new Color(238, 238, 238));
		txtChannelExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtChannelExplanation.setWrapStyleWord(true);
		txtChannelExplanation.setLineWrap(true);
		txtChannelExplanation.setEnabled(false);
		txtChannelExplanation.setEditable(false);
		txtChannelExplanation.setText("Please choose an existing sales channel from the list that this file "
				+ "format will be associated with. Please create a new channel in the channel panel before importing if need be.");
		GridBagConstraints gbc_txtChannelExplanation = new GridBagConstraints();
		gbc_txtChannelExplanation.insets = new Insets(0, 5, 0, 5);
		gbc_txtChannelExplanation.anchor = GridBagConstraints.WEST;
		gbc_txtChannelExplanation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtChannelExplanation.gridwidth = 2;
		gbc_txtChannelExplanation.gridx = 0;
		gbc_txtChannelExplanation.gridy = 4;
		this.add(txtChannelExplanation, gbc_txtChannelExplanation);
		for (Channel ch : SalesHistory.get().getListChannels()) {
			cbbChannel.addItem(ch);
		}
	}

	public int getFirstLine() {
		int firstLine = -1;
		try {
			firstLine = Integer.parseInt(tfFirstLine.getText().trim());	
		} catch (NumberFormatException e) {
			System.out.println("String was not parsable as an integer. " + e.getStackTrace());
		}
		return firstLine;
	}

	public Channel getChannel() {
		Channel channel = null;
		try {
			channel = (Channel) cbbChannel.getSelectedItem();
		} catch (Exception e) { 
			System.out.println("There was an error retrieving the channel. " + e.getStackTrace());
		}
		return channel;
	}

	@Override
	public Boolean validateUserInput() {
		Boolean success = false;
		if (getChannel() != null && getFirstLine() > -1) {
			success = true;
		}
		return success;
	}

	@Override
	public void saveUserInput() {
		this.overallDialog.setFirstLineOfData(getFirstLine());
		this.overallDialog.setChannel(getChannel());
		System.out.println("DONE!!");
	}
}
