import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

@WebServlet(name = "GetUser", urlPatterns = {"/getUser"})
public class GetUser extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            String req = RequestReader.readRequest(request);
//            System.out.println(req + ": request successful");
//            ListCollectionDAO listCollectionDAO = new ListCollectionDAO();
//            String json = new Gson().toJson(listCollectionDAO.getAllLists());
//            ResponseWriter.writeResponse(response, json);
//        } catch (Exception e) {
//            response.sendError(400, e.getMessage());
//        }
    }




    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            UserPOJO newUser = new Gson().fromJson(RequestReader.readRequest(request), UserPOJO.class);
            String userName = newUser.getUsername();
            String email = newUser.getEmail();
            String password = newUser.getPassword();

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
}

