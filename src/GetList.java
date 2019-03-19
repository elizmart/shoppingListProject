import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

@WebServlet(name = "GetList", urlPatterns = {"/getList"})
public class GetList extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ListCollectionPOJO newList = new Gson().fromJson(RequestReader.readRequest(request), ListCollectionPOJO.class);
            ListCollectionDAO listCollectionDAO = new ListCollectionDAO();
            System.out.println(newList.getId());
            listCollectionDAO.addList(newList);


            String json = new Gson().toJson(newList);
            ResponseWriter.writeResponse(response, json);
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws SecurityException, IOException {
        try {
            ListCollectionPOJO listToDelete = new Gson().fromJson(RequestReader.readRequest(request), ListCollectionPOJO.class);
            int id = listToDelete.getId();

            try {
                ShoppingListDAO shoppingListDAO = new ShoppingListDAO();
                shoppingListDAO.deleteAllItemsByParentListId(id);

                ListCollectionDAO listCollectionDAO = new ListCollectionDAO();
                listCollectionDAO.deleteListById(id);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws SecurityException, IOException {
        try {
            ListCollectionPOJO listToUpdate = new Gson().fromJson(RequestReader.readRequest(request), ListCollectionPOJO.class);
            System.out.println(listToUpdate.getName());
            try {
                ListCollectionDAO listCollectionDAO = new ListCollectionDAO();
                listCollectionDAO.updateListName(listToUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }
}