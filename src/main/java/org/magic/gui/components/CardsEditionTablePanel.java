package org.magic.gui.components;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCollection;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.MTGCardsProvider;
import org.magic.api.interfaces.MTGDao;
import org.magic.gui.abstracts.AbstractBuzyIndicatorComponent;
import org.magic.gui.models.MagicCardTableModel;
import org.magic.gui.renderer.MagicEditionsJLabelRenderer;
import org.magic.gui.renderer.ManaCellRenderer;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.services.MTGLogger;
import org.magic.services.threads.ThreadManager;
import org.magic.services.workers.AbstractObservableWorker;
import org.magic.sorters.CardsEditionSorter;
import org.magic.tools.UITools;
import javax.swing.JCheckBox;


public class CardsEditionTablePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JXTable table;
	private MagicCardTableModel model;
	private MagicEdition currentEdition;
	private AbstractBuzyIndicatorComponent buzy;
	private transient Logger logger = MTGLogger.getLogger(this.getClass());
	private JButton btnImport;
	private JComboBox<MagicCollection> cboCollection;
	private transient AbstractObservableWorker<List<MagicCard>, MagicCard,MTGCardsProvider> sw;
	private JCheckBox chkNeededCards;
	
	
	public CardsEditionTablePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panneauHaut = new JPanel();
		model = new MagicCardTableModel();
		
		table = new JXTable(model);
		buzy=AbstractBuzyIndicatorComponent.createProgressComponent();
		
		table.getColumnModel().getColumn(2).setCellRenderer(new ManaCellRenderer());
		table.getColumnModel().getColumn(6).setCellRenderer(new MagicEditionsJLabelRenderer());
		table.setColumnControlVisible(true);
		table.getColumnExt(model.getColumnName(1)).setVisible(false);
		table.getColumnExt(model.getColumnName(6)).setVisible(false);
		table.getColumnExt(model.getColumnName(8)).setVisible(false);
		table.getColumnExt(model.getColumnName(9)).setVisible(false);
		UITools.initTableFilter(table);
		
		panneauHaut.add(buzy);
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(panneauHaut,BorderLayout.NORTH);
		
		JPanel panneauBas = new JPanel();
		add(panneauBas, BorderLayout.SOUTH);
		
		cboCollection =  UITools.createComboboxCollection();
		panneauBas.add(cboCollection);
		
		btnImport = new JButton(MTGConstants.ICON_MASS_IMPORT_SMALL);
		btnImport.setEnabled(false);
		panneauBas.add(btnImport);
		
		chkNeededCards = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("FILTER_NEEDED"));
		panneauBas.add(chkNeededCards);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent componentEvent) {
				init(currentEdition);
			}

		});
		
		chkNeededCards.addActionListener(il->{
			
			if(chkNeededCards.isSelected()) {
				try {
					List<MagicCard> currents = MTGControler.getInstance().getEnabled(MTGDao.class).listCardsFromCollection(MTGControler.getInstance().get("default-library"),currentEdition);
					model.removeItem(currents);
				} catch (SQLException e) {
					MTGControler.getInstance().notify(e);
				}
			}
			else
			{
				init(currentEdition);
			}
		});
		
		
		btnImport.addActionListener(ae->{
			List<MagicCard> list = getSelectedCards();
			
			int res = JOptionPane.showConfirmDialog(null,MTGControler.getInstance().getLangService().getCapitalize("COLLECTION_IMPORT") + " :" + list.size() + " cards in " + cboCollection.getSelectedItem());
			if(res==JOptionPane.YES_OPTION)
			{
				buzy.start(list.size());
				
				SwingWorker<Void, MagicCard> swImp = new SwingWorker<>()
				{
				@Override
					protected void done() {
						buzy.end();
					}

					@Override
					protected void process(List<MagicCard> chunks) {
						buzy.progressSmooth(chunks.size());
					}

					@Override
					protected Void doInBackground() throws Exception {
						for(MagicCard mc : list)
							try {
								MTGControler.getInstance().saveCard(mc, (MagicCollection)cboCollection.getSelectedItem(),null);
								publish(mc);
							} catch (SQLException e) {
								logger.error("couln't save " + mc,e);
							}
						return null;
						}
					
						};
				
				
				
				ThreadManager.getInstance().runInEdt(swImp, "import cards in "+cboCollection.getSelectedItem());
			}
		});
	}
	
	public MagicCard getSelectedCard()
	{
		if(table.getSelectedRow()>-1)
			return (MagicCard) table.getValueAt(table.getSelectedRow(), 0);
		
		return null;
	}
	
	public List<MagicCard> getSelectedCards()
	{
		return UITools.getTableSelections(table,0);
	}
	
	
	public JXTable getTable() {
		return table;
	}
	
	public void init(MagicEdition ed)
	{
		this.currentEdition=ed;
		chkNeededCards.setSelected(false);
		if(isVisible())
			refresh();
	}
	
	public void enabledImport(boolean t)
	{
		btnImport.setEnabled(t);
	}
	
	private void refresh()
	{
		if(currentEdition==null)
			return;
	
		
		btnImport.setEnabled(false);
		
		
		if(sw!=null && !sw.isDone())
		{
			sw.cancel(true);
		}
		
		
		sw = new AbstractObservableWorker<>(buzy,MTGControler.getInstance().getEnabled(MTGCardsProvider.class),currentEdition.getCardCount()) {
			
			@Override
			protected List<MagicCard> doInBackground() {
				List<MagicCard> cards = new ArrayList<>();
				try {
					cards = MTGControler.getInstance().getEnabled(MTGCardsProvider.class).searchCardByEdition(currentEdition);
					Collections.sort(cards, new CardsEditionSorter() );
					return cards;
				} catch (IOException e) {
					logger.error(e);
					return cards;
				}
				
			}
			
			@Override
			protected void process(List<MagicCard> chunks) {
				super.process(chunks);
				model.addItems(chunks);
			}
			
			
			@Override
			protected void done() {
				super.done();
				try {
					model.init(get());
				} catch (Exception e) {
					logger.error(e);
				}
			}
			
			
			
		};
		
		ThreadManager.getInstance().runInEdt(sw, "loading edition "+currentEdition);
	}
	
	

}
