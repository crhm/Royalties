package gui.dataverification;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import java.text.ParseException;

import main.DataVerification;
import main.SalesHistory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("serial")
public class SalesImportSummaryPanel extends JPanel {
	private JTable tableDates;
	
	public SalesImportSummaryPanel() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		tableDates = getTable();
		scrollPane.setViewportView(tableDates);
	}

	private JTable getTable() {
		Object[] columnNames = {"Month", "Amazon", "Apple", "Kobo", "Nook", "Createspace"};

		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) { //For autoSorter
				switch (column) {
				case 0 : return Date.class;
				default : return Boolean.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		//Setting up JTable
		JTable table = new JTable(model);
		setTableSettings(table);

		return table;
	}

	/**Returns the data to plug into the model for the table, namely whether or not sales
	 *  have been found in for each channel for the existing months and years.
	 *  <br>First column: date (month and year format)
	 *  <br>Then the columns are, in order, Amazon, Apple, Kobo, Nook, Createspace.
	 * @return An array of objects, with Dates in the first column and Booleans everywhere else
	 */
	private Object[][] getData(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

		Object[][] data = new Object[SalesHistory.get().getListMonths().size()][6];
		int count = 0;
		for (String date : SalesHistory.get().getListMonths()) {
			try {
				data[count][0] = dateFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data[count][1] = salesFoundCheck("Amazon", date);
			data[count][2] = salesFoundCheck("Apple", date);
			data[count][3] = salesFoundCheck("Kobo", date);
			data[count][4] = salesFoundCheck("Nook", date);
			data[count][5] = salesFoundCheck("Createspace", date);

			count++;
		}
		return data;
	}

	/**Returns a boolean indicating whether or not sales have been found for the channel passed as argument 
	 * at the date passed as argument.
	 * @param channelName name of channel for which we want to check the presence of sales
	 * @param date month and year (MMM yyyy) representation of when we should check for sales
	 * @return true if sales are found, false if not.
	 */
	private Boolean salesFoundCheck(String channelName, String date) {

		HashMap<String, Set<String>> channelsPerMonth = DataVerification.getListChannelsWithSalesForEachMonth();

		if (channelsPerMonth.get(date).contains(channelName)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**Ensures that the table has a visible grid with light gray lines, is sortable, 
	 * renders the first column as dates in the correct format, and forbids the user from 
	 * reording columns.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setAutoCreateRowSorter(true);
		
		//Creating a cell renderer for the date column so that they are displayed as month and year only
		TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

			SimpleDateFormat f = new SimpleDateFormat("MMM yyyy");

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if( value instanceof Date) {
					value = f.format(value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
			}
		};
		
		//Setting the cell renderer for the date column
		table.getColumnModel().getColumn(0).setCellRenderer(tableCellRenderer);

		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);
	}

	/**Method to call when the data may have changed and the table should be updated.
	 */
	public void updateData() {
		TableModel model = getTable().getModel();
		tableDates.setModel(model);
		
		setTableSettings(tableDates);
	}

}


