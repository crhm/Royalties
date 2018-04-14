package gui.books;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import main.Book;
import main.SalesHistory;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class MergeBooksDialog extends JDialog implements ItemListener {
	
	private JComboBox<Long> comboBoxBook1;
	private JComboBox<Long> comboBoxBook2;
	private JLabel labelBook1 = new JLabel();
	private JLabel labelBook2 = new JLabel();
	
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

		//Setting up the panel of Book choices
		JPanel panelBookChoices = new JPanel();
		getContentPane().add(panelBookChoices, BorderLayout.CENTER);
		panelBookChoices.setLayout(new GridLayout(4, 1, 0, 0));
		
		Long[] listBooks = new Long[SalesHistory.get().getListPLPBooks().size()];		//Making an array of book numbers
		int count = 0;
		for (Book b : SalesHistory.get().getListPLPBooks()) {
			listBooks[count] = b.getBookNumber();
			count++;
		}
		Arrays.sort(listBooks);
		DefaultComboBoxModel<Long> model1 = new DefaultComboBoxModel<Long>(listBooks);
		DefaultComboBoxModel<Long> model2 = new DefaultComboBoxModel<Long>(listBooks);

		comboBoxBook1 = new JComboBox<Long>();
		comboBoxBook1.setModel(model1);
		comboBoxBook1.addItemListener(this);
		panelBookChoices.add(comboBoxBook1);
		panelBookChoices.add(labelBook1);

		comboBoxBook2 = new JComboBox<Long>();
		comboBoxBook2.setModel(model2);
		comboBoxBook2.addItemListener(this);
		panelBookChoices.add(comboBoxBook2);
		panelBookChoices.add(labelBook2);

		//Setting up button actions
		MergeBooksDialog window = this;

		btnCancel.addActionListener(new ActionListener() { //Window closes if cancel button is pressed
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			}
		});

		btnMerge.addActionListener(new ActionListener() { //Merge is performed if merge button is pressed, window closes
			public void actionPerformed(ActionEvent e) {
				Book book1 = SalesHistory.get().getBookWithNumber((Long) comboBoxBook1.getSelectedItem());
				Book book2 = SalesHistory.get().getBookWithNumber((Long) comboBoxBook2.getSelectedItem());
				if (book1.getBookNumber() == book2.getBookNumber()) { //If user attempts to merge the same two books
					JOptionPane.showMessageDialog(window, "You cannot merge a book with itself. "
									+ "Please select two separate books.", "Error!", JOptionPane.ERROR_MESSAGE);
				} else {
					int userChoice = JOptionPane.showConfirmDialog(window, "Please confirm that you want to merge these two books.", 
							"Confirmation Required", JOptionPane.OK_CANCEL_OPTION);
					if (userChoice == 0) {
						book1.merge(book2);
						SalesHistory.get().removeBook(book2);
						window.dispose();
					}
				}
			}
		});

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == comboBoxBook1) {
			labelBook1.setText(SalesHistory.get().getBookWithNumber((Long) comboBoxBook1.getSelectedItem()).getTitle());
		} else if (e.getSource() == comboBoxBook2) {
			labelBook2.setText(SalesHistory.get().getBookWithNumber((Long) comboBoxBook2.getSelectedItem()).getTitle());
		}
		
	}

}
