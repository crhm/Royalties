package gui.persons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Person;

/**Class intended to create/display a dialog for editing a person's info
 * 
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class EditPersonDialog extends JFrame implements ActionListener {
	private JTextField tfName;
	private JTextField tfOtherNames;
	private JButton btnCancel;
	private JButton btnConfirm;
	private Person person;

	public EditPersonDialog(Person p) {
		super();
		person = p;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Edit Person Details");

		JPanel panelDataEntry = new JPanel();
		getContentPane().add(panelDataEntry, BorderLayout.CENTER);
		panelDataEntry.setLayout(new GridLayout(3, 2, 0, 0));

		JLabel lblName = new JLabel("Person Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblName);

		tfName = new JTextField(person.getName());
		tfName.setToolTipText("Cannot be empty");
		panelDataEntry.add(tfName);

		JLabel lblOtherNames = new JLabel("Name Variations:");
		lblOtherNames.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblOtherNames);

		String otherNames = "";
		Boolean isFirstName = true;
		for (String s : person.getListNames()) {
			if (!s.equals(person.getName())) {
				if (isFirstName) {
					otherNames = otherNames.concat(s);
					isFirstName = false;
				} else {
					otherNames = otherNames.concat(", " + s);
				}
			}
		}
		tfOtherNames = new JTextField(otherNames);
		panelDataEntry.add(tfOtherNames);

		JLabel lblBalance = new JLabel("Balance (USD):");
		lblBalance.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblBalance);

		JLabel lblBalanceAmount = new JLabel(Double.toString(person.getBalance()));
		panelDataEntry.add(lblBalanceAmount);

		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new GridLayout(1, 2, 0, 0));

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		panelButtons.add(btnCancel);

		btnConfirm = new JButton("Save Changes");
		btnConfirm.addActionListener(this);
		panelButtons.add(btnConfirm);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) { //User cancels, window closes
			this.dispose();
		} else if (e.getSource() == btnConfirm) { //User confirms changes, new details are applied to the person
			String name = tfName.getText();
			String otherNames = tfOtherNames.getText();
			
			if (name == null || name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Error: A name is required for a person. Please input a person name.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return; //stop here and don't do anything below since there is no name
			}
			
			person.setName(name.trim());
			person.setListNames(new HashSet<String>());
			person.addName(name.trim());
			String[] otherNamesSeparated = null;
			if (otherNames.contains(",") ) {
				otherNamesSeparated = otherNames.split(",");
				for (String s : otherNamesSeparated) {
					person.addName(s.trim());
				}
			} else if (otherNames != null && !otherNames.isEmpty()) {
				person.addName(otherNames.trim());
			}
			
			this.dispose();
		}
	}

}
