package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class BookDetailsPageHandler implements HttpHandler {
    public static final BookDetailsPageHandler SINGLETON = new BookDetailsPageHandler();
    
    private BookDetailsPageHandler() {}
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        //Process inputs. Requires the user be logged in via an access token
        String req = he.getRequestURI().toString();
        HashMap<String, String> queryPairs = Utilities.ProcessRequestTokens(req);
        if(!queryPairs.containsKey(CometBooks.ACCESS_TOKEN_NAME)) { // You are not logged in to the mo fun zone, if you will
            Utilities.RedirectToPage(he, "/");
            return;
        }
        long accessToken;
        try {
            accessToken = Long.parseLong(queryPairs.get(CometBooks.ACCESS_TOKEN_NAME));
        } catch(NumberFormatException nfe) {
            Utilities.RedirectToPage(he, "/");
            return;
        }
        String accessTokenAsString = Long.toString(accessToken);
        
        //Find the book via its ISBN and get its details
        //@TODO
        
        //Generate the page and send it off
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("bookdetails.html"));
    }
}
