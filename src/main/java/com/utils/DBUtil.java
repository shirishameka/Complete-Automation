package com.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class DBUtil {
    public static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private Connection conn = null;
    public Statement stmt = null;
    public ResultSet rs = null;
    public String dbUrl = null, uname, pwd;


    public DBUtil() throws Exception {
        dbUrl = Utils.getConfig("jdbc.databaseurl");
        uname = Utils.getConfig("jdbc.username");
        pwd = Utils.getConfig("jdbc.password");

    }


    public Connection getConnection() throws Exception {
        String strSshUser = Utils.getConfig("jdbc.strSshUser");
        String strSshPassword = Utils.getConfig("jdbc.strSshPassword");
        String strSshHost = Utils.getConfig("jdbc.strSshHost");
        int nSshPort = 33022;
        String strRemoteHost = Utils.getConfig("jdbc.strRemoteHost");
        int nLocalPort = 3305;
        int nRemotePort = 3306;
        String strDbUser = Utils.getConfig("jdbc.strDbUser");
        String strDbPassword = Utils.getConfig("jdbc.strDbPassword");
        doSshTunnel(strSshUser,strSshPassword,strSshHost,nSshPort,strRemoteHost,nLocalPort,nRemotePort);
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:"+nLocalPort, strDbUser, strDbPassword);
        DatabaseMetaData metadata = con.getMetaData();
        return con;
    }


    public void closeConnection() {
        if (conn != null)
            try {
                if (conn != null && !conn.isClosed())
                    conn.close();

                if (stmt != null && !stmt.isClosed())
                    stmt.close();
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


    }

    public ResultSet execQuery(String query) {
        try {
            chkConnection();
            rs = stmt.executeQuery(query);
            rs.next();
            return rs;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            closeConnection();
        } finally {
            closeConnection();
        }
        return rs;
    }

    public String execQueryWithSingleColumnResult(String query) {
        try {
            chkConnection();
            rs = stmt.executeQuery(query);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            closeConnection();
            return null;
        } finally {
            closeConnection();
        }
    }

    public String[] execQueryWithSingleColumnMultipleResult(String query) {

        try {
            chkConnection();
            int i = 0;
            rs = stmt.executeQuery(query);
            String[] result = new String[getRowCount(rs)];
            while (rs.next()) {
                result[i++] = rs.getString(1);
            }
            return result;
        } catch (Exception e) {
            closeConnection();
            return null;
        } finally {
            closeConnection();
        }
    }


    public HashMap<String, String> execQueryWithSingleRowResult(String query) {
        HashMap<String, String> hm1 = new HashMap<String, String>();
        try {
            chkConnection();
            logger.debug("query is " + query);
            chkConnection();

            rs = stmt.executeQuery(query);
            if (rs.next() == false)
                return null;

            ResultSetMetaData md = rs.getMetaData();

            for (int i = 1; i <= md.getColumnCount(); i++) {
                hm1.put(md.getColumnName(i), rs.getString(md.getColumnName(i)));
            }
            return hm1;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            closeConnection();
        } finally {
            closeConnection();
        }
        // return hm;
        return hm1;
    }


    public HashMap[] execQueryWithResultHM(String query) {
        try {
            HashMap<String, String>[] hmArr;

            chkConnection();

            rs = stmt.executeQuery(query);
            rs.next();
            hmArr = new HashMap[getRowCount(rs)];

            ResultSetMetaData md = rs.getMetaData();
            int j = 0;
            while (rs.next()) {
                hmArr[j] = new HashMap<String, String>();
                for (int i = 1; i <= md.getColumnCount(); i++)
                    hmArr[j].put(md.getColumnName(i),
                            rs.getString(md.getColumnName(i)));

                j++;
            }
            return hmArr;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            closeConnection();
        } finally {
            closeConnection();
        }
        return null;
    }

    public int getRowCount(ResultSet rs) throws Exception {
        rs.last();
        int n = rs.getRow();
        rs.beforeFirst();
        return n;
    }

    public void chkConnection() throws SQLException, Exception {
        if (conn == null || conn.isClosed())
            getConnection();
        if (stmt == null || stmt.isClosed())
            stmt = conn.createStatement();
    }

    private static void doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException, JSchException {
        final JSch jsch = new JSch();
        Session session = jsch.getSession( strSshUser, strSshHost, 33022 );
        session.setPassword( strSshPassword );

        final Properties config = new Properties();
        config.put( "StrictHostKeyChecking", "no" );
        session.setConfig( config );

        session.connect();
        session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
    }




}
