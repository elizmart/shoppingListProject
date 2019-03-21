import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestReader {

    public static String readRequest(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuff = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line = null;
        while ((line = reader.readLine()) != null)
            jsonBuff.append(line);
        return jsonBuff.toString();
    }


    public static String readParameterisedRequest(HttpServletRequest req, HttpServletResponse res, String paramName) throws IOException {

        String paramValue = req.getParameter(paramName);

        if (paramValue == null) {
            System.out.println("Parameter " + paramName + " not found");
            res.sendError(400);
        }
        return paramValue;
    }
}