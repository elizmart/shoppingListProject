import javax.servlet.http.HttpServletRequest;
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
}
