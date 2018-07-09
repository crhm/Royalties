package gui.books;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

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
import main.ObjectFactory;
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
	private JComboBox<Person> cBAuthor1;
	private JComboBox<Person> cBAuthor2;
	private JComboBox<Person> cBPrefaceAuthor;
	private JComboBox<Person> cBAfterwordAuthor;
	private JComboBox<Person> cBTranslator;
	
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
		lblAuthor1.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		lblAuthor1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblAuthor1);
		
		lblAuthor2 = new JLabel("Secondary Author");
		lblAuthor2.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		panelLabels.add(lblAuthor2);
		
		lblTranslator = new JLabel("Translator:");
		lblTranslator.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		lblTranslator.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblTranslator);
		
		lblPrefaceAuthor = new JLabel("Preface Author:");
		lblPrefaceAuthor.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		lblPrefaceAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblPrefaceAuthor);
		
		lblAfterwordAuthor = new JLabel("Afterword Author:");
		lblAfterwordAuthor.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
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
		Person[] listAuthors = new Person[SalesHistory.get().getListAuthors().size()];
		int count = 0;
		for (Person p : SalesHistory.get().getListAuthors()) {
			listAuthors[count] = p;
			count++;
		}
		Arrays.sort(listAuthors, new Comparator<Person>() {

			@Override
			public int compare(Person o1, Person o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		DefaultComboBoxModel<Person> model1 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model2 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model3 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model4 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model5 = new DefaultComboBoxModel<Person>(listAuthors);

		//AUTHOR 1
		cBAuthor1 = new JComboBox<Person>();
		cBAuthor1.setModel(model1);
		cBAuthor1.setEditable(true);
		if (book.getAuthor1() != null) {
			int index1 = model1.getIndexOf(book.getAuthor1());
			cBAuthor1.setSelectedIndex(index1);
		} else {
			cBAuthor1.insertItemAt(null, 0);
			cBAuthor1.setSelectedIndex(0);
		}
		
		//AUTHOR 2
		cBAuthor2 = new JComboBox<Person>();
		cBAuthor2.setModel(model2);
		cBAuthor2.setEditable(true);
		if (book.getAuthor2() != null) {
			int index2 = model2.getIndexOf(book.getAuthor2());
			cBAuthor2.setSelectedIndex(index2);
		} else {
			cBAuthor2.insertItemAt(null, 0);
			cBAuthor2.setSelectedIndex(0);
		}
		
		//TRANSLATOR
		cBTranslator = new JComboBox<Person>();
		cBTranslator.setModel(model3);
		cBTranslator.setEditable(true);
		if (book.getTranslator() != null) {
			int index3 = model3.getIndexOf(book.getTranslator());
			cBTranslator.setSelectedIndex(index3);
		} else {
			cBTranslator.insertItemAt(null, 0);
			cBTranslator.setSelectedIndex(0);
		}

		//PREFACE AUTHOR
		cBPrefaceAuthor = new JComboBox<Person>();
		cBPrefaceAuthor.setModel(model4);
		cBPrefaceAuthor.setEditable(true);
		if (book.getPrefaceAuthor() != null) {
			int index4 = model4.getIndexOf(book.getPrefaceAuthor());
			cBPrefaceAuthor.setSelectedIndex(index4);
		} else {
			cBPrefaceAuthor.insertItemAt(null, 0);
			cBPrefaceAuthor.setSelectedIndex(0);
		}
		
		//AFTERWORD AUTHOR
		cBAfterwordAuthor = new JComboBox<Person>();
		cBAfterwordAuthor.setModel(model5);
		cBAfterwordAuthor.setEditable(true);
		if (book.getAfterwordAuthor() != null) {
			int index5 = model5.getIndexOf(book.getAfterwordAuthor());
			cBAfterwordAuthor.setSelectedIndex(index5);
		} else {
			cBAfterwordAuthor.insertItemAt(null, 0);
			cBAfterwordAuthor.setSelectedIndex(0);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bttnCancel) {
			this.dispose();
		} else if (e.getSource() == bttnConfirm) {
			String title = this.tfTitle.getText();
			Person author1 = null;
			Person author2 = null;
			Person translator = null;
			Person prefaceAuthor = null;
			Person afterwordAuthor = null;
			try {
				author1 = (Person) this.cBAuthor1.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBAuthor1.getSelectedItem() != null && !((String) this.cBAuthor1.getSelectedItem()).isEmpty()) {
					author1 = ObjectFactory.createPerson((String) this.cBAuthor1.getSelectedItem());	
				} 
			}
			book.setAuthor1(author1);
			
			try {
				author2 = (Person) this.cBAuthor2.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBAuthor2.getSelectedItem() != null && !((String) this.cBAuthor2.getSelectedItem()).isEmpty()) {
					author2 = ObjectFactory.createPerson((String) this.cBAuthor2.getSelectedItem());	
				} 
			}
			book.setAuthor2(author2); 
			
			try {
				translator = (Person) this.cBTranslator.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBTranslator.getSelectedItem() != null && !((String) this.cBTranslator.getSelectedItem()).isEmpty()) {
					translator = ObjectFactory.createPerson((String) this.cBTranslator.getSelectedItem());	
				} 
			}
			book.setTranslator(translator); 
			
			try {
				prefaceAuthor = (Person) this.cBPrefaceAuthor.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBPrefaceAuthor.getSelectedItem() != null && !((String) this.cBPrefaceAuthor.getSelectedItem()).isEmpty()) {
					prefaceAuthor = ObjectFactory.createPerson((String) this.cBPrefaceAuthor.getSelectedItem());	
				} 
			}
			book.setPrefaceAuthor(prefaceAuthor); 
			
			try {
				afterwordAuthor = (Person) this.cBAfterwordAuthor.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBAfterwordAuthor.getSelectedItem() != null && !((String) this.cBAfterwordAuthor.getSelectedItem()).isEmpty()) {
					afterwordAuthor = ObjectFactory.createPerson((String) this.cBAfterwordAuthor.getSelectedItem());	
				} 
			}
			book.setAfterwordAuthor(afterwordAuthor); 
			
			String identifiers = this.tfIdentifiers.getText();
			String otherTitles = this.tfOtherTitles.getText();

			if (title == null || title.isEmpty()) { //Does not allow empty title, shows an error message
				JOptionPane.showMessageDialog(this, "Error: A book title is required to create a book. Please input a title.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return; //stop here and don't do anything below since there is no title
			}
			
			//Setting the new details
			book.setTitle(title);
			
			book.setIdentifiers(new HashSet<String>());
			//Making sure that if several identifiers are inputted, they are all added separately
			String[] identifiersSeparated = null;
			if (identifiers.contains(",") ) {
				identifiersSeparated = identifiers.split(",");
			}
			if (identifiersSeparated != null) {
				for (String s : identifiersSeparated) {
					book.addIdentifier(s.trim());
				}
			} else if (!identifiers.isEmpty()){
				book.addIdentifier(identifiers.trim());
			}

			book.setListTitles(new HashSet<String>());
			book.addTitle(title); //VERY IMPORTANT!
			//Adding other titles if any
			String[] otherTitlesSeparated = null;
			if (otherTitles.contains(",") ) {
				otherTitlesSeparated = otherTitles.split(",");
			}
			if (otherTitlesSeparated != null) {
				for (String s : otherTitlesSeparated) {
					book.addTitle(s.trim());
				}
			} else if (!otherTitles.isEmpty()){
				book.addTitle(otherTitles.trim());
			}
			
			this.dispose();
		}
	}	

}
