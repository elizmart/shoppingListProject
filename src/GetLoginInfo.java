import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetUser", urlPatterns = {"/getLogin"})
public class GetLoginInfo extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserPOJO user = new Gson().fromJson(RequestReader.readRequest(request), UserPOJO.class);
            String name = user.getUsername();
            String password = user.getPassword();

            UserDAO userDAO = new UserDAO();
            List<String> listOfNames;
            listOfNames = userDAO.getAllUserNames();

            if (!listOfNames.contains(name)) {
                ResponseWriter.writeStringResponse(response, "Wrong Username");
            } else {
                String actualPassword = userDAO.getUserByName(name).getPassword();
                if (!actualPassword.equals(password)) {
                    ResponseWriter.writeStringResponse(response, "Wrong Password");
                } else

                ResponseWriter.writeStringResponse(response, "AllowLogIn");
            }

        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }
}
