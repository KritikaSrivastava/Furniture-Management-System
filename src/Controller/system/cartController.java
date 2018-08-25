package Controller.system;

import model.Cart;
import model.Item;
import model.Cart.cartLineItem;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Database.DbOperationHelper;
/**
 * [...]
 * This class is used as a controller to achieve checkout and cart handling functionality 
 *
 * @author  Priyanka Awaraddi
 */
public class cartController
{
	public Cart getCartData() throws SQLException
	{
		Cart cartObj = DbOperationHelper.getCart();
		cartObj = validateCartDetails(cartObj);
		return cartObj;
	}
	
	public Cart validateCartDetails(Cart cart) throws SQLException
	{
		for(Map.Entry<Integer, cartLineItem> entry : cart.getCartItemList().entrySet())
    	{
    		Item itemObj = DbOperationHelper.getItem(entry.getKey());
    		if(itemObj.getAvailability()<entry.getValue().getQuantity())
    		{
    			entry.getValue().setQuantity(itemObj.getAvailability());
    		}
    	}
		for(Map.Entry<Integer, cartLineItem> entry : cart.getCartItemList().entrySet())
    	{
    		if(entry.getValue().getQuantity()==0)
    		{
    			cart.cartItemList.remove(entry.getKey());
    		}
    	}
		return cart;
	}
	
	public void removeItemsFromCart(List<Integer> itemIdList) throws SQLException
	{
		Cart cartObj = DbOperationHelper.getCart();
		for(int itemId : itemIdList)
    	{
			cartObj.removeCartLineItem(itemId);
    	}
		DbOperationHelper.updateCart(cartObj, DbOperationHelper.getUserId());
	}
	
	public boolean checkoutCart() throws SQLException
	{
		Cart cartObj = DbOperationHelper.getCart();
		for(Map.Entry<Integer, cartLineItem> entry : cartObj.getCartItemList().entrySet())
    	{
			DbOperationHelper.updateItemQuantity(entry.getKey(),entry.getValue().getQuantity());
    	}
		DbOperationHelper.archieveCart(cartObj);
		return true;
	}
}
