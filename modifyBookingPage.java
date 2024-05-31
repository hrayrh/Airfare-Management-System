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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class modifyBookingPage {
	JFrame modifyBookingPageFrame;

	JLabel columnNameLabel;
	JLabel currentValueLabel;
	JLabel modifiedValueLabel;
	JLabel userIDLabel;
	JLabel flightIDLabel;
	JLabel numSeatsBookedLabel;
	JLabel totalPriceLabel;

	JTextField currentUserIDTextField;
	JTextField currentFlightIDTextField;
	JTextField currentNumSeatsBookedTextField;
	JTextField currentTotalPriceTextField;

	JTextField ModifyUserIDTextField;
	JTextField ModifyFlightIDTextField;
	JSpinner ModifyNumSeatsBookedSpinner;
	JTextField ModifyTotalPriceTextField;

	JButton backButton;
	JButton modifyButton;

    JPanel modifyBookingPagePanel;
    JDialog modifyBookingDialog;

    modifyBookingPage(int intModifyID) {
		final int HEIGHT = 350;
		final int WIDTH = 600;
		final int LocationX = 400;
		final int LocationY = 200;
		
		modifyBookingPageFrame = new JFrame();

		//
		// Adjust Labels
		//
		columnNameLabel = new JLabel("Column Name");
		currentValueLabel = new JLabel("Current Value");
		modifiedValueLabel = new JLabel("Modified Value");
		userIDLabel = new JLabel("UserID");
		flightIDLabel = new JLabel("FlightID");
		numSeatsBookedLabel = new JLabel("NumSeatsBooked");
		totalPriceLabel = new JLabel("TotalPrice");
		columnNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		modifiedValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		flightIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numSeatsBookedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust TextFields
		//
		currentUserIDTextField = new JTextField(20);
		currentFlightIDTextField = new JTextField(20);
		currentNumSeatsBookedTextField = new JTextField(20);
		currentTotalPriceTextField = new JTextField(20);
		ModifyUserIDTextField = new JTextField(20);
		ModifyFlightIDTextField = new JTextField(20);
		ModifyTotalPriceTextField = new JTextField(20);
		currentUserIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentFlightIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentNumSeatsBookedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentTotalPriceTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyUserIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyFlightIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyTotalPriceTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyUserIDTextField.setText(getUserIDByBookingID(intModifyID));
		ModifyFlightIDTextField.setText(getFlightIDByBookingID(intModifyID));
		ModifyTotalPriceTextField.setText(getTotalPriceByID(intModifyID));

		//
		// Current values should not be editable
		//
		currentUserIDTextField.setEditable(false);
		currentUserIDTextField.setText(getUserIDByBookingID(intModifyID));
		currentFlightIDTextField.setEditable(false);
		currentFlightIDTextField.setText(getFlightIDByBookingID(intModifyID));
		currentNumSeatsBookedTextField.setEditable(false);
		currentNumSeatsBookedTextField.setText(getNumSeatsBookedByID(intModifyID));
		currentTotalPriceTextField.setEditable(false);
		currentTotalPriceTextField.setText(getTotalPriceByID(intModifyID));

		//
		// Modify Total Price Text Field should also not be editable, it depends on ticket count
		//
		ModifyTotalPriceTextField.setEditable(false);

		//
		// User and Flight IDs should only contain digits
		//
		Document document1 = ModifyUserIDTextField.getDocument();
		if(document1 instanceof PlainDocument){
		    PlainDocument plainDocument = (PlainDocument)document1;
		    plainDocument.setDocumentFilter(new DigitOnlyDocumentFilter());
		}
		Document document2 = ModifyFlightIDTextField.getDocument();
		if(document2 instanceof PlainDocument){
		    PlainDocument plainDocument = (PlainDocument)document2;
		    plainDocument.setDocumentFilter(new DigitOnlyDocumentFilter());
		}

		//
		// Adjust JSpinner which can be from 0 to 100
		//
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
		ModifyNumSeatsBookedSpinner = new JSpinner(spinnerModel);

		//
		// If the number of seats is modified, price value text field should be updated
		//
		ModifyNumSeatsBookedSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedValue = (int) ModifyNumSeatsBookedSpinner.getValue();
                String flightIDValue = ModifyFlightIDTextField.getText();
                int flightIDInt = Integer.parseInt(flightIDValue);
                int totalPrice = selectedValue * calculatePriceByFlightID(flightIDInt);
                String totalPriceString = String.valueOf(totalPrice);
                ModifyTotalPriceTextField.setText(totalPriceString);
            }
        });

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
				modifyBookingDialog.setVisible(false);
			}
		});

		//
		// Modify Airport button
		//
		modifyButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){ 
				String userID = ModifyUserIDTextField.getText();
				String flightID = ModifyFlightIDTextField.getText();
				int numSeatsBooked = (int) ModifyNumSeatsBookedSpinner.getValue();
				String totalPrice = ModifyTotalPriceTextField.getText();

				//
				// TextField items should be valid
				//
				if (validateBookingParameters(userID, flightID, numSeatsBooked, totalPrice)) {
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
						String query = "UPDATE Bookings "
									 + "SET UserID = \"" + userID + "\""
									 + ", FlightID = \"" + flightID + "\""
									 + ", NumSeatsBooked = \"" + numSeatsBooked + "\""
									 + ", TotalPrice = \"" +  totalPrice + "\""
									 + " WHERE (BookingID = " + intModifyID + ")";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					modifyBookingDialog.setVisible(false);
				}
			}
		});

		//
		// Finalize Modify Airport Page Panel and Frame
		//
		modifyBookingPagePanel = new JPanel(new GridLayout(6, 3, 10, 10));
		modifyBookingPagePanel.add(columnNameLabel);
		modifyBookingPagePanel.add(currentValueLabel);
		modifyBookingPagePanel.add(modifiedValueLabel);

		modifyBookingPagePanel.add(userIDLabel);
		modifyBookingPagePanel.add(currentUserIDTextField);
		modifyBookingPagePanel.add(ModifyUserIDTextField);
		modifyBookingPagePanel.add(flightIDLabel);
		modifyBookingPagePanel.add(currentFlightIDTextField);
		modifyBookingPagePanel.add(ModifyFlightIDTextField);
		modifyBookingPagePanel.add(numSeatsBookedLabel);
		modifyBookingPagePanel.add(currentNumSeatsBookedTextField);
		modifyBookingPagePanel.add(ModifyNumSeatsBookedSpinner);
		modifyBookingPagePanel.add(totalPriceLabel);
		modifyBookingPagePanel.add(currentTotalPriceTextField);
		modifyBookingPagePanel.add(ModifyTotalPriceTextField);
		modifyBookingPagePanel.add(backButton);
		modifyBookingPagePanel.add(new JLabel(""));
		modifyBookingPagePanel.add(modifyButton);

		modifyBookingPageFrame.add(modifyBookingPagePanel);

		// 
		// Finalize the Modify Airport Dialog
		//
		modifyBookingDialog = new JDialog(modifyBookingPageFrame, "Modify Booking", true);
		modifyBookingDialog.getContentPane().add(modifyBookingPagePanel);
		modifyBookingDialog.pack();
		modifyBookingDialog.setSize(WIDTH,HEIGHT);
		modifyBookingDialog.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		modifyBookingDialog.setLocation(LocationX, LocationY);
		modifyBookingDialog.setVisible(true);
    }

    //
    // validate modified booking parameters
    //
    protected boolean validateBookingParameters(String userID, String flightID, int numOfSeat,
			String price) {
	    //
	    // Does UserID exist in DB?
	    //
		if (!userExists(userID)) {
			JOptionPane.showMessageDialog(null, "User ID " + userID + " not found", "User ID Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	    //
	    // Does FlightID exist in DB?
	    //
		if (!flightExists(flightID)) {
			JOptionPane.showMessageDialog(null, "Flight ID " + flightID + " not found", "Flight ID Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//
		// Validation passed
		//
		JOptionPane.showMessageDialog(null, "Booking modified succesfully!", "Booking Modified", JOptionPane.INFORMATION_MESSAGE);
		return true;	
	}

	//
	// User Existing validation
	//
	protected boolean userExists (String userID)
	{
		boolean userExists = false;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT UserID "
						 + "FROM Users "
						 + "WHERE EXISTS (SELECT UserID FROM Users WHERE Users.UserID = \"" + userID + "\")";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            userExists = rs.next();
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return userExists;
	}

	//
	// Flight Existing validation
	//
	protected boolean flightExists (String flightID)
	{
		boolean flightExists = false;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			Statement statement = null;
			String query = "SELECT FlightID "
						 + "FROM Airfares "
						 + "WHERE EXISTS (SELECT FlightID FROM Airfares WHERE Airfares.FlightID = \"" + flightID + "\")";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            flightExists = rs.next();
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return flightExists;
	}

	//
	// Get user ID by booking ID
	//
	protected String getUserIDByBookingID (int bookingID)
	{
		String userID = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT UserID "
						 + "FROM Bookings "
						 + "WHERE BookingID = " + bookingID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	userID = rs.getString("UserID");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return userID;
	}
	
	//
	// Get flight ID by booking ID
	//
	protected String getFlightIDByBookingID (int bookingID)
	{
		String flightID = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT FlightID "
						 + "FROM Bookings "
						 + "WHERE BookingID = " + bookingID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	flightID = rs.getString("FlightID");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return flightID;
	}

	//
	// Get number of seats booked by ID
	//
	protected String getNumSeatsBookedByID (int bookingID)
	{
		String numSeatsBooked = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT NumSeatsBooked "
						 + "FROM Bookings "
						 + "WHERE BookingID = " + bookingID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	numSeatsBooked = rs.getString("NumSeatsBooked");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return numSeatsBooked;
	}
	
	//
	// Get Total Price by ID
	//
	protected String getTotalPriceByID (int bookingID)
	{
		String TotalPrice = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT TotalPrice "
						 + "FROM Bookings "
						 + "WHERE BookingID = " + bookingID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	TotalPrice = rs.getString("TotalPrice");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return TotalPrice;
	}
	
	//
    // Get Price of the flight by Flight ID
    //
	protected int calculatePriceByFlightID(int flightID) {
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		int price = 0;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            
            String query = "SELECT Price FROM Airfares WHERE FlightID = " + flightID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            if (resultSet.next()) {
                price = resultSet.getInt("Price");
            } else {
    			JOptionPane.showMessageDialog(null, 
    					"Flight ID not found", "Flight ID Error", 
    					JOptionPane.ERROR_MESSAGE);	
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return price;
	}
}
