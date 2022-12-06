package CometBooks;

import com.sun.net.httpserver.HttpServer;

import java.net.InetAddress;
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
    public static final String RESOURCE_FOLDER_NAME = "/resources",
            LOCAL_SOURCES_DIR = "../src/main/java/CometBooks" + RESOURCE_FOLDER_NAME,
            MAIN_PAGE_NAME = "/main",
            LISTING_PAGE_NAME = "/listing";
    public static final LoginPageHandler LOGIN_PAGE = new LoginPageHandler();
    public static final ResourceRequestHandler RESOURCES = new ResourceRequestHandler();
    public static final MainPageHandler MAIN_PAGE = new MainPageHandler();
    public static final DummyUTDGalaxy UTD_GALAXY = new DummyUTDGalaxy();
    public static final ListingPageHandler LISTING_PAGE = new ListingPageHandler();
    
    public static void main(String[] args) {
        try {
            HttpServer server;
            server = HttpServer.create(new InetSocketAddress(81), 0);
            server.createContext("/", LOGIN_PAGE);
            server.createContext(RESOURCE_FOLDER_NAME, RESOURCES);
            server.createContext(MAIN_PAGE_NAME, MAIN_PAGE);
            server.createContext(LISTING_PAGE_NAME, LISTING_PAGE);
            server.setExecutor(null);
            server.start();
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
}
