package basicgui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import importing.ImportEverything;

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
			//Import data first, and only when done start with the GUI
			Thread importThread = new Thread(new ImportEverything(), "Importing data");
			importThread.start();
			importThread.join(); //wait for this thread to die before starting next one
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

		JTabbedPane allTabs = new JTabbedPane();        
		allTabs.add("Sales", new SalesPanel());
		allTabs.addTab("Royalty Holders", new RoyaltyHoldersPanel());
		allTabs.addTab("PLP Books", new BookPanel());
		allTabs.addTab("Channels", new ChannelPanel());
		allTabs.addTab("Royalty Holders Per Channel", new RoyaltiesPerChannelPanel());
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
}
