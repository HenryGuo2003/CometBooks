package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
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
    public static String ConvertRequestTokensToURI(HashMap<String, String> queryPairs) {
        if(queryPairs.isEmpty())
            return "";
        String result = "?";
        for(String key : queryPairs.keySet())
            result += key + "=" + queryPairs.get(key) + "&";
        return result.substring(0, result.length() - 1);
    }
    public static byte[] ProcessHTMLTemplate(String htmlFileName, String... params) { return ProcessHTMLTemplateString(htmlFileName, params).getBytes(); }
    public static String ProcessHTMLTemplateString(String htmlFileName, String... params) {
        String result = new String(LoadResource(htmlFileName), Charset.forName("UTF-8"));
        for(int i = params.length - 1; i >= 0; i--)
            result = result.replace("%" + (i + 1), params[i]);
        return result;
    }
    public static void RedirectToPage(HttpExchange he, String URI) {
        he.getResponseHeaders().set("Location", URI);
        try { he.sendResponseHeaders(302, -1); }
        catch(IOException ioe) {}
    }
    public static void SendHttpResponse(HttpExchange he, int responseCode, byte[] response) {
        try {
            he.sendResponseHeaders(responseCode, response.length);
            OutputStream os = he.getResponseBody();
            os.write(response);
            os.close();
        }
        catch (IOException ioe) {} // Ignored because the try block contents here will often noisily throw "waiting to send" style errors that aren't real problems
    }
    public static long ExtractAccessToken(HashMap<String, String> queryPairs) {
        if(!queryPairs.containsKey(CometBooks.ACCESS_TOKEN_NAME)) // You are not logged in to the mo fun zone, if you will
            return 0;
        long accessToken;
        try {
            accessToken = Long.parseLong(queryPairs.get(CometBooks.ACCESS_TOKEN_NAME));
        } catch(NumberFormatException nfe) {
            return 0;
        }
        return accessToken;
    }
}
