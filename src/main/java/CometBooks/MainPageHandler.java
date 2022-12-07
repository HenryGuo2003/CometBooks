package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Miles
 */
public class MainPageHandler implements HttpHandler {
    public static final MainPageHandler SINGLETON = new MainPageHandler();
    
    private MainPageHandler() {}
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        String req = he.getRequestURI().toString();
        HashMap<String, String> queryPairs = Utilities.ProcessRequestTokens(req);
        if(!queryPairs.containsKey(CometBooks.ACCESS_TOKEN_NAME)) // You are not logged in to the mo fun zone, if you will
            Utilities.RedirectToPage(he, "/");
        String body = "";
        if(req.contains("Browse")) {
            Utilities.RedirectToPage(he, CometBooks.LISTING_PAGE_NAME + "?netID=admin");
            return;
        }
        else if (req.contains("Sell"))
            body = "sell.mp4";
        else if (req.contains("Cart"))
            body = "cart.mp4";
        else if (req.contains("Bookmarks"))
            body = "bookmarks.mp4";
        else if (req.contains("Messages"))
            body = "messages.mp4";
        else
            body = "mainpage.mp4"; // @TODO: Should probably be an error, rather than this
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
