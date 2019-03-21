import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

@WebServlet(name = "GetUser", urlPatterns = {"/getUser"})
public class GetUser extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = RequestReader.readParameterisedRequest(request, response, "username");
            String password = RequestReader.readParameterisedRequest(request, response, "password");
            String actualPassword = null;
            UserDAO userDAO = new UserDAO();
            UserPOJO userPOJO = userDAO.getUserByName(name);
            checkloginInfo(userPOJO, response, password);
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserPOJO newUser = new Gson().fromJson(RequestReader.readRequest(request), UserPOJO.class);
            String userName = newUser.getUsername();
            String email = newUser.getEmail();
            String password = newUser.getPassword();
            checkRegistrationInfo(response, newUser, userName, email, password);
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    private boolean userNameExists(String name) throws SQLException {
        UserDAO userDAO = new UserDAO();
        List<String> listOfNames = userDAO.getAllUserNames();
        return (listOfNames.contains(name));
    }

    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean passwordIsValid(String password){
        return (password.length()>4);
    }

    private void checkRegistrationInfo(HttpServletResponse response, UserPOJO newUser, String userName, String email, String password) throws SQLException, IOException {
        if (userNameExists(userName)){
            ResponseWriter.writeStringResponse(response, "false");
        }

        else  {
            if (!isValidEmailAddress(email)) {
                ResponseWriter.writeStringResponse(response, "invalidEmail");

            }

            else if (!passwordIsValid(password)){
                ResponseWriter.writeStringResponse(response, "shortPassword");
            }
            else {
                UserDAO userDAO = new UserDAO();
                userDAO.addUser(newUser);
                ResponseWriter.writeStringResponse(response, "true");
            }
        }
    }
    
    private void checkloginInfo(UserPOJO userPOJO, HttpServletResponse response, String password) throws IOException {
        if (userPOJO == null) {
            ResponseWriter.writeStringResponse(response, "Wrong Username");
        } else {
           String actualPassword = userPOJO.getPassword();
            if (!actualPassword.equals(password)) {
                ResponseWriter.writeStringResponse(response, "Wrong Password");
            } else {
                ResponseWriter.writeResponse(response, new Gson().toJson(userPOJO));
            }
        }
    }
}

