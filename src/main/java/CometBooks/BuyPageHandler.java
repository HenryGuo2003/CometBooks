package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class BuyPageHandler implements HttpHandler {
    public static final BuyPageHandler SINGLETON = new BuyPageHandler();
    
    private BuyPageHandler() {}
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        //Process inputs. Requires the user be logged in via an access token
        String req = he.getRequestURI().toString();
        HashMap<String, String> queryPairs = Utilities.ProcessRequestTokens(req);
        long accessToken = Utilities.ExtractAccessToken(queryPairs);
        if(accessToken == 0) {
            Utilities.RedirectToPage(he, "/");
            return;
        }
        String accessTokenAsString = Long.toString(accessToken);
        
        //Generate the page and send it off
        HashMap<String, String> backQuery = new HashMap<>();
        backQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("buypage.html"));
    }
}
