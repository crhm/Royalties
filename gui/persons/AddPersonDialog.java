package gui.persons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.ObjectFactory;
import main.Person;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class AddPersonDialog extends JFrame implements ActionListener {

	private JTextField tfName;
	private JTextField tfOtherNames;
	private JButton bttnCancel;
	private JButton bttnCreate;
	
	public AddPersonDialog() {
		super();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Create A New Person");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelDataEntry = new JPanel();
		getContentPane().add(panelDataEntry, BorderLayout.CENTER);
		panelDataEntry.setLayout(new GridLayout(2, 2, 0, 0));
		
		JLabel lblName = new JLabel("Person Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblName);
		
		tfName = new JTextField();
		tfName.setToolTipText("A name is required to create a new person.");
		panelDataEntry.add(tfName);
		tfName.setColumns(10);
		
		JLabel lblOtherNames = new JLabel("Name Variations:");
		lblOtherNames.setToolTipText("Such as alternative spellings, with and without accents or middle name...");
		lblOtherNames.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblOtherNames);
		
		tfOtherNames = new JTextField();
		tfOtherNames.setToolTipText("Optional");
		panelDataEntry.add(tfOtherNames);
		tfOtherNames.setColumns(10);
		
		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new GridLayout(1, 2, 0, 0));
		
		bttnCancel = new JButton("Cancel");
		bttnCancel.addActionListener(this);
		panelButtons.add(bttnCancel);
		
		bttnCreate = new JButton("Create");
		bttnCreate.addActionListener(this);
		panelButtons.add(bttnCreate);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bttnCancel) {
			this.dispose();
		} else if (e.getSource() == bttnCreate) {
			String name = tfName.getText();
			String otherNames = tfOtherNames.getText();
			if (name == null || name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Error: A name is required to create a person. Please input a person name.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return; //stop here and don't do anything below since there is no name
			}
			Person newPerson = ObjectFactory.createPerson(name);

			String[] otherNamesSeparated = null;
			if (otherNames.contains(",") ) {
				otherNamesSeparated = otherNames.split(",");
				for (String s : otherNamesSeparated) {
					newPerson.addName(s);
				}
			} else if (otherNames != null && !otherNames.isEmpty()) {
				newPerson.addName(otherNames);
			}
			this.dispose();
		}
	}

}
