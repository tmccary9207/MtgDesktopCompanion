package org.magic.game.gui.components.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import org.magic.game.model.AbstractSpell;
import org.magic.game.model.abilities.AbstractAbilities;
import org.magic.game.model.abilities.TriggeredAbilities;
import org.magic.game.model.effects.AbstractEffect;
import org.magic.gui.components.MagicTextPane;
import org.magic.services.MTGConstants;
import org.magic.tools.ColorParser;

public class SpellRendererPanel extends JPanel {
	
	private MagicTextPane textPane;
	private JLabel lblCardName;
	private JLabel lblIconCard;

	public void setColor(Color fore,Color back)
	{
		lblIconCard.setBackground(back);
		lblIconCard.setForeground(fore);
		lblCardName.setBackground(back);
		lblCardName.setForeground(fore);
	}
	
	public SpellRendererPanel()
	{
		setLayout(new BorderLayout(0, 0));
		
		lblCardName = new JLabel();
		textPane = new MagicTextPane();
		lblIconCard = new JLabel();
		
		add(lblCardName, BorderLayout.NORTH);
		add(textPane, BorderLayout.CENTER);
		add(lblIconCard, BorderLayout.WEST);
	
		textPane.setEditable(false);
		textPane.setPreferredSize(new Dimension(5,60));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblIconCard.setOpaque(true);
		lblCardName.setOpaque(true);
		
	}
	
	
	public void setSpell(AbstractSpell value) {
		
		lblCardName.setText(value.getTitle());
		
		
		if(value instanceof AbstractAbilities)
		{
			AbstractAbilities abs = (AbstractAbilities)value;
			
			textPane.setText(abs.getEffects().toString());
			
			if(abs.isActivated())
			{
				lblIconCard.setIcon(MTGConstants.ICON_GAME_ACTIVATED);
			}
			else if(abs.isTriggered())
			{
				lblIconCard.setIcon(MTGConstants.ICON_GAME_TRIGGER);
			}
		}
		else
		{
			textPane.setText(value.getCard().getText());
			lblIconCard.setIcon(MTGConstants.ICON_BACK);
		}
		
		textPane.updateTextWithIcons();
		Color c = ColorParser.getFullNameColorParse(value.getCard().getColors());
		setColor(Color.BLACK, c);
		
		if(c.equals(Color.BLACK))
			setColor(Color.WHITE, Color.GRAY);
     }
	
	

}