package basicgui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.DataVerification;

@SuppressWarnings("serial")
public class DataVerificationPanel extends JPanel {

	public DataVerificationPanel() {
		super();
		this.setLayout(new GridLayout(2, 2));
		this.add(new JScrollPane(getTable()));
	}
	
	private JTable getTable() {
		Object[] columnNames = {"Verifying...", "Success", "Reason for failure"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
            public Class<?> getColumnClass(int column) {
				switch (column) {
				case 1 : return Boolean.class;
				default : return String.class;
				}
			}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		JTable table = new JTable(model);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(1).setMaxWidth(100);
		table.setAutoCreateRowSorter(true);
		return table;
	}
	
	private Object[][] getData(){
		Object[][] data = new Object[4][3];
		data[0][0] = "Sales have been imported for all channels each month?";
		data[0][1] = null;
		if (DataVerification.checkSalesDataForAllChannels().isEmpty()) {
			data[0][1] = true;
		} else {
			data[0][1] = false;
		}
		data[0][2] = DataVerification.checkSalesDataForAllChannels();
		
		data[1][0] = "Foreign Exchange rates have been found for all sales?";
		data[1][1] = null;
		if (DataVerification.checkForexDataForRelevantChannels().isEmpty()) {
			data[1][1] = true;
		} else {
			data[1][1] = false;
		}
		data[1][2] = DataVerification.checkForexDataForRelevantChannels();
		
		data[2][0] = "Royalty information has been found for all books sold?";
		data[2][1] = null;
		if (DataVerification.checkRoyaltiesDataForAllChannels().isEmpty()) {
			data[2][1] = true;
		} else {
			data[2][1] = false;
		}
		data[2][2] = DataVerification.checkRoyaltiesDataForAllChannels();
		
		data[3][0] = "TBD";
		data[3][1] = true;
		data[3][2] = "";
		
		return data;
	}
}
