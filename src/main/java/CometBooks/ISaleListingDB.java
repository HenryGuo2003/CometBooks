package CometBooks;

public interface ISaleListingDB {
    SaleListing[] getListingsFor(String ISBN);
    boolean changeHoldStatus(SaleListing listing);
}