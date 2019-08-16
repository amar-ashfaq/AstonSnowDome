/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    //Check whether the username is 7 characters or more
    function maxLen(length){
        var usernameLen = document.getElementById("username").value.length;
        
        //if username length is greater than 7, enable login button
        if(usernameLen > length){
            document.getElementById("loginButton").disabled = false;       
        }
        //else, disable the login button and alert the user
        else{
            document.getElementById("loginButton").disabled = true;       
            alert("Please enter more than 7 characters!");
        }
    }
    
    var connection;
    
    //checkname() to check see if username exists. 
    //processJSONResponse is triggered in the process
    function checkName(username){
        //create an AJAX request object
        connection = new XMLHttpRequest();

        if(connection){
            connection.open("POST","do/checkName", true);

            connection.onreadystatechange = function (){
                //invoke the method below
                processJSONResponse();
            }

            connection.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            connection.send("newUsername=" + username); //sending an asynchronous request to the server
        }
        return;
    }
    //processJSONResponse function performs the username availability operations 
    function processJSONResponse(){
        /**
        * My colleague, Satish Tamang, has assisted with this section of code - Lines 55-64
        */
       
        if(connection.readyState == 4 && connection.status == 200){
            var show = document.getElementById("showWarning");
            
            //trim() function removes empty spaces, ensuring 'response' string is complete
            var response = connection.responseText.trim(); 

            if(response == "Username is already taken!"){
                show.innerHTML = response;
                document.getElementById("signUpButton").disabled=true;
                show.style.color = "red";
            }
            else{
                show.innerHTML = response;
                document.getElementById("signUpButton").disabled=false;
                show.style.color = "green";
            }
        }
        return;
    }