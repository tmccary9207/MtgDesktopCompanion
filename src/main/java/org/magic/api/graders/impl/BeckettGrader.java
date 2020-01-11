package org.magic.api.graders.impl;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.magic.api.beans.Grading;
import org.magic.api.interfaces.abstracts.AbstractGradersProvider;
import org.magic.tools.RequestBuilder;
import org.magic.tools.URLTools;
import org.magic.tools.URLToolsClient;
import org.magic.tools.RequestBuilder.METHOD;

public class BeckettGrader extends AbstractGradersProvider{

	
	@Override
	public String getWebSite() {
		return "https://www.beckett.com";
	}

	@Override
	public Grading loadGrading(String identifier) throws IOException {
		
		URLToolsClient c = URLTools.newClient();
		
		String urlLogin = getWebSite()+"/login?utm_content=bkthp&utm_term=login";
		String urlCheking = getWebSite()+"/grading/card-lookup";
		
		Document d = RequestBuilder.build().url(urlLogin).setClient(c).method(METHOD.GET).toHtml();
		String token = d.select("input[name='login_token']").first().attr("value");
		
		
			d=RequestBuilder.build().url(urlLogin).setClient(c).method(METHOD.POST)
						  .addContent("redirect_url", getWebSite()+"/account")
						  .addContent("login_token", token)
						  .addContent("email",getString("EMAIL"))
						  .addContent("password", getString("PASS"))
						  .toHtml();
			
		boolean	connected = !d.getElementsByTag("title").html().equalsIgnoreCase("Member Login");
	
			
		if(!connected)
			throw new IOException("Error when login to website");
		
		
			d=RequestBuilder.build().url(urlCheking).setClient(c).method(METHOD.GET)
					.addContent("item_type", "BGS")
					.addContent("submit", "Submit")
					.addContent("item_id", identifier)
					 .toHtml();
			
			Element table = d.select("table.cardDetail").first();
			
			if(table==null)
				return null;
				
				
				
			Elements trs=table.select("tr");
			Grading grad = new Grading();
			grad.setGraderName(getName());
			grad.setNumberID(identifier);
			trs.forEach(tr->{
				if(tr.text().startsWith("Centering"))
					grad.setCentering(Double.parseDouble(tr.text().replace("Centering Grade : ","").trim()));
				
				if(tr.text().startsWith("Corner"))
					grad.setCorners(Double.parseDouble(tr.text().replace("Corner Grade : ","").trim()));
				
				if(tr.text().startsWith("Edges"))
					grad.setEdges(Double.parseDouble(tr.text().replace("Edges Grade : ","").trim()));
				
				if(tr.text().startsWith("Surfaces"))
					grad.setSurface(Double.parseDouble(tr.text().replace("Surfaces Grade : ","").trim()));

				if(tr.text().startsWith("Final"))
					grad.setGradeNote(Double.parseDouble(tr.text().replace("Final Grade : ","").trim()));
			});
		return grad;
	}

	@Override
	public String getName() {
		return "BGS";
	}
	
	
	@Override
	public void initDefault() {
		setProperty("EMAIL", "");
		setProperty("PASS", "");
	}
}
