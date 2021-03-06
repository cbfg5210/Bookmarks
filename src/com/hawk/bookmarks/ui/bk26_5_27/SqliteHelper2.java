package com.hawk.bookmarks.ui.bk26_5_27;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hawk.bookmarks.utils.common.ResultSetExtractor;
import com.hawk.bookmarks.utils.common.RowMapper;


/**
 * sqlite帮助类，直接创建该类示例，并调用相应的借口即可对sqlite数据库进行操作
 * 
 * 本类基于 sqlite jdbc v56
 * 
 * @author haoqipeng
 */
public class SqliteHelper2 {
	private static SqliteHelper2 mInstance;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String dbFilePath;
    
    public static SqliteHelper2 getInstance(String dbPath){
    	mInstance=new SqliteHelper2(dbPath);
    	return mInstance;
    }
    
    /**
     * 构造函数
     * @param dbFilePath sqlite db 文件路径
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private SqliteHelper2(String dbFilePath){
        this.dbFilePath = dbFilePath;
    }
    
    /**
     * 获取数据库连接
     * @param dbFilePath db文件路径
     * @return 数据库连接
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection(String dbFilePath) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        return conn;
    }
    
    /**
     * 执行sql查询
     * @param sql sql select 语句
     * @param rse 结果集处理类对象
     * @return 查询结果
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> T executeQuery(String sql, ResultSetExtractor<T> rse) throws SQLException, ClassNotFoundException {
        try {
            resultSet = getStatement().executeQuery(sql);
            T rs = rse.extractData(resultSet);
            return rs;
        } finally {
            destroyed();
        }
    }
    
    /**
     * 执行select查询，返回结果列表
     * 
     * @param sql sql select 语句
     * @param rm 结果集的行数据处理类对象
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public <T> List<T> executeQuery(String sql, RowMapper<T> rm) throws SQLException, ClassNotFoundException {
        List<T> rsList = new ArrayList<T>();
        try {
            resultSet = getStatement().executeQuery(sql);
            while (resultSet.next()) {
                rsList.add(rm.mapRow(resultSet, resultSet.getRow()));
            }
        } finally {
            destroyed();
        }
        return rsList;
    }
    
    /**
     * 执行数据库更新sql语句
     * @param sql
     * @return 更新行数
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int executeUpdate(String sql) throws SQLException, ClassNotFoundException {
        try {
            int c = getStatement().executeUpdate(sql);
            return c;
        } finally {
            destroyed();
        }
        
    }

    /**
     * 执行多个sql更新语句
     * @param sqls
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    /*public void executeUpdate(String...sqls) throws SQLException, ClassNotFoundException {
        try {
            for (String sql : sqls) {
                getStatement().executeUpdate(sql);
            }
        } finally {
            destroyed();
        }
    }*/
    
    
    public int[] executeBatch(String...sqls) throws ClassNotFoundException, SQLException{
    	try {
    		Statement statement=getStatement();
    		for(String sql:sqls){
    			statement.addBatch(sql);
    		}
    		return statement.executeBatch();
        } finally {
            destroyed();
        }
    }
    
    /**
     * 批量执行sql语句
     * @param sqls
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public int[] executeBatch(List<String>sqls) throws ClassNotFoundException, SQLException{
    	try {
    		Statement statement=getStatement();
    		for(String sql:sqls){
    			statement.addBatch(sql);
    		}
    		return statement.executeBatch();
        } finally {
            destroyed();
        }
    }
    
    /**
     * 执行数据库更新 sql List
     * @param sqls sql列表
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    /*public void executeUpdate(List<String> sqls) throws SQLException, ClassNotFoundException {
        try {
            for (String sql : sqls) {
                getStatement().executeUpdate(sql);
            }
        } finally {
            destroyed();
        }
    }*/
    
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        if (null == connection){
        	connection = getConnection(dbFilePath);
        }
        return connection;
    }
    
    private Statement getStatement() throws SQLException, ClassNotFoundException {
        if (null == statement){
        	statement = getConnection().createStatement();
        }
        return statement;
    }
    
    /**
     * 数据库资源关闭和释放
     */
    public void destroyed() {
        try {
            if (null != resultSet) {
                resultSet.close();
                resultSet = null;
            }
            if (null != statement) {
            	statement.close();
            	statement = null;
            }
            if (null != connection) {
            	connection.close();
            	connection = null;
            }
        } catch (SQLException e) {
            System.out.println("Sqlite数据库关闭时异常:"+e.getMessage());
        }
    }
}