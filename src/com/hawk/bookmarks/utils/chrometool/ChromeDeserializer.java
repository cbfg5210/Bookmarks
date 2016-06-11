package com.hawk.bookmarks.utils.chrometool;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.Utils;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneUtils;

public class ChromeDeserializer implements JsonDeserializer<LikeLynne>{
	private JsonObject tempLikeLynne;
	private int currentId;
	
	@Override
	public LikeLynne deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext arg2)
			throws JsonParseException {
		// TODO Auto-generated method stub
		JsonObject json_root_folder = json.getAsJsonObject();
		
		getLikeLynneFolder(json_root_folder);
		currentId=new LikeLynneUtils().getMaxId();
		if(currentId<0){
			return null;
		}
		
		return transIntoLikeLynne(1, tempLikeLynne);
	}
	
	/**
	 * 将jsonobject转换成likelynne
	 * @param parentId
	 * @param chromenodeJson
	 * @return
	 */
	private LikeLynne transIntoLikeLynne(int parentId, JsonObject chromenodeJson) {
		LikeLynne lynneNode = new LikeLynne();

		Long date_added = chromenodeJson.get("date_added").getAsLong();
		String title = chromenodeJson.get("name").getAsString();
		lynneNode.setDateAdded(date_added);
		lynneNode.setTitle(title);
		lynneNode.setParent(parentId);
		currentId++;
		lynneNode.setId(currentId);
		lynneNode.setChildren(new ArrayList<>());

		String type = chromenodeJson.get("type").getAsString();
		if (type.equals("url")) {
			lynneNode.setType(1);
			String url = chromenodeJson.get("url").getAsString();
			lynneNode.setUrl(url);
		} else {
			lynneNode.setType(2);
			lynneNode.setUrl(Utils.getUuid());

			JsonArray jChildren = chromenodeJson.getAsJsonArray("children");
			if (!jChildren.isJsonNull()) {
				int len = jChildren.size();
				for (int i = 0; i < len; i++) {
					lynneNode.getChildren().add(transIntoLikeLynne(lynneNode.getId(), jChildren.get(i).getAsJsonObject()));
				}
			}
		}
		return lynneNode;
	}
	
	/**
	 * 遍历jsonobject获取如影书签文件夹
	 * @param folderObject
	 */
	private void getLikeLynneFolder(JsonObject folderObject){
		String title = folderObject.get("name").getAsString();
		if(title.equals("如影书签")){
			tempLikeLynne=folderObject;
			return;
		}
		JsonArray jChildren = folderObject.getAsJsonArray("children");
		if (null!=jChildren&&jChildren.size()>0) {
			int len = jChildren.size();
			for (int i = 0; i < len; i++) {
				JsonObject child=jChildren.get(i).getAsJsonObject();
				if(child.get("type").getAsString().equals("folder")){
					getLikeLynneFolder(child);
				}
			}
		}
	}
}
