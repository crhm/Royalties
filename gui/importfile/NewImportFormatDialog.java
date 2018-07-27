package gui.importfile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import gui.importfile.newformatpanels.*;
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
		tabbedPane.add("Date Format", new DateFormatDetailsPanel(this));
		tabbedPane.add("Date Options 1", new DateDetailsPanel(this));
		tabbedPane.add("Date Options 2", new DateProcessingDetailsPanel(this));
		tabbedPane.add("Book Title", new BookTitleDetailsPanel(this));
		tabbedPane.add("Book Author", new AuthorDetailsPanel(this));
		tabbedPane.add("Book ID", new BookIDDetailsPanel(this));
		tabbedPane.add("Net Units Sold", new NetUnitsSoldDetailsPanel(this));
		tabbedPane.add("PLP Revenues", new RevenuesPLPDetailsPanel(this));
		tabbedPane.add("Price", new PriceDetailsPanel(this));
		tabbedPane.add("PLP Royalty Percentage", new RoyaltyTypePLPDetailsPanel(this));
		tabbedPane.add("Delivery Costs", new DeliveryCostDetailsPanel(this));
		tabbedPane.add("Currency", new CurrencyDetailsPanel(this));
		tabbedPane.add("Country", new CountryDetailsPanel(this));
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
			SalesFileFormat newFormat = null;
			if (dateRowIndex == -1) {
				newFormat = new SalesFileFormat(firstLineOfData, oldDateFormat, channel, dateRowIndex, dateColumnIndex, bookTitleSettings, 
						bookAuthorSettings, bookIDSettings, netUnitsSoldSettings,
						revenuesPLPSettings, priceSettings, royaltyTypePLPSettings,
						deliveryCostSettings, currencySettings,countrySettings);
			} else {
				newFormat = new SalesFileFormat(firstLineOfData, oldDateFormat, channel, bookTitleSettings, 
						bookAuthorSettings, bookIDSettings, netUnitsSoldSettings,
						revenuesPLPSettings, priceSettings, royaltyTypePLPSettings,
						deliveryCostSettings, dateSettings, currencySettings,countrySettings);
			}
			if (monthsFromDate != 0) {
				newFormat.setMonthsFromDate(monthsFromDate);
			}
			
			//TODO finish (values separated by + importing file with this format now... and adding the salesfileformat to the channel
			
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
	 * @return the dateColumnIndex
	 */
	public int getDateColumnIndex() {
		return dateColumnIndex;
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
