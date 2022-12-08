package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

public class ListingPageHandler implements HttpHandler {
    public static final ListingPageHandler SINGLETON = new ListingPageHandler();
    
    private ListingPageHandler() {}
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        //Process inputs. Requires the user be logged in via an access token
        String req = he.getRequestURI().toString();
        System.out.println(req);
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
        
        //Get user's schedule
        Schedule s = CometBooks.UTD_GALAXY.getStudentSchedule(accessToken);
        Course[] courses;
        String body = "";
        if(s == null) 
            courses = new Course[]{};
        else
            courses = s.requestCourseList();
        
        //Generate the row html layout template for each book in each course of the user's schedule and add it to the page body
        for(Course c : courses) {
            for(Book b : c.requestBookList()) {
                HashMap<String, String> returnQuery = new HashMap<String, String>();
                returnQuery.put(CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString);
                returnQuery.put("selection", b.ISBN);
                body += Utilities.ProcessHTMLTemplateString("salelistingbooktemplate.html", b.imageName, b.name, b.author, c.semester, c.code, c.name, 
                        CometBooks.LISTING_PAGE_NAME + Utilities.ConvertRequestTokensToURI(returnQuery));
            }
        }
        
        //Generate the page and send it off
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("listingpage.html", CometBooks.ACCESS_TOKEN_NAME, accessTokenAsString, body));
    }
}
