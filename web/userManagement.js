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