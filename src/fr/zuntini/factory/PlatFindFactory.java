package fr.zuntini.factory;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.zuntini.platform.AGList;

public class PlatFindFactory extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFileChooser fc;
	private final String name;
	/*
	  Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public PlatFindFactory(String name) {
		super();
		this.name = name;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		this.setBounds(100, 100, 364, 156);
		
		
		fc = new JFileChooser();
		setLayout(null);
		
		JLabel title = new JLabel(this.name);
		title.setBounds(118, 27, 46, 14);
		add(title);
	
		
		
		JButton btDownload = new JButton("Download");
		btDownload.setAlignmentX(CENTER_ALIGNMENT);
		btDownload.setBounds(174, 100, 100, 23);
		btDownload.addActionListener(arg0 -> {
			  try {
					Desktop.getDesktop().browse(new URL(AGList.getdownloadlink(name)).toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
		});
		this.add(btDownload);
		
		JLabel lblNewLabel = new JLabel(name);
		lblNewLabel.setBounds(118, 27, 46, 14);
		add(lblNewLabel);
		
		
	}
		
	public File getFile()
	{
		return fc.getSelectedFile();
	}
}
