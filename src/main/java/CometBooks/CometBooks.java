package CometBooks;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/**
 *
 * @author Joel
 * 
 * The entry point for the CometBooks server application. Responsible for
 * creating and providing public access to all the singleton implementations
 * for each page and static resource, as well as the runtime locations of
 * static files.
 */
public class CometBooks {
    public static final String RESOURCE_FOLDER_NAME = "/resources",
            LOCAL_SOURCES_DIR = "../src/main/java/CometBooks" + RESOURCE_FOLDER_NAME,
            MAIN_PAGE_NAME = "/main", ACCESS_TOKEN_NAME = "accessToken", LISTING_PAGE_NAME = "/listing",
            BOOK_DETAILS_PAGE_NAME = "/details", BUY_PAGE_NAME = "/buy", CONFIRM_PAGE_NAME = "/confirm",
            TRANSACTIONS_PAGE_NAME = "/transactions", TRANSACTION_DETAILS_PAGE_NAME = "/transdetails";
    public static final IUTDGalaxy UTD_GALAXY = new DummyUTDGalaxy();
    public static final ISaleListingDB SALE_LISTING_DB = new DummyListingDB();
    
    public static void main(String[] args) {
        try {
            HttpServer server;
            server = HttpServer.create(new InetSocketAddress(81), 0);
            server.createContext("/", LoginPageHandler.SINGLETON);
            server.createContext(RESOURCE_FOLDER_NAME, ResourceRequestHandler.SINGLETON);
            server.createContext(MAIN_PAGE_NAME, MainPageHandler.SINGLETON);
            server.createContext(LISTING_PAGE_NAME, ListingPageHandler.SINGLETON);
            server.createContext(BOOK_DETAILS_PAGE_NAME, BookDetailsPageHandler.SINGLETON);
            server.createContext(BUY_PAGE_NAME, BuyPageHandler.SINGLETON);
            server.createContext(CONFIRM_PAGE_NAME, ConfirmPageHandler.SINGLETON);
            server.createContext(TRANSACTIONS_PAGE_NAME, TransactionsPageHandler.SINGLETON);
            server.createContext(TRANSACTION_DETAILS_PAGE_NAME, TransactionDetailsPageHandler.SINGLETON);
            server.setExecutor(null);
            server.start();
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
}
