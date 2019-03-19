import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import com.google.gson.Gson;

@WebServlet(name = "GetItemsById", urlPatterns = {"/getItemsById"})
public class GetItemsByListId extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ListCollectionPOJO list = new Gson().fromJson(RequestReader.readRequest(request), ListCollectionPOJO.class);
            int id = list.getId();

            ShoppingListDAO shoppingListDAO = new ShoppingListDAO();
            String json = new Gson().toJson(shoppingListDAO.getAllItemsByParentListId(id));
            ResponseWriter.writeResponse(response, json);
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

}