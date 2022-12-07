package CometBooks;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/**
 *
 * @author Miles
 * 
 * The entry point for the CometBooks server application. Responsible for
 * creating and providing public access to all the singleton implementations
 * for each page and static resource, as well as the runtime locations of
 * static files.
 */
public class CometBooks {
    public static final String RESOURCE_FOLDER_NAME = "/resources", LOCAL_SOURCES_DIR = "../src/main/java/CometBooks" + RESOURCE_FOLDER_NAME,
            MAIN_PAGE_NAME = "/main", ACCESS_TOKEN_NAME = "accessToken";
    public static final IUTDGalaxy UTD_GALAXY = new DummyUTDGalaxy();
    
    public static void main(String[] args) {
        try {
            HttpServer server;
            server = HttpServer.create(new InetSocketAddress(81), 0);
            server.createContext("/", LoginPageHandler.SINGLETON);
            server.createContext(RESOURCE_FOLDER_NAME, ResourceRequestHandler.SINGLETON);
            server.createContext(MAIN_PAGE_NAME, MainPageHandler.SINGLETON);
            server.setExecutor(null);
            server.start();
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
}
