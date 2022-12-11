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
        
        //Generate the page and send it off
        HashMap<String, String> backQuery = new HashMap<>();
        backQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("bookdetails.html", targetBook.imageName, targetBook.name, 
                "Edition: " + targetBook.edition, "Author: " + targetBook.author, "ISBN: " + targetBook.ISBN, CometBooks.LISTING_PAGE_NAME + Utilities.ConvertRequestTokensToURI(backQuery)));
    }
}
