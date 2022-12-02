package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 *
 * @author Miles
 */
public class LoginPageHandler implements HttpHandler {
    public static final String USERNAME_INPUT_FIELD_NAME = "netID", PASSWORD_INPUT_FIELD_NAME = "password";
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        String req = he.getRequestURI().toString();
        if (req.contains("?")) { //Process an inbound login request form
            HashMap<String, String> queryPairs = Utilities.ProcessRequestTokens(req);
            if (queryPairs.containsKey(USERNAME_INPUT_FIELD_NAME) && queryPairs.containsKey(PASSWORD_INPUT_FIELD_NAME)) {
                if(CometBooks.UTD_GALAXY.IsValid(queryPairs.get(USERNAME_INPUT_FIELD_NAME), queryPairs.get(PASSWORD_INPUT_FIELD_NAME)))
                    Utilities.RedirectToPage(he, CometBooks.MAIN_PAGE_NAME + "?netID=admin");
                else
                    LoadPage(he, "Invalid login details");
            }
            else
                LoadPage(he, "Invalid query");
        }
        else //Load the page fresh
            LoadPage(he, "");
    }
    public void LoadPage(HttpExchange he, String errMsg) {
        byte[] response = Utilities.ProcessHTMLTemplate("login.html", USERNAME_INPUT_FIELD_NAME, PASSWORD_INPUT_FIELD_NAME, errMsg);
        try {
            he.sendResponseHeaders(200, response.length);
            OutputStream os = he.getResponseBody();
            os.write(response);
            os.close();
        }
        catch (IOException ioe) {} // Ignored because the try block contents here will often noisily throw "waiting to send" style errors that aren't real problems
    }
}
