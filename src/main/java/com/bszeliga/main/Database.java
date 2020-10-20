package com.bszeliga.main;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.*;

public class Database {
    private Connection connection;

    public boolean connect(String host, String base, String user, String pass){
        try{
            //STEP 1: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            // host - localhost, base - szkola?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
            String url = "jdbc:mysql://" + host + "/" + base;
            this.connection = DriverManager.getConnection(url,user,pass);
            return true;
        }catch(SQLException se){
            return false;
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection(){
        return this.connection;
    }


}
