import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class modifyAirfaresPage {
	JFrame modifyAirfaresPageFrame;

	JLabel columnNameLabel;
	JLabel currentValueLabel;
	JLabel modifiedValueLabel;
	JLabel departureAirportIDLabel;
	JLabel destinationAirportIDLabel;
	JLabel departureTimeLabel;
	JLabel arrivalTimeLabel;
	JLabel airlineLabel;
	JLabel priceLabel;

	JTextField currentDepartureAirportIDTextField;
	JTextField currentDestinationAirportIDTextField;
	JTextField currentDepartureTimeTextField;
	JTextField currentArrivalTimeTextField;
	JTextField currentAirlineTextField;
	JTextField currentPriceTextField;

	JTextField ModifyDepartureAirportIDTextField;
	JTextField ModifyDestinationAirportIDTextField;
	JTextField ModifyDepartureTimeTextField;
	JTextField ModifyArrivalTimeTextField;
	JTextField ModifyAirlineTextField;
	JTextField ModifyPriceTextField;
	
	JButton backButton;
	JButton modifyButton;

    JPanel modifyAirfaresPagePanel;
    JDialog modifyAirfaresDialog;
    
    modifyAirfaresPage(int intModifyID) {
		final int HEIGHT = 350;
		final int WIDTH = 600;
		final int LocationX = 400;
		final int LocationY = 200;
		
		modifyAirfaresPageFrame = new JFrame();

		//
		// Adjust Labels
		//
		columnNameLabel = new JLabel("Column Name");
		currentValueLabel = new JLabel("Current Value");
		modifiedValueLabel = new JLabel("Modified Value");
		departureAirportIDLabel = new JLabel("DepartureAirportID");
		destinationAirportIDLabel = new JLabel("DestinationAirportID");
		departureTimeLabel = new JLabel("DepartureTime");
		arrivalTimeLabel = new JLabel("ArrivalTime");	
		airlineLabel = new JLabel("Airline");
		priceLabel = new JLabel("Price");

		columnNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		modifiedValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		departureAirportIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		destinationAirportIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		departureTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		arrivalTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		airlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust TextFields
		//
		currentDepartureAirportIDTextField = new JTextField(20);
		currentDestinationAirportIDTextField = new JTextField(20);
		currentDepartureTimeTextField = new JTextField(20);
		currentArrivalTimeTextField = new JTextField(20);
		currentAirlineTextField = new JTextField(20);
		currentPriceTextField = new JTextField(20);

		ModifyDepartureAirportIDTextField = new JTextField(20);
		ModifyDestinationAirportIDTextField = new JTextField(20);
		ModifyDepartureTimeTextField = new JTextField(20);
		ModifyArrivalTimeTextField = new JTextField(20);
		ModifyAirlineTextField = new JTextField(20);
		ModifyPriceTextField = new JTextField(20);

		currentDepartureAirportIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentDestinationAirportIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentDepartureTimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentArrivalTimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentAirlineTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentPriceTextField.setHorizontalAlignment(SwingConstants.CENTER);

		ModifyDepartureAirportIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyDestinationAirportIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyDepartureTimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyArrivalTimeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyAirlineTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyPriceTextField.setHorizontalAlignment(SwingConstants.CENTER);
	
		ModifyDepartureAirportIDTextField.setText(getDepartureAirportByAirfareID(intModifyID));
		ModifyDestinationAirportIDTextField.setText(getDestinationAirportByAirfareID(intModifyID));
		ModifyDepartureTimeTextField.setText(getDepartureTimeByID(intModifyID));
		ModifyArrivalTimeTextField.setText(getArrivalTimeByID(intModifyID));
		ModifyAirlineTextField.setText(getAirlineByID(intModifyID));
		ModifyPriceTextField.setText(getPriceByID(intModifyID));

		//
		// Current values should not be editable
		//
		currentDepartureAirportIDTextField.setEditable(false);
		currentDepartureAirportIDTextField.setText(getDepartureAirportByAirfareID(intModifyID));
		currentDestinationAirportIDTextField.setEditable(false);
		currentDestinationAirportIDTextField.setText(getDestinationAirportByAirfareID(intModifyID));
		currentDepartureTimeTextField.setEditable(false);
		currentDepartureTimeTextField.setText(getDepartureTimeByID(intModifyID));
		currentArrivalTimeTextField.setEditable(false);
		currentArrivalTimeTextField.setText(getArrivalTimeByID(intModifyID));
		currentAirlineTextField.setEditable(false);
		currentAirlineTextField.setText(getAirlineByID(intModifyID));
		currentPriceTextField.setEditable(false);
		currentPriceTextField.setText(getPriceByID(intModifyID));

		System.out.println(getPriceByID(intModifyID));
		//
		// Add buttons
		//
		backButton = new JButton("Back");
		modifyButton = new JButton("Apply Changes");

		//
		// Back button should go back to Main page
		//
		backButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				modifyAirfaresDialog.setVisible(false);
			}
		});

		//
		// Modify Air fare button
		//
		modifyButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String departureAirportID = ModifyDepartureAirportIDTextField.getText();
				String destinationAirportID = ModifyDestinationAirportIDTextField.getText();
				String departureTime = ModifyDepartureTimeTextField.getText();
				String arrivalTime = ModifyArrivalTimeTextField.getText();
				String airline = ModifyAirlineTextField.getText();
				String price = ModifyPriceTextField.getText();

				//
				// TextField items should be valid
				//
				if (validateAirfareParameters(departureAirportID, destinationAirportID, departureTime, 
						arrivalTime, airline, price)) {
					final String url = "jdbc:mysql://localhost:3306/airfare";
					final String username = "root";
					final String password = "s#MF!xh9";
					Connection connection = null;
					//
					// Register the airport into DB
					//
					try {
						connection = DriverManager.getConnection(url, username, password);
						
						Statement statement = null;
						String query = "UPDATE Airfares "
									 + "SET DepartureAirportID = \"" + departureAirportID + "\""
									 + ", DestinationAirportID = \"" + destinationAirportID + "\""
									 + ", DepartureTime = \"" + departureTime + "\""
									 + ", ArrivalTime = \"" + arrivalTime + "\""
									 + ", Airline = \"" + airline + "\""
									 + ", Price = \"" +  price + "\""
									 + " WHERE (FlightID = " + intModifyID + ")";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					modifyAirfaresDialog.setVisible(false);
				}
			}
		});

		//
		// Finalize Modify Airport Page Panel and Frame
		//
		modifyAirfaresPagePanel = new JPanel(new GridLayout(8, 3, 10, 10));
		modifyAirfaresPagePanel.add(columnNameLabel);
		modifyAirfaresPagePanel.add(currentValueLabel);
		modifyAirfaresPagePanel.add(modifiedValueLabel);
		modifyAirfaresPagePanel.add(departureAirportIDLabel);
		modifyAirfaresPagePanel.add(currentDepartureAirportIDTextField);
		modifyAirfaresPagePanel.add(ModifyDepartureAirportIDTextField);
		modifyAirfaresPagePanel.add(destinationAirportIDLabel);
		modifyAirfaresPagePanel.add(currentDestinationAirportIDTextField);
		modifyAirfaresPagePanel.add(ModifyDestinationAirportIDTextField);
		modifyAirfaresPagePanel.add(departureTimeLabel);
		modifyAirfaresPagePanel.add(currentDepartureTimeTextField);
		modifyAirfaresPagePanel.add(ModifyDepartureTimeTextField);
		modifyAirfaresPagePanel.add(arrivalTimeLabel);
		modifyAirfaresPagePanel.add(currentArrivalTimeTextField);
		modifyAirfaresPagePanel.add(ModifyArrivalTimeTextField);
		modifyAirfaresPagePanel.add(airlineLabel);
		modifyAirfaresPagePanel.add(currentAirlineTextField);
		modifyAirfaresPagePanel.add(ModifyAirlineTextField);
		modifyAirfaresPagePanel.add(priceLabel);
		modifyAirfaresPagePanel.add(currentPriceTextField);
		modifyAirfaresPagePanel.add(ModifyPriceTextField);
		modifyAirfaresPagePanel.add(backButton);
		modifyAirfaresPagePanel.add(new JLabel(""));
		modifyAirfaresPagePanel.add(modifyButton);

		modifyAirfaresPageFrame.add(modifyAirfaresPagePanel);

		// 
		// Finalize the Modify Airport Dialog
		//
		modifyAirfaresDialog = new JDialog(modifyAirfaresPageFrame, "Modify Airfare", true);
		modifyAirfaresDialog.getContentPane().add(modifyAirfaresPagePanel);
		modifyAirfaresDialog.pack();
		modifyAirfaresDialog.setSize(WIDTH,HEIGHT);
		modifyAirfaresDialog.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		modifyAirfaresDialog.setLocation(LocationX, LocationY);
		modifyAirfaresDialog.setVisible(true);
    }

    //
    // Get Price By Air fare ID
    //
    private String getPriceByID(int intModifyID) {
		String price = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT Price "
						 + "FROM Airfares "
						 + "WHERE FlightID = " + intModifyID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	price = rs.getString("Price");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return price;
	}

    //
    // Get Airline By Air fare ID
    //
    private String getAirlineByID(int intModifyID) {
		String airline = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT Airline "
						 + "FROM Airfares "
						 + "WHERE FlightID = " + intModifyID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	airline = rs.getString("Airline");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return airline;
	}

	//
    // Get Arrival time by Air fare ID
    //
    private String getArrivalTimeByID(int intModifyID) {
		String arrivalTime = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT ArrivalTime "
						 + "FROM Airfares "
						 + "WHERE FlightID = " + intModifyID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	arrivalTime = rs.getString("ArrivalTime");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return arrivalTime;
	}

	//
    // Get Departure Time by Air Fare ID
    //
    private String getDepartureTimeByID(int intModifyID) {
		String departureTime = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT DepartureTime "
						 + "FROM Airfares "
						 + "WHERE FlightID = " + intModifyID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	departureTime = rs.getString("DepartureTime");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return departureTime;
    }

	//
    // Get Departure Airport ID by Air fare ID
    //
    private String getDepartureAirportByAirfareID(int intModifyID) {
		String departureAirportID = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT DepartureAirportID "
						 + "FROM Airfares "
						 + "WHERE FlightID = " + intModifyID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	departureAirportID = rs.getString("DepartureAirportID");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return departureAirportID;
    }

    //
    // Get Destination Airport ID by Air fare ID
    //
    private String getDestinationAirportByAirfareID(int intModifyID) {
		String destinationAirportID = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT DestinationAirportID "
						 + "FROM Airfares "
						 + "WHERE FlightID = " + intModifyID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	destinationAirportID = rs.getString("DestinationAirportID");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return destinationAirportID;
    }

	//
    // Validate Air fare parameters
    //
	protected boolean validateAirfareParameters(String departureAirportID, String destinationAirportID,
			String departureTime, String arrivalTime, String airline, String price) {
		//
		// Airport names validation
		//
		if (!airportExists(departureAirportID)) {
			JOptionPane.showMessageDialog(null, 
					"Airport with ID " + departureAirportID + " does not exist!", "Airport Not Found", 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!airportExists(destinationAirportID)) {
			JOptionPane.showMessageDialog(null, 
					"Airport with ID " + destinationAirportID + " does not exist!", "Airport Not Found", 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//
		// Departure and Destination airports cannot be the same
		//
		if(departureAirportID == destinationAirportID)
		{
			JOptionPane.showMessageDialog(null, 
					"Departure and Destination Airports cannot be the same!", "Airports Error", 
					JOptionPane.ERROR_MESSAGE);
			return false;			
		}		

		//
		// Validate times
		//
		if(!isValidTimeFormat(departureTime)) {
			JOptionPane.showMessageDialog(null, 
					"Departure time should be 00:00 - 23:59", "Time Error", 
					JOptionPane.ERROR_MESSAGE);
			return false;			
		}
		if(!isValidTimeFormat(arrivalTime)) {
			JOptionPane.showMessageDialog(null, 
					"Arrival time should be 00:00 - 23:59", "Time Error", 
					JOptionPane.ERROR_MESSAGE);
			return false;			
		}

		//
		// Airline should not be empty
		//
		if (airline.isEmpty()) {
			JOptionPane.showMessageDialog(null, 
					"Airline Name should not be empty!", "Airline Name Error", 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//
		// Validate price
		//
		if(!isValidDecimalFormat(price)) {
			JOptionPane.showMessageDialog(null, 
					"Price should be decimal(10,2)", "Price Error", 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//
		// Validation passed
		//
		JOptionPane.showMessageDialog(null, 
				"Airfare modified succesfully!", "Airfare Modified", 
				JOptionPane.INFORMATION_MESSAGE);
		return true;	
	}

	//
	// Airport Existing validation
	//
	protected boolean airportExists (String airportID)
	{
		boolean airportExists = false;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT AirportID "
						 + "FROM Airports "
						 + "WHERE EXISTS (SELECT AirportName FROM Airports WHERE Airports.AirportID = \"" + airportID + "\")";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            airportExists = rs.next();
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return airportExists;
	}

	//
	// Validate time format
	//
	protected boolean isValidTimeFormat(String time) {
	    String timePattern = "([01]?\\d|2[0-3]):[0-5]\\d";
	    if(time.matches(timePattern)) {
	        return true;
	    } else {
	        return false;
	    }
	}

	//
	// Validate decimal format
	//
	protected boolean isValidDecimalFormat(String price) {
	    String decimalPattern = "\\d{1,8}(\\.\\d{1,2})?";  
	    if(price.matches(decimalPattern)) {
	        return true;
	    } else {
	        return false;
	    }
	}
}
