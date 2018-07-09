package gui.books;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Book;
import main.SalesHistory;

/**A JPanel containing a JTable representing the list of books managed by PLP.
 * <br>Information displayed for each book: Book Number, Title, Author, Identifiers, Total Sold.
 * <br>Table can be sorted by user, but prioritises sorting by Book title.
 * <br>Cells are non-editable.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class BookPanel extends JPanel implements ActionListener, ListSelectionListener, TableColumnModelListener {

	private JTable booksTable;
	private JButton addButton = new JButton("Create New Book");
	private JButton editButton = new JButton("Edit Selected Book");
	private JButton deleteButton = new JButton("Delete Selected Book");
	private JButton mergeButton = new JButton("Merge Together Two Books Selected");
	int selectedIndex1 = -1;
	int selectedIndex2 = -1;
	int widthCol0 = 50;
	int widthCol1 = 300;
	int widthCol2 = 130;
	int widthCol3 = 300;
	int widthCol4 = 50;


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
		mergeButton.addActionListener(this);
		mergeButton.setEnabled(false);

		//Setting up button Panel
		JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(mergeButton);

		//Setting up JTable and selection behavior
		this.booksTable = getTable();
		this.booksTable.getSelectionModel().addListSelectionListener(this);
		this.booksTable.getColumnModel().addColumnModelListener(this);

		//Setting up container Panel
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(new JScrollPane(this.booksTable), BorderLayout.CENTER);
	}

	/**Returns the JTable representing the list of books managed by PLP.
	 */
	private JTable getTable() {
		//Prepares table model
		Object[] columnNames = {"Book Number", "Title", "Main Author", "Identifiers", "Total Sold"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0 : return Long.class;
				case 3 : return Arrays.class;
				case 4 : return Integer.class;
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
		setTableSettings(table);

		return table;
	}

	/**Returns the data to be plugged into the model for the JTable representing the list of books managed by PLP
	 * Titles and Authors are stripped of potential quotation marks for cleanliness of presentation.
	 */
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListPLPBooks().size()][5];
		int count = 0;
		for (Book b : SalesHistory.get().getListPLPBooks()) {
			data[count][0] = b.getBookNumber();
			data[count][1] = b.getTitle();
			if (b.getAuthor1() != null) {
				data[count][2] = b.getAuthor1().getName();
			} else {
				data[count][2] = "";
			}
			data[count][3] = b.getIdentifiers();
			BigDecimal totalSold = new BigDecimal(b.getTotalUnitsSold());
			data[count][4] = totalSold.setScale(0);

			count++;
		}
		return data;
	}

	/**Defines behavior of buttons in Button Panel.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == editButton) {
			int row = booksTable.convertRowIndexToModel(booksTable.getSelectedRow());
			Long bookNumber = (Long) booksTable.getModel().getValueAt(row, 0);
			Book book = SalesHistory.get().getBookWithNumber(bookNumber);
			EditBookDialog editBookDialog = new EditBookDialog(book);
			editBookDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) { //update and repaint table on close of editBookDialog
					updateData();
				}				
			});
		} else if (e.getSource() == addButton) {
			AddBookDialog addBookDialog = new AddBookDialog();
			addBookDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) { //update and repaint table on close of addBookDialog
					updateData();
				}				
			});

		} else if (e.getSource() == deleteButton) { 
			int option = JOptionPane.showConfirmDialog(this, "Please confirm you want to delete this book."
					+ "\nDeleting this book will not affect past sales for which royalties"
					+ "\nhave already been calculated, but it will remove it from royalty lists"
					+ "\nfor any future royalty calculation.", "Warning!", 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (option == 0) { //If user clicks OK
				int row = booksTable.convertRowIndexToModel(booksTable.getSelectedRow());
				Long bookNumber = (Long) booksTable.getModel().getValueAt(row, 0);
				Book book = SalesHistory.get().getBookWithNumber(bookNumber);
				SalesHistory.get().removeBook(book);
				updateData();
			}
		} else if (e.getSource() == mergeButton) {
			int row1 = booksTable.convertRowIndexToModel(selectedIndex1);
			int row2 = booksTable.convertRowIndexToModel(selectedIndex2);
			long bookNumber1 = (long) booksTable.getModel().getValueAt(row1, 0);
			long bookNumber2 = (long) booksTable.getModel().getValueAt(row2, 0);
			Book book1 = SalesHistory.get().getBookWithNumber(bookNumber1);
			Book book2 = SalesHistory.get().getBookWithNumber(bookNumber2);
			int userChoice = JOptionPane.showConfirmDialog(null, "Please confirm that you want to merge these two books. "
					+ "This will affect royalties and past sales.", 
					"Confirmation Required", JOptionPane.OK_CANCEL_OPTION);
			if (userChoice == JOptionPane.OK_OPTION) {
				book1.merge(book2);
				SalesHistory.get().removeBook(book2);
				updateData();
			}
		}
	}

	/**Makes sure buttons for editing a book's details or deleting a book are inactive if no book is selected
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (booksTable.getSelectedRows().length == 1) {
			mergeButton.setEnabled(false);
			deleteButton.setEnabled(true);
			editButton.setEnabled(true);
		} else if (booksTable.getSelectedRows().length == 2) {
			selectedIndex1 = booksTable.getSelectionModel().getMinSelectionIndex();
			selectedIndex2 = booksTable.getSelectionModel().getMaxSelectionIndex();
			mergeButton.setEnabled(true);
			deleteButton.setEnabled(false);
			editButton.setEnabled(false);
		} else {
			mergeButton.setEnabled(false);
			deleteButton.setEnabled(false);
			editButton.setEnabled(false);
		}
	}

	/**Ensures the table has a max width for its fourth column, that it is sorted by its first, and 
	 * that the reordering of columns by the user is disabled.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		//Hiding the book number column, hence offsetting column indexes but just for the columnModel!
		//This allows the book number data to be retrieved from the model while not being displayed to user.
		columnModel.removeColumn(columnModel.getColumn(0));
		columnModel.getColumn(0).setPreferredWidth(widthCol0);
		columnModel.getColumn(1).setPreferredWidth(widthCol1);
		columnModel.getColumn(1).setMaxWidth(160);
		columnModel.getColumn(2).setPreferredWidth(widthCol2);
		columnModel.getColumn(3).setPreferredWidth(widthCol3);
		columnModel.getColumn(3).setMaxWidth(85);

		//Sets up sorting by book title
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 1; //this is the table, not the table column model, hence it still is column 1 and not 0.
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();

		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);
	}

	/**Method to be called when the data of the list of books in SalesHistory has changed, and thus the table needs 
	 * to updated.
	 */
	public void updateData() {
		//To avoid crashes while it is being reworked
		booksTable.getSelectionModel().removeListSelectionListener(this);
		booksTable.getColumnModel().removeColumnModelListener(this); ;

		//Update data by updating model
		TableModel model = getTable().getModel();
		booksTable.setModel(model);
		setTableSettings(booksTable);

		//Re-add the listSelectionListener
		booksTable.getSelectionModel().addListSelectionListener(this);		
		booksTable.getColumnModel().addColumnModelListener(this);

		//By redrawing the table, the selection has disappeared, so disable edit and delete buttons
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	@Override
	public void columnMarginChanged(ChangeEvent e) {
		TableColumnModel model = booksTable.getColumnModel();
		widthCol0 = model.getColumn(0).getWidth();
		widthCol1 = model.getColumn(1).getWidth();
		widthCol2 = model.getColumn(2).getWidth();
		widthCol3 = model.getColumn(3).getWidth();
	}

	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
	}

	@Override
	public void columnAdded(TableColumnModelEvent e) {
	}

	@Override
	public void columnRemoved(TableColumnModelEvent e) {
	}

	@Override
	public void columnMoved(TableColumnModelEvent e) {
	}



}
