package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class TransactionsPageHandler implements HttpHandler {
    public static final TransactionsPageHandler SINGLETON = new TransactionsPageHandler();
    
    private TransactionsPageHandler(){}

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
        
        //Generate buyer transaction templates
        String body = "";
        for(SaleListing sl : CometBooks.SALE_LISTING_DB.getBuyerListings(accessToken)) {
            Book b = CometBooks.UTD_GALAXY.getBookByISBN(sl.getISBN());
            if(b == null)
                continue;
            HashMap<String, String> transactionDetailsQuery = new HashMap<>();
            transactionDetailsQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
            transactionDetailsQuery.put(BookDetailsPageHandler.LISTING_ID_QUERY_TOKEN, Long.toString(sl.getID()));
            body += Utilities.ProcessHTMLTemplateString("transactiontemplate.html", 
                    b.imageName,
                    Double.toString(sl.getPrice()),
                    CometBooks.UTD_GALAXY.getStudentName(sl.getSellerID()),
                    b.name,
                    CometBooks.TRANSACTION_DETAILS_PAGE_NAME + Utilities.ConvertRequestTokensToURI(transactionDetailsQuery));
        }
        
        HashMap<String, String> browseRedirQuery = new HashMap<>();
        browseRedirQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("transactions.html",
                CometBooks.LISTING_PAGE_NAME + Utilities.ConvertRequestTokensToURI(browseRedirQuery), body));
    }
    
}
