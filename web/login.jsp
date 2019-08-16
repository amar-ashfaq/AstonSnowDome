<%-- 
    Document   : index
    Created on : 15-Mar-2010, 14:47:22
    Author     : bastinl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <!--external Java script file attached for good cohesion and abstraction-->
        <script type="text/javascript" src="usernameValidate.js"></script>
        <title>Login / signup page</title>
    </head>
    
    <body>
        <h2 style="font-family: cursive;">Please log in!</h2>
        <form method="POST" action="/coursework/do/login">
            Username:<input type="text" name="username" id="username" value="" onblur="maxLen(7)"/>----
            Password:<input type="password" name="password" value="" />        
          <input type="submit" id="loginButton" value="Click to log in" />
        </form>
        
        <form method="POST" action="/coursework/do/addUser">
            <h2 style="font-family: cursive;">Don't yet have an account? Sign up below</h2>
                Username:<input type="text" name="newUsername" id="newUsername" value="" onchange="checkName(this.value)"/>----
                Password:<input type="password" name="newPassword" value="" />      
            <input type="submit" id="signUpButton" value="Sign up as a new user" />   
        </form>
        
        <!--create a <p> warning display for the user to see-->
        <p id ="showWarning"></p>
        
    </body>
</html>