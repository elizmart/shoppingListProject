import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetLogin", urlPatterns = {"/getLogin"})
public class GetLoginInfo extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            UserPOJO user = new Gson().fromJson(RequestReader.readRequest(request), UserPOJO.class);
            String name = user.getUsername();
            String password = user.getPassword();
            String actualPassword = null;


            UserDAO userDAO = new UserDAO();
            UserPOJO userPOJO = userDAO.getUserByName(name);
            if (userPOJO == null) {
                ResponseWriter.writeStringResponse(response, "Wrong Username");
            } else {
                actualPassword = userPOJO.getPassword();
                if (!actualPassword.equals(password)) {
                    ResponseWriter.writeStringResponse(response, "Wrong Password");
                } else {
                    ResponseWriter.writeResponse(response, new Gson().toJson(userPOJO));
                }
            }
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }
}
