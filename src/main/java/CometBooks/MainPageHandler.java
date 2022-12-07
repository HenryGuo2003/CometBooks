package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Miles
 */
public class MainPageHandler implements HttpHandler {
    public static final MainPageHandler SINGLETON = new MainPageHandler();
    
    private MainPageHandler() {}
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        //Process inputs. Requires the user be logged in via an access token
        String req = he.getRequestURI().toString();
        HashMap<String, String> queryPairs = Utilities.ProcessRequestTokens(req);
        if(!queryPairs.containsKey(CometBooks.ACCESS_TOKEN_NAME)) { // You are not logged in to the mo fun zone, if you will
            Utilities.RedirectToPage(he, "/");
            return;
        }
        
        //Handle button presses if they occurred
        if(queryPairs.containsKey("Browse.x")) { //The .x is an artifact added by browsers when the user clicks on an input-image (detailing the x coord of the click; there's also a y)
            Utilities.RedirectToPage(he, CometBooks.LISTING_PAGE_NAME + "?" + CometBooks.ACCESS_TOKEN_NAME + "=" + queryPairs.get(CometBooks.ACCESS_TOKEN_NAME));
            return;
        }
        if(queryPairs.containsKey("Cart.x")) {
            Utilities.RedirectToPage(he, req); //@TODO: implement cart page
            return;
        }
        
        //Deliver the default page
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("mainpage.html", CometBooks.ACCESS_TOKEN_NAME, queryPairs.get(CometBooks.ACCESS_TOKEN_NAME)));
    }
}
