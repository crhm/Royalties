package gui.importfile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class NewImportFormatDialog extends JFrame implements ActionListener{
	String filePath;
	JButton bttnNext;
	JScrollPane scrollPaneCSV;
	JPanel choicesPanel;
	JPanel detailsPanel;
	
	public NewImportFormatDialog(String filePath) {
		setAlwaysOnTop(true);
		this.filePath = filePath;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
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
		
		JLabel label_2 = new JLabel("");
		nextPanel.add(label_2);
		
		bttnNext = new JButton("Next");
		nextPanel.add(bttnNext);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setEnabled(false);
		choicesPanel.add(tabbedPane, BorderLayout.CENTER);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
