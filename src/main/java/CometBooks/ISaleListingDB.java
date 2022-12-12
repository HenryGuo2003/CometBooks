package CometBooks;

public interface ISaleListingDB {
    SaleListing[] getListingsFor(String ISBN);
    SaleListing[] getBuyerListings(long buyerID);
    boolean changeHoldStatus(SaleListing listing);
    SaleListing getListingByID(long id);
}
