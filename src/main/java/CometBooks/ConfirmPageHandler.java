package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class ConfirmPageHandler  implements HttpHandler {
    public static final ConfirmPageHandler SINGLETON = new ConfirmPageHandler();
    
    private ConfirmPageHandler() {}

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
        if(listing == null || listing.isOnHold()) { //Can't reserve a listing that doesn't exist or is already on hold
            Utilities.RedirectToPage(he, "/"); //@TODO: Probably want an error here or something
            return;
        }
        
        //Place the book on hold
        listing.changeHoldStatus(accessToken);
        CometBooks.SALE_LISTING_DB.changeHoldStatus(listing);
        
        //Generate & send the confirmation page
        HashMap<String, String> listingQuery = new HashMap<>();
        listingQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("confirm.html",
                CometBooks.UTD_GALAXY.getStudentName(listing.getSellerID()),
                CometBooks.UTD_GALAXY.getStudentNumber(listing.getSellerID()),
                CometBooks.LISTING_PAGE_NAME + Utilities.ConvertRequestTokensToURI(listingQuery)));
    }
    
}
