package gui.books;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import main.Book;
import main.SalesHistory;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class MergeBooksDialog extends JDialog {
	public MergeBooksDialog() {
		setTitle("Choose Two Books To Merge Together");
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
		JPanel panelBookChoices = new JPanel();
		getContentPane().add(panelBookChoices, BorderLayout.CENTER);
		panelBookChoices.setLayout(new GridLayout(2, 1, 0, 0));
		//Making an array of book titles
		String[] listBooks = new String[SalesHistory.get().getListPLPBooks().values().size()];
		int count = 0;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			listBooks[count] = b.getTitle();
			count++;
		}
		Arrays.sort(listBooks);
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<String>(listBooks);
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<String>(listBooks);

		JComboBox<String> comboBoxBook1 = new JComboBox<String>();
		comboBoxBook1.setModel(model1);
		panelBookChoices.add(comboBoxBook1);

		JComboBox<String> comboBoxBook2 = new JComboBox<String>();
		comboBoxBook2.setModel(model2);
		panelBookChoices.add(comboBoxBook2);

		//Setting up button actions
		MergeBooksDialog window = this;

		btnCancel.addActionListener(new ActionListener() { //Window closes if cancel button is pressed
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			}
		});

		btnMerge.addActionListener(new ActionListener() { //Merge is performed if merge button is pressed, window closes
			public void actionPerformed(ActionEvent e) {
				Book book1 = SalesHistory.get().getBook((String) comboBoxBook1.getSelectedItem());
				Book book2 = SalesHistory.get().getBook((String) comboBoxBook2.getSelectedItem());
				if (book1.getBookNumber() == book2.getBookNumber()) { //If user attempts to merge the same two books
					JOptionPane.showMessageDialog(window, "You cannot merge a book with itself. "
									+ "Please select two separate books.", "Error!", JOptionPane.ERROR_MESSAGE);
				} else {
					book1.merge(book2);
					SalesHistory.get().removeBook(book2);
					window.dispose();
				}
			}
		});

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

}
