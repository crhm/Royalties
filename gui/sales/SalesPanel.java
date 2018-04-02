package gui.sales;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Sale;
import main.SalesHistory;

/**A JPanel containing a JTable representing the sales history of PLP.
 * <br>For each sale, the Table contains:
 * <br>Channel through which it was sold.
 * <br>Country or region in which it was sold.
 * <br>Date of sale (month and year in which it was sold). 
 * <br>Book sold (Title and Author).
 * <br>Net Units sold (can be negative due to returns).
 * <br>PLP's Royalty (what percentage of the sales revenue the sales channel gives to PLP)
 * <br>Price (per unit).
 * <br>Delivery costs (may be zero).
 * <br>PLP revenues (taken directly from raw data provided by sales channel, but should be equal to 
 * net units sold * (price - delivery costs) * PLP's Royalty.
 * <br>Currency the sale was carried out in.
 * <br>Whether or not the royalties PLP owes for this sale have been calculated and added to the relevant persons' balances.
 * <br><br>The Table is not editable but is sortable by the user. 
 * It prioritises sorting by Date, then by Channel, then by Country, then by Book.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class SalesPanel extends JPanel {

	public SalesPanel() {
		super();

		this.setLayout(new GridLayout());
		this.setOpaque(true);

		JScrollPane salesScrollPane = new JScrollPane(getTable());
		this.add(salesScrollPane);
	}

	/**Returns the JTable representing the sales history of PLP.
	 */
	private JTable getTable() {
		//Sets up the model
		Object[] columnNames = {"Channel", "Country", "Date", "Book", "Net Units Sold", "PLP's Royalty", 
				"Price", "Delivery Costs", "PLP's Revenues", "Currency", "Royalties Have Been Calculated"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 4 : return Double.class;
				case 5 : return Double.class;
				case 6 : return Double.class;
				case 7 : return Double.class;
				case 8 : return Double.class;
				case 9 : return Currency.class;
				case 10 : return Boolean.class;
				default : return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		//Sets up the table
		JTable table = new JTable(model);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setMaxWidth(100);
		columnModel.getColumn(1).setMaxWidth(60);
		columnModel.getColumn(2).setMaxWidth(65);
		columnModel.getColumn(4).setMaxWidth(110);
		columnModel.getColumn(5).setMaxWidth(90);
		columnModel.getColumn(6).setMaxWidth(60);
		columnModel.getColumn(7).setMaxWidth(110);
		columnModel.getColumn(8).setMaxWidth(110);
		columnModel.getColumn(9).setMaxWidth(60);
		columnModel.getColumn(10).setMaxWidth(110);

		//Sorts the table by Date, Channel, Country and Book (in that order)
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSortFirst = 2;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortFirst, SortOrder.ASCENDING));
		int columnIndexToSortSecond = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortSecond, SortOrder.ASCENDING));
		int columnIndexToSortThird = 1;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortThird, SortOrder.ASCENDING));
		int columnIndexToSortFourth = 3;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortFourth, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();

		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);
		
		return table;
	}

	/**Returns the data to be plugged into the appropriate table model for PLP's sale history.
	 * <br>Book titles and authors are stripped of quotation marks for cleanliness of presentation.
	 */
	private Object[][] getData() {
		int numberOfSales = SalesHistory.get().getSalesHistory().size();
		Object[][] data = new Object[numberOfSales][11];
		int rowCounter = 0;
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			data[rowCounter][0] = s.getChannel().getName();
			data[rowCounter][1] = s.getCountry();
			data[rowCounter][2] = s.getDate();
			data[rowCounter][3] = s.getBook().getTitle() + ", " + s.getBook().getAuthor();
			data[rowCounter][4] = s.getNetUnitsSold();
			data[rowCounter][5] = s.getRoyaltyTypePLP();
			data[rowCounter][6] = s.getPrice();
			data[rowCounter][7] = s.getDeliveryCost();
			data[rowCounter][8] = s.getRevenuesPLP();
			data[rowCounter][9] = s.getCurrency();
			data[rowCounter][10] = s.getRoyaltyHasBeenCalculated();
			rowCounter++;
		}
		return data;
	}
}
