/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author bastinl
 * @author ashfaqa1
 */
public class LessonSelection  {
    
    private HashMap<String, Lesson> chosenLessons;
    private int ownerID;
    
    private DataSource ds = null;
    private ResultSet rs = null;
    private Statement st = null;

    public LessonSelection(int owner) {
        
        chosenLessons = new HashMap<String, Lesson>();
        this.ownerID = owner;

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
        
        // Connect to the database - this is a pooled connection, so you don't need to close it afterwards
        try {

            Connection connection = ds.getConnection();

             try {

                if (connection != null) {
                  
                    // TODO get the details of any lessons currently selected by this user
                    // One way to do this: create a join query which:
                       // 1. finds rows in the 'lessons_booked' table which relate to this clientid
                       // 2. links 'lessons' to 'lessons_booked' by 'lessonid
                       // 3. selects all fields from lessons for these rows
                   
                    //Selecting fields from 'lesson' table and joining with 'lessons_booked' where clientid is ownerID
                    String query = "SELECT * FROM lessons ls JOIN lessons_booked lsb ON ls.lessonid WHERE lsb.clientid = '" + ownerID + "'";
                    
                    st = connection.createStatement();
                    rs = st.executeQuery(query);
                    
                    // For each one, instantiate a new Lesson object, 
                    // and add it to this collection (use 'LessonSelection.addLesson()' )
                    while(rs.next()){
                        Lesson lesson = new Lesson(rs.getString(1), rs.getTimestamp(3), rs.getTimestamp(4), rs.getInt(2), rs.getString(5));
                        this.addLesson(lesson); //Add lesson to the hashmap collection
                    }      
                }
                connection.close();
            }
            catch(SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            }
        
        
            }
            catch(Exception e){
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            }
    }

    /**
     * @return the items
     */
    public Set <Entry <String, Lesson>> getItems() {
        return chosenLessons.entrySet();
    }

    public void addLesson(Lesson l) {
        Lesson i = new Lesson(l);
        this.chosenLessons.put(l.getId(), i);
    }
    //created to allow the 'removal' of a chosen lesson using the 'lessonid' 
    public void removeLesson(String lessonid){
        this.chosenLessons.remove(lessonid);
    }

    public Lesson getLesson(String id){
        return this.chosenLessons.get(id);
    }
    
    public int getNumChosen(){
        return this.chosenLessons.size();
    }

    public int getOwner() {
        return this.ownerID;
    }
    
    public void updateBooking(int userID) {
        
        // TODO get a connection to the database as in the method above
        // TODO In the database, delete any existing lessons booked for this user in the table 'lessons_booked'
        // REMEMBER to use executeUpdate, not executeQuery
        // TODO - write and execute a query which, for each selected lesson, will insert into the correct table:
                    // the owner id into the clientid field
                    // the lesson ID into the lessonid field
        
        try{
            Connection connection = ds.getConnection();
            
            if(connection != null){
                
                //apply the delete function if there is at least ONE lesson record
                //if(this.getNumChosen() > 0){               
                    String query = "DELETE FROM lessons_booked WHERE clientid = '" + userID + "'";                
                    st = connection.createStatement();
                    st.executeUpdate(query);
                //}
                
                // A tip: here is how you can get the ids of any lessons that are currently selected
                Object[] lessonKeys = chosenLessons.keySet().toArray();
                
                int i = 0;
                
                while(i < lessonKeys.length){          
                    String insertQuery = "INSERT INTO lessons_booked VALUES(?,?)";
                    
                    PreparedStatement prepst = connection.prepareStatement(insertQuery);
                    
                    prepst.setInt(1, ownerID); //set 'ownerID' as an INT
                    prepst.setString(2, (String)lessonKeys[i]); //set '(String)lessonKeys[i]' as STRING             
                    prepst.execute();
                    
                    i++;
                    
                    // Temporary check to see what the current lesson ID is....
                    //System.out.println("Lesson ID is : " + (String)lessonKeys[i]);  
                }
            }
            connection.close();
        }
        catch(SQLException ex){
            System.out.println("Exception is ;" + ex + ": message is " + ex.getMessage());
        }
    }
    
    //'cancelLesson' function to cancel any 'selected' lessons of the user
    //passes in lesson to be cancelled along with the userID
    public void cancelLesson(String lessonCancel, int userID){ //OPTIONAL PART
        
        try{
            Connection connection = ds.getConnection();
            if(connection != null){
                //Remove the lesson
                String query = "DELETE FROM lessons_booked WHERE lessonid '" + lessonCancel + "' AND clientid = " + userID;
                st = connection.createStatement();
                st.executeUpdate(query);
                
                //remove the lesson from the collection
                removeLesson(lessonCancel);
            }
            connection.close();
        }
        catch(SQLException ex){
            System.out.println("Exception is ;" + ex + ": message is " + ex.getMessage());
        }
    }
}