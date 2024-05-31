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

public class bookAFlightPage {
	JFrame bookAFlightPageFrame;

	JLabel userIDLabel;
	JLabel flightIDLabel;
	JLabel numberOfSeatsLabel;
	JLabel priceLabel;
	JTextField userIDTextField;
	JTextField flightIDTextField;
	JSpinner numberOfSeatsSpinner;
	JTextField priceValueTextField;
	JButton backButton;
	JButton bookButton;

    JPanel bookAFlightPagePanel;
    JDialog bookAFlightDialog;
    
    bookAFlightPage(int userID) {
		final int HEIGHT = 270;
		final int WIDTH = 480;
		final int LocationX = 400;
		final int LocationY = 200;

		bookAFlightPageFrame = new JFrame();

		//
		// Adjust Labels
		//
		userIDLabel = new JLabel("User ID:");
		flightIDLabel = new JLabel("Flight ID:");
		numberOfSeatsLabel = new JLabel("Number of Seats:");
		priceLabel = new JLabel("Price:");

		userIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		flightIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numberOfSeatsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust TextFields
		//
		userIDTextField = new JTextField(20);
		flightIDTextField = new JTextField(20);
		priceValueTextField = new JTextField(20);
		userIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		flightIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		priceValueTextField.setHorizontalAlignment(SwingConstants.CENTER);
		//
		// price text field should not be editable, it's calculated automatically
		//
		priceValueTextField.setEditable(false);
		priceValueTextField.setText("0");

		//
		// If UserID is not zero, we came to this page from Main page not from Whole DB page
		// So we need to automatically set the userIDTextField to the logged user ID
		//
		if(userID != 0) {
			userIDTextField.setEditable(false);
			String userIDString = String.valueOf(userID);
			userIDTextField.setText(userIDString);
		}

		//
		// User and Flight ID text fields should only contain digits
		//
		Document document1 = userIDTextField.getDocument();
		if(document1 instanceof PlainDocument){
		    PlainDocument plainDocument = (PlainDocument)document1;
		    plainDocument.setDocumentFilter(new DigitOnlyDocumentFilter());
		}
		Document document2 = flightIDTextField.getDocument();
		if(document2 instanceof PlainDocument){
		    PlainDocument plainDocument = (PlainDocument)document2;
		    plainDocument.setDocumentFilter(new DigitOnlyDocumentFilter());
		}

		//
		// Add buttons
		//
		backButton = new JButton("Back");
		bookButton = new JButton("Book a flight");

		//
		// Back button should go back to whole DB page
		//
		backButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				bookAFlightDialog.setVisible(false);
			}
		});

		//
		// Book button should book a flight if parameters are valid
		//
		bookButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String userIDString = userIDTextField.getText();
                int userIDInt = Integer.parseInt(userIDString);
				String flightIDString = flightIDTextField.getText();
                int flightIDInt = Integer.parseInt(flightIDString);
				int numOfSeat = (int) numberOfSeatsSpinner.getValue();
				String price = priceValueTextField.getText();

				//
				// Parameters should be valid
				//
				if (validateBookingParameters(userIDInt, flightIDInt, numOfSeat, price)) {
					final String url = "jdbc:mysql://localhost:3306/airfare";
					final String username = "root";
					final String password = "s#MF!xh9";
					Connection connection = null;				
					//
					// Register the booking into DB
					//
					try {
						connection = DriverManager.getConnection(url, username, password);
						
						Statement statement = null;
						String query = "INSERT INTO Bookings (UserID, FlightID, NumSeatsBooked, TotalPrice)" 
									 + "VALUES"
									 + "('" + userIDString + "','" + flightIDString + "','" + numOfSeat + "','" + price + "');";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					bookAFlightDialog.setVisible(false);
				}
			}
		});

		//
		// Adjust JSpinner which can be from 0 to 100
		//
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
		numberOfSeatsSpinner = new JSpinner(spinnerModel);
		
		//
		// If the number of seats is modified, price value text field should be updated
		//
		numberOfSeatsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedValue = (int) numberOfSeatsSpinner.getValue();
                String flightIDValue = flightIDTextField.getText();
                int flightIDInt = Integer.parseInt(flightIDValue);
                int totalPrice = selectedValue * calculatePriceByFlightID(flightIDInt);
                String totalPriceString = String.valueOf(totalPrice);
                priceValueTextField.setText(totalPriceString);
            }
        });
 
		//
		// Finalize Book a Flight Page Panel and Frame
		//
		bookAFlightPagePanel = new JPanel(new GridLayout(5, 2, 10, 10));
		bookAFlightPagePanel.add(userIDLabel);
		bookAFlightPagePanel.add(userIDTextField);
		bookAFlightPagePanel.add(flightIDLabel);
		bookAFlightPagePanel.add(flightIDTextField);
		bookAFlightPagePanel.add(numberOfSeatsLabel);
		bookAFlightPagePanel.add(numberOfSeatsSpinner);
		bookAFlightPagePanel.add(priceLabel);
		bookAFlightPagePanel.add(priceValueTextField);
		bookAFlightPagePanel.add(backButton);
		bookAFlightPagePanel.add(bookButton);

		bookAFlightPageFrame.add(bookAFlightPagePanel);
		
		// 
		// Finalize Book a Flight Dialog
		//
		bookAFlightDialog = new JDialog(bookAFlightPageFrame, "Book a Flight", true);
		bookAFlightDialog.getContentPane().add(bookAFlightPagePanel);
		bookAFlightDialog.pack();
		bookAFlightDialog.setSize(WIDTH,HEIGHT);
		bookAFlightDialog.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		bookAFlightDialog.setLocation(LocationX, LocationY);
		bookAFlightDialog.setVisible(true);
    }

    //
    // Validate Booking Parameters
    //
    protected boolean validateBookingParameters(int userID, int flightID, int numOfSeat,
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
		JOptionPane.showMessageDialog(null, "Booking added succesfully!", "Booking Added", JOptionPane.INFORMATION_MESSAGE);
		return true;	
	}

	//
	// User Existing validation
	//
	protected boolean userExists (int userID)
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
	protected boolean flightExists (int flightID)
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
