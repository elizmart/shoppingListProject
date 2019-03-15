import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseWriter {

    public static void writeResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    public static void writeStringResponse(HttpServletResponse response, String text) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(text);
    }

}
