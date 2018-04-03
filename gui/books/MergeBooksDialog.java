package gui.books;

import javax.swing.JDialog;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import main.Book;
import main.SalesHistory;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class MergeBooksDialog extends JDialog {
	public MergeBooksDialog() {
		setTitle("Choose Two Books To Merge Together");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnCancel = new JButton("Cancel");
		panelButtons.add(btnCancel);
		
		JButton btnMerge = new JButton("Merge");
		panelButtons.add(btnMerge);
		
		JPanel panelBookChoices = new JPanel();
		getContentPane().add(panelBookChoices, BorderLayout.CENTER);
		panelBookChoices.setLayout(new GridLayout(2, 1, 0, 0));
		
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
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

}
