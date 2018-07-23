package gui.importfile.newformatpanels;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JTextField;

import main.Channel;
import main.SalesHistory;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class GeneralDetailsPanel extends JFrame{
	private JTextField tfFirstLine;
	private JComboBox<Channel> cbbChannel;
	
	public GeneralDetailsPanel() {
		getContentPane().setLayout(null);
		
		JLabel lblFirstLine = new JLabel("First Line Containing A Sale:");
		lblFirstLine.setBounds(16, 29, 186, 16);
		getContentPane().add(lblFirstLine);
		
		tfFirstLine = new JTextField();
		tfFirstLine.setBounds(214, 24, 130, 26);
		getContentPane().add(tfFirstLine);
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
		txtFirstLineExplanation.setBounds(16, 62, 409, 50);
		getContentPane().add(txtFirstLineExplanation);
		
		JLabel lblChannel = new JLabel("Sales Channel:");
		lblChannel.setBounds(16, 158, 98, 16);
		getContentPane().add(lblChannel);
		
		cbbChannel = new JComboBox<Channel>();
		for (Channel ch : SalesHistory.get().getListChannels()) {
			cbbChannel.addItem(ch);
		}
		cbbChannel.setSelectedIndex(-1);
		cbbChannel.setEditable(true);
		cbbChannel.setBounds(214, 153, 52, 27);
		
		getContentPane().add(cbbChannel);
		
		JTextArea txtChannelExplanation = new JTextArea();
		txtChannelExplanation.setBackground(new Color(238, 238, 238));
		txtChannelExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtChannelExplanation.setWrapStyleWord(true);
		txtChannelExplanation.setLineWrap(true);
		txtChannelExplanation.setEnabled(false);
		txtChannelExplanation.setEditable(false);
		txtChannelExplanation.setText("Please choose an existing sales channel from the list that this file format will be associated with, or type in the name of a new channel to create one.");
		txtChannelExplanation.setBounds(16, 192, 409, 50);
		getContentPane().add(txtChannelExplanation);
	}
}
