package com.hawk.bookmarks.utils.chrometool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hawk.bookmarks.api.ChromeBookmarkApi;
import com.hawk.bookmarks.model.ChromeBookmark;
import com.hawk.bookmarks.model.ChromeNode;
import com.hawk.bookmarks.model.ChromeRoots;
import com.hawk.bookmarks.utils.common.FileUtils;

public class CChromeDeserializer implements JsonDeserializer<ChromeBookmark>{
	private int currentMaxId=-1;
	
	@Override
	public ChromeBookmark deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		ChromeBookmark chromeBookmark = new ChromeBookmark();
		JsonObject jsonObject=json.getAsJsonObject();
		
		String checksum=jsonObject.get("checksum").getAsString();
		int version=jsonObject.get("version").getAsInt();
		chromeBookmark.setChecksum(checksum);
		chromeBookmark.setVersion(version);
		
		JsonObject roots=jsonObject.getAsJsonObject("roots");
		ChromeRoots chromeRoots=new ChromeRoots();
		
		JsonObject jBookmark_bar=roots.getAsJsonObject("bookmark_bar");
		JsonObject jOther=roots.getAsJsonObject("other");
		JsonObject jSynced=roots.getAsJsonObject("synced");
		
		ChromeNode bookmark_bar=transIntoChromeNode(jBookmark_bar);
		ChromeNode other=transIntoChromeNode(jOther);
		ChromeNode synced=transIntoChromeNode(jSynced);
		
		chromeRoots.setBookmark_bar(bookmark_bar);
		chromeRoots.setOther(other);
		chromeRoots.setSynced(synced);
		
		chromeBookmark.setRoots(chromeRoots);
		chromeBookmark.setCurrentMaxId(currentMaxId);
 
        return chromeBookmark;
	}
	
	private ChromeNode transIntoChromeNode(JsonObject chromenodeJson){
		ChromeNode chromeNode=new ChromeNode();
		List<ChromeNode>children=new ArrayList<>();
		
		String date_added=chromenodeJson.get("date_added").getAsString();
		int id=chromenodeJson.get("id").getAsInt();
		currentMaxId=currentMaxId>id?currentMaxId:id;
		
		String name=chromenodeJson.get("name").getAsString();
		String type=chromenodeJson.get("type").getAsString();
		chromeNode.setDate_added(date_added);
		chromeNode.setId(id);
		chromeNode.setName(name);
		chromeNode.setType(type);
		if(type.equals("url")){
			String url=chromenodeJson.get("url").getAsString();
			chromeNode.setUrl(url);
		}else{
			String date_modified=chromenodeJson.get("date_modified").getAsString();
			chromeNode.setDate_modified(date_modified);
			
			JsonArray jChildren=chromenodeJson.getAsJsonArray("children");
			if(!jChildren.isJsonNull()){
				int len=jChildren.size();
				for(int i=0;i<len;i++){
					children.add(transIntoChromeNode(jChildren.get(i).getAsJsonObject()));
				}
			}
			chromeNode.setChildren(children);
		}
		return chromeNode;
	}
	
	public static ChromeBookmark getObjectBookmarks(){//"Bookmarks"
		String json=null;
    	
    	json=FileUtils.readTxtFile(ChromeBookmarkApi.getBookmarkPath());
    	if(null==json){
    		return null;
    	}
        Gson gson = new GsonBuilder().registerTypeAdapter(ChromeBookmark.class, new CChromeDeserializer()).create();
        ChromeBookmark chromeBookmark = gson.fromJson(json,ChromeBookmark.class);
        
        return chromeBookmark;
	}
 
    public static void main(String[] args) {
//    	String json=null;
//
//    	json=FileUtils.readTxtFile("Bookmarks");
//        Gson gson = new GsonBuilder().registerTypeAdapter(ChromeBookmark.class, new CChromeDeserializer()).create();
//        ChromeBookmark chromeBookmark = gson.fromJson(json,ChromeBookmark.class);
////        System.out.println(chromeBookmark.toString());
//        System.out.println(gson.toJson(chromeBookmark));
    }
}
