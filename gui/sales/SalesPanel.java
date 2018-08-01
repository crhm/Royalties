package gui.sales;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import gui.renderers.NumberRenderer;
import main.Sale;
import main.SalesHistory;

/**A JPanel containing a JTable representing the sales history of PLP.
 * <br>For each sale, the Table contains:
 * <br>Channel through which it was sold.
 * <br>Country or region in which it was sold.
 * <br>Date of sale (month and year in which it was sold). 
 * <br>Book sold (Title and Author).
 * <br>Net Units sold (can be negative due to returns).
 * <br>PLP revenues (taken directly from raw data provided by sales channel, but should be equal to 
 * net units sold * (price - delivery costs) * PLP's Royalty.
 * <br>Currency the sale was carried out in.
 * <br>Exchange rate into US Dollars for that date
 * <br>PLP revenues in US Dollars
 * <br>Whether or not the royalties PLP owes for this sale have been calculated and added to the relevant persons' balances.
 * <br><br>The Table is not editable but is sortable by the user. 
 * It prioritises sorting by Date, then by Channel, then by Country, then by Book.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class SalesPanel extends JPanel implements ActionListener, TableColumnModelListener {
	JTable salesTable;
	JPanel tablePanel = new JPanel(new BorderLayout());
	JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 0, 0));
	JButton bttnCalculateRoyalties = new JButton("Calculate Royalties");
	
	int widthCol0 = 75;
	int widthCol1 = 50;
	int widthCol2 = 70;
	int widthCol3 = 350;
	int widthCol4 = 120;
	int widthCol5 = 80;
	int widthCol6 = 80;
	int widthCol7 = 65;
	int widthCol8 = 80;
	int widthCol9 = 100;
	int widthCol10 = 100;

	
	public SalesPanel() {
		super();

		this.setLayout(new BorderLayout());
		this.setOpaque(true);
		salesTable = getTable();
		salesTable.getColumnModel().addColumnModelListener(this);
		JScrollPane salesScrollPane = new JScrollPane(salesTable);
		tablePanel.add(salesScrollPane, BorderLayout.CENTER);
		
		bttnCalculateRoyalties.addActionListener(this);
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());
		buttonPanel.add(bttnCalculateRoyalties);
		
		this.add(tablePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.NORTH);
	}

	/**Returns the JTable representing the sales history of PLP.
	 */
	private JTable getTable() {
		//Sets up the model
		Object[] columnNames = {"Channel", "Country", "Date", "Book", "Main Author", "Net Units Sold", 
				"PLP Revenues", "Currency", "Exchange Rate", "PLP Revenues in USD", "Royalties Have Been Calculated"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 5 : return Double.class;
				case 6 : return Double.class;
				case 7 : return Currency.class;
				case 8 : return Double.class;
				case 9 : return Double.class;
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
		setTableSettings(table);
		
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
			data[rowCounter][3] = s.getBook().getTitle();
			String author = "";
			if (s.getBook().getAuthor1() != null) {
				author = s.getBook().getAuthor1().getName();
			}
			data[rowCounter][4] = author;
			data[rowCounter][5] = s.getNetUnitsSold();
			data[rowCounter][6] = s.getRevenuesPLP();;
			data[rowCounter][7] = s.getCurrency();
			if (s.getChannel().getName().equals("Amazon") || s.getChannel().getName().equals("Apple")) {
				try {
					data[rowCounter][8] = s.getChannel().getHistoricalForex().get(s.getDate()).get(s.getCurrency().getCurrencyCode());
					data[rowCounter][9] = s.getRevenuesPLP() * (Double) data[rowCounter][8];
				} catch (Exception e) { //TODO fix this mess properly and not like this (when the hist forex hasn't been imported)
					data[rowCounter][8] = null;
					data[rowCounter][9] = null;
				}
			} else {
				data[rowCounter][8] = 1.00;
				data[rowCounter][9] = s.getRevenuesPLP();
			}
			data[rowCounter][10] = s.getRoyaltyHasBeenCalculated();
			rowCounter++;
		}
		return data;
	}

	/**Ensures that the table has a max width for its columns, renders its tenth column as a currency, 
	 * that the user cannot reorder the table's columns, 
	 * and that it is sorted in the following order of columns: 3, 1, 2, 4.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(widthCol0);
		columnModel.getColumn(0).setMaxWidth(100);
		columnModel.getColumn(1).setPreferredWidth(widthCol1);
		columnModel.getColumn(1).setMaxWidth(60);
		columnModel.getColumn(2).setPreferredWidth(widthCol2);
		columnModel.getColumn(2).setMaxWidth(65);
		columnModel.getColumn(3).setPreferredWidth(widthCol3);
		columnModel.getColumn(3).setMaxWidth(400);
		columnModel.getColumn(4).setPreferredWidth(widthCol4);
		columnModel.getColumn(4).setMaxWidth(150);
		columnModel.getColumn(5).setPreferredWidth(widthCol5);
		columnModel.getColumn(5).setMaxWidth(110);
		columnModel.getColumn(6).setPreferredWidth(widthCol6);
		columnModel.getColumn(6).setMaxWidth(110);
		columnModel.getColumn(7).setPreferredWidth(widthCol7);
		columnModel.getColumn(7).setMaxWidth(80);
		columnModel.getColumn(8).setPreferredWidth(widthCol8);
		columnModel.getColumn(8).setMaxWidth(110);
		columnModel.getColumn(9).setPreferredWidth(widthCol9);
		columnModel.getColumn(9).setMaxWidth(110);
		columnModel.getColumn(10).setPreferredWidth(widthCol10);
		columnModel.getColumn(10).setMaxWidth(110);
		
		columnModel.getColumn(9).setCellRenderer(NumberRenderer.getCurrencyRenderer());

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
	}
	
	/**Method to call when the data may have changed and the table needs to be updated.
	 */
	public void updateData() {
		salesTable.getColumnModel().removeColumnModelListener(this);
		TableModel model = getTable().getModel();
		salesTable.setModel(model);
		setTableSettings(salesTable);
		salesTable.getColumnModel().addColumnModelListener(this);
		
		Boolean noMoreRoyalties = true;
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			if (!s.getRoyaltyHasBeenCalculated()) {
				noMoreRoyalties = false;
			}
		}
		if (noMoreRoyalties) {
			bttnCalculateRoyalties.setEnabled(false);
			bttnCalculateRoyalties.setToolTipText("All royalties have been calculated");
		} else {
			bttnCalculateRoyalties.setEnabled(true);
			bttnCalculateRoyalties.setToolTipText("Calculate royalties owed by PLP for sales where it has not been done yet.");	
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bttnCalculateRoyalties) {
			SalesHistory.get().calculateAllRoyalies();
			updateData();
		}
	}

	@Override
	public void columnMarginChanged(ChangeEvent e) {
		TableColumnModel model = salesTable.getColumnModel();
		widthCol0 = model.getColumn(0).getWidth();
		widthCol1 = model.getColumn(1).getWidth();
		widthCol2 = model.getColumn(2).getWidth();
		widthCol3 = model.getColumn(3).getWidth();
		widthCol4 = model.getColumn(4).getWidth();
		widthCol5 = model.getColumn(5).getWidth();
		widthCol6 = model.getColumn(6).getWidth();
		widthCol7 = model.getColumn(7).getWidth();
		widthCol8 = model.getColumn(8).getWidth();
		widthCol9 = model.getColumn(9).getWidth();
		widthCol10 = model.getColumn(10).getWidth();
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

	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
	}
}
