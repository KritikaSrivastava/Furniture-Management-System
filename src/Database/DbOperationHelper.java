package Database;

/**
 * [...]
 * This class used to execute and build queries to access data from controller to database
 *
 * @author  Priyanka Awaraddi
 */
import model.Cart;
import model.Credential;
import model.Cart.cartLineItem;
import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DbOperationHelper {
    private static DB_management dbObj = new DB_management();
    private static String userName = "";
    
    public static void setUserName(String uName)
    {
    	userName = uName;
    }
    
    public static int getUserId() throws SQLException
    {
    	String sql = "select * from user where user_name = " +
				 "\"" +userName + "\"" + ";";
    	int userId = 0;
		ResultSet rsResult = DbOperationHelper.execute(sql);
		try
		{
			if (rsResult.next())
			{
				userId = rsResult.getInt(1);			
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return userId;
    }
    
    public static boolean register (Credential creObj) throws SQLException
	{
		String psUserName=creObj.getUserName();
		String psPassword=creObj.getPassword();
		
		String sql = "select * from user where user_name = " +
					 "\"" +psUserName + "\"" + ";";
		
		ResultSet rsResult = DbOperationHelper.execute(sql);
		try
		{
			if (rsResult.next())
			{
				return false;
			}
			
			else
			{
				String insertSql = "INSERT INTO user (user_name, Fname, Lname, password) values ('" + 
			    psUserName + "', " + "'none', " +  "'none', '" + psPassword + "');";
			    return DbOperationHelper.insertUser(insertSql);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return false;
		}
		
	}
    
    public static boolean validateLogin (Credential creObj) throws SQLException
	{
		String psUserName=creObj.getUserName();
		String psPassword=creObj.getPassword();
		
		String sql = "select * from user where user_name = " +
					 "\"" +psUserName + "\"" + "and password = "+
					 "\"" +psPassword + "\"" + ";";
		
		ResultSet rsResult = DbOperationHelper.execute(sql);

		try
		{
			if (rsResult.next())
			{
				return true;
			}
			
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return false;
		}
		
	}
    
    public static void saveCart(Cart cart, int userId) throws SQLException
    {
    	String sql = " delete from cart where user_id = "+ userId +";";
        dbObj.updateOrDelete(sql);
        
    	for(Map.Entry<Integer, cartLineItem> entry : cart.getCartItemList().entrySet())
    	{
    		sql = " Insert into cart(user_id, item_id, quantity, forSale) values( "+
                    " "+entry.getValue().getUserId()+","+
                    " "+entry.getValue().getItemId()+","+
                    " "+entry.getValue().getQuantity()+","+
                    " "+entry.getValue().isForSale()+");";
            dbObj.insert(sql);
    	}
    }
    
    public static Item getItem(int itemId) throws SQLException
    {
    	String sql = " select * from item where item_id = "+
                ""+itemId+";";
        ResultSet rs = dbObj.execute(sql);
        Item item = null;
        if(rs.next())
        {
        	item = createItem(rs.getString(2),
         		   rs.getString(3),
         		   rs.getInt(4),
         		   rs.getString(5),
         		   rs.getInt(6),
         		   rs.getString(7),
         		   rs.getString(8),
         		   rs.getInt(9),
         		   rs.getBoolean(10),
         		   rs.getBoolean(11),
         		   rs.getString(12),
         		   rs.getString(13));
        }
        return item;
    }
    
    public static void deleteCartObject(int itemId, int userId) throws SQLException
    {
        String sql = " delete from cart where item_id = "+ itemId+" and user_id = "+ userId +";";
        dbObj.updateOrDelete(sql);
    }

    public static ResultSet getCartItems() throws SQLException
    {
        String sql = " select * from cart where user_id = "+
                ""+DbOperationHelper.getUserId()+";";
        return dbObj.execute(sql);
    }
    
    public static Item createItem(String itemName, String userName, int price, String category, int quantity,
            String description, String postDate, int availability, boolean forSale, boolean forRent,
            String rentStartDate, String rentEndDate)
    {
    	Item itemobj = new Item();
		itemobj.setItem_name(itemName);
		itemobj.setUser_name(userName);
		itemobj.setPrice(price);
		itemobj.setCategory(category);
		itemobj.setQuantity(quantity);
		itemobj.setDescription(description);
		itemobj.setPost_date(postDate);
		itemobj.setAvailability(availability);
		itemobj.setForSale(forSale);
		itemobj.setForRent(forRent);
		itemobj.setRentStartDate(rentStartDate);
		itemobj.setRentEndDate(rentEndDate);
		return itemobj;
    }
    
    public static Cart createCart()
    {
    	return new Cart();
    }
    
    public static Cart getCartItems(Cart c, int userId) throws SQLException
    {
    	String sql = " select * from cart where user_id = "+
                ""+userId+";";
        ResultSet rs = dbObj.execute(sql);
        while(rs.next())
		{
        	cartLineItem cartItemsObj = c.new cartLineItem(rs.getInt("item_id"),
        										 rs.getInt("user_id"),
        										 rs.getInt("quantity"),
        										 rs.getBoolean("forSale"));
        	c.getCartItemList().put(rs.getInt("item_id"),cartItemsObj);
		}
    	return c;
    }
    
    public static Cart getCart() throws SQLException
    {
    	Cart cartObj = DbOperationHelper.createCart();
		cartObj = DbOperationHelper.getCartItems(cartObj, DbOperationHelper.getUserId());
		return cartObj;
    }
    
    public static void updateCart(Cart cart, int userId) throws SQLException
    {
    	for(Map.Entry<Integer, cartLineItem> entry : cart.getCartItemList().entrySet())
    	{
    		if(entry.getValue() == null)
    		{
    			String sql = " delete from cart where item_id = "+ entry.getKey()+" and user_id = "+ userId +";";
                dbObj.updateOrDelete(sql);
    		}
    	}
    }
    
    public static void archieveCart(Cart cart) throws SQLException
    {
    	for(Map.Entry<Integer, cartLineItem> entry : cart.getCartItemList().entrySet())
    	{
    		String sql = " delete from cart where item_id = "+ entry.getKey()+" and user_id = "+ entry.getValue().getUserId() +";";
            dbObj.updateOrDelete(sql);
    	}
    	for(Map.Entry<Integer, cartLineItem> entry : cart.getCartItemList().entrySet())
    	{
    		String sql = " Insert into checkout(user_id, item_id, quantity, forSale) values( "+
                    " "+entry.getValue().getUserId()+","+
                    " "+entry.getValue().getItemId()+","+
                    " "+entry.getValue().getQuantity()+","+
                    " "+entry.getValue().isForSale()+");";
            dbObj.insert(sql);
    	}
    }

    public static boolean insertItem(Item item)
    {
    	boolean insertSuccesfully = false;
        String sql = " Insert into item(item_name, user_name,price, category,"+
                " quantity, description, post_date,availability, forsale,forrent, rentstartdate, rentenddate)"+
                " values( "+
                " '"+item.getItem_name()+"',"+
                " '"+item.getUser_name()+"',"+
                " "+item.getPrice()+","+
                " '"+item.getCategory()+"',"+
                " "+item.getQuantity()+","+
                " '"+item.getDescription()+"',"+
                " '"+item.getPost_date()+"',"+
                " "+item.getAvailability()+","+
                " "+item.isForSale()+","+
                " "+item.isForRent()+","+
                " '"+item.getRentStartDate()+"',"+
                " '"+item.getRentEndDate()+"' "+
                ")";
        try {
            dbObj.insert(sql);
            insertSuccesfully = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
        	return insertSuccesfully;
        }
    }
    
    public static ResultSet retriveSaleSearchResults(String Key)
	{
		ResultSet rs=null;
		String sql ="";
		if (Key.equals ("Search") || Key.equals (""))
		{
			sql	= "select item_id,item_name, price,availability from item "
					+ "where availability > 0 and forSale = true" ;
		}
		else
		{
			sql	= "select item_id,item_name, price,availability from item "
					+ "where availability>0 and forSale = true and item_name like '"+Key+"'"+
					" and description like '"+Key+"'";
		}
		try 
		{
			rs = dbObj.execute(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return rs;
	}
    
    public static ResultSet retriveRentSearchResults(String Key)
	{
		ResultSet rs=null;
		String sql ="";
		if (Key.equals ("Search") || Key.equals (""))
		{
			sql	= "select item_id,item_name, price,availability,rentStartDate, rentEndDate from item "
					+ "where availability >0 and forSale = false" ;
		}
		else
		{
			sql	= "select item_id,item_name, price,availability,rentStartDate, rentEndDate from item "
					+ "where availability>0 and forSale = false and item_name like '"+Key+"'"+
					" and description like '"+Key+"'";
		}
		try 
		{
			rs = dbObj.execute(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return rs;
	}


    public static void updateItemQuantity(int itemId, int quatityTaken) throws SQLException
    {
        String sql = " update item set availability = quantity - "+quatityTaken+
                " where item_id = "+itemId+";";
        dbObj.updateOrDelete(sql);
    }

    public static ResultSet execute(String sql) throws SQLException 
    {
        return dbObj.execute(sql);
    }
    
    public static boolean insertUser(String sql)
    {
    	boolean insertSuccesfully = false;
        try {
            dbObj.insert(sql);
            insertSuccesfully = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
        	return insertSuccesfully;
        }
    }
}
