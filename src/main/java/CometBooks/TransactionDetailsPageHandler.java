package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class TransactionDetailsPageHandler implements HttpHandler {
    public static final TransactionDetailsPageHandler SINGLETON = new TransactionDetailsPageHandler();
    
    private TransactionDetailsPageHandler() {}

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
        
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("transactiondetails.html"));
    }
}
