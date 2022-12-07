package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author Miles
 */
public class Utilities {
    public static byte[] LoadResource(String name) {
        try {
            return Files.readAllBytes(Paths.get(CometBooks.LOCAL_SOURCES_DIR + '/' + name));
        }
        catch (IOException ioe) {
            System.err.println("Error loading resource: " + ioe);
            System.err.println("CWD: " + System.getProperty("user.dir"));
        }
        return "Resource unavailable".getBytes();
    }
    public static HashMap<String, String> ProcessRequestTokens(String request) {
        HashMap<String, String> result = new HashMap<>();
        String[] query = request.split("\\?");
        if(query.length <= 1)
            return result;
        String[] keyValuePairs = query[query.length - 1].split("&");
        for(String pair : keyValuePairs) {
            String[] tokens = pair.split("=");
            result.put(tokens[0], tokens.length > 1 ? tokens[1] : "");
        }
        return result;
    }
    public static byte[] ProcessHTMLTemplate(String htmlFileName, String... params) {
        String result = new String(LoadResource(htmlFileName), Charset.forName("UTF-8"));
        for(int i = 0; i < params.length; i++)
            result = result.replace("%" + (i + 1), params[i]);
        return result.getBytes();
    }
    public static void RedirectToPage(HttpExchange he, String URI) {
        he.getResponseHeaders().set("Location", URI);
        try {
            he.sendResponseHeaders(302, -1);
        }
        catch(IOException ioe) {}
    }
}
