package org.magic.api.pricers.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.beans.MagicPrice;
import org.magic.api.interfaces.abstracts.AbstractMagicPricesProvider;
import org.magic.services.MTGConstants;
import org.magic.tools.URLTools;

public class ParkagePricer extends AbstractMagicPricesProvider {

	
	private static final String URL_BASE="https://www.parkage.com";
	
	@Override
	public String getName() {
		return "Parkage";
	}
	
	@Override
	protected List<MagicPrice> getLocalePrice(MagicEdition me, MagicCard card) throws IOException {

		ArrayList<MagicPrice> ret= new ArrayList<>();
		
		URLTools.extractHtml(URL_BASE+"/en/search/?text="+URLEncoder.encode(card.getName(),MTGConstants.DEFAULT_ENCODING)+"&page=1&products_per_page=50&page_view=3")
		.select("table.table-condensed > tbody > tr")
		.forEach(tr->{
			
			if(!tr.select("select").hasAttr("disabled"))
			{
				MagicPrice mp = new MagicPrice();
				mp.setCountry("FR");
				mp.setCurrency("EUR");
				mp.setSite(getName());
				mp.setUrl(tr.select("td").first().select("a").attr("href"));
				mp.setSeller(tr.select("td").get(2).text());
				
				String urlFlag =tr.select("td").first().select("img").attr("src");
				mp.setLanguage(urlFlag.substring(urlFlag.lastIndexOf('/')+1,urlFlag.lastIndexOf('.')));
				
				String price = tr.select("td.col-price").text().replaceAll(",", ".").replaceAll("€","");
				mp.setValue(Double.parseDouble(price));
				mp.setFoil(tr.select("td").select("i.fa-star")!=null);
				
				ret.add(mp);
			}
		});
		
		
		
		return ret;
	}
}