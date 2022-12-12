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
        
        //Get the listing the user clicked on if possible
        if (!queryPairs.containsKey(BookDetailsPageHandler.LISTING_ID_QUERY_TOKEN)) {
            Utilities.RedirectToPage(he, "/"); //@TODO: Probably want an error here or something
            return;
        }
        long listingID = 0;
        try {
            listingID = Long.parseLong(queryPairs.get(BookDetailsPageHandler.LISTING_ID_QUERY_TOKEN));
        } catch(NumberFormatException nfe) {
            Utilities.RedirectToPage(he, "/"); //@TODO: Probably want an error here or something
            return;
        }
        SaleListing listing = CometBooks.SALE_LISTING_DB.getListingByID(listingID);
        if(listing == null) {
            Utilities.RedirectToPage(he, "/"); //@TODO: Probably want an error here or something
            return;
        }
        
        //Get the book that the listing corresponds to for display purposes
        Book b = CometBooks.UTD_GALAXY.getBookByISBN(listing.getISBN());
        if(b == null) {
            Utilities.RedirectToPage(he, "/"); //@TODO: Probably want an error here or something
            return;
        }
        
        //Generate the page and send it off
        HashMap<String, String> backQuery = new HashMap<>();
        backQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        backQuery.put(ListingPageHandler.ISBN_QUERY_TOKEN, listing.getISBN());
        HashMap<String, String> buyQuery = new HashMap<>();
        buyQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        buyQuery.put(BookDetailsPageHandler.LISTING_ID_QUERY_TOKEN, queryPairs.get(BookDetailsPageHandler.LISTING_ID_QUERY_TOKEN));
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("buypage.html",
                b.imageName,
                b.name,
                "Edition: " + b.edition,
                "Author: " + b.author,
                "ISBN: " + b.ISBN,
                CometBooks.BOOK_DETAILS_PAGE_NAME + Utilities.ConvertRequestTokensToURI(backQuery),
                Double.toString(listing.getPrice()),
                CometBooks.UTD_GALAXY.getStudentName(listing.getSellerID()),
                listing.getCondition().toString(),
                listing.getDescription(),
                listing.isOnHold() ? "" : CometBooks.CONFIRM_PAGE_NAME + Utilities.ConvertRequestTokensToURI(buyQuery),
                listing.isOnHold() ? "ON HOLD" : "BUY BOOK",
                listing.isOnHold() ? "959595" : "d65f30"));
    }
}
