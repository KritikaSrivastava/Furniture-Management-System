package Controller.system;

import Database.DbOperationHelper;
import model.Item;
/**
 * [...]
 * This class is used as a controller to achieve Sell functionality 
 *
 * @author  Priyanka Awaraddi
 */

public class sellController
{
	
	public boolean addItem(String item_name, String user_name, int price, String category, int quantity,
            String description, String post_date, boolean forSale, boolean forRent,
            String rentStartDate, String rentEndDate)
	{
		Item itemObj = DbOperationHelper.createItem(item_name, user_name, price, category, quantity, 
				 description, post_date, quantity, forSale, forRent,rentStartDate,rentEndDate);
		boolean insertSuccesfully = DbOperationHelper.insertItem(itemObj);
		return insertSuccesfully;
		
	}
	
}
