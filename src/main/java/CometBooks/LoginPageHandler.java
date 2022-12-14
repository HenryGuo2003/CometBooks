package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Miles
 */
public class LoginPageHandler implements HttpHandler {
    public static final String USERNAME_INPUT_FIELD_NAME = "netID", PASSWORD_INPUT_FIELD_NAME = "password";
    public static final LoginPageHandler SINGLETON = new LoginPageHandler();
    
    private LoginPageHandler() {}
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        String req = he.getRequestURI().toString();
        HashMap<String, String> queryPairs = Utilities.ProcessRequestTokens(req);
        if (!queryPairs.isEmpty()) { //Process an inbound login request form
            if (queryPairs.containsKey(USERNAME_INPUT_FIELD_NAME) && queryPairs.containsKey(PASSWORD_INPUT_FIELD_NAME)) {
                long accessToken = CometBooks.UTD_GALAXY.login(queryPairs.get(USERNAME_INPUT_FIELD_NAME), queryPairs.get(PASSWORD_INPUT_FIELD_NAME));
                if(accessToken != 0)
                    Utilities.RedirectToPage(he, CometBooks.MAIN_PAGE_NAME + "?" + CometBooks.ACCESS_TOKEN_NAME + "=" + accessToken);
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
        Utilities.SendHttpResponse(he, 200, Utilities.ProcessHTMLTemplate("login.html", USERNAME_INPUT_FIELD_NAME, PASSWORD_INPUT_FIELD_NAME, errMsg));
    }
}
