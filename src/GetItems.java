import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.google.gson.Gson;

@WebServlet(name = "GetItems", urlPatterns = {"/getItems"})
public class GetItems extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String parameter = RequestReader.readParameterisedRequest(request,response, "parentList");
            int parentListId = Integer.parseInt(parameter);
            ShoppingListDAO shoppingListDAO = new ShoppingListDAO();
            String json = new Gson().toJson(shoppingListDAO.getAllItemsByParentListId(parentListId));
            ResponseWriter.writeResponse(response, json);
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ShoppingListPOJO item = new Gson().fromJson(RequestReader.readRequest(request), ShoppingListPOJO.class);
            ShoppingListDAO shoppingListDAO = new ShoppingListDAO();
            shoppingListDAO.addItem(item);

            String json = new Gson().toJson(item);
            ResponseWriter.writeResponse(response, json);
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws SecurityException, IOException {
        try {
            ShoppingListPOJO item = new Gson().fromJson(RequestReader.readRequest(request), ShoppingListPOJO.class);
            int id = item.getId();

            try {
                ShoppingListDAO shoppingListDAO = new ShoppingListDAO();
                shoppingListDAO.deleteItemById(id);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws SecurityException, IOException {
        try {
            ShoppingListPOJO item = new Gson().fromJson(RequestReader.readRequest(request), ShoppingListPOJO.class);
            try {
                ShoppingListDAO shoppingListDAO = new ShoppingListDAO();
                shoppingListDAO.updateItem(item);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }
}