/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LessonSelection;
import model.LessonTimetable;
import model.Users;

/**
 *
 * @author bastinl
 * @author ashfaqa1
 */
public class Controller extends HttpServlet {

   private Users users;
   private LessonTimetable availableLessons;

    public void init() {
        /**
        * My colleague, Satish Tamang, has assisted with this section of code - Line 42
        */  
         users = new Users();
         availableLessons = new LessonTimetable();
         // TODO Attach the lesson timetable to an appropriate scope
         
         //casting 'LessonTimetable' to ensure compatibility
         LessonTimetable lessonTimetable = (LessonTimetable) getServletContext().getAttribute("availableLessons");
         
         //'this' - refering to the newly created object 'LessonTimetable'
         this.getServletContext().setAttribute("lessonTimetable", lessonTimetable);
    }
    
    public void destroy() {
        
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getPathInfo();
        RequestDispatcher dispatcher = null;
        
        //Create session object to store username
        HttpSession session = request.getSession();
        
        if(action.equals("/login")){ //Login Page
            //obtain the username and password
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            //create 'userID' to check validity
            int userID = users.isValid(username, password);
            
            //if user is valid, then proceed to login
            if(userID != 0){
                
                //set the session attributes accordingly
                session.setAttribute("user", username);
                session.setAttribute("id", userID);
                
                LessonSelection lessonSelection = new LessonSelection(userID);
                //set the session attribute for lessonSelection
                session.setAttribute("selected", lessonSelection);
                
                dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
            }
            else{
                //invalid login, therefore dispatcher redirects to the login view
                dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            }
        }
        else if(action.equals("/viewTimetable")){               
            //dispatcher directs to the LessonTimetableView
            dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");                                 
        }
        else if(action.equals("/viewSelection")){               
            //dispatcher directs to the LessonSelectionView
            dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");                              
        }
        else if(action.equals("/addUser")){ //OPTIONAL PART
            
            //Obtain the new username and password upon signup
            String addUser = request.getParameter("newUsername");
            String user_password = request.getParameter("newPassword");
            
            int userID = users.isValid(addUser, user_password);
            
            if(userID == 0){
                users.addUser(addUser, user_password); //add user
                //dispatcher directs to the login page
                dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            }
            else{
                //dispatcher directs to the login page
                dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            }            
        }
        else if(action.equals("/checkName")){ //OPTIONAL PART
            /**
             * My colleague, Satish Tamang, has assisted with this section of code - Lines 131 to 138
             */             
            response.setContentType("application/xml;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            String userCheckName = request.getParameter("newUsername");
            
            session.setAttribute("newUsername", userCheckName);
            
            int check = users.checkName(userCheckName);           
            
            try{
                if(check == 0){
                    out.println("Username is available");
                    dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
                }
                else{
                    out.println("Username is already taken!");
                    dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
                }
            }
            finally{
                out.close();
            }        
        }
        else if(action.equals("/chooseLesson")){
            /**
             * My colleague, Maher Askary, has assisted with this section of code - Lines 148, 153-154
             */             
            session = request.getSession();
            
            //Obtain the chosen lesson using the lessonid
            String chosenLesson_id = request.getParameter("chosenLesson_id");
            //get the session attribute of the selected lessons
            LessonSelection lessonSelection = (LessonSelection) session.getAttribute("selected");         
            lessonSelection.addLesson(this.availableLessons.getLesson(chosenLesson_id));
            
            //dispatcher directs to the LessonSelectionView
            dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
        }
        else if(action.equals("/cancelLesson")){ //OPTIONAL ROUTE
            
            session = request.getSession();
            
            //Obtain the lesson to be cancelled, using the lessonid
            String cancelLesson_id = request.getParameter("cancelLesson_id");
            LessonSelection lessonCancel = (LessonSelection) session.getAttribute("selected");
            
            //remove the lesson, using the lessonid
            lessonCancel.removeLesson(cancelLesson_id);
           
            //dispatcher directs to the LessonSelectionView
            dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");    
        }
        else if(action.equals("/finaliseBooking")){
            
            session = request.getSession();
            
            int userID = (Integer)session.getAttribute("id");
            LessonSelection lessonBooking = (LessonSelection) session.getAttribute("selected");
            
            //delete existing bookings if they exist, and insert new booking records using 'userid'
            lessonBooking.updateBooking(userID);
            
            //dispatcher directs to the LessonTimetableView
            dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
        }
        else if (action.equals("/logOut")){
            //invalidate the session and direct back to login
            session.invalidate();
            dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
        }
            //request is forwarded to the selected view
             dispatcher.forward(request, response);
        }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}