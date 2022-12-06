package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Yuchen
 */
public class ListingPageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        LoadPage(he);
    }
    
    public void LoadPage(HttpExchange he) {
        String req = he.getRequestURI().toString();
        String body = "";
        byte[] response = Utilities.ProcessHTMLTemplate("listingpage.html"); // @TODO: Extract the response try/catch logic into utilities...? It gets copy+pased around a lot
        try {
            he.sendResponseHeaders(200, response.length);
            OutputStream os = he.getResponseBody();
            os.write(response);
            os.close();
        }
        catch (IOException ioe) {} // Ignored because the try block contents here will often noisily throw "waiting to send" style errors that aren't real problems
    }
}
