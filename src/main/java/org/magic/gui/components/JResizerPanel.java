package org.magic.gui.components;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.magic.services.MTGConstants;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class JResizerPanel extends JPanel {
	private double ratio = MTGConstants.CARD_PICS_RATIO;
	private JLabel lblDimension;
	private Dimension dimension;
	private JSpinner spinner;
	
	public JResizerPanel(Dimension d) {
		dimension=d;
		init();
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	
	
	private void init() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(60, 20));
		spinner.setModel(new SpinnerNumberModel(0, null, null, 1));
		add(spinner);
		lblDimension = new JLabel("");
		add(lblDimension);
		
		update();
		spinner.addChangeListener(ce-> {
			Number val = (Number)spinner.getValue();
			int w = (int) (dimension.getWidth()+val.intValue());
			int h = (int) (w*ratio);
			dimension.setSize(w, h);
			update();
		});
	}
	
	
	private void update()
	{
		lblDimension.setText((int)dimension.getWidth()+"x"+(int)dimension.getHeight());
	}

}
