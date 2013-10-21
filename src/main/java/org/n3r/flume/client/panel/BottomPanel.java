package org.n3r.flume.client.panel;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.LayoutManager2;

import javax.swing.JPanel;

public class BottomPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public BottomPanel(LayoutManager2 gridBagLayout) {
		super(gridBagLayout);
		initialize();
	}

	private void initialize() {
		add(new LogMainPanel(new GridBagLayout()), "log");
		add(new HistoryMainPanel(new GridBagLayout()), "history");
	}

	public void showPanel(Container panel, String commond) {
		CardLayout cl = (CardLayout) getLayout();
		cl.show(panel, commond);
	}

}
