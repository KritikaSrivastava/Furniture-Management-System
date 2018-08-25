package Controller.system;

import model.Cart;
import model.util.Constant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import Database.DbOperationHelper;
/**
 * [...]
 * This class is used as a controller to achieve rent functionality 
 *
 * @author  Priyanka Awaraddi
 */
public class rentController
{
	public ResultSet getSearchResults(String Key)
	{
		return DbOperationHelper.retriveRentSearchResults(Key);
	}	
	
	public void addToCart(Map<Integer,Integer> itemIdQuantity) throws SQLException
	{
		Cart cartObj = DbOperationHelper.createCart();
		cartObj = DbOperationHelper.getCartItems(cartObj, DbOperationHelper.getUserId());
		for(Map.Entry<Integer, Integer> entry : itemIdQuantity.entrySet())
		{
			if(cartObj.getCartItemList().containsKey(entry.getKey()) &&
					cartObj.getCartItemList().get(entry.getKey()).getQuantity() != entry.getValue())
			{
				cartObj.getCartItemList().get(entry.getKey()).setQuantity(entry.getValue());
			}
			else
			{
				cartObj.addCartLineItem(entry.getKey(),DbOperationHelper.getUserId(),entry.getValue(),false);
			}
		}
		DbOperationHelper.saveCart(cartObj, DbOperationHelper.getUserId());
	}
}
