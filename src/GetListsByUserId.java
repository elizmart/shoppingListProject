import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

@WebServlet(name = "GetListsByUserId", urlPatterns = {"/getListsByUserId"})
public class GetListsByUserId extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String req = RequestReader.readRequest(request);
            System.out.println(req + ": request successful");
            int userId = Integer.parseInt(req);
            ListCollectionDAO listCollectionDAO = new ListCollectionDAO();
            String json = new Gson().toJson(listCollectionDAO.getAllListsByUserId(userId));
            ResponseWriter.writeResponse(response, json);
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
 }
}