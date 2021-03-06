package org.magic.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXTable;
import org.magic.api.beans.EnumStock;
import org.magic.api.beans.MagicEdition;
import org.magic.api.beans.Packaging;
import org.magic.api.beans.SealedStock;
import org.magic.api.interfaces.MTGDao;
import org.magic.gui.abstracts.MTGUIComponent;
import org.magic.gui.components.GedPanel;
import org.magic.gui.components.PackagesBrowserPanel;
import org.magic.gui.components.charts.SealedHistoryPricesPanel;
import org.magic.gui.editor.ComboBoxEditor;
import org.magic.gui.editor.IntegerCellEditor;
import org.magic.gui.models.SealedStockTableModel;
import org.magic.gui.renderer.MagicEditionJLabelRenderer;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.tools.UITools;

public class SealedStockGUI extends MTGUIComponent {

	private static final long serialVersionUID = 1L;
	private PackagesBrowserPanel packagePanel;
	
	private SealedStockTableModel model;
	private Packaging selectedItem;

	
	public SealedStockGUI() {
		initGUI();
	}
	
	private void initGUI() {
		model = new SealedStockTableModel();
		JXTable table = new JXTable(model);
		packagePanel = new PackagesBrowserPanel(false);
		GedPanel<SealedStock> gedPanel = new GedPanel<>();
		
		JPanel toolsPanel = new JPanel();
		JSplitPane centerPanel = new JSplitPane();
		centerPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		centerPanel.setDividerLocation(0.5);
		centerPanel.setResizeWeight(0.5);
		JTabbedPane panneauDetail = new JTabbedPane();
		SealedHistoryPricesPanel historyPricePanel= new SealedHistoryPricesPanel();
		
		JButton buttonNew = new JButton(MTGConstants.ICON_NEW);
		buttonNew.setEnabled(false);
		JButton buttonDelete = new JButton(MTGConstants.ICON_DELETE);
		JButton buttonUpdate = new JButton(MTGConstants.ICON_REFRESH);
		
		setLayout(new BorderLayout());
		
		JLabel lblNewLabel = new JLabel("WARNING THIS MODULE IS NOT YET STABLE");
		lblNewLabel.setForeground(Color.RED);
		toolsPanel.add(lblNewLabel);
		
		toolsPanel.add(buttonNew);
		toolsPanel.add(buttonDelete);
		toolsPanel.add(buttonUpdate);
		
		panneauDetail.addTab(MTGControler.getInstance().getLangService().getCapitalize("INFO"),MTGConstants.ICON_TAB_PICTURE,packagePanel.getThumbnailPanel());
		panneauDetail.addTab(historyPricePanel.getTitle(),historyPricePanel.getIcon(),historyPricePanel);
		panneauDetail.addTab(gedPanel.getTitle(),MTGConstants.ICON_TAB_GED,gedPanel);
		
		
		add(packagePanel,BorderLayout.WEST);
		centerPanel.setLeftComponent(new JScrollPane(table));
		centerPanel.setRightComponent(panneauDetail);
		add(centerPanel,BorderLayout.CENTER);
		add(toolsPanel,BorderLayout.NORTH);
		
		model.setWritable(true);
	
		table.setDefaultEditor(Integer.class, new IntegerCellEditor());
		table.setDefaultEditor(EnumStock.class, new ComboBoxEditor<>(EnumStock.values()));
		table.setDefaultRenderer(MagicEdition.class, new MagicEditionJLabelRenderer());
		
		packagePanel.getTree().addTreeSelectionListener(e-> {
			
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)packagePanel.getTree().getLastSelectedPathComponent();
			
			if(selectedNode==null || selectedNode.getUserObject()==null)
				return;
			
			boolean isPackage = selectedNode.getUserObject() instanceof Packaging;
			buttonNew.setEnabled(isPackage);
			
			if(selectedNode!=null && isPackage)
			{
				
				selectedItem = (Packaging)selectedNode.getUserObject();
				historyPricePanel.init(selectedItem, selectedItem.getEdition()+"-"+selectedItem.getType());
			}
		});
		
		table.getSelectionModel().addListSelectionListener(l->{
			
			if(!l.getValueIsAdjusting())
			{
				SealedStock ss = UITools.getTableSelection(table, 0);
				if(ss!=null)
				{
					historyPricePanel.init(ss.getProduct(), ss.getProduct().getEdition()+"-"+ ss.getProduct().getType());
					packagePanel.load(ss.getProduct());
				}
			}
		});
		
		buttonDelete.addActionListener(el->{
			SealedStock it = UITools.getTableSelection(table, 0);
			
			int res = JOptionPane.showConfirmDialog(null, MTGControler.getInstance().getLangService().get("CONFIRM_DELETE", it.getProduct()), MTGControler.getInstance().getLangService().get("DELETE"),JOptionPane.YES_NO_OPTION);
			
			if(res==JOptionPane.YES_OPTION)
			{
				try {
					MTGControler.getInstance().getEnabled(MTGDao.class).deleteStock(it);
					model.removeItem(it);
				} catch (SQLException e1) {
					MTGControler.getInstance().notify(e1);
				}
			}
			
			
		});
		
		buttonUpdate.addActionListener(el->{
			try {
				model.init(MTGControler.getInstance().getEnabled(MTGDao.class).listSeleadStocks());
			} catch (SQLException e1) {
				MTGControler.getInstance().notify(e1);
			}			
			
		});
		
		
		buttonNew.addActionListener(el->{
			try {
				
				SealedStock s = new SealedStock(selectedItem,1);
				MTGControler.getInstance().getEnabled(MTGDao.class).saveOrUpdateStock(s);
				model.addItem(s);
			} catch (SQLException e) {
				MTGControler.getInstance().notify(e);
			}
		});
	}

	@Override
	public String getTitle() {
		return MTGControler.getInstance().getLangService().getCapitalize("PACKAGES");
	}

	@Override
	public ImageIcon getIcon() {
		return MTGConstants.ICON_PACKAGE;
	}
	
	@Override
	public void onFirstShowing() {
		packagePanel.initTree();
		
		try {
			model.init(MTGControler.getInstance().getEnabled(MTGDao.class).listSeleadStocks());
		} catch (SQLException e) {
			MTGControler.getInstance().notify(e);
		}
	}
}
