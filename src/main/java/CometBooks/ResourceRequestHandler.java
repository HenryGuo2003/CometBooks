package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 *
 * @author Miles
 */
public class ResourceRequestHandler implements HttpHandler {
    public static final ResourceRequestHandler SINGLETON = new ResourceRequestHandler();
    
    private ResourceRequestHandler() {}
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        String target = he.getRequestURI().toString();
        if(target.contains("..")) //Malicious: attempting to leave the root working directory
            target = "";
        if (target.startsWith(CometBooks.RESOURCE_FOLDER_NAME))
            target = target.substring(CometBooks.RESOURCE_FOLDER_NAME.length() + 1); //get rid of the preceeding '/' because LoadResource will add it
        Utilities.SendHttpResponse(he, 200, Utilities.LoadResource(target));
    }
}
