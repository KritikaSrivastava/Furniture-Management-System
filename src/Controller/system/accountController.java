package Controller.system;

import model.Credential;
import java.sql.SQLException;

import Database.DbOperationHelper;
/**
 * [...]
 * This class is used as a controller to login and authorization functionality 
 *
 * @author  Priyanka Awaraddi
 */

public class accountController {
	
	public Credential createCredential(String userName,String password) throws SQLException 
	{
		Credential credentialObj = new Credential(userName,password);
		return credentialObj;
	}
	
	public boolean loginValidation(Credential creObj) throws SQLException 
	{
		boolean result = DbOperationHelper.validateLogin(creObj);
		if(result)
		{
			DbOperationHelper.setUserName(creObj.getUserName());
		}
		return result;
	}
	
	public boolean register(Credential creObj) throws SQLException 
	{
		boolean result = DbOperationHelper.register(creObj);
		return result;
	}
	
}
