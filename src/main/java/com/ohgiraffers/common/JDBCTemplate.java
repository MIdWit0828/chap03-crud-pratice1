package com.ohgiraffers.common;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

//자주 쓰는 기능을 여러 프로젝트에서 사용하기 위해서 작성
public class JDBCTemplate {
    //내가 실행 할 때 마다 connection을 얻어 올 수 있는 메소드
    public static Connection getConnection() {
        Properties prop = new Properties();
        Connection con = null;
        try {
            prop.load(new FileReader("src/main/java/com/ohgiraffers/config/connection-info.properties"));
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");

            Class.forName(driver);
            con = DriverManager.getConnection(url, prop);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    //더이상 사용하지 않을 때 사용
    public static void close(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                //System.out.println("con.colse()...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement stat) {
        try {
            if (stat != null && !stat.isClosed()) {
                stat.close();
                System.out.println("state.colse()...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rset) {
        try {
            if (rset != null && !rset.isClosed()) {
                rset.close();
                System.out.println("rset.colse()...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
