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

public class addAirfarePage {
	JFrame addAirfarePageFrame;

	JLabel departureAirportNameLabel;
	JLabel destinationAirportNameLabel;
	JLabel departureTimeLabel;
	JLabel arrivalTimeLabel;
	JLabel airlineLabel;
	JLabel priceLabel;
	final JLabel timeSeparatorLabel1;
	final JLabel timeSeparatorLabel2;
	
	JTextField departureAirportNameTextField;
	JTextField destinationAirportNameTextField;
	JTextField departureTimeTextField1;
	JTextField departureTimeTextField2;
	JTextField arrivalTimeTextField1;
	JTextField arrivalTimeTextField2;
	JTextField airlineTextField;
	JTextField priceTextField;

	JButton backButton;
	JButton addButton;

	JPanel departureTimePanel;
	JPanel arrivalTimePanel;

    JPanel addAirfarePagePanel;
    JDialog addAirfareDialog;
    
    addAirfarePage()
    {
		final int HEIGHT = 270;
		final int WIDTH = 480;
		final int LocationX = 400;
		final int LocationY = 200;
		
		addAirfarePageFrame = new JFrame();

		//
		// Adjust Labels
		//
		departureAirportNameLabel = new JLabel("Enter departure airport name:");
		destinationAirportNameLabel = new JLabel("Enter destination airport name:");
		departureTimeLabel = new JLabel("Enter departure time:");
		arrivalTimeLabel = new JLabel("Enter arrival time:");	
		airlineLabel = new JLabel("Enter airline:");
		priceLabel = new JLabel("Enter price:");
		timeSeparatorLabel1 = new JLabel(":");
		timeSeparatorLabel2 = new JLabel(":");

		departureAirportNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		destinationAirportNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		departureTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		arrivalTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		airlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeSeparatorLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		timeSeparatorLabel2.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust TextFields
		//
		departureAirportNameTextField = new JTextField(20);
		destinationAirportNameTextField = new JTextField(20);
		departureTimeTextField1 = new JTextField(20);
		arrivalTimeTextField1 = new JTextField(20);
		departureTimeTextField2 = new JTextField(20);
		arrivalTimeTextField2 = new JTextField(20);
		airlineTextField = new JTextField(20);
		priceTextField = new JTextField(20);
		departureAirportNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		destinationAirportNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		departureTimeTextField1.setHorizontalAlignment(SwingConstants.RIGHT);
		arrivalTimeTextField1.setHorizontalAlignment(SwingConstants.RIGHT);
		departureTimeTextField2.setHorizontalAlignment(SwingConstants.LEFT);
		arrivalTimeTextField2.setHorizontalAlignment(SwingConstants.LEFT);
		airlineTextField.setHorizontalAlignment(SwingConstants.CENTER);
		priceTextField.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Add buttons
		//
		backButton = new JButton("Back");
		addButton = new JButton("Add Airfare");

		//
		// Back button should go back to Main page
		//
		backButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				addAirfareDialog.setVisible(false);
			}
		});

		//
		// Add Air fare button
		//
		addButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String departureAirportName = departureAirportNameTextField.getText();
				String destinationAirportName = destinationAirportNameTextField.getText();
				String departureTime = departureTimeTextField1.getText() 
									   + timeSeparatorLabel1.getText() 
									   + departureTimeTextField2.getText();
				String arrivalTime   = arrivalTimeTextField1.getText() 
						   		       + timeSeparatorLabel2.getText() 
						   		       + arrivalTimeTextField2.getText();
				String airline = airlineTextField.getText();
				String price = priceTextField.getText();

				//
				// TextField items should be valid
				//
				if (validateAirfareParameters(departureAirportName, destinationAirportName, 
						departureTime, arrivalTime, airline, price)) {
					final String url = "jdbc:mysql://localhost:3306/airfare";
					final String username = "root";
					final String password = "s#MF!xh9";
					Connection connection = null;				
					//
					// Register the air fare into DB
					//
					try {
						connection = DriverManager.getConnection(url, username, password);
						
						Statement statement = null;
						String query = "INSERT INTO Airfares (DepartureAirportID, DestinationAirportID, DepartureTime, ArrivalTime, Airline, Price)" 
									 + "VALUES"
									 + "('" + getAirportIDbyName(departureAirportName) + "','" 
									 + getAirportIDbyName(destinationAirportName) + "','"
									 + departureTime + "','" + arrivalTime + "','" + airline + "','" + price + "');";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					addAirfareDialog.setVisible(false);
				}
			}
		});

		//
		// Finalize time related panels
		//
		departureTimePanel = new JPanel(new GridLayout(1, 3, 5, 5));
		departureTimePanel.add(departureTimeTextField1);
		departureTimePanel.add(timeSeparatorLabel1);
		departureTimePanel.add(departureTimeTextField2);

		arrivalTimePanel = new JPanel(new GridLayout(1, 3, 5, 5));
		arrivalTimePanel.add(arrivalTimeTextField1);
		arrivalTimePanel.add(timeSeparatorLabel2);
		arrivalTimePanel.add(arrivalTimeTextField2);

		//
		// Finalize Add Air fare Page Panel and Frame
		//
		addAirfarePagePanel = new JPanel(new GridLayout(7, 2, 10, 10));
		addAirfarePagePanel.add(departureAirportNameLabel);
		addAirfarePagePanel.add(departureAirportNameTextField);
		addAirfarePagePanel.add(destinationAirportNameLabel);
		addAirfarePagePanel.add(destinationAirportNameTextField);
		addAirfarePagePanel.add(departureTimeLabel);
		addAirfarePagePanel.add(departureTimePanel);
		addAirfarePagePanel.add(arrivalTimeLabel);
		addAirfarePagePanel.add(arrivalTimePanel);
		addAirfarePagePanel.add(airlineLabel);
		addAirfarePagePanel.add(airlineTextField);
		addAirfarePagePanel.add(priceLabel);
		addAirfarePagePanel.add(priceTextField);
		addAirfarePagePanel.add(backButton);
		addAirfarePagePanel.add(addButton);

		addAirfarePageFrame.add(addAirfarePagePanel);

		// 
		// Finalize the Add Air fare Dialog
		//
		addAirfareDialog = new JDialog(addAirfarePageFrame, "Add Airfare", true);
		addAirfareDialog.getContentPane().add(addAirfarePagePanel);
		addAirfareDialog.pack();
		addAirfareDialog.setSize(WIDTH,HEIGHT);
		addAirfareDialog.setLocation(LocationX, LocationY);
		addAirfareDialog.setVisible(true);
    }
    
	//
	//	Validate Air fare Parameters: airport names, departure and arrival times, airline, price
	//
	protected boolean validateAirfareParameters(String departureAirportName, String destinationAirportName,
			String departureTime, String arrivalTime, String airline, String price)
	{
		//
		// Airport names validation
		//
		if (!airportExists(departureAirportName)) {
			JOptionPane.showMessageDialog(null, 
					"Airport " + departureAirportName + " does not exist!", "Airport Not Found", 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!airportExists(destinationAirportName)) {
			JOptionPane.showMessageDialog(null, 
					"Airport " + destinationAirportName + " does not exist!", "Airport Not Found", 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//
		// Departure and Destination airports cannot be the same
		//
		if(departureAirportName.equals(destinationAirportName))
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
				"Airfare added succesfully!", "Airfare Added", 
				JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	//
	// Airport Existing validation
	//
	protected boolean airportExists (String airportName)
	{
		boolean airportExists = false;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT AirportName "
						 + "FROM Airports "
						 + "WHERE EXISTS (SELECT AirportName FROM Airports WHERE Airports.AirportName = \"" + airportName + "\")";
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
	// Get airport ID by name
	//
	protected int getAirportIDbyName (String airportName)
	{
		int airportID = 0;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT AirportID "
						 + "FROM Airports "
						 + "WHERE AirportName LIKE BINARY \"" + airportName + "\"";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
                airportID = rs.getInt("AirportID");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return airportID;
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
