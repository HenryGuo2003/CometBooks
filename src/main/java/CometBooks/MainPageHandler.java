package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 *
 * @author Miles
 */
public class MainPageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        LoadPage(he);
    }
    
    public void LoadPage(HttpExchange he) {
        String req = he.getRequestURI().toString();
        String body = "";
        if(req.contains("Browse"))
            body = "browse.mp4";
        else if (req.contains("Sell"))
            body = "sell.mp4";
        else if (req.contains("Cart"))
            body = "cart.mp4";
        else if (req.contains("Bookmarks"))
            body = "bookmarks.mp4";
        else if (req.contains("Messages"))
            body = "messages.mp4";
        else
            body = "mainpage.mp4";
        byte[] response = Utilities.ProcessHTMLTemplate("mainpage.html", "resources/" + body);
        try {
            he.sendResponseHeaders(200, response.length);
            OutputStream os = he.getResponseBody();
            os.write(response);
            os.close();
        }
        catch (IOException ioe) {} // Ignored because the try block contents here will often noisily throw "waiting to send" style errors that aren't real problems
    }
}
