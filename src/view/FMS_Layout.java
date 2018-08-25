package view;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import Controller.system.accountController;
import Controller.system.buyController;
import Controller.system.cartController;
import Controller.system.rentController;
import Controller.system.sellController;
import Database.DB_management;
import net.proteanit.sql.DbUtils;
import model.util.Constant;
import model.Cart;
import model.Credential;
import model.Cart.cartLineItem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class FMS_Layout extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private	JTabbedPane	tabbedPane;
	
	private	int	iScreenWidth;
	private	int	iScreenHeight;
	private	Dimension screenSize;
	private	JTable tableForSearchBuy;
	private	JTable tableForSearchRent;
	private JScrollPane	scrollPaneForSearchBuy;
	private JScrollPane	scrollPaneForSearchRent;
	private	JTextField	tfSearchBuy;
	private	JTextField	tfSearchRent;
	private JButton	bSearchBuy;
	private JButton	bSearchRent;
	private JButton	bAddToCartBuy;
	private JButton	bAddToCartRent;
	private	JButton	bCheckout;
	private	JButton	bremoveFromCart;
	private	JTable	tableForCart;
	private JScrollPane	scrollPaneForCart;
	// Login related artifacts - START
	
		
	private	boolean	bFlag;
	private JFrame	frame;
	private JFrame	Loginframe;
	// Login related artifacts - END
	private sellController sellControllerObj;
	private buyController buyControllerObj;
	private rentController rentControllerObj;
	private cartController cartControllerObj;
	private accountController accountControllerObj;
	
	private Map<Integer,Integer> itemIdQuantityForBuy;
	private Map<Integer,Integer> itemIdQuantityForRent;
	private List<Integer> listItemIdToRemove;
	public DB_management dbObj = null;
	
	private JPanel sellPanel;
	private JPanel rentPanel;
	private JPanel buyPanel;
	private JPanel cartPanel;
	
	public FMS_Layout ()
	{	
		screenSize	= Toolkit.getDefaultToolkit().getScreenSize();	
		iScreenWidth	= screenSize.width;
		iScreenHeight	= screenSize.height;
		itemIdQuantityForBuy = new  HashMap<Integer,Integer>();
		itemIdQuantityForRent = new  HashMap<Integer,Integer>();
		listItemIdToRemove = new ArrayList<Integer>();
		
		sellControllerObj = new sellController();
		buyControllerObj = new buyController();
		rentControllerObj = new rentController();
		cartControllerObj = new cartController();
		accountControllerObj = new accountController();
	}
	
	class BgPanel extends JPanel {
	    Image bg = new ImageIcon("resources//4.png").getImage();
	    @Override
	    public void paintComponent(Graphics g) {
	        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	    }
	}
	class customPanel extends JPanel {
	    Image bg = new ImageIcon("resources//5.png").getImage();
	    @Override
	    public void paintComponent(Graphics g) {
	        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	    }
	}

	
	public void set_main_layout (String pTitle) throws SQLException
	{
		frame = new JFrame (pTitle);
		Loginframe = new JFrame("Login");
		
		frame.getContentPane().setLayout (new GridLayout(1, 1));
		frame.setSize (iScreenWidth, iScreenHeight);
		frame.setDefaultCloseOperation (EXIT_ON_CLOSE);
		
		Loginframe.getContentPane().setLayout (new GridLayout(1, 1));
		Loginframe.setBounds(0, 0, iScreenWidth-50, iScreenHeight-50);
		
		
		tabbedPane = (new JTabbedPane (JTabbedPane.TOP));
		
		Loginframe.add("Furniture Managment System - Login", makeLoginPanel ("Furniture Managment System"));
		Loginframe.setVisible(true);
		frame.setVisible(false);
				
		sellPanel = makeSellPanel ("Sell");
		rentPanel =  makeRentPanel ("Buy");
		buyPanel =   makeBuyPanel("Rent");
		cartPanel = makeCartPanel ("Cart");
		tabbedPane.add ("Sell", sellPanel);
		tabbedPane.add ("Buy",buyPanel);
		tabbedPane.add ("Rent",rentPanel);
		tabbedPane.add ("Cart",cartPanel);
						
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 4, false);	// Sell Tab
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 3, false);	// Buy Tab
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 2, false);	// Rent Tab
		tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 1, false);	// model.Cart Tab
			
		bSearchBuy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				populateBuyGrid(buyControllerObj.getSearchResults(tfSearchBuy.getText()));
			}
		});
		
		bSearchRent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				populateRentGrid(rentControllerObj.getSearchResults(tfSearchRent.getText()));
			}
		});
		
		bAddToCartBuy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getSelectedBuyItems();
				try {
					buyControllerObj.addToCart(itemIdQuantityForBuy);
					populateCartGrid();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		bAddToCartRent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getSelectedRentalItems();
				try {
					rentControllerObj.addToCart(itemIdQuantityForRent);
					populateCartGrid();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		bremoveFromCart.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeSelectedItemsFromCart();
				try {
					cartControllerObj.removeItemsFromCart(listItemIdToRemove);
					populateCartGrid();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		//tableForSearchRent.addPropertyChangeListener();
		
		populateBuyGrid(buyControllerObj.getSearchResults(tfSearchBuy.getText()));
		populateRentGrid(rentControllerObj.getSearchResults(tfSearchBuy.getText()));
		populateCartGrid();
		frame.getContentPane().add (tabbedPane);
		//frame.setVisible (true);
	}
	
	private  JPanel makeLoginPanel (String pID)
	{	
		JPanel	panel	= new BgPanel ();
		panel.setLayout(null);
		panel.setBackground(Color.lightGray);
		
		JLabel title = new JLabel("Welcome To Furniture Management System");
		title.setFont(new Font("Chalkboard", 1, 28));
		title.setBounds(10, 30,800,100);
		
		JLabel	lLoginUserName = new JLabel("Enter UserName: ");
		lLoginUserName.setFont(new Font("Chalkboard", 1, 16));
		lLoginUserName.setBounds(50, 150, 300, 30);
		JLabel	lUserPassword = new JLabel("Enter Password: ");
		lUserPassword.setFont(new Font("Chalkboard", 1, 16));
		lUserPassword.setBounds(50, 200, 300, 30);
		
		JTextField	tfLoginUserName = new JTextField(iScreenWidth/40);
		tfLoginUserName.setBounds(250, 150, 300, 30);
		JPasswordField	tfLoginPassword= new JPasswordField(iScreenWidth/40);
		tfLoginPassword.setBounds(250, 200, 300, 30);
		
		JButton	bLogin = new JButton("Login");	
		bLogin.setBounds(250, 250, 150, 30);
		
		JButton	bRegister = new JButton("Register");	
		bRegister.setBounds(410, 250, 150, 30);
		
		tfLoginUserName.setVisible (true);
		tfLoginPassword.setVisible (true);
		
		panel.add(tfLoginUserName);
		panel.add(tfLoginPassword);
		panel.add(lLoginUserName);
		panel.add(lUserPassword);
		panel.add(bLogin);
		panel.add(bRegister);
		panel.add(title);
		bRegister.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{	
                    Credential creObj=new Credential(tfLoginUserName.getText(),String.valueOf (tfLoginPassword.getPassword()));
					
					bFlag = accountControllerObj.register (creObj);
					
					System.out.println("User Validated = " + bFlag);
					
					if (bFlag == true)
					{
						JOptionPane.showMessageDialog(frame, "registered successfully. Now you can login.");
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "The Username has already been registered.");
					}
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		bLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					bFlag = accountControllerObj.loginValidation(accountControllerObj.createCredential(tfLoginUserName.getText(),String.valueOf (tfLoginPassword.getPassword())));
                    
					if (bFlag == true)
					{
						Loginframe.setVisible(false);
						frame.setVisible(true);
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 4, true);	// Sell Tab
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 3, true);	// Buy Tab
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 2, true);	// Rent Tab
						tabbedPane.setEnabledAt (tabbedPane.getTabCount() - 1, true);	// model.Cart Tab
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "Credentials Invalid. Cannot Login to the system");
					}
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		
		return panel;
	}
	// Login related artifacts - END
	
	private void removeSelectedItemsFromCart()
	{
		listItemIdToRemove.clear();
		int [] iaIndeces;
		try
		{
			iaIndeces	= tableForCart.getSelectedRows();
			for (int iCount : iaIndeces)
			{
				try
				{
					int key = Integer.valueOf (tableForCart.getValueAt (iCount, Constant.ITEM_ID_INDEX).toString ());
					listItemIdToRemove.add(key);
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void  getSelectedBuyItems()
	{
		Integer	iReqQty	= 0;
		int [] iaIndeces;
		try
		{
			iaIndeces	= tableForSearchBuy.getSelectedRows();
			for (int iCount : iaIndeces)
			{
				try
				{
					iReqQty	= Integer.valueOf (tableForSearchBuy.getValueAt (iCount, Constant.REQ_QUANTITY_INDEX_BUY).toString ());
					if (iReqQty > 0)
					{
						int key = Integer.valueOf (tableForSearchBuy.getValueAt (iCount, Constant.ITEM_ID_INDEX).toString ());
						itemIdQuantityForBuy.put(key, iReqQty);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	private void  getSelectedRentalItems()
	{
		Integer	iReqQty	= 0;
		int [] iaIndeces;
		try
		{
			iaIndeces	= tableForSearchRent.getSelectedRows();
			for (int iCount : iaIndeces)
			{
				try
				{
					iReqQty	= Integer.valueOf (tableForSearchRent.getValueAt (iCount, Constant.REQ_QUANTITY_INDEX_RENT).toString ());
					if (iReqQty > 0)
					{
						int key = Integer.valueOf (tableForSearchRent.getValueAt (iCount, Constant.ITEM_ID_INDEX).toString ());
						itemIdQuantityForRent.put(key, iReqQty);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace ();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	private void populateCartGrid() throws SQLException
	{
		Cart cart =cartControllerObj.getCartData();
		DefaultTableModel	tableModel;
		tableModel = new DefaultTableModel();
		
		// Add required columns here
		// Quantity Required
		tableModel.addColumn ("ItemID");
		tableModel.addColumn ("Quantity");
		
		// Add the model to the table
		tableForCart.setModel (tableModel);
		
		for(Entry<Integer, cartLineItem> entry : cart.cartItemList.entrySet())
		{
			// Quantity Required column is set to zero
			Object[] rowData = new Object[2];
			rowData[0]=entry.getKey();
			rowData[1]=entry.getValue().getQuantity();
			
			tableModel.addRow(rowData);
		}
		tableForCart.setVisible(true);
	}
	
	private void populateBuyGrid(ResultSet rs)
	{
		DefaultTableModel	tableModel;
		int iRowCount;
		tableModel = (DefaultTableModel) DbUtils.resultSetToTableModel (rs);
		
		// Add required columns here
		// Quantity Required
		tableModel.addColumn ("Quantity Required");
		
		// Add the model to the table
		tableForSearchBuy.setModel (tableModel);
		
		tableForSearchBuy.getModel().addTableModelListener(new TableModelListener() {

		      public void tableChanged(TableModelEvent e) {
		         if(e.getColumn()==Constant.REQ_QUANTITY_INDEX_BUY)
		         {
		        	 int i =e.getLastRow();
		        	 Object avail_quantity =  tableForSearchBuy.getModel().getValueAt(i, 3);
		        	 Object req_quantity =  tableForSearchBuy.getModel().getValueAt(i, Constant.REQ_QUANTITY_INDEX_BUY);
		        	 if(req_quantity.getClass().equals(String.class))
		        	 {
		        		 if((int)avail_quantity<Integer.parseInt((String) req_quantity))
		        		 {
		        			 tableForSearchBuy.getModel().setValueAt(0, i, Constant.REQ_QUANTITY_INDEX_BUY);
			        		 JOptionPane.showMessageDialog(buyPanel, "Entered quantity is greater than available quantity"); 
		        		 }
		        	 }
		         }
		      }
		    });
		
		// Set the default value for the columns as required
		iRowCount	= tableForSearchBuy.getRowCount();
		
		System.out.println ("Num of rows returned - " + iRowCount);
		
		for (int iCount = 0; iCount < iRowCount; iCount++)
		{
			// Quantity Required column is set to zero
			tableModel.setValueAt (0, iCount, 4);
		}
		
		bAddToCartBuy.setVisible (true);
		tableForSearchBuy.setVisible (true);
		scrollPaneForSearchBuy.setVisible (true);
	}
	
	private void populateRentGrid(ResultSet rs)
	{
		DefaultTableModel	tableModel;
		int iRowCount;
		tableModel = (DefaultTableModel) DbUtils.resultSetToTableModel (rs);
		
		// Add required columns here
		// Quantity Required
		tableModel.addColumn ("Quantity Required");
		
		// Add the model to the table
		tableForSearchRent.setModel (tableModel);
		
		tableForSearchRent.getModel().addTableModelListener(new TableModelListener() {

		      public void tableChanged(TableModelEvent e) {
		         if(e.getColumn()==Constant.REQ_QUANTITY_INDEX_RENT)
		         {
		        	 int i =e.getLastRow();
		        	 Object avail_quantity =  tableForSearchRent.getModel().getValueAt(i, 3);
		        	 Object req_quantity =  tableForSearchRent.getModel().getValueAt(i, Constant.REQ_QUANTITY_INDEX_RENT);
		        	 if(req_quantity.getClass().equals(String.class))
		        	 {
		        		 if((int)avail_quantity<Integer.parseInt((String) req_quantity))
		        		 {
		        			 tableForSearchRent.getModel().setValueAt(0, i, Constant.REQ_QUANTITY_INDEX_RENT);
			        		 JOptionPane.showMessageDialog(rentPanel, "Entered quantity is greater than available quantity"); 
		        		 }
		        	 }
		         }
		      }
		    });
		
	
		// Set the default value for the columns as required
		iRowCount	= tableForSearchRent.getRowCount();
		
		System.out.println ("Num of rows returned - " + iRowCount);
		
		for (int iCount = 0; iCount < iRowCount; iCount++)
		{
			// Quantity Required column is set to zero
			tableModel.setValueAt (0, iCount, 6);
		}
		
		bAddToCartRent.setVisible (true);
		tableForSearchRent.setVisible (true);
		scrollPaneForSearchRent.setVisible (true);
	} 
	
	private  JPanel makeBuyPanel (String pID)
	{	
		JPanel	panelMain;
		JPanel	panelSearch;
		JPanel	panelTable;
		JPanel	panelBuyOperations;
		
		panelMain	= new JPanel ();
		panelMain.setLayout (new GridLayout(6,1));
		
		panelSearch	= new JPanel ();
		panelSearch.setLayout (new FlowLayout());
		tfSearchBuy =(new JTextField("Search", iScreenWidth/40));
		tfSearchBuy.setVisible (true);
		bSearchBuy		= new JButton("Search");
		bSearchBuy.setVisible (true);
		panelSearch.add (tfSearchBuy);
		panelSearch.add (bSearchBuy);
		
		// Table view Panel
		panelTable = new JPanel ();
		panelTable.setLayout (new GridLayout ());
		
		tableForSearchBuy = (new JTable ()
		{
			// Define the editable columns of the table here
			private static final long serialVersionUID = 1L;
					
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return column==Constant.REQ_QUANTITY_INDEX_BUY ? true : false;
		    }
		});
		
		scrollPaneForSearchBuy = (new JScrollPane (tableForSearchBuy));
		panelTable.add (scrollPaneForSearchBuy);
		tableForSearchBuy.setVisible (false);
		scrollPaneForSearchBuy.setVisible (false);
		
		// Add To model.Cart view Panel - Part of Buy Operations
		panelBuyOperations	= new JPanel ();
		panelBuyOperations.setLayout (new FlowLayout ());
		bAddToCartBuy = (new JButton("Add to Cart"));
		bAddToCartBuy.setVisible(false);
		panelBuyOperations.add (bAddToCartBuy);
		
		panelMain.add (panelSearch);
		panelMain.add (panelTable);
		panelMain.add (panelBuyOperations);

		return panelMain;
	}
	
	private  JPanel makeRentPanel (String pID)
	{	
		JPanel	panelMainRent;
		JPanel	panelSearchRent;
		JPanel	panelTableRent;
		JPanel	panelRentOperationsRent;
		
		panelMainRent	= new JPanel ();
		panelMainRent.setLayout (new GridLayout(6,1));
		
		// Add the elements here
		// Search Panel
		panelSearchRent	= new JPanel ();
		panelSearchRent.setLayout (new FlowLayout());
		tfSearchRent = new JTextField("Search", iScreenWidth/40);
		tfSearchRent.setVisible (true);
		bSearchRent		= new JButton("Search");
		bSearchRent.setVisible (true);
		panelSearchRent.add (tfSearchRent);
		panelSearchRent.add (bSearchRent);
		
		// Table view Panel
		panelTableRent		= new JPanel ();
		panelTableRent.setLayout (new GridLayout ());
		
		tableForSearchRent = (new JTable ()
		{
			// Define the editable columns of the table here
			private static final long serialVersionUID = 1L;
					
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return column==Constant.REQ_QUANTITY_INDEX_RENT ? true : false;
		    }
		});
		
		
		scrollPaneForSearchRent = (new JScrollPane (tableForSearchRent));
		panelTableRent.add (scrollPaneForSearchRent);
		tableForSearchRent.setVisible (false);
		scrollPaneForSearchRent.setVisible (false);
		
		// Add To model.Cart view Panel - Part of Rent Operations
		panelRentOperationsRent	= new JPanel ();
		panelRentOperationsRent.setLayout (new FlowLayout ());
		bAddToCartRent = (new JButton("Add To Cart"));
		bAddToCartRent.setVisible(false);
		panelRentOperationsRent.add (bAddToCartRent);
		
		panelMainRent.add (panelSearchRent);
		panelMainRent.add (panelTableRent);
		panelMainRent.add (panelRentOperationsRent);

		return panelMainRent;
	}
	
	// Function to set the layout of the window - Sell
	private  JPanel makeSellPanel (String pID)
	{
		JPanel	panel	= new customPanel ();
		panel.setLayout(null);
		JLabel title = new JLabel("Enter Details of Item to be Sold/Rented");
		title.setFont(new Font("Chalkboard", 1, 28));
		title.setBounds(400, -30,800,100);
		
		JLabel itemName  = new JLabel("Enter Item Name:");
		itemName.setBounds(10, 50, 300, 30);
		JLabel itemDesc = new JLabel("Enter Desciption:");
		itemDesc.setBounds(10, 100, 300, 30);
		JLabel price = new JLabel("Enter the Price:");
		price.setBounds(10, 150, 300, 30);
		JLabel category = new JLabel("Select Category");
		category.setBounds(10, 200, 300, 30);
		JLabel quantity = new JLabel("Enter quantity");
		quantity.setBounds(10, 250, 300, 30);
		
		JTextField itemNameText = new JTextField();
		itemNameText.setBounds(200, 50, 300, 30);
		JTextField itemDescText = new JTextField();
		itemDescText.setBounds(200, 100, 300, 30);
		JTextField priceText = new JTextField();
		priceText.setBounds(200, 150, 300, 30);
		JComboBox<String> cb = new JComboBox<String>(Constant.TYPES);
		cb.setBounds(200, 200, 300, 30);
		JTextField quantityText = new JTextField();
		quantityText.setBounds(200, 250, 300, 30);
		
		JRadioButton sale = new JRadioButton("Sale");
		sale.setBounds(100, 300, 100, 30);
		JRadioButton rent = new JRadioButton("Rent");
		rent.setBounds(200, 300, 100, 30);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(sale);
		buttonGroup.add(rent);
				
		JButton addItem = new JButton("ADD");
		addItem.setEnabled(true);
		addItem.setBounds(200, 450, 100, 30);
		
		JLabel toDateForRentText  = new JLabel("Enter Rent start date:");
		toDateForRentText.setBounds(10, 350, 300, 30);
		JLabel fromDateForRentText = new JLabel("Enter Rent End date:");
		fromDateForRentText.setBounds(10, 400, 300, 30);
		JTextField fromDateForRent = new JTextField();
		fromDateForRent.setBounds(200, 350, 100, 30);
		fromDateForRent.setVisible(false);
		JTextField toDateForRent = new JTextField();
		toDateForRent.setBounds(200, 400, 100, 30);
		toDateForRent.setVisible(false);
		toDateForRentText.setVisible(false);
		fromDateForRentText.setVisible(false);
		
		rent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toDateForRent.setVisible(true);
				fromDateForRent.setVisible(true);
				toDateForRentText.setVisible(true);
				fromDateForRentText.setVisible(true);
			}
		});
		
		sale.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toDateForRent.setVisible(false);
				fromDateForRent.setVisible(false);
				fromDateForRent.setText(null);
				toDateForRent.setText(null);
				toDateForRentText.setVisible(false);
				fromDateForRentText.setVisible(false);
			}
		});
		
		addItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				//method call to controller to create item 
				boolean insertSuccesfully=false;
				try {
					insertSuccesfully = sellControllerObj.addItem(itemNameText.getText(), 
										   "1", 
										   Integer.parseInt(priceText.getText()),
										   cb.getSelectedItem().toString(),
										   Integer.parseInt(quantityText.getText()),
										   itemDescText.getText(),
										   new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()),
										   sale.isSelected(),
										   rent.isSelected(),
										   fromDateForRent.getText(),
										   toDateForRent.getText());
					populateBuyGrid(buyControllerObj.getSearchResults(""));
					populateRentGrid(rentControllerObj.getSearchResults(""));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} 
				if(insertSuccesfully)
				{
					JOptionPane.showMessageDialog(panel, "Item Added Successfully.!!!");
				}
			}
		});

		panel.add(title);
		panel.add(itemName);
		panel.add(itemDesc);
		panel.add(price);
		panel.add(category);
		panel.add(quantity);
		panel.add(itemNameText);
		panel.add(itemDescText);
		panel.add(priceText);
		panel.add(cb);
		panel.add(quantityText);
		panel.add(addItem);
		panel.add(rent);
		panel.add(sale);
		
		panel.add(fromDateForRent);
		panel.add(toDateForRent);
		panel.add(toDateForRentText);
		panel.add(fromDateForRentText);
		
		return panel;
	}
	
	// Function to set the layout of the window - model.Cart
	private  JPanel makeCartPanel (String pID)
	{
		JPanel		panelMain;
		JPanel		panelTableCart;
		JPanel		panelCartOperations;
		
		panelMain	= new JPanel ();
		panelMain.setLayout (new GridLayout (6,1));
		
		// Add the elements here
		// Table view Panel
		panelTableCart = new JPanel ();
		panelTableCart.setLayout (new GridLayout());
		
		tableForCart =  (new JTable ()
		{
			// Define the editable columns of the table here
			private static final long serialVersionUID = 1L;
				
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
		    }		
		});
		
		scrollPaneForCart = (new JScrollPane (tableForCart));
		panelTableCart.add (scrollPaneForCart);
		tableForCart.setVisible (true);
		scrollPaneForCart.setVisible (true);
		
		panelCartOperations	= new JPanel ();
		panelCartOperations.setLayout (new FlowLayout());
		bCheckout =  (new JButton ("Checkout"));
		bCheckout.setVisible (true);
		panelCartOperations.add (bCheckout);
		bremoveFromCart = new JButton("Remove Items from Cart");
		bremoveFromCart.setVisible(true);
		panelCartOperations.add(bremoveFromCart);
		
		panelMain.add (panelTableCart);
		panelMain.add (panelCartOperations);
		
		bCheckout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					if(cartControllerObj.checkoutCart())
					{
						JOptionPane.showMessageDialog(panelMain, "Checkout Successfully.!!!");
					}
					else
					{
						JOptionPane.showMessageDialog(panelMain, "Checkout UnSuccessfully.!!!");
					}
					populateCartGrid();
					populateBuyGrid(buyControllerObj.getSearchResults(""));
					populateRentGrid(rentControllerObj.getSearchResults(""));
				} 
				catch (SQLException e2) 
				{
					e2.printStackTrace();
				}				
			}
		});
		
		return panelMain;
	}
}

