function createJsonFromUserRegistrationForm() {
    var paramUserName = document.getElementById("registrationName").value;
    var paramEmail = document.getElementById("registrationEmail").value;
    var paramPassword = document.getElementById("registrationPassword").value;
    var userInfoJson = {"username": paramUserName, "email": paramEmail, "password": paramPassword};
    saveNewUser(userInfoJson);
}



function saveNewUser(userInfoJson){
    console.log("saveNewUser is called for " + userInfoJson.username);
    var data = JSON.stringify(userInfoJson);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", OnPostResponseForSaveUser);
    xhr.open("POST", document.URL + "getUser");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function OnPostResponseForSaveUser(){
    if (this.readyState === 4) {
        if (this.status === 200) {
            console.log("Response is " + this.responseText);
            displayNameIsTaken(this.responseText);
        }
    }
}

function displayNameIsTaken(responseText){
    if (responseText === "false"){
        document.getElementById("userNameIsTakenWarning").style.visibility = 'visible';

    }

    if (responseText === "invalidEmail"){
        document.getElementById("invalidEmail").style.visibility = 'visible';
    }

    if (responseText === "shortPassword"){
        document.getElementById("shortPassword").style.visibility = 'visible';
    }

    if (responseText === "true"){
        document.getElementById("regForm").innerHTML = "You are registered";
        document.getElementById("regButton").style.visibility = 'hidden';
    }

}

function getLoginInfo(){
    console.log("getLoginInfo is called...");
    var username = document.getElementById("defaultForm-username").value;
    var password = document.getElementById("defaultForm-pass").value;
    var userAndPassword = {"id":1, "username":username, "email": "some mail", "password":password};
    logIn(userAndPassword);
}

function logIn(userAndPassword){
    console.log(userAndPassword.username + userAndPassword.password);
    var data = JSON.stringify(userAndPassword);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", OnGetResponseForSaveUser);
    xhr.open("POST", document.URL + "getLogin");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function OnGetResponseForSaveUser() {
    console.log("OnGetResponseForSaveUser...");

        if (this.readyState === 4) {
            if (this.status === 200) {
                console.log("Response is " + this.responseText);
                displayWrongUsernameOrPassword(this.responseText);
            }
        }
    }

function displayWrongUsernameOrPassword(responseText){


    if (responseText === "Wrong Username"){
        document.getElementById("wrongUsername").style.visibility = 'visible';
    }
    if (responseText === "Wrong Password"){
        document.getElementById("wrongPassword").style.visibility = 'visible';
    }
    if (responseText === "AllowLogIn") {
        document.getElementById("logInForm").innerHTML = "You are logged in";
        //function to load lists by user id
        document.getElementById("logInButton").style.visibility = 'hidden';
    }
}

