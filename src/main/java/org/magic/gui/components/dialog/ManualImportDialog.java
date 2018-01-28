package org.magic.gui.components.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.magic.services.MTGControler;

public class ManualImportDialog extends JDialog {
	
	
	JEditorPane editorPane;
	
	public String getStringDeck()
	{
		return editorPane.getText();
	}
	
	public ManualImportDialog() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(400, 400));
		setModal(true);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnImport = new JButton(MTGControler.getInstance().getLangService().getCapitalize("IMPORT"));
		btnImport.addActionListener(e->dispose());
		panel.add(btnImport);
		
		JButton btnCancel = new JButton(MTGControler.getInstance().getLangService().getCapitalize("CANCEL"));
		btnCancel.addActionListener(e->{
				editorPane.setText("");
				dispose();
		});
		panel.add(btnCancel);
		
		JLabel lblPastYourDeck = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("IMPORT_HELP"));
		getContentPane().add(lblPastYourDeck, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		editorPane = new JEditorPane();
		editorPane.setPreferredSize(new Dimension(106, 300));
		scrollPane.setViewportView(editorPane);
		setLocationRelativeTo(null);
	}

}
