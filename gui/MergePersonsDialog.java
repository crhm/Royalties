package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.Person;
import main.SalesHistory;

@SuppressWarnings("serial")
public class MergePersonsDialog extends JDialog {
	public MergePersonsDialog() {
		setTitle("Choose Two Persons To Merge Together");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		//Setting up the buttons
		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new GridLayout(1, 0, 0, 0));

		JButton btnCancel = new JButton("Cancel");
		panelButtons.add(btnCancel);

		JButton btnMerge = new JButton("Merge");
		panelButtons.add(btnMerge);

		//Setting up the combo boxes
		JPanel panelPersonChoices = new JPanel();
		getContentPane().add(panelPersonChoices, BorderLayout.CENTER);
		panelPersonChoices.setLayout(new GridLayout(2, 1, 0, 0));
		//Making an array of person names
		String[] listPersons = new String[SalesHistory.get().getListPersons().size()];
		int count = 0;
		for (Person p : SalesHistory.get().getListPersons()) {
			listPersons[count] = p.getName();
			count++;
		}
		Arrays.sort(listPersons);
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<String>(listPersons);
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<String>(listPersons);

		JComboBox<String> comboBoxPerson1 = new JComboBox<String>();
		comboBoxPerson1.setModel(model1);
		panelPersonChoices.add(comboBoxPerson1);

		JComboBox<String> comboBoxPerson2 = new JComboBox<String>();
		comboBoxPerson2.setModel(model2);
		panelPersonChoices.add(comboBoxPerson2);

		//Setting up button actions
		MergePersonsDialog window = this;

		btnCancel.addActionListener(new ActionListener() { //Window closes if cancel button is pressed
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			}
		});

		btnMerge.addActionListener(new ActionListener() { //Merge is performed if merge button is pressed, window closes
			public void actionPerformed(ActionEvent e) {
				Person person1 = SalesHistory.get().getPerson((String) comboBoxPerson1.getSelectedItem());
				Person person2 = SalesHistory.get().getPerson((String) comboBoxPerson2.getSelectedItem());
				if (person1.getPersonNumber() == person2.getPersonNumber()) { //If user attempts to merge the same two books
					JOptionPane.showMessageDialog(window, "You cannot merge a person with itself. "
									+ "Please select two separate persons.", "Error!", JOptionPane.ERROR_MESSAGE);
				} else {
					person1.merge(person2);
					SalesHistory.get().removePerson(person2);
					window.dispose();
				}
			}
		});

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

}
