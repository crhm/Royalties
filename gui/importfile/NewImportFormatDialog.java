package gui.importfile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import gui.importfile.newformatpanels.FormatDetailsPanel;
import gui.importfile.newformatpanels.GeneralDetailsPanel;
import importing.ObjectToImport;
import importing.SalesFileFormat;
import main.Channel;

@SuppressWarnings("serial")
public class NewImportFormatDialog extends JFrame implements ActionListener{
	String filePath;
	JButton bttnNext;
	JScrollPane scrollPaneCSV;
	JPanel choicesPanel;
	JPanel detailsPanel;
	JTabbedPane tabbedPane;


	private String valuesSeparatedBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"; //commas, except those in quotation marks
	private int firstLineOfData;
	private SimpleDateFormat oldDateFormat;
	private int dateColumnIndex = -1;
	private int dateRowIndex = -1;
	private int monthsFromDate = 0;
	private Channel channel;

	private ObjectToImport bookTitleSettings = null;
	private ObjectToImport bookAuthorSettings = null;
	private ObjectToImport bookIDSettings = null;
	private ObjectToImport netUnitsSoldSettings = null;
	private ObjectToImport revenuesPLPSettings = null;
	private ObjectToImport priceSettings = null;
	private ObjectToImport royaltyTypePLPSettings = null;
	private ObjectToImport deliveryCostSettings = null;
	private ObjectToImport dateSettings = null;
	private ObjectToImport currencySettings = null;
	private ObjectToImport countrySettings = null;
	private JButton bttnSave;

	//TODO figure out how it's going to work with the panels passing the result of the user input to here, 
	//and when to do it, and who does the validation, etc...

	public NewImportFormatDialog(String filePath) {
		this.filePath = filePath;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setTitle("Unrecognised File Format");
		getContentPane().setLayout(new GridLayout(2, 1, 0, 0));

		scrollPaneCSV = new JScrollPane();
		getContentPane().add(scrollPaneCSV);

		choicesPanel = new JPanel();
		getContentPane().add(choicesPanel);
		choicesPanel.setLayout(new BorderLayout(0, 0));

		JPanel nextPanel = new JPanel();
		choicesPanel.add(nextPanel, BorderLayout.SOUTH);
		nextPanel.setLayout(new GridLayout(1, 4, 0, 0));

		JLabel label = new JLabel("");
		nextPanel.add(label);

		JLabel label_1 = new JLabel("");
		nextPanel.add(label_1);

		bttnNext = new JButton("Next");
		bttnNext.addActionListener(this);
		nextPanel.add(bttnNext);

		bttnSave = new JButton("Save");
		bttnSave.setEnabled(false);
		nextPanel.add(bttnSave);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.add("General Details", new GeneralDetailsPanel(this));
		tabbedPane.setEnabled(false);
		choicesPanel.add(tabbedPane, BorderLayout.CENTER);

		this.pack();
		this.setVisible(true);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bttnNext) {
			int activeTab = tabbedPane.getSelectedIndex();
			//TODO make sure it doesn't go over last tab

			FormatDetailsPanel currentPanel = (FormatDetailsPanel) tabbedPane.getSelectedComponent();
			Boolean canSave = currentPanel.validateUserInput();
			if (canSave) {
				currentPanel.saveUserInput();
				tabbedPane.setSelectedIndex(activeTab + 1);
			} else {
				JOptionPane.showMessageDialog(this, "Sorry, you cannot proceed to the next step until all information"
						+ " is filled in correctly for this stage.", "Incomplete or Incorrect Information", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == bttnSave) {
			//TODO
		}
		//TODO make sure to activate and deactivate buttons when appropriate
	}

	/**
	 * @param firstLineOfData the firstLineOfData to set
	 */
	public void setFirstLineOfData(int firstLineOfData) {
		this.firstLineOfData = firstLineOfData;
	}

	/**
	 * @param oldDateFormat the oldDateFormat to set
	 */
	public void setOldDateFormat(SimpleDateFormat oldDateFormat) {
		this.oldDateFormat = oldDateFormat;
	}

	/**
	 * @param dateColumnIndex the dateColumnIndex to set
	 */
	public void setDateColumnIndex(int dateColumnIndex) {
		this.dateColumnIndex = dateColumnIndex;
	}

	/**
	 * @param dateRowIndex the dateRowIndex to set
	 */
	public void setDateRowIndex(int dateRowIndex) {
		this.dateRowIndex = dateRowIndex;
	}

	/**
	 * @param monthsFromDate the monthsFromDate to set
	 */
	public void setMonthsFromDate(int monthsFromDate) {
		this.monthsFromDate = monthsFromDate;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * @param bookTitleSettings the bookTitleSettings to set
	 */
	public void setBookTitleSettings(ObjectToImport bookTitleSettings) {
		this.bookTitleSettings = bookTitleSettings;
	}

	/**
	 * @param bookAuthorSettings the bookAuthorSettings to set
	 */
	public void setBookAuthorSettings(ObjectToImport bookAuthorSettings) {
		this.bookAuthorSettings = bookAuthorSettings;
	}

	/**
	 * @param bookIDSettings the bookIDSettings to set
	 */
	public void setBookIDSettings(ObjectToImport bookIDSettings) {
		this.bookIDSettings = bookIDSettings;
	}

	/**
	 * @param netUnitsSoldSettings the netUnitsSoldSettings to set
	 */
	public void setNetUnitsSoldSettings(ObjectToImport netUnitsSoldSettings) {
		this.netUnitsSoldSettings = netUnitsSoldSettings;
	}

	/**
	 * @param revenuesPLPSettings the revenuesPLPSettings to set
	 */
	public void setRevenuesPLPSettings(ObjectToImport revenuesPLPSettings) {
		this.revenuesPLPSettings = revenuesPLPSettings;
	}

	/**
	 * @param priceSettings the priceSettings to set
	 */
	public void setPriceSettings(ObjectToImport priceSettings) {
		this.priceSettings = priceSettings;
	}

	/**
	 * @param royaltyTypePLPSettings the royaltyTypePLPSettings to set
	 */
	public void setRoyaltyTypePLPSettings(ObjectToImport royaltyTypePLPSettings) {
		this.royaltyTypePLPSettings = royaltyTypePLPSettings;
	}

	/**
	 * @param deliveryCostSettings the deliveryCostSettings to set
	 */
	public void setDeliveryCostSettings(ObjectToImport deliveryCostSettings) {
		this.deliveryCostSettings = deliveryCostSettings;
	}

	/**
	 * @param dateSettings the dateSettings to set
	 */
	public void setDateSettings(ObjectToImport dateSettings) {
		this.dateSettings = dateSettings;
	}

	/**
	 * @param currencySettings the currencySettings to set
	 */
	public void setCurrencySettings(ObjectToImport currencySettings) {
		this.currencySettings = currencySettings;
	}

	/**
	 * @param countrySettings the countrySettings to set
	 */
	public void setCountrySettings(ObjectToImport countrySettings) {
		this.countrySettings = countrySettings;
	}

}
