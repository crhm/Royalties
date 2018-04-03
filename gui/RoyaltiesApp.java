package gui;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import gui.authors.AuthorsPanel;
import gui.books.BookPanel;
import gui.channels.ChannelPanel;
import gui.dataverification.DataVerificationPanel;
import gui.dataverification.SalesImportSummaryPanel;
import gui.royalties.RoyaltiesRulesPanel;
import gui.royaltyholders.RoyaltyHoldersPanel;
import gui.sales.SalesPanel;
import importing.ImportEverything;
import main.SalesHistory;

/**GUI for royalties app. Opens a full screen window with several panels displaying different information:
 * <br>Sales, Royalty Holders, PLP Books, Channels, Royalty Holders Per Channel, Data Verification.
 * 
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class RoyaltiesApp extends JFrame implements Runnable {

	public static void main(String[] args) {
		try {
			obtainData();
			RoyaltiesApp test = new RoyaltiesApp();
			Thread guiThread = new Thread(test, "Swing GUI");
			guiThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setTitle("Royalties App");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        
		//Saves data by serialising it on close.
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				SalesHistory.get().serialise();
			}
		});


		JTabbedPane allTabs = new JTabbedPane();        
		allTabs.addTab("PLP Books", new BookPanel());
		allTabs.addTab("Authors", new AuthorsPanel());
		allTabs.addTab("Royalty Holders", new RoyaltyHoldersPanel());
		allTabs.addTab("Channels", new ChannelPanel());
		allTabs.addTab("Royalty Rules", new RoyaltiesRulesPanel());
		allTabs.add("Sales", new SalesPanel());
		allTabs.addTab("Sales Import Summary", new SalesImportSummaryPanel());
		allTabs.addTab("Data Verification", new DataVerificationPanel());

		this.setContentPane(allTabs);
		this.pack();
		this.setVisible(true);
		this.revalidate();
	}


	@Override
	public void run() {
		initialize();
	} 

	/**Gets royalties data by deserialising SalesHistory if it finds the corresponding file, 
	 * or by importing everything if it doesn't.
	 * @throws InterruptedException if a thread interrupts the import thread.
	 */
	private static void obtainData() throws InterruptedException {
		File f = new File("/tmp/data10.ser");
		if(f.exists() && !f.isDirectory()) { 
			SalesHistory.get().deSerialise();
		} else {
			Thread importThread = new Thread(new ImportEverything(), "Importing data");
			importThread.start();
			importThread.join(); //wait for this thread to die before starting next one
		}
	}
}
