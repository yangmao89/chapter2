package org.smart4j.chapter2.helper;

import com.mysql.jdbc.Driver;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.CollectionUtil;
import org.smart4j.chapter2.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CPR014 on 2017-04-12.
 */
public class DatabaseHelper {
    private static final String DRIVER;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final QueryRunner QUERY_RUNNER;
    private static final ThreadLocal<Connection> CONNECTION_HANDLER = new ThreadLocal<Connection>();
    private static final BasicDataSource DATA_SOURCE;

    static {
        Properties props = PropsUtil.loadProps("config.properties");
        DRIVER = props.getProperty("jdbc.driver");
        URL = props.getProperty("jdbc.url");
        USER = props.getProperty("jdbc.username");
        PASSWORD = props.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        QUERY_RUNNER = new QueryRunner();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USER);
        DATA_SOURCE.setPassword(PASSWORD);
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_HANDLER.get();
        if(conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CONNECTION_HANDLER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(){
        try {
            Connection conn = CONNECTION_HANDLER.get();
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CONNECTION_HANDLER.remove();
        }
    }

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Connection conn, Object... params){
        List<T> entityList;
        try {
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity list failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity list failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 查询实体（单个实体）
     * @param entityClass
     * @param sql
     * @param params
     * @param <E>
     * @return
     */
    public static <E> E queryEntity(Class<E> entityClass, String sql, Object... params){
        E entity = null;
        try{
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<E>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity failure", e);
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return entity;
    }

    /**
     * 查询列表
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params){
        List<Map<String, Object>> resultList;
        try{
            Connection conn = getConnection();
            resultList = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOG.error("execute query failure");
            throw new RuntimeException(e);
        }
        return resultList;
    }

    /**
     * 执行更新操作
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params){
        int result = 0;
        try{
            Connection conn = getConnection();
            result = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOG.error("execute update failure");
        }
        return result;
    }

    /**
     * 插入实体类
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
        int rows = 0;
        if(CollectionUtil.isNotEmpty(fieldMap)){
            StringBuilder keySql = new StringBuilder("insert into " + getTableName(entityClass) + " ( ");
            StringBuilder valueSql = new StringBuilder("( ");
            for(String key : fieldMap.keySet()){
                keySql.append( key + ", ");
                valueSql.append("?, ");
            }
            keySql.replace(keySql.lastIndexOf(","), keySql.length(), ") ");
            valueSql.replace(valueSql.lastIndexOf(","), valueSql.length(), ") ");
            String sql = keySql + " values " + valueSql;
            Object[] params = fieldMap.values().toArray();
            rows = executeUpdate(sql, params);
        }
        return rows == 1;
    }

    /**
     * 更新实体类
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, Long id, Map<String, Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            return false;
        }
        String sql = "update " + getTableName(entityClass) + " set ";
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        for(String key : fieldMap.keySet()){
            sb.append(key).append("=?,");
        }
        sb.replace(sb.lastIndexOf(","), sb.length(), "");
        sql += sb + " where id=? ";
        params.addAll(fieldMap.values());
        params.add(id);
        return executeUpdate(sql, params.toArray()) == 1;
    }

    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, Long id){
        String sql = "delete from " + getTableName(entityClass) + " where id=?";
        return executeUpdate(sql, new Object[]{id}) == 1;
    }

    public static void executeSqlFile(String filePath){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String sql;
        try {
            while ((sql = br.readLine()) != null){
                executeUpdate(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTableName(Class entityClass){
        return entityClass.getSimpleName();
    }


}
