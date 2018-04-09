package gui.dataverification;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import gui.royalties.MultilineCellEditor;
import gui.royalties.MultilineCellRenderer;
import main.DataVerification;

/**Provides a JPanel containing a JTable representing information obtained through the DataVerification methods. 
 * <br>Displays details of where errors occur.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class DataVerificationPanel extends JPanel {
	
	JTable table;
	
	/**Constructor - sets up the DataVerification panel with a GridLayout with only one cell, 
	 * and adds to it a ScrollPane containing the panel.
	 */
	public DataVerificationPanel() {
		super();
		this.setLayout(new GridLayout(1, 1));
		this.add(new JScrollPane(getTable()));
	}
	
	/**Returns the JTable representing the information obtained through the DataVerification methods.
	 * <br>Three columns (Description of what is being verified, Success or failure, Reason for failure) 
	 * and three rows (verification of sales data, verification of fx rates, verification of royalty data).
	 * <br>Table is auto sorted at user's request. 
	 * @return The appropriate JTable
	 */
	private JTable getTable() {
		Object[] columnNames = {"Verifying...", "Success", "Reason for failure"};
		
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) { //For autoSorter
				switch (column) {
				case 1 : return Boolean.class;
				case 2 : return JList.class;
				default : return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 2) { //So that user can use scroll bars in third column cells
					return true;
				} else {
					return false;
				}
			}
		};
		
		//Setting up JTable
		table = new JTable(model);
		setTableSettings(table);

		return table;
	}

	/**Returns the data to be plugged into a TableModel to obtain the appropriate JTable
	 * <br>First column: description of what is being checked (string)
	 * <br>Second column: success or failure (boolean, which is automatically rendered as a JCheckBox)
	 * <br>Third column: empty if DataVerification method detects nothing wrong, otherwise array of strings describing the errors.
	 * @return Object[][] - the data to be plugged into a TableModel to obtain the appropriate JTable
	 */
	private Object[][] getData(){
		Object[][] data = new Object[3][3];
		//Sales
		data[0][0] = "All channels have sales for every month imported so far?";
		data[0][1] = null;
		if (DataVerification.checkSalesDataForAllChannels().isEmpty()) {
			data[0][1] = true;
		} else {
			data[0][1] = false;
		}
		String[] listSales = DataVerification.checkSalesDataForAllChannels().split("\n");
		data[0][2] = listSales;

		//FX rates
		data[1][0] = "Foreign Exchange rates have been found for all sales?";
		data[1][1] = null;
		if (DataVerification.checkForexDataForRelevantChannels().isEmpty()) {
			data[1][1] = true;
		} else {
			data[1][1] = false;
		}
		String[] listFX = DataVerification.checkForexDataForRelevantChannels().split("\n");
		data[1][2] = listFX;

		//Royalties
		data[2][0] = "Royalty information has been found for all books sold?";
		data[2][1] = null;
		if (DataVerification.checkRoyaltiesDataForAllChannels().isEmpty()) {
			data[2][1] = true;
		} else {
			data[2][1] = false;
		}
		String[] listRoyalties = DataVerification.checkRoyaltiesDataForAllChannels().split("\n");
		data[2][2] = listRoyalties;

		return data;
	}

	/**Ensures the table has a certain row heighth, a visible grid of light gray lines, is autosorted, 
	 * has a fixed width for its first 2 columns, a custom renderer + editor for its third, and that 
	 * the user cannot reorder its columns.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		table.setRowHeight(150);
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setAutoCreateRowSorter(true);
		
		//Setting column width (problem on other terminals though?)
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setMinWidth(325);
		columnModel.getColumn(0).setMaxWidth(325);
		columnModel.getColumn(1).setMinWidth(75);
		columnModel.getColumn(1).setMaxWidth(75);
		
		//Setting a custom cell renderer and editor for third column so that each
		//of its cells is a JList (of potential errors) and can be scrolled.
		columnModel.getColumn(2).setCellRenderer(new MultilineCellRenderer());
		columnModel.getColumn(2).setCellEditor(new MultilineCellEditor());
		
		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	/**Method to call when the data may have changed and the table ought to be updated.
	 */
	public void updateData() {
		TableModel model = getTable().getModel();
		table.setModel(model);
		
		setTableSettings(table);
	}
}
