package gui.books;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import main.Book;
import main.SalesHistory;

/**A JPanel containing a JTable representing the list of books managed by PLP.
 * <br>Information displayed for each book: Title, Author, Identifiers, Total Sold.
 * <br>Table can be sorted by user, but prioritises sorting by Book title.
 * <br>Cells are non-editable.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class BookPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	private JTable booksTable;
	private JButton editButton = new JButton("Edit Book");
	private JButton addButton = new JButton("Add New Book");
	private JButton deleteButton = new JButton("Delete Book");
	
	//TODO fix appearance of edit button to only happen when one book is selected, and think about delete behavior when more than one is selected
	//TODO add "these are the same books" button for multiple selection?


	public BookPanel() {
		super();
		this.setLayout(new BorderLayout());
		//Because no book is selected initially 
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		
		//Adding behavior to buttons
		editButton.addActionListener(this);
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		
		//Setting up button Panel
		JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
		buttonPanel.add(new JLabel());//Bad solution to problem of making buttons smaller - fix please? TODO
		buttonPanel.add(new JLabel());
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(editButton);
		
		//Setting up JTable and selection behavior
		this.booksTable = getTable();
		this.booksTable.getSelectionModel().addListSelectionListener(this);
		
		//Setting up container Panel
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(new JScrollPane(this.booksTable), BorderLayout.CENTER);
	}

	/**Returns the JTable representing the list of books managed by PLP.
	 */
	private JTable getTable() {
		//Prepares table model
		Object[] columnNames = {"Title", "Author", "Identifiers", "Total Sold"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 2 : return Array.class;
				case 3 : return Integer.class;
				default : return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		//Sets up table
		JTable table = new JTable(model);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(3).setMaxWidth(100);

		//Sets up sorting by book title
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);

		return table;
	}

	/**Returns the data to be plugged into the model for the JTable representing the list of books managed by PLP
	 * Titles and Authors are stripped of potential quotation marks for cleanliness of presentation.
	 */
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListPLPBooks().values().size()][4];
		int count = 0;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			data[count][0] = b.getTitle();
			data[count][1] = b.getAuthor();
			data[count][2] = b.getIdentifiers();
			BigDecimal totalSold = new BigDecimal(b.getTotalUnitsSold());
			data[count][3] = totalSold.setScale(0);

			count++;
		}
		return data;
	}

	/**Defines behavior of buttons in Button Panel.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == editButton) {
				//TODO figure out how I manage consequences for other tables in other tabs that represent such info...?
				int row = booksTable.convertRowIndexToModel(booksTable.getSelectedRow());
				String title = (String) booksTable.getModel().getValueAt(row, 0);
				Book book = SalesHistory.get().getListPLPBooks().get(title);
				EditBookDialog editBookDialog = new EditBookDialog(book);
				editBookDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) { //update and repaint table on close of editBookDialog
					updateTable();
				}				
			});
		} else if (e.getSource() == addButton) {
			AddBookDialog addBookDialog = new AddBookDialog();
			addBookDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) { //update and repaint table on close of addBookDialog
					updateTable();
				}				
			});

		} else if (e.getSource() == deleteButton) { 
			//TODO figure out stuff below
			// What should the consequences be for existing sales of that book?
			// What should the consequences be if it is in royalty lists?
			int option = JOptionPane.showConfirmDialog(this, "Please confirm you want to delete this book.", "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (option == 0) { //If user clicks OK
				int row = booksTable.convertRowIndexToModel(booksTable.getSelectedRow());
				String title = (String) booksTable.getModel().getValueAt(row, 0);
				Book book = SalesHistory.get().getListPLPBooks().get(title);
				SalesHistory.get().removeBook(book);
				updateTable();
			}
		}
	}

	/**Makes sure buttons for editing a book's details or deleting a book are inactive if no book is selected
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (booksTable.getSelectedRow() != -1) {
			deleteButton.setEnabled(true);
			editButton.setEnabled(true);
		} else {
			deleteButton.setEnabled(false);
			editButton.setEnabled(false);
		}	
	}
	
	/**Method to be called when the data of the list of books in SalesHistory has changed, and thus the table needs 
	 * to be repainted.
	 */
	private void updateTable() {
		//To avoid crashes while it is being reworked
		booksTable.getSelectionModel().removeListSelectionListener(this);
		
		//Update data by updating model
		TableModel model = getTable().getModel();
		booksTable.setModel(model);
		TableColumnModel columnModel = booksTable.getColumnModel();
		columnModel.getColumn(3).setMaxWidth(100);

		//Sets up sorting by book title
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(booksTable.getModel());
		booksTable.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();

		//Repaint with current data
		booksTable.revalidate();
		booksTable.repaint();

		//Re-add the listSelectionListener
		booksTable.getSelectionModel().addListSelectionListener(this);				

		//By redrawing the table, the selection has disappeared, so disable edit and delete buttons
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

}
