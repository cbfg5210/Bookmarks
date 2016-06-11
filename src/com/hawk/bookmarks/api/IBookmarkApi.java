package com.hawk.bookmarks.api;

import com.hawk.bookmarks.model.LikeLynne;

/**
 * 浏览器书签操作Api
 * @author hawk
 * @param <T>
 *
 * @param <T>
 */
public interface IBookmarkApi{
	//将浏览器数据转换成LikeLynne数据
	abstract LikeLynne parseToLikeLynne();
	//将LikeLynne数据保存为浏览器数据
	abstract int insertBookmarks();
	abstract Object transFromLikeLynne();
}
