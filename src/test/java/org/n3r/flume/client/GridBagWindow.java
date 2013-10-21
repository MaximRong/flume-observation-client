package org.n3r.flume.client;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class GridBagWindow extends JFrame {
	private final JButton searchBtn;
	private final JComboBox modeCombo;
	private final JLabel tagLbl;
	private final JLabel tagModeLbl;
	private final JLabel previewLbl;
	private final JTable resTable;
	private final JTextField tagTxt;

	public GridBagWindow() {
		Container contentPane = getContentPane();
		GridBagLayout gridbag = new GridBagLayout();
		contentPane.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		// setting a default constraint value
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		tagLbl = new JLabel("Tags");
		c.gridx = 0; // x grid position
		c.gridy = 0; // y grid position
		gridbag.setConstraints(tagLbl, c); // associate the label with a
											// constraint object
		contentPane.add(tagLbl); // add it to content pane

		tagModeLbl = new JLabel("Tag Mode");
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(tagModeLbl, c);
		contentPane.add(tagModeLbl);
		tagTxt = new JTextField("plinth");
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		gridbag.setConstraints(tagTxt, c);
		contentPane.add(tagTxt);
		String[] options = { "all", "any" };
		modeCombo = new JComboBox(options);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		gridbag.setConstraints(modeCombo, c);
		contentPane.add(modeCombo);
		searchBtn = new JButton("Search");
		c.gridx = 1;
		c.gridy = 2;
		gridbag.setConstraints(searchBtn, c);
		contentPane.add(searchBtn);
		resTable = new JTable(5, 3);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		gridbag.setConstraints(resTable, c);
		contentPane.add(resTable);
		previewLbl = new JLabel("Preview goes here");
		c.gridx = 0;
		c.gridy = 4;
		gridbag.setConstraints(previewLbl, c);
		contentPane.add(previewLbl);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public static void main(String args[]) {
		GridBagWindow window = new GridBagWindow();
		window.setTitle("GridBagWindow");
		window.pack();
		window.setVisible(true);
	}
}
