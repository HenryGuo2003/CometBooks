package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class BookDetailsPageHandler implements HttpHandler {
    public static String LISTING_ID_QUERY_TOKEN = "ListingID";
    public static final BookDetailsPageHandler SINGLETON = new BookDetailsPageHandler();
    
    private BookDetailsPageHandler() {}
    
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
        
        //Find the book via its ISBN and get its details
        if(!queryPairs.containsKey(ListingPageHandler.ISBN_QUERY_TOKEN)) { // The query is invalid; we need a book ISBN to display
            Utilities.RedirectToPage(he, "/"); // @TODO: This should probably... render an error, or something.
            return;
        }
        String ISBN = queryPairs.get(ListingPageHandler.ISBN_QUERY_TOKEN);
        Book targetBook = CometBooks.UTD_GALAXY.getBookByISBN(ISBN);
        if(targetBook == null) {
            Utilities.RedirectToPage(he, "/"); // @TODO: As above, so below.
            return;
        }
        
        //Find all the listings associated with the book and generate display templates for them all
        String body = "";
        for(SaleListing sl : CometBooks.SALE_LISTING_DB.getListingsFor(ISBN)) {
            HashMap<String, String> buyQuery = new HashMap();
            buyQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
            buyQuery.put(LISTING_ID_QUERY_TOKEN, Long.toString(sl.getID()));
            body += Utilities.ProcessHTMLTemplateString("detailedsalelistingtemplate.html", 
                    Long.toString(sl.getID()), 
                    Double.toString(sl.getPrice()), 
                    CometBooks.UTD_GALAXY.getStudentName(sl.getSellerID()), 
                    sl.getCondition().toString(),
                    sl.isOnHold() ? "resources/onhold.PNG" : "resources/buy.PNG", 
                    sl.isOnHold() ? "ON HOLD" : "BUY",
                    sl.isOnHold() ? "" : CometBooks.BUY_PAGE_NAME + Utilities.ConvertRequestTokensToURI(buyQuery));
        }
        
        //Generate the page and send it off
        HashMap<String, String> backQuery = new HashMap<>();
        backQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("bookdetails.html", targetBook.imageName, targetBook.name, 
                "Edition: " + targetBook.edition, "Author: " + targetBook.author, "ISBN: " + targetBook.ISBN, CometBooks.LISTING_PAGE_NAME + Utilities.ConvertRequestTokensToURI(backQuery), body));
    }
}
