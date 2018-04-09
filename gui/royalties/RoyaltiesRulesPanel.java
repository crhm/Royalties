package gui.royalties;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

/**A JPanel displaying royalties information.
 * <br>The user selects which channel he wishes to see the royalties for, then is shown the channel's list of books for which 
 * there are royalties.
 * <br>The user can then select a book, to display the royalty holders (and their percentages) for that book.
 * <br>Display is initialised as if Channel "Apple" had been selected.
 * <br>The list of channels is a JComboBox.
 * <br>The list of books is actually a one column JTable, which is sorted.
 * <br>The royalty details per book is a JTable (not sortable, not editable) with two columns.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class RoyaltiesRulesPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	//TODO add button should be dynamic; if only no particular royalty is selected, it should read 'add book to list of books with royalties...'
	//and first add a book (from list of books - books already with a royalty for this channel, or create a new one) and then add a royalty to it. 
	//If one is selected, it should read 'add royalty to this book' and perform that action only.
		
	private JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
	private JButton editButton = new JButton("Edit");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	
	private JPanel containerPanel = new JPanel(new BorderLayout());

	//ComboBox to select the channel whose royalties are to be displayed
	private JComboBox<String> channelsDropDown = null; 

	//Displays the book titles for which there are royalty holders (for the selected channel)
	private JTable bookTitles = null; 
	
	//Displays the royalties for the selected book
	private JTable royaltiesTable = null;

	//Displays the details of the royalties for the book selected amongst the book titles. If none are selected, no details are shown
	private JPanel royaltyDetails = new JPanel(new BorderLayout()); 

	//Holds the index of selected book title so that in case of sorting the selection can be maintained despite index change
	private int selectionIndexBeforeSort = 0;
	
	//Holds the name of the channel that is currently selected. Initialised as "Apple" because it is first in the Channel list
	private String currentChannel = "Apple";
	
	//Holds the name of the royalty holder that is currently selected
	private String currentPerson = null;
	
	//Holds the title of the book that is currently selected
	private String currentBook = null;

	public RoyaltiesRulesPanel() {
		super();
		this.setLayout(new BorderLayout());

		//Obtains list of channels and passes it to the combobox
		String[] listChannels = new String[SalesHistory.get().getListChannels().keySet().size()];
		int counter = 0;
		for (String s : SalesHistory.get().getListChannels().keySet()) {
			listChannels[counter] = s;
			counter++;
		}
		channelsDropDown = new JComboBox<String>(listChannels);
		channelsDropDown.addActionListener(this);

		//Gets the list of book titles for initial channel (apple)
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
		containerPanel.add(channelsDropDown, BorderLayout.NORTH);
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
			new EditRoyaltyDialog(currentChannel, currentBook, currentPerson);
		} else { //Action is from channels Combobox 
			//Change channel variable to reflect one currently selected
			this.currentChannel = (String) channelsDropDown.getSelectedItem();

			//Empty and erase royalty details panel
			if (royaltiesTable != null && royaltiesTable.isShowing()) {
				royaltiesTable.getSelectionModel().removeListSelectionListener(this);
			}
			royaltyDetails.removeAll();
			royaltyDetails.revalidate();
			royaltyDetails.repaint();

			//Have to do this to avoid crashes as it would listen to a selection on a table that is going to be completely redone
			bookTitles.getSelectionModel().removeListSelectionListener(this);

			//Fills the table with the new data (as obtained by getting the model off of a new table)
			TableModel model = getTableBooks().getModel();
			bookTitles.setModel(model);
			bookTitles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//Sorts table
			TableRowSorter<TableModel> sorter = new TableRowSorter<>(bookTitles.getModel());
			bookTitles.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>();
			int columnIndexToSort = 0;
			sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
			sorter.setSortKeys(sortKeys);
			sorter.sort();

			//Repaint with current data
			bookTitles.revalidate();
			bookTitles.repaint();

			//Reinstate list selection listener so that royalty details can be obtained for this new list
			bookTitles.getSelectionModel().addListSelectionListener(this);
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
				int tableRow = bookTitles.getSelectedRow();
				String bookTitle = bookTitles.getValueAt(tableRow, 0).toString();
				Book book = SalesHistory.get().getBook(bookTitle);
				if (royaltiesTable != null && royaltiesTable.isShowing()) {
					royaltiesTable.getSelectionModel().removeListSelectionListener(this);
				}
				royaltyDetails.removeAll();
				royaltyDetails.revalidate();
				if (royaltiesTable != null) {
					TableModel model = getTableRoyalties(book).getModel(); 
					royaltiesTable.setModel(model);
					royaltiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				} else {
					royaltiesTable = getTableRoyalties(book);
					royaltiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				}
				royaltiesTable.getSelectionModel().addListSelectionListener(this);
				royaltyDetails.add(new JScrollPane(royaltiesTable), BorderLayout.CENTER);
				royaltyDetails.revalidate();
				royaltyDetails.repaint();

				currentBook = (String) bookTitles.getValueAt(tableRow, 0); 
			}	
		} else if (e.getSource() == royaltiesTable.getSelectionModel()) {
			if (royaltiesTable.getSelectedRow() != -1) {
				deleteButton.setEnabled(true);
				editButton.setEnabled(true);
				int row = royaltiesTable.convertRowIndexToModel(royaltiesTable.getSelectedRow());
				currentPerson = (String) royaltiesTable.getValueAt(row, 0);
			} else {
				deleteButton.setEnabled(false);
				editButton.setEnabled(false);
			}
		}	
	}

	/**Returns a table contains the royalty information for the book passed as argument
	 * @param b Book of which we want the royalty details
	 * @return a JTable (not sortable, not editable) containing royalties data for that book
	 */
	private JTable getTableRoyalties(Book b) {
		String[] columnNames = {"Royalty Holder", "Royalty"};
		DefaultTableModel model = new DefaultTableModel(getDataRoyalties(b), columnNames) {
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

	/**Obtains the data to be put into a table model for the royalties of the book passed as argument
	 * @param b Book whose royalty data we want
	 * @return The royalties data to be put in to a tablemodel
	 */
	private Object[][] getDataRoyalties(Book b){
		HashMap<Person, IRoyaltyType> listRoyalties = SalesHistory.get().getListChannels().get(currentChannel).getListRoyalties().get(b);
		int numberOfRows = listRoyalties.keySet().size(); //THIS IS ALWAYS WHERE IT FAILS WHEN THERE'S A BUG
		Object[][] data = new Object[numberOfRows][2];
		int count = 0;
		for (Person p : listRoyalties.keySet()) {
			data[count][0] = p.getName();
			data[count][1] = listRoyalties.get(p);
			count++;
		}
		return data;
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

	/**Returns the data to be put into a tablemodel for the list of books with royalties info for currently selected channel
	 * 
	 * @return data to be put into a tablemodel for the list of books with royalties info for currently selected channel
	 */
	private Object[][] getDataBooks(){
		int numberOfRows = SalesHistory.get().getListChannels().get(currentChannel).getListRoyalties().keySet().size();
		Object[][] data = new Object[numberOfRows][1];
		int count = 0;
		for (Book b : SalesHistory.get().getListChannels().get(currentChannel).getListRoyalties().keySet()) {
			data[count][0] = b.getTitle();
			count++;
		}
		return data;
	}

	public void updateData() {
		//TODO
	}

}
