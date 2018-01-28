package org.magic.gui.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Level;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCollection;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.MagicDAO;
import org.magic.services.IconSetProvider;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.services.MTGLogger;
import org.magic.services.ThreadManager;
import org.magic.tools.InstallCert;

public class ConfigurationPanel extends JPanel {
	
	
	private JTextField textField;
	private JComboBox<MagicDAO> cboTargetDAO;
	private JComboBox<MagicCollection> cboCollections;
	private JComboBox<Level> cboLogLevels;
	private JTextField txtdirWebsite;
	private JComboBox<MagicEdition> cboEditions;
	private JComboBox<MagicEdition> cboEditionLands;
	private JTextField txtMinPrice;
	private JComboBox<String> cbojsonView;
	private JTextField txtWebSiteCertificate;
	private JCheckBox chkToolTip ;
	private JLabel lblLoading = new JLabel();
	private JTextField txtName;
	private JLabel lblIconAvatar;
	private JSpinner spinCardW;
	private JSpinner spinCardH;
	private JCheckBox chckbxIconset;
	private JCheckBox chckbxIconcards;
	
	private JCheckBox chckbxSearch;
	private JCheckBox chckbxCollection;
	private JCheckBox chckbxDashboard;
	private JCheckBox chckbxGame;
	private JCheckBox chckbxDeckBuilder;
	private JCheckBox chckbxShopper;
	private JCheckBox chckbxAlert;
	private JCheckBox chckbxRss;
	private JCheckBox chckbxCardBuilder;
	private JCheckBox chckbxStock;

	
	
	public void loading(boolean show,String text)
	{
		lblLoading.setText(text);
		lblLoading.setVisible(show);
	}
	
	public ConfigurationPanel() {
		lblLoading.setIcon(MTGConstants.ICON_LOADING);
		lblLoading.setVisible(false);
		
		
		
		cboTargetDAO = new JComboBox<>();
		cboCollections = new JComboBox<>();
		cboEditionLands=new JComboBox<>();
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{396, 212, 0};
		gridBagLayout.rowHeights = new int[]{179, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		for(MagicDAO daos :  MTGControler.getInstance().getDaoProviders())
			if(!daos.getName().equals(MTGControler.getInstance().getEnabledDAO().getName()))
			{
			
				cboTargetDAO.addItem(daos);
			}
		
		JPanel panelDAO = new JPanel();
		panelDAO.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), MTGControler.getInstance().getLangService().getCapitalize("DATABASES"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		GridBagConstraints gbcpanelDAO = new GridBagConstraints();
		gbcpanelDAO.insets = new Insets(0, 0, 5, 5);
		gbcpanelDAO.fill = GridBagConstraints.BOTH;
		gbcpanelDAO.gridx = 0;
		gbcpanelDAO.gridy = 0;
		add(panelDAO, gbcpanelDAO);
		GridBagLayout gbl_panelDAO = new GridBagLayout();
		gbl_panelDAO.columnWidths = new int[]{0, 0, 130, 0, 0};
		gbl_panelDAO.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelDAO.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelDAO.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelDAO.setLayout(gbl_panelDAO);
		
		JLabel lblBackupDao = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("DAO_BACKUP")+" : ");
		GridBagConstraints gbclblBackupDao = new GridBagConstraints();
		gbclblBackupDao.insets = new Insets(0, 0, 5, 5);
		gbclblBackupDao.gridx = 0;
		gbclblBackupDao.gridy = 0;
		panelDAO.add(lblBackupDao, gbclblBackupDao);
		
		textField = new JTextField();
		GridBagConstraints gbctextField = new GridBagConstraints();
		gbctextField.fill = GridBagConstraints.HORIZONTAL;
		gbctextField.gridwidth = 2;
		gbctextField.insets = new Insets(0, 0, 5, 5);
		gbctextField.gridx = 1;
		gbctextField.gridy = 0;
		panelDAO.add(textField, gbctextField);
		textField.setColumns(10);
		
		JButton btnBackup = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnBackup = new GridBagConstraints();
		gbcbtnBackup.insets = new Insets(0, 0, 5, 0);
		gbcbtnBackup.gridx = 3;
		gbcbtnBackup.gridy = 0;
		panelDAO.add(btnBackup, gbcbtnBackup);
		
		JLabel lblDuplicateDb = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("DUPLICATE_TO", MTGControler.getInstance().getEnabledDAO()));
		GridBagConstraints gbclblDuplicateDb = new GridBagConstraints();
		gbclblDuplicateDb.insets = new Insets(0, 0, 5, 5);
		gbclblDuplicateDb.gridx = 0;
		gbclblDuplicateDb.gridy = 1;
		panelDAO.add(lblDuplicateDb, gbclblDuplicateDb);
		
		
		GridBagConstraints gbccboTargetDAO = new GridBagConstraints();
		gbccboTargetDAO.fill = GridBagConstraints.HORIZONTAL;
		gbccboTargetDAO.gridwidth = 2;
		gbccboTargetDAO.insets = new Insets(0, 0, 5, 5);
		gbccboTargetDAO.gridx = 1;
		gbccboTargetDAO.gridy = 1;
		panelDAO.add(cboTargetDAO, gbccboTargetDAO);
		
		JButton btnDuplicate = new JButton((MTGControler.getInstance().getLangService().getCapitalize("SAVE")));
		GridBagConstraints gbcbtnDuplicate = new GridBagConstraints();
		gbcbtnDuplicate.insets = new Insets(0, 0, 5, 0);
		gbcbtnDuplicate.gridx = 3;
		gbcbtnDuplicate.gridy = 1;
		panelDAO.add(btnDuplicate, gbcbtnDuplicate);
		
		
		
		btnDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			
				
				ThreadManager.getInstance().execute(new Runnable() {
					
					@Override
					public void run() {
						try{
							MagicDAO dao = (MagicDAO)cboTargetDAO.getSelectedItem();
							loading(true,MTGControler.getInstance().getLangService().getCapitalize("DUPLICATE_TO",MTGControler.getInstance().getEnabledDAO()) + " " + dao);
							
							dao.init();
							for(MagicCollection col : MTGControler.getInstance().getEnabledDAO().getCollections())
								for(MagicCard mc : MTGControler.getInstance().getEnabledDAO().getCardsFromCollection(col))
									{
										dao.saveCard(mc, col);
									}
							
						loading(false,"");
					}catch(Exception e)
					{
						loading(false,"");
						MTGLogger.printStackTrace(e);
					}
						
					}
				}, "duplicate " + MTGControler.getInstance().getEnabledDAO() + " to " + cboTargetDAO.getSelectedItem() );
			}
		});
		btnBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
					ThreadManager.getInstance().execute(new Runnable() {
						
						@Override
						public void run() {
							try {
								loading(true,"backup " + MTGControler.getInstance().getEnabledDAO() +" database");
								MTGControler.getInstance().getEnabledDAO().backup(new File(textField.getText()));
								loading(false,"backup " + MTGControler.getInstance().getEnabledDAO() +" end");
								
							} 
							catch (Exception e1) {
								MTGLogger.printStackTrace(e1);
							}
						}
					}, "backup " + MTGControler.getInstance().getEnabledDAO() +" database");
					
				
			}
		});
		
		JPanel panelConfig = new JPanel();
		panelConfig.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), MTGControler.getInstance().getLangService().getCapitalize("CONFIGURATION"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		GridBagConstraints gbcpanelConfig = new GridBagConstraints();
		gbcpanelConfig.gridheight = 2;
		gbcpanelConfig.insets = new Insets(0, 0, 5, 0);
		gbcpanelConfig.fill = GridBagConstraints.BOTH;
		gbcpanelConfig.gridx = 1;
		gbcpanelConfig.gridy = 0;
		add(panelConfig, gbcpanelConfig);
		GridBagLayout gbl_panelConfig = new GridBagLayout();
		gbl_panelConfig.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelConfig.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelConfig.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelConfig.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelConfig.setLayout(gbl_panelConfig);
		
		JLabel lblMainCol = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("MAIN_COLLECTION") + " :");
		GridBagConstraints gbclblMainCol = new GridBagConstraints();
		gbclblMainCol.insets = new Insets(0, 0, 5, 5);
		gbclblMainCol.gridx = 0;
		gbclblMainCol.gridy = 0;
		panelConfig.add(lblMainCol, gbclblMainCol);
		
		GridBagConstraints gbccboCollections = new GridBagConstraints();
		gbccboCollections.fill = GridBagConstraints.HORIZONTAL;
		gbccboCollections.gridwidth = 3;
		gbccboCollections.insets = new Insets(0, 0, 5, 5);
		gbccboCollections.gridx = 1;
		gbccboCollections.gridy = 0;
		panelConfig.add(cboCollections, gbccboCollections);
		
		JButton btnSaveDefaultLib = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnSave = new GridBagConstraints();
		gbcbtnSave.insets = new Insets(0, 0, 5, 0);
		gbcbtnSave.gridx = 4;
		gbcbtnSave.gridy = 0;
		panelConfig.add(btnSaveDefaultLib, gbcbtnSave);
		
		JLabel lblDefaultLandManuel = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("DEFAULT_LAND_IMPORT")+ " :");
		GridBagConstraints gbclblDefaultLandManuel = new GridBagConstraints();
		gbclblDefaultLandManuel.insets = new Insets(0, 0, 5, 5);
		gbclblDefaultLandManuel.gridx = 0;
		gbclblDefaultLandManuel.gridy = 1;
		panelConfig.add(lblDefaultLandManuel, gbclblDefaultLandManuel);
		
		
		GridBagConstraints gbccboEditionLands = new GridBagConstraints();
		gbccboEditionLands.fill = GridBagConstraints.HORIZONTAL;
		gbccboEditionLands.gridwidth = 3;
		gbccboEditionLands.insets = new Insets(0, 0, 5, 5);
		gbccboEditionLands.gridx = 1;
		gbccboEditionLands.gridy = 1;
		panelConfig.add(cboEditionLands, gbccboEditionLands);
		
		JButton btnSaveDefaultLandDeck = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnSave_1 = new GridBagConstraints();
		gbcbtnSave_1.insets = new Insets(0, 0, 5, 0);
		gbcbtnSave_1.gridx = 4;
		gbcbtnSave_1.gridy = 1;
		panelConfig.add(btnSaveDefaultLandDeck, gbcbtnSave_1);
		
		JLabel lblLogLevel = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("LOG_LEVEL")+ " :");
		GridBagConstraints gbclblLogLevel = new GridBagConstraints();
		gbclblLogLevel.insets = new Insets(0, 0, 5, 5);
		gbclblLogLevel.gridx = 0;
		gbclblLogLevel.gridy = 2;
		panelConfig.add(lblLogLevel, gbclblLogLevel);
		
		cboLogLevels = new JComboBox(new Level[]{Level.INFO,Level.ERROR,Level.DEBUG,Level.TRACE});
		GridBagConstraints gbccboLogLevels = new GridBagConstraints();
		gbccboLogLevels.gridwidth = 3;
		gbccboLogLevels.insets = new Insets(0, 0, 5, 5);
		gbccboLogLevels.gridx = 1;
		gbccboLogLevels.gridy = 2;
		panelConfig.add(cboLogLevels, gbccboLogLevels);
		
		JButton btnSaveLoglevel = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnSaveLoglevel = new GridBagConstraints();
		gbcbtnSaveLoglevel.insets = new Insets(0, 0, 5, 0);
		gbcbtnSaveLoglevel.gridx = 4;
		gbcbtnSaveLoglevel.gridy = 2;
		panelConfig.add(btnSaveLoglevel, gbcbtnSaveLoglevel);
		
		JLabel lblShowJsonPanel = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("SHOW_JSON_PANEL")+ " :");
		GridBagConstraints gbclblShowJsonPanel = new GridBagConstraints();
		gbclblShowJsonPanel.insets = new Insets(0, 0, 5, 5);
		gbclblShowJsonPanel.gridx = 0;
		gbclblShowJsonPanel.gridy = 3;
		panelConfig.add(lblShowJsonPanel, gbclblShowJsonPanel);
		
		cbojsonView = new JComboBox<>();
		GridBagConstraints gbccbojsonView = new GridBagConstraints();
		gbccbojsonView.fill = GridBagConstraints.HORIZONTAL;
		gbccbojsonView.gridwidth = 3;
		gbccbojsonView.insets = new Insets(0, 0, 5, 5);
		gbccbojsonView.gridx = 1;
		gbccbojsonView.gridy = 3;
		panelConfig.add(cbojsonView, gbccbojsonView);
		cbojsonView.setModel(new DefaultComboBoxModel<String>(new String[] {"true", "false"}));
		cbojsonView.setSelectedItem(MTGControler.getInstance().get("debug-json-panel"));
		
		JButton btnSaveJson = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnSaveJson = new GridBagConstraints();
		gbcbtnSaveJson.insets = new Insets(0, 0, 5, 0);
		gbcbtnSaveJson.gridx = 4;
		gbcbtnSaveJson.gridy = 3;
		panelConfig.add(btnSaveJson, gbcbtnSaveJson);
		btnSaveJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MTGControler.getInstance().setProperty("debug-json-panel", cbojsonView.getSelectedItem());
				
			}
		});
		
		JLabel lblDontTakeAlert = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("SHOW_LOW_PRICES")+ " :");
		GridBagConstraints gbclblDontTakeAlert = new GridBagConstraints();
		gbclblDontTakeAlert.insets = new Insets(0, 0, 5, 5);
		gbclblDontTakeAlert.gridx = 0;
		gbclblDontTakeAlert.gridy = 4;
		panelConfig.add(lblDontTakeAlert, gbclblDontTakeAlert);
		
		txtMinPrice = new JTextField(MTGControler.getInstance().get("min-price-alert"));
		GridBagConstraints gbctxtMinPrice = new GridBagConstraints();
		gbctxtMinPrice.gridwidth = 3;
		gbctxtMinPrice.insets = new Insets(0, 0, 5, 5);
		gbctxtMinPrice.gridx = 1;
		gbctxtMinPrice.gridy = 4;
		panelConfig.add(txtMinPrice, gbctxtMinPrice);
		txtMinPrice.setColumns(10);
		
		JButton btnSavePrice = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnSavePrice = new GridBagConstraints();
		gbcbtnSavePrice.insets = new Insets(0, 0, 5, 0);
		gbcbtnSavePrice.gridx = 4;
		gbcbtnSavePrice.gridy = 4;
		panelConfig.add(btnSavePrice, gbcbtnSavePrice);
		btnSavePrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MTGControler.getInstance().setProperty("min-price-alert", txtMinPrice.getText());
				
			}
		});
		
		JLabel lblShowTooltip = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("SHOW_TOOLTIP")+ " :");
		GridBagConstraints gbclblShowTooltip = new GridBagConstraints();
		gbclblShowTooltip.insets = new Insets(0, 0, 5, 5);
		gbclblShowTooltip.gridx = 0;
		gbclblShowTooltip.gridy = 5;
		panelConfig.add(lblShowTooltip, gbclblShowTooltip);
		
		chkToolTip = new JCheckBox("");
		GridBagConstraints gbcchkToolTip = new GridBagConstraints();
		gbcchkToolTip.insets = new Insets(0, 0, 5, 5);
		gbcchkToolTip.gridx = 2;
		gbcchkToolTip.gridy = 5;
		panelConfig.add(chkToolTip, gbcchkToolTip);
		chkToolTip.setSelected(MTGControler.getInstance().get("tooltip").equals("true"));
		chkToolTip.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("tooltip",chkToolTip.isSelected());	
			}
		});
		
		JLabel lblCardsLanguage = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("CARDS_LANGUAGE")+ " :");
		GridBagConstraints gbclblCardsLanguage = new GridBagConstraints();
		gbclblCardsLanguage.insets = new Insets(0, 0, 5, 5);
		gbclblCardsLanguage.gridx = 0;
		gbclblCardsLanguage.gridy = 6;
		panelConfig.add(lblCardsLanguage, gbclblCardsLanguage);
		
		final JComboBox cboLanguages = new JComboBox();
		
		for(String s : MTGControler.getInstance().getEnabledProviders().getLanguages())
		{
			cboLanguages.addItem(s);
			if(MTGControler.getInstance().get("langage").equals(s))
				cboLanguages.setSelectedItem(s);
		}
		
		
		GridBagConstraints gbccboLanguages = new GridBagConstraints();
		gbccboLanguages.gridwidth = 3;
		gbccboLanguages.insets = new Insets(0, 0, 5, 5);
		gbccboLanguages.fill = GridBagConstraints.HORIZONTAL;
		gbccboLanguages.gridx = 1;
		gbccboLanguages.gridy = 6;
		panelConfig.add(cboLanguages, gbccboLanguages);
	
		JButton btnSave_lang = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		btnSave_lang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MTGControler.getInstance().setProperty("langage", cboLanguages.getSelectedItem().toString());
			}
		});
		
		GridBagConstraints gbcbtnSave_lang = new GridBagConstraints();
		gbcbtnSave_lang.insets = new Insets(0, 0, 5, 0);
		gbcbtnSave_lang.gridx = 4;
		gbcbtnSave_lang.gridy = 6;
		panelConfig.add(btnSave_lang, gbcbtnSave_lang);
		
		JLabel lblGuiLocal = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("LOCALISATION")+" :");
		GridBagConstraints gbclblGuiLocal = new GridBagConstraints();
		gbclblGuiLocal.insets = new Insets(0, 0, 5, 5);
		gbclblGuiLocal.gridx = 0;
		gbclblGuiLocal.gridy = 7;
		panelConfig.add(lblGuiLocal, gbclblGuiLocal);
		
		JComboBox<Locale> cboLocales = new JComboBox<>(new DefaultComboBoxModel<Locale>(MTGControler.getInstance().getLangService().getAvailableLocale()));
		GridBagConstraints gbccboLocales = new GridBagConstraints();
		gbccboLocales.gridwidth = 3;
		gbccboLocales.insets = new Insets(0, 0, 5, 5);
		gbccboLocales.fill = GridBagConstraints.HORIZONTAL;
		gbccboLocales.gridx = 1;
		gbccboLocales.gridy = 7;
		panelConfig.add(cboLocales, gbccboLocales);
		
			cboLocales.setSelectedItem(MTGControler.getInstance().getLocale());
		JButton btnSave = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MTGControler.getInstance().setProperty("locale", cboLocales.getSelectedItem());
			}
		});
		
		GridBagConstraints gbcbtnSave3 = new GridBagConstraints();
		gbcbtnSave3.insets = new Insets(0, 0, 5, 0);
		gbcbtnSave3.gridx = 4;
		gbcbtnSave3.gridy = 7;
		panelConfig.add(btnSave, gbcbtnSave3);
		
		JLabel lblCleancache = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("CLEAN_CACHE")+" :");
		GridBagConstraints gbclblCleancache = new GridBagConstraints();
		gbclblCleancache.insets = new Insets(0, 0, 5, 5);
		gbclblCleancache.gridx = 0;
		gbclblCleancache.gridy = 8;
		panelConfig.add(lblCleancache, gbclblCleancache);
		
		JButton btnClean = new JButton(MTGControler.getInstance().getLangService().getCapitalize("CLEAN"));
		btnClean.addActionListener(ae->{
				
				try {
					IconSetProvider.getInstance().clean();
					MTGControler.getInstance().getEnabledCache().clear();
				} catch (Exception e) {
					MTGLogger.printStackTrace(e);
				}
		});
		
		chckbxIconset = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("IMG_SET"));
		chckbxIconset.setSelected(true);
		GridBagConstraints gbcchckbxIconset = new GridBagConstraints();
		gbcchckbxIconset.insets = new Insets(0, 0, 5, 5);
		gbcchckbxIconset.gridx = 1;
		gbcchckbxIconset.gridy = 8;
		panelConfig.add(chckbxIconset, gbcchckbxIconset);
		
		chckbxIconcards = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("IMG_CARD"));
		chckbxIconcards.setSelected(true);
		GridBagConstraints gbcchckbxIconcards = new GridBagConstraints();
		gbcchckbxIconcards.insets = new Insets(0, 0, 5, 5);
		gbcchckbxIconcards.gridx = 2;
		gbcchckbxIconcards.gridy = 8;
		panelConfig.add(chckbxIconcards, gbcchckbxIconcards);
		GridBagConstraints gbcbtnClean = new GridBagConstraints();
		gbcbtnClean.fill = GridBagConstraints.HORIZONTAL;
		gbcbtnClean.insets = new Insets(0, 0, 5, 0);
		gbcbtnClean.gridx = 4;
		gbcbtnClean.gridy = 8;
		panelConfig.add(btnClean, gbcbtnClean);
		
		btnSaveLoglevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxIconset.isSelected())
					MTGControler.getInstance().setProperty("loglevel", (Level)cboLogLevels.getSelectedItem());
				
				
				MTGLogger.changeLevel((Level)cboLogLevels.getSelectedItem());
			}
		});
		
		
		cboLogLevels.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MTGLogger.changeLevel((Level)cboLogLevels.getSelectedItem());
			}
		});
		btnSaveDefaultLandDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MTGControler.getInstance().setProperty("default-land-deck", ((MagicEdition)cboEditionLands.getSelectedItem()).getId());
				
			}
		});
		btnSaveDefaultLib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					
					MTGControler.getInstance().setProperty("default-library", (MagicCollection)cboCollections.getSelectedItem());
				}catch(Exception e)
				{
					MTGLogger.printStackTrace(e);
				}
			}
		});
		try {
			for(MagicCollection col :  MTGControler.getInstance().getEnabledDAO().getCollections())
			{
				cboCollections.addItem(col);
				if(col.getName().equalsIgnoreCase(MTGControler.getInstance().get("default-library")))
				{
					cboCollections.setSelectedItem(col);
				}
			}
		} catch (Exception e1) {
			MTGLogger.printStackTrace(e1);
		}
		
		JPanel panelWebSite = new JPanel();
		panelWebSite.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), MTGControler.getInstance().getLangService().getCapitalize("WEBSITE"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		GridBagConstraints gbcpanelWebSite = new GridBagConstraints();
		gbcpanelWebSite.insets = new Insets(0, 0, 5, 5);
		gbcpanelWebSite.fill = GridBagConstraints.BOTH;
		gbcpanelWebSite.gridx = 0;
		gbcpanelWebSite.gridy = 1;
		add(panelWebSite, gbcpanelWebSite);
		GridBagLayout gbl_panelWebSite = new GridBagLayout();
		gbl_panelWebSite.columnWidths = new int[]{0, 0, 0, 103, 0, 0};
		gbl_panelWebSite.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelWebSite.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelWebSite.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelWebSite.setLayout(gbl_panelWebSite);
		
		JLabel lblWebsiteDir = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("DIRECTORY")+ " :");
		GridBagConstraints gbclblWebsiteDir = new GridBagConstraints();
		gbclblWebsiteDir.insets = new Insets(0, 0, 5, 5);
		gbclblWebsiteDir.gridx = 0;
		gbclblWebsiteDir.gridy = 0;
		panelWebSite.add(lblWebsiteDir, gbclblWebsiteDir);
		
		txtdirWebsite = new JTextField(MTGControler.getInstance().get("default-website-dir"));
		GridBagConstraints gbctxtdirWebsite = new GridBagConstraints();
		gbctxtdirWebsite.fill = GridBagConstraints.HORIZONTAL;
		gbctxtdirWebsite.gridwidth = 3;
		gbctxtdirWebsite.insets = new Insets(0, 0, 5, 5);
		gbctxtdirWebsite.gridx = 1;
		gbctxtdirWebsite.gridy = 0;
		panelWebSite.add(txtdirWebsite, gbctxtdirWebsite);
		txtdirWebsite.setColumns(10);
		
		JButton btnWebsiteSave = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnWebsiteSave = new GridBagConstraints();
		gbcbtnWebsiteSave.insets = new Insets(0, 0, 5, 0);
		gbcbtnWebsiteSave.gridx = 4;
		gbcbtnWebsiteSave.gridy = 0;
		panelWebSite.add(btnWebsiteSave, gbcbtnWebsiteSave);
		
		JLabel lblAddWebsiteCertificate = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("ADD_CERTIFICATE")+ " :");
		GridBagConstraints gbclblAddWebsiteCertificate = new GridBagConstraints();
		gbclblAddWebsiteCertificate.insets = new Insets(0, 0, 5, 5);
		gbclblAddWebsiteCertificate.gridx = 0;
		gbclblAddWebsiteCertificate.gridy = 1;
		panelWebSite.add(lblAddWebsiteCertificate, gbclblAddWebsiteCertificate);
		
		txtWebSiteCertificate = new JTextField();
		GridBagConstraints gbctxtWebSiteCertificate = new GridBagConstraints();
		gbctxtWebSiteCertificate.fill = GridBagConstraints.HORIZONTAL;
		gbctxtWebSiteCertificate.gridwidth = 3;
		gbctxtWebSiteCertificate.insets = new Insets(0, 0, 5, 5);
		gbctxtWebSiteCertificate.gridx = 1;
		gbctxtWebSiteCertificate.gridy = 1;
		panelWebSite.add(txtWebSiteCertificate, gbctxtWebSiteCertificate);
		txtWebSiteCertificate.setText("www.");
		txtWebSiteCertificate.setColumns(10);
		
		JButton btnAdd = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		GridBagConstraints gbcbtnAdd = new GridBagConstraints();
		gbcbtnAdd.insets = new Insets(0, 0, 5, 0);
		gbcbtnAdd.gridx = 4;
		gbcbtnAdd.gridy = 1;
		panelWebSite.add(btnAdd, gbcbtnAdd);
		
		JPanel panelProfil = new JPanel();
		panelProfil.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), MTGControler.getInstance().getLangService().getCapitalize("GAME"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		GridBagConstraints gbcpanelProfil = new GridBagConstraints();
		gbcpanelProfil.insets = new Insets(0, 0, 5, 5);
		gbcpanelProfil.fill = GridBagConstraints.BOTH;
		gbcpanelProfil.gridx = 0;
		gbcpanelProfil.gridy = 2;
		add(panelProfil, gbcpanelProfil);
		GridBagLayout gbl_panelProfil = new GridBagLayout();
		gbl_panelProfil.columnWidths = new int[]{0, 71, 0, 0, 0, 0};
		gbl_panelProfil.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelProfil.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelProfil.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelProfil.setLayout(gbl_panelProfil);
		
		JLabel lblName = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("NAME")+" :");
		GridBagConstraints gbclblName = new GridBagConstraints();
		gbclblName.anchor = GridBagConstraints.EAST;
		gbclblName.insets = new Insets(0, 0, 5, 5);
		gbclblName.gridx = 0;
		gbclblName.gridy = 0;
		panelProfil.add(lblName, gbclblName);
		
		txtName = new JTextField(MTGControler.getInstance().get("/game/player-profil/name"));
		GridBagConstraints gbctxtName = new GridBagConstraints();
		gbctxtName.gridwidth = 3;
		gbctxtName.insets = new Insets(0, 0, 5, 5);
		gbctxtName.fill = GridBagConstraints.HORIZONTAL;
		gbctxtName.gridx = 1;
		gbctxtName.gridy = 0;
		panelProfil.add(txtName, gbctxtName);
		txtName.setColumns(10);
		
		JLabel lblAvatar = new JLabel("Avatar :");
		GridBagConstraints gbclblAvatar = new GridBagConstraints();
		gbclblAvatar.insets = new Insets(0, 0, 5, 5);
		gbclblAvatar.gridx = 0;
		gbclblAvatar.gridy = 1;
		panelProfil.add(lblAvatar, gbclblAvatar);
		
		try{
			lblIconAvatar = new JLabel(new ImageIcon(ImageIO.read(new File(MTGControler.getInstance().get("/game/player-profil/avatar")))));
		}
		catch(Exception e)
		{
			lblIconAvatar = new JLabel();
		}

		lblIconAvatar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				JFileChooser jf = new JFileChooser();
				jf.setFileFilter(new FileNameExtensionFilter("Images", "bmp", "gif", "jpg", "jpeg", "png"));
				 int result = jf.showOpenDialog(null);
				if(result==JFileChooser.APPROVE_OPTION)
				{
					MTGControler.getInstance().setProperty("/game/player-profil/avatar",jf.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		lblIconAvatar.setBorder(new LineBorder(Color.RED, 1, true));
		GridBagConstraints gbclblIconAvatar = new GridBagConstraints();
		gbclblIconAvatar.fill = GridBagConstraints.BOTH;
		gbclblIconAvatar.gridwidth = 2;
		gbclblIconAvatar.gridheight = 4;
		gbclblIconAvatar.insets = new Insets(0, 0, 0, 5);
		gbclblIconAvatar.gridx = 1;
		gbclblIconAvatar.gridy = 1;
		panelProfil.add(lblIconAvatar, gbclblIconAvatar);
		
		JButton btnSave_2 = new JButton(MTGControler.getInstance().getLangService().getCapitalize("SAVE"));
		btnSave_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MTGControler.getInstance().setProperty("/game/player-profil/name",txtName.getText());
				MTGControler.getInstance().setProperty("/game/cards/card-width",spinCardW.getValue());
				MTGControler.getInstance().setProperty("/game/cards/card-heigth",spinCardH.getValue());
			}
		});
		
		JPanel panelSubGame = new JPanel();
		GridBagConstraints gbcpanelSubGame = new GridBagConstraints();
		gbcpanelSubGame.gridheight = 3;
		gbcpanelSubGame.insets = new Insets(0, 0, 5, 5);
		gbcpanelSubGame.fill = GridBagConstraints.BOTH;
		gbcpanelSubGame.gridx = 3;
		gbcpanelSubGame.gridy = 1;
		panelProfil.add(panelSubGame, gbcpanelSubGame);
		panelSubGame.setLayout(new GridLayout(3, 2, 0, 0));
		
		JLabel lblCardW = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("CARD_WIDTH") +" :");
		panelSubGame.add(lblCardW);
		
		spinCardW = new JSpinner();
		spinCardW.setModel(new SpinnerNumberModel(154, 0, null, 1));
		spinCardW.setValue(Integer.parseInt(MTGControler.getInstance().get("/game/cards/card-width")));
		
		
		panelSubGame.add(spinCardW);
		
		JLabel lblCardH = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("CARD_HEIGHT")+" :");
		panelSubGame.add(lblCardH);
		
		spinCardH = new JSpinner();
		spinCardH.setModel(new SpinnerNumberModel(215, 0, null, 1));
		spinCardH.setValue(Integer.parseInt(MTGControler.getInstance().get("/game/cards/card-height")));
		panelSubGame.add(spinCardH);
		
		GridBagConstraints gbcbtnSave_2 = new GridBagConstraints();
		gbcbtnSave_2.insets = new Insets(0, 0, 0, 5);
		gbcbtnSave_2.gridx = 3;
		gbcbtnSave_2.gridy = 4;
		panelProfil.add(btnSave_2, gbcbtnSave_2);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Modules", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));
		GridBagConstraints gbcpanel = new GridBagConstraints();
		gbcpanel.insets = new Insets(0, 0, 5, 0);
		gbcpanel.fill = GridBagConstraints.BOTH;
		gbcpanel.gridx = 1;
		gbcpanel.gridy = 2;
		add(panel, gbcpanel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 103, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		
		
		chckbxSearch = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("SEARCH_MODULE"));
		chckbxSearch.setSelected(MTGControler.getInstance().get("modules/search").equals("true"));
		chckbxSearch.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/search",chckbxSearch.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxSearch = new GridBagConstraints();
		gbcchckbxSearch.anchor = GridBagConstraints.WEST;
		gbcchckbxSearch.insets = new Insets(0, 0, 5, 5);
		gbcchckbxSearch.gridx = 1;
		gbcchckbxSearch.gridy = 0;
		panel.add(chckbxSearch, gbcchckbxSearch);
		
		chckbxCollection = new JCheckBox("Collection");
		chckbxCollection.setSelected(MTGControler.getInstance().get("modules/collection").equals("true"));
		chckbxCollection.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/collection",chckbxCollection.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxCollection = new GridBagConstraints();
		gbcchckbxCollection.anchor = GridBagConstraints.WEST;
		gbcchckbxCollection.insets = new Insets(0, 0, 5, 0);
		gbcchckbxCollection.gridx = 3;
		gbcchckbxCollection.gridy = 0;
		panel.add(chckbxCollection, gbcchckbxCollection);
		
		chckbxDashboard = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("DASHBOARD_MODULE"));
		chckbxDashboard.setSelected(MTGControler.getInstance().get("modules/dashboard").equals("true"));
		chckbxDashboard.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/dashboard",chckbxDashboard.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxDashboard = new GridBagConstraints();
		gbcchckbxDashboard.anchor = GridBagConstraints.WEST;
		gbcchckbxDashboard.insets = new Insets(0, 0, 5, 5);
		gbcchckbxDashboard.gridx = 1;
		gbcchckbxDashboard.gridy = 1;
		panel.add(chckbxDashboard, gbcchckbxDashboard);
		
		chckbxGame = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("GAME_MODULE"));
		chckbxGame.setSelected(MTGControler.getInstance().get("modules/game").equals("true"));
		chckbxGame.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/game",chckbxGame.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxGame = new GridBagConstraints();
		gbcchckbxGame.anchor = GridBagConstraints.WEST;
		gbcchckbxGame.insets = new Insets(0, 0, 5, 0);
		gbcchckbxGame.gridx = 3;
		gbcchckbxGame.gridy = 1;
		panel.add(chckbxGame, gbcchckbxGame);
		
		chckbxDeckBuilder = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("DECK_MODULE"));
		chckbxDeckBuilder.setSelected(MTGControler.getInstance().get("modules/deckbuilder").equals("true"));
		chckbxDeckBuilder.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/deckbuilder",chckbxDeckBuilder.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxDeckBuilder = new GridBagConstraints();
		gbcchckbxDeckBuilder.anchor = GridBagConstraints.WEST;
		gbcchckbxDeckBuilder.insets = new Insets(0, 0, 5, 5);
		gbcchckbxDeckBuilder.gridx = 1;
		gbcchckbxDeckBuilder.gridy = 2;
		panel.add(chckbxDeckBuilder, gbcchckbxDeckBuilder);
		
		chckbxShopper = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("SHOPPING_MODULE"));
		chckbxShopper.setSelected(MTGControler.getInstance().get("modules/shopper").equals("true"));
		chckbxShopper.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/shopper",chckbxShopper.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxShopper = new GridBagConstraints();
		gbcchckbxShopper.anchor = GridBagConstraints.WEST;
		gbcchckbxShopper.insets = new Insets(0, 0, 5, 0);
		gbcchckbxShopper.gridx = 3;
		gbcchckbxShopper.gridy = 2;
		panel.add(chckbxShopper, gbcchckbxShopper);
		
		chckbxAlert = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("ALERT_MODULE"));
		chckbxAlert.setSelected(MTGControler.getInstance().get("modules/alarm").equals("true"));
		chckbxAlert.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/alarm",chckbxAlert.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxAlert = new GridBagConstraints();
		gbcchckbxAlert.anchor = GridBagConstraints.WEST;
		gbcchckbxAlert.insets = new Insets(0, 0, 5, 5);
		gbcchckbxAlert.gridx = 1;
		gbcchckbxAlert.gridy = 3;
		panel.add(chckbxAlert, gbcchckbxAlert);
		
		chckbxRss = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("RSS_MODULE"));
		chckbxRss.setSelected(MTGControler.getInstance().get("modules/rss").equals("true"));
		chckbxRss.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/rss",chckbxRss.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxRss = new GridBagConstraints();
		gbcchckbxRss.anchor = GridBagConstraints.WEST;
		gbcchckbxRss.insets = new Insets(0, 0, 5, 0);
		gbcchckbxRss.gridx = 3;
		gbcchckbxRss.gridy = 3;
		panel.add(chckbxRss, gbcchckbxRss);
		
		chckbxCardBuilder = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("BUILDER_MODULE"));
		chckbxCardBuilder.setSelected(MTGControler.getInstance().get("modules/cardbuilder").equals("true"));
		chckbxCardBuilder.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/cardbuilder",chckbxCardBuilder.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxCardBuilder = new GridBagConstraints();
		gbcchckbxCardBuilder.anchor = GridBagConstraints.WEST;
		gbcchckbxCardBuilder.insets = new Insets(0, 0, 5, 5);
		gbcchckbxCardBuilder.gridx = 1;
		gbcchckbxCardBuilder.gridy = 4;
		panel.add(chckbxCardBuilder, gbcchckbxCardBuilder);
		
		chckbxStock = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("STOCK_MODULE"));
		chckbxStock.setSelected(MTGControler.getInstance().get("modules/stock").equals("true"));
		
		chckbxStock.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/stock",chckbxStock.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxStock = new GridBagConstraints();
		gbcchckbxStock.insets = new Insets(0, 0, 5, 0);
		gbcchckbxStock.anchor = GridBagConstraints.WEST;
		gbcchckbxStock.gridx = 3;
		gbcchckbxStock.gridy = 4;
		panel.add(chckbxStock, gbcchckbxStock);
		
		JCheckBox chckbxHistory = new JCheckBox(MTGControler.getInstance().getLangService().getCapitalize("HISTORY_MODULE"));
		chckbxHistory.setSelected(MTGControler.getInstance().get("modules/history").equals("true"));
		
		chckbxHistory.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MTGControler.getInstance().setProperty("modules/history",chckbxHistory.isSelected());	
			}
		});
		GridBagConstraints gbcchckbxHistory = new GridBagConstraints();
		gbcchckbxHistory.anchor = GridBagConstraints.WEST;
		gbcchckbxHistory.insets = new Insets(0, 0, 0, 5);
		gbcchckbxHistory.gridx = 1;
		gbcchckbxHistory.gridy = 5;
		panel.add(chckbxHistory, gbcchckbxHistory);
		
		GridBagConstraints gbclblLoading = new GridBagConstraints();
		gbclblLoading.gridwidth = 2;
		gbclblLoading.gridx = 0;
		gbclblLoading.gridy = 3;
		add(lblLoading, gbclblLoading);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					InstallCert.install(txtWebSiteCertificate.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e,"ERROR",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnWebsiteSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MTGControler.getInstance().setProperty("default-website-dir", txtdirWebsite.getText());
			}
		});
		for(int i=0;i<cboLogLevels.getItemCount();i++)
		{
			if(cboLogLevels.getItemAt(i).toString().equals(MTGControler.getInstance().get("loglevel")))
				cboLogLevels.setSelectedIndex(i);
			
		}
		try {
				for(MagicEdition col :  MTGControler.getInstance().getEnabledProviders().loadEditions())
				{
					cboEditionLands.addItem(col);
					if(col.getId().equalsIgnoreCase(MTGControler.getInstance().get("default-land-deck")))
					{
						cboEditionLands.setSelectedItem(col);
					}
				}
			
			
			
		} catch (Exception e1) {
			MTGLogger.printStackTrace(e1);
		}
	}

}
