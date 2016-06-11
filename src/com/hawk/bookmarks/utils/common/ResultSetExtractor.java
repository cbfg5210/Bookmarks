package com.hawk.bookmarks.utils.common;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {

	public abstract T extractData(ResultSet rs) throws SQLException;

}