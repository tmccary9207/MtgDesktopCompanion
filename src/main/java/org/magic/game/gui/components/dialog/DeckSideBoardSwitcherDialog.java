package org.magic.game.gui.components.dialog;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicDeck;
import org.magic.gui.renderer.MagicCardListRenderer;
import org.magic.services.MTGConstants;

public class DeckSideBoardSwitcherDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MagicDeck savedDeck;
	private MagicDeck bckDeck;
	private JLabel lblDecksize;

	JList<MagicCard> listMain;
	JList<MagicCard> listSide;

	DefaultListModel<MagicCard> modMain;
	DefaultListModel<MagicCard> modSide;

	public DeckSideBoardSwitcherDialog(MagicDeck deck) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(MTGConstants.ICON_DECK.getImage());
		this.savedDeck = deck;
		this.bckDeck = deck;

		setTitle("Switch Sideboard " + deck.getName());
		lblDecksize = new JLabel();
		lblDecksize.setAlignmentX(Component.CENTER_ALIGNMENT);
		modMain = new DefaultListModel<>();
		modSide = new DefaultListModel<>();

		init();

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.WEST);

		listMain = new JList<>(modMain);

		listMain.setCellRenderer(new MagicCardListRenderer());
		scrollPane.setViewportView(listMain);

		JScrollPane scrollPane1 = new JScrollPane();
		getContentPane().add(scrollPane1, BorderLayout.EAST);

		listSide = new JList<>(modSide);
		listSide.setCellRenderer(new MagicCardListRenderer());

		scrollPane1.setViewportView(listSide);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JButton btnAdd = new JButton(">>>");
		btnAdd.addActionListener(ae -> {
			for (MagicCard mc : listMain.getSelectedValuesList()) {
				modSide.addElement(mc);
				modMain.removeElement(mc);

				savedDeck.getMap().put(mc, savedDeck.getMap().get(mc) - 1);

				if (savedDeck.getMapSideBoard().get(mc) == null)
					savedDeck.getMapSideBoard().put(mc, 1);
				else
					savedDeck.getMapSideBoard().put(mc, savedDeck.getMapSideBoard().get(mc) + 1);

				refresh();
			}
		});
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(btnAdd);

		JButton btnRemove = new JButton("<<<");
		btnRemove.addActionListener(e -> {
			for (MagicCard mc : listSide.getSelectedValuesList()) {
				modMain.addElement(mc);
				modSide.removeElement(mc);

				savedDeck.getMapSideBoard().put(mc, savedDeck.getMapSideBoard().get(mc) - 1);

				if (savedDeck.getMap().get(mc) == null)
					savedDeck.getMap().put(mc, 1);
				else
					savedDeck.getMap().put(mc, savedDeck.getMap().get(mc) + 1);

				refresh();
			}
		});
		btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(btnRemove);

		JButton btnRestore = new JButton("Restore");
		btnRestore.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(btnRestore);
		btnRestore.addActionListener(ae -> {
			savedDeck = bckDeck;
			init();
		});
		panel.add(lblDecksize);

		JPanel panel1 = new JPanel();
		getContentPane().add(panel1, BorderLayout.SOUTH);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(e -> dispose());

		panel1.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			savedDeck = bckDeck;
			dispose();
		});
		panel1.add(btnCancel);

		pack();
	}

	private void refresh() {
		lblDecksize.setText("DeckSize : " + savedDeck.getNbCards());
		
	}

	private void init() {

		modMain.removeAllElements();
		modSide.removeAllElements();

		for (MagicCard mc : bckDeck.getAsList())
			modMain.addElement(mc);

		for (MagicCard mc : bckDeck.getSideAsList())
			modSide.addElement(mc);

		refresh();
	}

	public MagicDeck getDeck() {
		return savedDeck;
	}

}
