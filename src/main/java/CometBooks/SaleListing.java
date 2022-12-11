package CometBooks;

public class SaleListing {
    private String ISBN, description;
    private long ID, seller, buyer;
    private double price;
    private ListingCondition condition;
    
    public SaleListing(long ID, String ISBN, long seller, double price, ListingCondition condition, String description) {
        this.ISBN = ISBN;
        this.description = description;
        this.ID = ID;
        this.seller = seller;
        this.price = price;
        this.condition = condition;
    }
    
    public boolean isOnHold() { return buyer != 0; }
    public void changeHoldStatus(long buyer) { this.buyer = buyer; }
    public String getISBN() { return ISBN; }
    public String getDescription() { return description; }
    public long getID() { return ID; }
    public long getSellerID() { return seller; }
    public long getBuyerID() { return buyer; }
    public double getPrice() { return price; }
    public ListingCondition getCondition() { return condition; }
}
