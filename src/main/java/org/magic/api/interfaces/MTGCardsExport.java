package org.magic.api.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Icon;

import org.magic.api.beans.MagicCardStock;
import org.magic.api.beans.MagicDeck;
import org.magic.api.interfaces.abstracts.AbstractCardExport.MODS;

public interface MTGCardsExport extends MTGPlugin {

	public String getFileExtension();

	public void exportDeck(MagicDeck deck, File dest) throws IOException;
	public void exportStock(List<MagicCardStock> stock, File f) throws IOException;

	
	public MagicDeck importDeckFromFile(File f) throws IOException;
	public MagicDeck importDeck(String f, String name) throws IOException;

	public List<MagicCardStock> importStockFromFile(File f) throws IOException;
	public List<MagicCardStock> importStock(String content) throws IOException;
	
	
	public Icon getIcon();

	public boolean needDialogGUI();
	
	public boolean needFile();
	

	public MODS getMods();

}