package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import gui.renderers.NumberRenderer;
import main.Person;
import main.SalesHistory;

@SuppressWarnings("serial")
public class PersonsPanel extends JPanel implements ActionListener, ListSelectionListener {
	JTable royaltyHoldersTable;
	int selectedIndex1 = -1;
	int selectedIndex2 = -1;

	public PersonsPanel() {
		super();
		this.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3, 0, 0));
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());
		JButton mergeButton = new JButton("Merge two persons");
		mergeButton.addActionListener(this);
		buttonPanel.add(mergeButton);
		this.add(buttonPanel, BorderLayout.NORTH);
		royaltyHoldersTable = getTable();
		royaltyHoldersTable.getSelectionModel().addListSelectionListener(this);
		this.add(new JScrollPane(royaltyHoldersTable), BorderLayout.CENTER);

	}

	/**Returns the JTable representing royalty holders and their balance.
	 */
	private JTable getTable() {
		//Sets the table model
		String[] columnNames = {"Name", "Balance (USD)", "Person Number"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column ) {
				case 1 : return Double.class;
				case 2 : return Long.class;
				default : return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		//Sets the table
		JTable table = new JTable(model);
		setTableSettings(table);

		return table;
	}

	/**Returns the data to be plugged into the appropriate table model.
	 * <br>Strips person names of quotation marks for cleanliness of presentation.
	 */
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListPersons().size()][3];
		int count = 0;
		for (Person p : SalesHistory.get().getListPersons()) {
			data[count][0] = p.getName();
			data[count][1] = p.getBalance();
			data[count][2] = p.getPersonNumber();
			count++;
		}
		return data;
	}

	/**Ensures that the table's second column is rendered as a currency, that it is sorted by its first column, 
	 * and that the user cannot reorder the columns.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		//Renders last column as currency yet allows it to be sorted as a double
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(1).setCellRenderer(NumberRenderer.getCurrencyRenderer());

		//Sorts the table by person name
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();

		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);
	}

	/**Method to call when the underlying data may have changed and the table needs to be updated.
	 */
	public void updateData() {
		TableModel model = getTable().getModel();
		royaltyHoldersTable.setModel(model);
		setTableSettings(royaltyHoldersTable);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (royaltyHoldersTable.getSelectedRows().length > 2) {
			JOptionPane.showMessageDialog(this, "Error. Please select only two persons.", "Error!", JOptionPane.ERROR_MESSAGE);
		} else if (selectedIndex1 == -1 || selectedIndex2 == -1 || selectedIndex1 == selectedIndex2) {
			JOptionPane.showMessageDialog(this, "Error. Please select two separate persons.", "Error!", JOptionPane.ERROR_MESSAGE);
		} else {
			int row1 = royaltyHoldersTable.convertRowIndexToModel(selectedIndex1);
			int row2 = royaltyHoldersTable.convertRowIndexToModel(selectedIndex2);
			long personNumber1 = (long) royaltyHoldersTable.getModel().getValueAt(row1, 2);
			long personNumber2 = (long) royaltyHoldersTable.getModel().getValueAt(row2, 2);
			Person person1 = null;
			Person person2 = null;
			for (Person p : SalesHistory.get().getListPersons()) {
				if (p.getPersonNumber() == personNumber1) {
					person1 = p;
				}
				if (p.getPersonNumber() == personNumber2) {
					person2 = p;
				}
			}
			person1.merge(person2);
			updateData();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		selectedIndex1 = royaltyHoldersTable.getSelectionModel().getMinSelectionIndex();
		selectedIndex2 = royaltyHoldersTable.getSelectionModel().getMaxSelectionIndex();
	}

}
