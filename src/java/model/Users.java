/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.PreparedStatement;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author bastinl
 * @author ashfaqa1
 */
public class Users {
  
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;
    DataSource ds = null;
   
    public Users() {
        
        // You don't need to make any changes to the try/catch code below
        try {
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            ds = (DataSource)envCtx.lookup("jdbc/LessonDatabase");
        }
            catch(Exception e) {
            System.out.println("Exception message is " + e.getMessage());
        }
        
    }

    public int isValid(String name, String pwd) {
       
        try {
            
            Connection connection = ds.getConnection();
            //System.out.println(name + " " + pwd + " ");
            if (connection != null) {
               
               //TODO: implement this method so that if the user does not exist, it returns 0.
               
               // If the username and password are correct, it should return the 'clientID' value from the database.
                
               //SELECT all columns from the clients table where the username is passed in
               String query = "SELECT * FROM clients WHERE username = '" + name + "'";
               pstmt = connection.prepareStatement(query);
               rs = pstmt.executeQuery(query);
               
               //if the user exists in the database table, the password will be checked
               if(rs.next()){                 
                   if(rs.getString(3).equals(pwd)){
                       //return 'clientid'
                       return rs.getInt(1);
                   }
                   else{
                       return 0;
                   }
               
               }
               
               else{
                   return 0;
               }
               
            }
            else {
                //if connection is null
                return 0;
            }
            
            
        } catch(SQLException e) {
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return 0;
        }
        
    }
    
    // TODO (Optional steps 3 and 4) add a user with specified username and password
    public void addUser(String name, String pwd) {
       
        //TODO: implement this method so that the specified username and password are inserted into the database.

         try {
            
            Connection connection = ds.getConnection();

            if (connection != null) {
                
                pstmt = connection.prepareStatement("INSERT INTO clients ( username, password) VALUES (?,?)");
                pstmt.setString(1, name);
                pstmt.setString(2, pwd);
                
                //executeUpdate() used for the insertion of records
                pstmt.executeUpdate();
            }
            connection.close();
         }
            catch(SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
         }
    }
    
    public int checkName(String username){
        
        try{
            Connection connection = ds.getConnection();
           
            if(connection != null){
                
                //select from 'clients' table where the 'username' is passed in as the parameter
                String query = "SELECT * FROM clients WHERE username = '" + username + "'";
                pstmt = connection.prepareStatement(query);
                rs = pstmt.executeQuery(query);
                
                //if there is a result from the executed query, return 1
                if(rs.next()){
                    return 1;
                }
                else{
                    return 0;
                }
            }
            else{
                return 0;
            }
        }
        
        catch(SQLException ex){
            System.out.println("Exception is ;"+ex + ": message is " + ex.getMessage());
            return 0;
        }
    }
}