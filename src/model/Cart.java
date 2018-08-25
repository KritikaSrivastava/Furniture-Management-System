package model;

/**
 * [...]
 * This class used to create and modify cart entity
 *
 * @author  Priyanka Awaraddi
 */
import java.util.HashMap;
import java.util.Map;

public class Cart {
    
	public Map<Integer,cartLineItem> cartItemList;
	
	public Cart()
	{
		cartItemList = new HashMap<Integer,cartLineItem>();
	}
	public Map<Integer, cartLineItem> getCartItemList()
	{
		return cartItemList;
	}
	
	public void addCartLineItem(int itemId, int userId, int quantity, boolean forSale)
	{
		this.cartItemList.put(itemId, new cartLineItem(itemId, userId, quantity, forSale));
	}
	
	public void removeCartLineItem(int itemId)
	{
		this.cartItemList.put(itemId, null);
	}
	
	public class cartLineItem
	{
		private int itemId;
	    private int userId;
	    private int quantity;
	    private boolean forSale;
	    	    
	    public cartLineItem(int itemId, int userId, int quantity, boolean forSale) {
		    this.setItemId(itemId);
		    this.setUserId(userId);
		    this.setQuantity(quantity);
		    this.setForSale(forSale);
	    }

		public int getItemId() {
			return itemId;
		}

		public void setItemId(int itemId) {
			this.itemId = itemId;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public boolean isForSale() {
			return forSale;
		}

		public void setForSale(boolean forSale) {
			this.forSale = forSale;
		}
	}
}