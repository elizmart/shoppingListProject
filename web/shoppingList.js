var itemStatus = "active";
var parentListId = 1;
var userId;



//Hit enter to add an item (replacing button)
function doit_onkeypress(event) {
    if (event.keyCode === 13 || event.which === 13) {
        createJsonFromInput();
    }
}

function doit_onkeypress_listName(event) {
    if (event.keyCode === 13 || event.which === 13) {
        createListJsonFromInput();
    }
}

function showHiddenForm() {
    document.getElementById("form").style.visibility = 'visible';
}

function showHiddenTitle() {
    document.getElementById("hiddenTitle").style.visibility = 'visible';

}

function showHiddenButton() {
    document.getElementById("printButton").style.visibility = 'visible';
}

//Typing effect for header
var i = 0;
var txt = 'Shopping List';
var speed = 100;

function typeWriter() {
    if (i < txt.length) {
        document.getElementById("header").innerHTML += txt.charAt(i);
        i++;
        setTimeout(typeWriter, speed);
    }
    makeRegisterButtonVisible();
    makeLoginButtonVisible();
}



function makeRegisterButtonVisible(){
    document.getElementById("register").style.visibility = 'visible';
}


function makeLoginButtonVisible(){
    document.getElementById("login").style.visibility = 'visible';
}


function getAllListNames() {

    var data = null;
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", getResponseWithAllListNames);
    xhr.open("GET", document.URL + "getList?userId="+userId);
    xhr.setRequestHeader("Content-Type", "text");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function getResponseWithAllListNames() {
    if (this.readyState === 4) {
        if (this.status === 200) {
            var jsonFromResponse = JSON.parse(this.responseText);
            console.log(this.responseText);
            displayListNamesOnload(jsonFromResponse);

        }
    }
}


function createListJsonFromInput() {
    var paramListName = document.getElementById('myListName').value;
    var listJson = {"name": paramListName, "userId": userId};
    saveNewLists(listJson);
}


function saveNewLists(listJson) {
    var data = JSON.stringify(listJson);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", OnPostResponseForList);
    xhr.open("POST", document.URL + "getList");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
 }

function OnPostResponseForList() {
       if (this.readyState === 4) {
        if (this.status === 200) {
            var jsonFromResponse = JSON.parse(this.responseText);
            parentListId = jsonFromResponse.id;
            getAllListNames();
        }
    }
}


function displayListNamesOnload(jsonFromResponse) {
    console.log("Display list names...");
    document.getElementById('mainContainer').style.visibility = 'visible';
    var listOfLists = document.getElementById("listOfLists");
    listOfLists.innerHTML = '';
    for (var i = 0; i < jsonFromResponse.length; i++) {
        displayListNames(jsonFromResponse[i]);
    }
}

function displayListNames(jsonFromResponse) {
    var aList = document.createElement('li');
    aList.innerText = jsonFromResponse.name;
    document.getElementById("listOfLists").appendChild(aList);
    aList.appendChild(createButtonsForLists(jsonFromResponse, aList));
    aList.onclick = function () {
        parentListId = jsonFromResponse.id;
        document.getElementById("hiddenTitle").innerText = "Add items to " + jsonFromResponse.name;
        showHiddenTitle();
        showHiddenForm();
        callItemsById(jsonFromResponse);
    };
}


//working with children list
function createJsonFromInput() {
    var paramProduct = document.getElementById('myProduct').value;
    var paramAmount = document.getElementById('myAmount').value;
    var isValid = validateInput(paramProduct, paramAmount);
    if (isValid === true) {
        var json = {"product": paramProduct, "amount": paramAmount, "status": itemStatus, "parentListId": parentListId};
        saveItem(json);
    }
}

function saveItem(json) {
    var data = JSON.stringify(json);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", OnPostResponse);
    xhr.open("POST", document.URL + "getItems");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function OnPostResponse() {
    if (this.readyState === 4) {
        if (this.status === 200) {
            var list = document.getElementById("myList");
            var jsonFromResponse = JSON.parse(this.responseText);
            var listItem = document.createElement('li');
            var textNode = document.createTextNode(jsonFromResponse.product + " " + jsonFromResponse.amount);
            listItem.appendChild(textNode);
            listItem.appendChild(createButtons(jsonFromResponse, listItem));
            list.insertBefore(listItem, list.childNodes[0]);
        }
    }
}


function callItemsById(jsonFromResponse) {
    var data = null;
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", OnPostResponseForItemsSortedById);
    xhr.open("GET", document.URL + "getItems?parentList="+parentListId);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function OnPostResponseForItemsSortedById() {
    if (this.readyState === 4) {
        if (this.status === 200) {
            var itemsSortedById = JSON.parse(this.responseText);
            displayItemsById(itemsSortedById);
        }
    }
}

function displayItemsById(itemsSortedById) {
    var list = document.getElementById("myList");
    list.innerHTML = '';
    for (var i = 0; i < itemsSortedById.length; i++) {
        var listItem = document.createElement('li');

        listItem.innerText += itemsSortedById[i].product + ' ' + itemsSortedById[i].amount;
        if (itemsSortedById[i].status === 'done') {
            listItem.innerText = itemsSortedById[i].product + ' ' + itemsSortedById[i].amount;
            listItem.innerHTML = listItem.innerHTML.strike();
        }
        list.appendChild(listItem);
        listItem.appendChild(createButtons(itemsSortedById[i], listItem));
    }
    showHiddenButton();
}


function createButtons(jsonFromInput, listItem) {
    var span = document.createElement("span");
    span.appendChild(createDoneButton(jsonFromInput));
    span.appendChild(createEditButton(jsonFromInput));
    span.appendChild(createDeleteButton(jsonFromInput));
    return span;
}

function createButtonsForLists(jsonFromInput, listItem) {
    var span = document.createElement("span");
    span.appendChild(createEditListNameButton(jsonFromInput));
    span.appendChild(createDeleteListButton(jsonFromInput));
    return span;
}


function createDeleteButton(jsonFromInput) {
    var deleteButton = document.createElement("button");
    deleteButton.innerHTML = " &#10007";
    deleteButton.className = "deleteEdit";
    deleteButton.onclick = function () {
        var span = this.parentNode;
        var listItem = span.parentNode;
        var ul = listItem.parentNode;
        ul.removeChild(listItem);
        deleteItem(jsonFromInput);

    };
    return deleteButton;
}

function createDeleteListButton(jsonFromInput) {
    var deleteButton = document.createElement("button");
    deleteButton.innerHTML = " &#10007";
    deleteButton.className = "deleteEdit";
    deleteButton.onclick = function () {
        deleteList(jsonFromInput);
        var span = this.parentNode;
        var listItem = span.parentNode;
        var ul = listItem.parentNode;
        ul.removeChild(listItem);
    };
    return deleteButton;
}


function deleteItem(jsonFromInput) {
    var data = JSON.stringify(jsonFromInput);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            console.log(this.responseText);
        }
    });

    xhr.open("DELETE", document.URL + "getItems");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function deleteList(jsonFromInput) {
    var data = JSON.stringify(jsonFromInput);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            console.log(this.responseText);
            getAllListNames();
        }
    });

    xhr.open("DELETE", document.URL + "getList");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function createDoneButton(jsonFromInput) {
    var doneButton = document.createElement("button");
    doneButton.innerHTML = " &#9745;";
    doneButton.className = "deleteEdit";
    doneButton.onclick = function () {
        var span = this.parentNode;
        var listItem = span.parentNode;
        listItem.innerHTML = listItem.innerHTML.strike();
        jsonFromInput.status = "done";
        updateItem(jsonFromInput);
    };
    return doneButton;
}

function updateItem(jsonFromInput) {
    var updateData = JSON.stringify(jsonFromInput);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            console.log(this.responseText);
        }
    });

    xhr.open("PUT", document.URL + "getItems");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(updateData);
}


function createEditButton(jsonFromInput) {
    var editButton = document.createElement("button");
    editButton.innerHTML = " &#9998";
    editButton.className = "deleteEdit";
    editButton.onclick = function () {
        var span = this.parentNode;
        var listItem = span.parentNode;
        listItem.removeChild(span);

        var oldContent = listItem.innerText;
        listItem.contentEditable = "true";

        listItem.onblur = function () {
            var newContent = listItem.innerText;
            var isValid = processEditInput(newContent, jsonFromInput);
            if (isValid === true) {
                listItem.innerText = newContent;
                updateItem(jsonFromInput);
            }
            if (isValid === false) {
                listItem.innerText = oldContent;
            }

            listItem.appendChild(span);
        }
    };

    return editButton;
}

function createEditListNameButton(jsonFromInput) {
    var editListNameButton = document.createElement("button");
    editListNameButton.innerHTML = " &#9998";
    editListNameButton.className = "deleteEdit";
    editListNameButton.onclick = function () {
        var span = this.parentNode;
        var listItem = span.parentNode;
        listItem.removeChild(span);
        listItem.contentEditable = "true";
        listItem.onblur = function () {
            var newContent = listItem.innerText;
            jsonFromInput.name = newContent;

            listItem.innerText = newContent;
            updateListName(jsonFromInput);
            listItem.appendChild(span);
        };

    };
    return editListNameButton;
}

function updateListName(jsonFromInput) {
    var updatedName = JSON.stringify(jsonFromInput);
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            console.log(this.responseText);
        }
    });

    xhr.open("PUT", document.URL + "getList");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(updatedName);
}

function processEditInput(newContent, jsonFromInput) {
    var newWords = newContent.split(" ");

    if (!isNaN(newWords[length - 1])) {
        alert("Amount must be a number");
        return false;
    }
    if (newWords.length === 1) {
        jsonFromInput.product = newWords[0];
        jsonFromInput.amount = "";
        return true;
    }
    if (newWords.length === 2) {
        jsonFromInput.product = newWords[0];
        jsonFromInput.amount = newWords[1];
        return true;

    }
    if (newWords.length === 3) {
        jsonFromInput.product = newWords[0] + " " + newWords[1];
        jsonFromInput.amount = newWords[2];
        return true;
    }
    if (newWords.length === 4) {
        jsonFromInput.product = newWords[0] + " " + newWords[1] + newWords[2];
        jsonFromInput.amount = newWords[3];
        return true;
    } else {
        alert("No more than THREE words can be used for product");
        return false;
    }
}


function validateInput(product, amount) {
    if (product === "") {
        alert("Product must be filled out");
        return false;
    }
    if (amount === "") {
        alert("Amount must be filled out");
        return false;
    }
    if (product.split(' ').length > 3) {
        alert("No more than THREE words can be used for product");
        return false;
    }
    if (isNaN(amount)) {
        alert("Enter a number");
        return false;
    }
    return true;
}


function createJsonFromUserRegistrationForm() {
    var paramUserName = document.getElementById("registrationName").value;
    var paramEmail = document.getElementById("registrationEmail").value;
    var paramPassword = document.getElementById("registrationPassword").value;
    var userInfoJson = {"username": paramUserName, "email": paramEmail, "password": paramPassword};
    saveNewUser(userInfoJson);
}


function saveNewUser(userInfoJson) {
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

function OnPostResponseForSaveUser() {
    if (this.readyState === 4) {
        if (this.status === 200) {
            console.log("Response is " + this.responseText);
            displayNameIsTaken(this.responseText);
        }
    }
}

function displayNameIsTaken(responseText) {
    if (responseText === "false") {
        document.getElementById("userNameIsTakenWarning").style.visibility = 'visible';

    }

    if (responseText === "invalidEmail") {
        document.getElementById("invalidEmail").style.visibility = 'visible';
    }

    if (responseText === "shortPassword") {
        document.getElementById("shortPassword").style.visibility = 'visible';
    }

    if (responseText === "true") {
        document.getElementById("regForm").innerHTML = "You are registered";
        document.getElementById("regButton").style.visibility = 'hidden';
    }

}


function getLoginInfo() {
    console.log("getLoginInfo is called...");
    var username = document.getElementById("defaultForm-username").value;
    var password = document.getElementById("defaultForm-pass").value;
    var userAndPassword = {"username": username, "password": password};
    logIn(userAndPassword);
}

function logIn(userAndPassword) {

    var data = null;
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", OnPostResponseForLogIn);
    xhr.open("GET", document.URL + "getUser?username="+userAndPassword.username+"&password="+userAndPassword.password);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send(data);
}

function OnPostResponseForLogIn() {
    console.log("OnPostResponseForLogIn...");

    if (this.readyState === 4) {
        if (this.status === 200) {
            console.log("Response is " + this.responseText);
            displayWrongUsernameOrPassword(this.responseText);
        }
    }
}

function displayWrongUsernameOrPassword(responseText) {
    if (responseText === "Wrong Username") {
        document.getElementById("wrongUsername").style.visibility = 'visible';
    }
    if (responseText === "Wrong Password") {
        document.getElementById("wrongPassword").style.visibility = 'visible';
    }
    if (responseText !== "Wrong Username" && responseText !== "Wrong Password") {
        document.getElementById("logInForm").innerHTML = "You are logged in";
        document.getElementById("logInButton").style.visibility = 'hidden';
        var jsonFromResponseText = JSON.parse(responseText);
        console.log(jsonFromResponseText.id);
        userId = jsonFromResponseText.id;
        getAllListNames();
    }
}






