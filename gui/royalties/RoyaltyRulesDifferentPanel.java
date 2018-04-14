package gui.royalties;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Book;
import main.Person;
import main.SalesHistory;
import main.royalties.IRoyaltyType;

@SuppressWarnings("serial")
public class RoyaltyRulesDifferentPanel extends JPanel implements ActionListener, ListSelectionListener {
	//TODO add button should be dynamic; if only no particular royalty is selected, it should read 'add book to list of books with royalties...'
	//and first add a book (from list of books - books already with a royalty for this channel, or create a new one) and then add a royalty to it. 
	//If one is selected, it should read 'add royalty to this book' and perform that action only.

	private JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
	private JButton editButton = new JButton("Edit");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");

	private JPanel containerPanel = new JPanel(new BorderLayout());

	//Displays the book titles for which there are royalty holders (for the selected channel)
	private JTable bookTitles = null; 

	//Displays the details of the royalties for the book selected amongst the book titles. If none are selected, no details are shown
	private JPanel royaltyDetails = new JPanel(new GridLayout(5, 1, 0, 0)); 

	//Holds the index of selected book title so that in case of sorting the selection can be maintained despite index change
	private int selectionIndexBeforeSort = 0;

	//Holds the title of the book that is currently selected
	private String currentBook = null;

	public RoyaltyRulesDifferentPanel() {
		super();
		this.setLayout(new BorderLayout());

		//Gets the list of book titles
		bookTitles = getTableBooks();
		bookTitles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Sort the table 
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(bookTitles.getModel());
		bookTitles.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();	

		//Add ListSelectionListener to list of books
		bookTitles.getSelectionModel().addListSelectionListener(this);

		//Adds components to the main container panel
		containerPanel.add(new JScrollPane(bookTitles), BorderLayout.WEST);
		containerPanel.add(royaltyDetails, BorderLayout.CENTER);

		//Because no royalty is selected to start with
		deleteButton.setEnabled(false);
		editButton.setEnabled(false);

		//Adding ActionListener to buttons
		deleteButton.addActionListener(this);
		addButton.addActionListener(this);
		editButton.addActionListener(this);

		//Set up buttonPanel
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(editButton);

		//Add both containerPanel and ButtonPanel to main Panel
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(containerPanel, BorderLayout.CENTER);
	}

	/**When the user selects another channel in the combobox, it changes the the list of book titles 
	 * accordingly and erases displayed royalties data.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == deleteButton) {
			if (bookTitles.getSelectedRow() != -1) {
				//TODO
			}
		} else if (e.getSource() == addButton) {
			if (bookTitles.getSelectedRow() != -1) {
				//TODO
			}
		} else if (e.getSource() == editButton) {
			//new EditRoyaltyDialog(currentChannel, currentBook, currentPerson);
			//TODO
		}
	}

	/**When a book title is selected in the list of titles, it displays the royalties for that book in the royaltyDetails panel
	 * as a table with royalty holders and their royalty type (and percentage)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == bookTitles.getSelectionModel()) {
			//Change in the selection of Book Title
			if (bookTitles.getSelectedRow() < 0) { 
				//Sorting caused the selection to be void somehow and return an index of -1
				// So, I clear the selection and set it at the same model index (which is now a different view index)
				bookTitles.clearSelection();
				bookTitles.getSelectionModel().setSelectionInterval(0, bookTitles.convertRowIndexToView(selectionIndexBeforeSort));
			} else {
				//Genuine value change, another book title has been selected
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
				royaltyDetails.removeAll();
				royaltyDetails.revalidate();
				int tableRow = bookTitles.getSelectedRow();
				String bookTitle = bookTitles.getValueAt(tableRow, 0).toString();
				Book book = SalesHistory.get().getBook(bookTitle);
				JTable royaltiesAmazon = getTableRoyalties(book, "Amazon");
				JTable royaltiesApple = getTableRoyalties(book, "Apple");
				JTable royaltiesKobo = getTableRoyalties(book, "Kobo");
				JTable royaltiesNook = getTableRoyalties(book, "Nook");
				JTable royaltiesCreatespace = getTableRoyalties(book, "Createspace");
				if (royaltiesAmazon != null) {
					royaltyDetails.add(royaltiesAmazon);
				} else {
					royaltyDetails.add(new JLabel("No royalties found for Amazon"));
				}
				if (royaltiesApple != null) {
					royaltyDetails.add(royaltiesApple);
				} else {
					royaltyDetails.add(new JLabel("No royalties found for Apple"));
				}
				if (royaltiesKobo != null) {
					royaltyDetails.add(royaltiesKobo);
				} else {
					royaltyDetails.add(new JLabel("No royalties found for Kobo"));
				}
				if (royaltiesNook != null) {
					royaltyDetails.add(royaltiesNook);
				} else {
					royaltyDetails.add(new JLabel("No royalties found for Nook"));
				}
				if (royaltiesCreatespace != null) {
					royaltyDetails.add(royaltiesCreatespace);
				} else {
					royaltyDetails.add(new JLabel("No royalties found for Createspace"));
				}

				royaltyDetails.revalidate();
				royaltyDetails.repaint();

				currentBook = (String) bookTitles.getValueAt(tableRow, 0); 
			}	
		} else {
			//TODO
		}
	}

	/**Returns a table contains the royalty information for the book passed as argument
	 * @param b Book of which we want the royalty details
	 * @return a JTable (not sortable, not editable) containing royalties data for that book
	 */
	private JTable getTableRoyalties(Book b, String channelName) {
		String[] columnNames = {"Royalty Holder", "Royalty"};
		if (getDataRoyalties(b, channelName) != null) {
			DefaultTableModel model = new DefaultTableModel(getDataRoyalties(b, channelName), columnNames) {
				@Override
				public Class<?> getColumnClass(int column) {
					switch (column ) {
					default : return String.class;
					}
				}
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			JTable table = new JTable(model);

			//Disables the user-reordering table columns
			table.getTableHeader().setReorderingAllowed(false);

			return table;
		} else {
			return null;
		}	
	}

	/**Obtains the data to be put into a table model for the royalties of the book passed as argument
	 * @param b Book whose royalty data we want
	 * @return The royalties data to be put in to a tablemodel
	 */
	private Object[][] getDataRoyalties(Book b, String channelName){
		HashMap<Person, IRoyaltyType> listRoyalties = SalesHistory.get().getListChannels().get(channelName).getListRoyalties().get(b);
		if (listRoyalties != null) {
			int numberOfRows = listRoyalties.keySet().size(); //THIS IS ALWAYS WHERE IT FAILS WHEN THERE'S A BUG
			Object[][] data = new Object[numberOfRows][2];
			int count = 0;
			for (Person p : listRoyalties.keySet()) {
				data[count][0] = p.getName();
				data[count][1] = listRoyalties.get(p);
				count++;
			}
			return data;
		} else {
			return null;
		}
	}

	/**Returns a JTable containing the list of book titles that have royalties for the currently selected channel
	 * @return the JTable of book titles (sortable alphabetically, single selection only, not editable)
	 */
	private JTable getTableBooks() {
		String[] columnNames = {"Book Title"};
		DefaultTableModel model = new DefaultTableModel(getDataBooks(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column ) {
				default : return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}	
		};
		JTable table = new JTable(model);

		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);

		return table;
	}

	/**Returns the data to be put into a tablemodel for the list of books 
	 * 
	 * @return data to be put into a tablemodel for the list of books
	 */
	private Object[][] getDataBooks(){
		Set<Book> listBooks = new HashSet<Book>();
		for (Book b : SalesHistory.get().getListPLPBooks()) {
			if (!SalesHistory.get().getUniformRoyalties().keySet().contains(b)) {
				listBooks.add(b);
			} else {
			}
		}
		int numberOfRows = listBooks.size();
		Object[][] data = new Object[numberOfRows][1];
		int count = 0;
		
		for (Book b : listBooks) {
			data[count][0] = b.getTitle();
			count++;
		}
		return data;
	}

	public void updateData() {
		bookTitles.getSelectionModel().removeListSelectionListener(this);
		currentBook = null;
		selectionIndexBeforeSort = 0;
		editButton.setEnabled(false);
		addButton.setEnabled(false);
		deleteButton.setEnabled(false);
		
		royaltyDetails.removeAll();
		royaltyDetails.revalidate();
		
		TableModel model = getTableBooks().getModel();
		bookTitles.setModel(model);
		
		//Sorts table
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(bookTitles.getModel());
		bookTitles.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		bookTitles.getTableHeader().setReorderingAllowed(false);
		bookTitles.getSelectionModel().addListSelectionListener(this);
	}

//	private static Boolean royaltiesUniformAcrossChannelsCheck(Book book) {
//		Boolean uniformity = true;
//		//For each channel (except createspace), check that it has a royalty list for that book
//		for (Channel ch : SalesHistory.get().getListChannels().values()) {
//			if (!ch.getName().equals("Createspace") && ch.getListRoyalties().get(book) == null) {
//				uniformity = false;
//			}
//		}
//		if (uniformity) {
//			HashMap<Person, IRoyaltyType> royaltiesAmazon = SalesHistory.get().getListChannels().get("Amazon").getListRoyalties().get(book);
//			HashMap<Person, IRoyaltyType> royaltiesApple = SalesHistory.get().getListChannels().get("Apple").getListRoyalties().get(book);
//			HashMap<Person, IRoyaltyType> royaltiesKobo = SalesHistory.get().getListChannels().get("Kobo").getListRoyalties().get(book);
//			HashMap<Person, IRoyaltyType> royaltiesNook = SalesHistory.get().getListChannels().get("Nook").getListRoyalties().get(book);
//
//			if (!checkRoyaltyRules(royaltiesAmazon, royaltiesApple) || !checkRoyaltyRules(royaltiesAmazon, royaltiesKobo)
//					|| !checkRoyaltyRules(royaltiesAmazon, royaltiesNook)) {
//				uniformity = false;
//			}
//		}
//		return uniformity;
//	}
//
//	private static Boolean checkRoyaltyRules(HashMap<Person, IRoyaltyType> channel1, HashMap<Person, IRoyaltyType> channel2) {
//		Boolean same = true;
//
//		for (Person p : channel1.keySet()) {
//			if (!channel2.keySet().contains(p)) {
//				same = false;
//			}
//		}
//		if (same) {
//			for (Person p : channel1.keySet()) {
//				if (((RoyaltyPercentage) channel1.get(p)).getPercentage() != ((RoyaltyPercentage) channel2.get(p)).getPercentage()) {
//					same = false;
//				}
//			}
//		}
//
//
//		return same;
//	}

}
