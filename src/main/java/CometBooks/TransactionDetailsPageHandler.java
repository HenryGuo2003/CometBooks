package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class TransactionDetailsPageHandler implements HttpHandler {
    public static final String CANCEL_HOLD_QUERY = "cancel";
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
        
        //Get the listing
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
        Book b = CometBooks.UTD_GALAXY.getBookByISBN(listing.getISBN());
        if(b == null) {
            Utilities.RedirectToPage(he, "/"); //@TODO: Probably want an error here or something
            return;
        }
        
        //Process a cancel request, if one occurred
        HashMap<String, String> backQuery = new HashMap<>();
        backQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        if(queryPairs.containsKey(CANCEL_HOLD_QUERY)) {
            if(listing.getBuyerID() != accessToken) { //Can't cancel a hold we don't own
                Utilities.RedirectToPage(he, "/"); //@TODO: Probably want an error here or something
                return;
            }
            listing.changeHoldStatus(0); //0 is the invalid ID specifier; indicates no hold
            CometBooks.SALE_LISTING_DB.changeHoldStatus(listing);
            Utilities.RedirectToPage(he, CometBooks.TRANSACTIONS_PAGE_NAME + Utilities.ConvertRequestTokensToURI(backQuery));
            return;
        }
        
        //Render the page
        HashMap<String, String> cancelQuery = new HashMap<>();
        cancelQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        cancelQuery.put(BookDetailsPageHandler.LISTING_ID_QUERY_TOKEN, queryPairs.get(BookDetailsPageHandler.LISTING_ID_QUERY_TOKEN));
        cancelQuery.put(CANCEL_HOLD_QUERY, "true");
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("transactiondetails.html",
                b.imageName,
                b.name,
                "Edition: " + b.edition,
                "Author: " + b.author,
                "ISBN: " + b.ISBN,
                CometBooks.TRANSACTIONS_PAGE_NAME + Utilities.ConvertRequestTokensToURI(backQuery),
                Double.toString(listing.getPrice()),
                CometBooks.UTD_GALAXY.getStudentName(listing.getSellerID()),
                listing.getCondition().toString(),
                listing.getDescription(),
                CometBooks.TRANSACTION_DETAILS_PAGE_NAME + Utilities.ConvertRequestTokensToURI(cancelQuery)));
    }
}
