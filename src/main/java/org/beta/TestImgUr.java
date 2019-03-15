package org.beta;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.magic.api.beans.Wallpaper;
import org.magic.api.interfaces.abstracts.AbstractWallpaperProvider;
import org.magic.services.MTGConstants;
import org.magic.tools.URLTools;
import org.magic.tools.URLToolsClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class TestImgUr extends AbstractWallpaperProvider {

	public static void main(String[] args) throws IOException {
		
		new TestImgUr().search("liliana");
	}

	@Override
	public List<Wallpaper> search(String search) {
		
		
		List<Wallpaper> ret = new ArrayList<>();
		URLToolsClient c = URLTools.newClient();
		Map<String,String> m = new HashMap<>();
		m.put("Authorization","Client-ID "+getString("CLIENTID"));
		
		try {
			String s= c.doGet("https://api.imgur.com/3/gallery/search/time/all/1?q="+URLEncoder.encode(search,MTGConstants.DEFAULT_ENCODING), m);
			JsonArray arr = URLTools.toJson(s).getAsJsonObject().get("data").getAsJsonArray();
			arr.forEach(je->{
				Wallpaper w = new Wallpaper();
				w.setName(je.getAsJsonObject().get("title").getAsString());
				w.setUrl(URI.create("https://imgur.com/gallery/"+je.getAsJsonObject().get("id").getAsString()));
				
				System.out.println(w.getName());
				
				ret.add(w);
			});
		} catch (IOException e) {
			logger.error(e);
		}
		
		return ret;

	}


	@Override
	public String getName() {
		return "Imgur";
	}

	@Override
	public void initDefault() {
		setProperty("CLIENTID", "");
	}
}
