package model;
/**
 * [...]
 * This class used to create and modify Item entity
 *
 * @author  Priyanka Awaraddi
 */

public class Item {
	private int item_id;
	private String item_name;
	private String user_name;
	private int price;
	private String category;
	private int quantity;
	private String description;
	private String post_date;
	private boolean forSale;
	private boolean forRent;
	private int availability;
	private String rentStartDate;
	private String rentEndDate;

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}

	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}

	public void setForRent(boolean forRent) {
		this.forRent = forRent;
	}

	public void setRentStartDate(String rentStartDate) {
		this.rentStartDate = rentStartDate;
	}

	public void setRentEndDate(String rentEndDate) {
		this.rentEndDate = rentEndDate;
	}
	
	public void setAvailability(int availability) {
		this.availability = availability;
	}
	
    public Item(String item_name, String user_name, int price, String category, int quantity,
                String description, String post_date, boolean forSale, boolean forRent, int availability,
                String rentStartDate, String rentEndDate) {
        this.item_name = item_name;
        this.user_name = user_name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.description = description;
        this.post_date = post_date;
        this.forSale = forSale;
        this.forRent = forRent;
        this.availability = availability;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
    }
    
    public Item() {
        
    }
    
	public int getItem_id() {
		return item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public int getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getDescription() {
		return description;
	}

	public String getPost_date() {
		return post_date;
	}

	public boolean isForSale() {
		return forSale;
	}

	public boolean isForRent() {
		return forRent;
	}

	public int getAvailability() {
		return availability;
	}

	public String getRentStartDate() {
		return rentStartDate;
	}

	public String getRentEndDate() {
		return rentEndDate;
	}
	
	
}
