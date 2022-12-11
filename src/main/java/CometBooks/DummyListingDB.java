package CometBooks;

import java.util.ArrayList;

public class DummyListingDB implements ISaleListingDB {
    private static final SaleListing[] DUMMY_LISTING_DB = new SaleListing[] {
        new SaleListing(1, "0131489062", 2, 75.0, ListingCondition.NEAR_MINT, "Great value!"),
        new SaleListing(2, "0131489062", 2, 69.0, ListingCondition.LIGHTLY_USED, "In good condition"),
        new SaleListing(3, "0131489062", 2, 35.0, ListingCondition.HEAVILY_USED, "Cover torn; lots of markings in pages"),
        new SaleListing(4, "0321267974", 2, 45.0, ListingCondition.LIGHTLY_USED, "My personal copy, took good care of it"),
        new SaleListing(5, "9780470848319", 2, 150.0, ListingCondition.NEAR_MINT, "Contains test answers")
    };

    @Override
    public SaleListing[] getListingsFor(String ISBN) {
        ArrayList<SaleListing> result = new ArrayList<>();
        for(SaleListing sl : DUMMY_LISTING_DB)
            if(sl.getISBN().equals(ISBN))
                result.add(sl);
        SaleListing[] resultAsArray = new SaleListing[result.size()];
        return result.toArray(resultAsArray);
    }

    @Override
    public boolean changeHoldStatus(SaleListing listing) {
        for(int i = 0; i < DUMMY_LISTING_DB.length; i++) {
            if(DUMMY_LISTING_DB[i].getID() == listing.getID()) {
                DUMMY_LISTING_DB[i] = listing;
                return true;
            }
        }
        return false;
    }
    
}
