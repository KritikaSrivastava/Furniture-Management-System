package Main;

/**
 * [...]
 * This class is the main method to instantiate the project and  bring it up
 *
 * @author  Priyanka Awaraddi
 */
import view.FMS_Layout;

public class FurnitureManagmentSystem {
	public static void main(String[] args) {
		try
		{
			FMS_Layout layout = new FMS_Layout();
			layout.set_main_layout("Furniture Managment System");
		}
		catch(Exception e)
		{
			System.out.println("Error with DB connection");
			e.printStackTrace();
		}
	}
}
