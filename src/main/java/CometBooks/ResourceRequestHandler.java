package CometBooks;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Miles
 */
public class ResourceRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        String target = t.getRequestURI().toString();
        if(target.contains("..")) //Malicious: attempting to leave the root working directory
            target = "";
        if (target.startsWith(CometBooks.RESOURCE_FOLDER_NAME))
            target = target.substring(CometBooks.RESOURCE_FOLDER_NAME.length() + 1); //get rid of the preceeding '/' because LoadResource will add it
        byte[] response = Utilities.LoadResource(target);
        t.sendResponseHeaders(200, response.length);
        OutputStream os = t.getResponseBody();
        os.write(response);
        os.close();
    }
}
