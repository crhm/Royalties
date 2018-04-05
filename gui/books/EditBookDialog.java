package gui.books;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Book;
import main.Person;
import main.SalesHistory;

import javax.swing.JComboBox;

public class EditBookDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3471608692321566363L;
	private JPanel panelButtons;
	private JPanel panelLabels;
	private JPanel panelEntries;
	private JButton bttnConfirm;
	private JButton bttnCancel;
	private JLabel lblTitle;
	private JLabel lblAuthor1;
	private JLabel lblAuthor2;
	private JLabel lblTranslator;
	private JLabel lblAfterwordAuthor;
	private JLabel lblPrefaceAuthor;
	private JLabel lblIdentifiers;
	private JLabel lblUnitsSold;
	private JLabel lblOtherTitles;
	private JLabel lblUnitsSoldAmount;
	private JTextField tfTitle;
	private JTextField tfIdentifiers;
	private JTextField tfOtherTitles;
	private JComboBox<String> cBAuthor1;
	private JComboBox<String> cBAuthor2;
	private JComboBox<String> cBPrefaceAuthor;
	private JComboBox<String> cBAfterwordAuthor;
	private JComboBox<String> cBTranslator;
	
	private Book book;
	
	public EditBookDialog(Book b) {
		super();
		this.setTitle("Edit Book Details");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		book = b;
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		//BUTTONS
		panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		bttnCancel = new JButton("Cancel");
		panelButtons.add(bttnCancel);
		
		bttnConfirm = new JButton("Save");
		panelButtons.add(bttnConfirm);
		
		bttnConfirm.addActionListener(this);
		bttnCancel.addActionListener(this);
		
		
		//LABELS
		panelLabels = new JPanel();
		panelLabels.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelLabels, BorderLayout.WEST);
		panelLabels.setLayout(new GridLayout(9, 1, 0, 0));
		
		lblTitle = new JLabel("Title:");
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblTitle);
		
		lblOtherTitles = new JLabel("Other Titles:");
		lblOtherTitles.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblOtherTitles);
		
		lblIdentifiers = new JLabel("Identifiers:");
		lblIdentifiers.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblIdentifiers);
		
		lblUnitsSold = new JLabel("Units Sold:");
		lblUnitsSold.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblUnitsSold);
		
		lblAuthor1 = new JLabel("Main Author:");
		lblAuthor1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblAuthor1);
		
		lblAuthor2 = new JLabel("Secondary Author");
		panelLabels.add(lblAuthor2);
		
		lblTranslator = new JLabel("Translator:");
		lblTranslator.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblTranslator);
		
		lblPrefaceAuthor = new JLabel("Preface Author:");
		lblPrefaceAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblPrefaceAuthor);
		
		lblAfterwordAuthor = new JLabel("Afterword Author:");
		lblAfterwordAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblAfterwordAuthor);
		
		//ENTRIES
		panelEntries = new JPanel();
		panelEntries.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelEntries, BorderLayout.CENTER);
		panelEntries.setLayout(new GridLayout(9, 1, 0, 0));
		
		tfTitle = new JTextField(book.getTitle());
		tfTitle.setToolTipText("A title is required.");
		panelEntries.add(tfTitle);
		
		String titles = "";
		int counter2 = 0;
		for (String s : book.getListTitles()) { //So that no brackets appear in edit box which could confuse user
			if (!s.equals(book.getTitle())) {
				if (counter2 != 0) {
					titles = titles.concat(", ");
				} 
				titles = titles.concat(s);
				counter2++;
			}
		}
		tfOtherTitles = new JTextField(titles);
		panelEntries.add(tfOtherTitles);
		
		String identifiers = "";
		int counter = 0;
		for (String s : book.getIdentifiers()) { //So that no brackets appear in edit box which could confuse user
			if (counter != 0) {
				identifiers = identifiers.concat(", ");
			}
			identifiers = identifiers.concat(s);
			counter++;
		}
		tfIdentifiers = new JTextField(identifiers);
		tfIdentifiers.setToolTipText("To define multiple identifiers, separate them by a comma (i.e. 123456789, 987654321)");
		panelEntries.add(tfIdentifiers);
		
		lblUnitsSoldAmount = new JLabel("" + book.getTotalUnitsSold());
		panelEntries.add(lblUnitsSoldAmount);
		
		makeAuthorsComboBoxes();
		
		panelEntries.add(cBAuthor1);
		panelEntries.add(cBAuthor2);
		panelEntries.add(cBTranslator);
		panelEntries.add(cBPrefaceAuthor);
		panelEntries.add(cBAfterwordAuthor);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}
	
	private void makeAuthorsComboBoxes() {
		//Making an array of authors
		String[] listAuthors = new String[SalesHistory.get().getListAuthors().size()];
		int count = 0;
		for (Person p : SalesHistory.get().getListAuthors()) {
			listAuthors[count] = p.getName();
			count++;
		}
		Arrays.sort(listAuthors);
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<String>(listAuthors);
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<String>(listAuthors);
		DefaultComboBoxModel<String> model3 = new DefaultComboBoxModel<String>(listAuthors);
		DefaultComboBoxModel<String> model4 = new DefaultComboBoxModel<String>(listAuthors);
		DefaultComboBoxModel<String> model5 = new DefaultComboBoxModel<String>(listAuthors);

		//AUTHOR 1
		cBAuthor1 = new JComboBox<String>();
		cBAuthor1.setModel(model1);
		if (book.getAuthor1() != null) {
			int index1 = model1.getIndexOf(book.getAuthor1().getName());
			cBAuthor1.setSelectedIndex(index1);
		} else {
			cBAuthor1.insertItemAt("", 0);
			cBAuthor1.setSelectedIndex(0);
		}
		
		//AUTHOR 2
		cBAuthor2 = new JComboBox<String>();
		cBAuthor2.setModel(model2);
		if (book.getAuthor2() != null) {
			int index2 = model2.getIndexOf(book.getAuthor2().getName());
			cBAuthor2.setSelectedIndex(index2);
		} else {
			cBAuthor2.insertItemAt("", 0);
			cBAuthor2.setSelectedIndex(0);
		}
		
		//TRANSLATOR
		cBTranslator = new JComboBox<String>();
		cBTranslator.setModel(model3);
		if (book.getTranslator() != null) {
			int index3 = model3.getIndexOf(book.getTranslator().getName());
			cBTranslator.setSelectedIndex(index3);
		} else {
			cBTranslator.insertItemAt("", 0);
			cBTranslator.setSelectedIndex(0);
		}

		//PREFACE AUTHOR
		cBPrefaceAuthor = new JComboBox<String>();
		cBPrefaceAuthor.setModel(model4);
		if (book.getPrefaceAuthor() != null) {
			int index4 = model4.getIndexOf(book.getPrefaceAuthor().getName());
			cBPrefaceAuthor.setSelectedIndex(index4);
		} else {
			cBPrefaceAuthor.insertItemAt("", 0);
			cBPrefaceAuthor.setSelectedIndex(0);
		}
		
		//AFTERWORD AUTHOR
		cBAfterwordAuthor = new JComboBox<String>();
		cBAfterwordAuthor.setModel(model5);
		if (book.getAfterwordAuthor() != null) {
			int index5 = model5.getIndexOf(book.getAfterwordAuthor().getName());
			cBAfterwordAuthor.setSelectedIndex(index5);
		} else {
			cBAfterwordAuthor.insertItemAt("", 0);
			cBAfterwordAuthor.setSelectedIndex(0);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bttnCancel) {
			this.dispose();
		} else if (e.getSource() == bttnConfirm) {
			//TODO 
			String title = tfTitle.getText().trim();
			if (title == null || title.isEmpty()) { //Does not allow empty title, shows an error message
				JOptionPane.showMessageDialog(this, "Error: A book title is required. Please input a title for this book.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return; //stop here and don't do anything below since there is no title
			}
			
			
			this.dispose();
		}
	}	

}
