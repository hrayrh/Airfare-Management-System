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

public class modifyAirportsPage {
	JFrame modifyAirportsPageFrame;

	JLabel columnNameLabel;
	JLabel currentValueLabel;
	JLabel modifiedValueLabel;
	JLabel airportNameLabel;
	JLabel cityLabel;
	JLabel countryLabel;
	JLabel IATALabel;
	JTextField currentAirportNameTextField;
	JTextField currentCityTextField;
	JTextField currentCountryTextField;
	JTextField currentIATATextField;
	JTextField ModifyAirportNameTextField;
	JTextField ModifyCityTextField;
	JTextField ModifyCountryTextField;
	JTextField ModifyIATATextField;
	JButton backButton;
	JButton modifyButton;

    JPanel modifyAirportsPagePanel;
    JDialog modifyAirportsDialog;
    
    modifyAirportsPage(int intModifyID) {
		final int HEIGHT = 350;
		final int WIDTH = 600;
		final int LocationX = 400;
		final int LocationY = 200;
		
		modifyAirportsPageFrame = new JFrame();

		//
		// Adjust Labels
		//
		columnNameLabel = new JLabel("Column Name");
		currentValueLabel = new JLabel("Current Value");
		modifiedValueLabel = new JLabel("Modified Value");
		airportNameLabel = new JLabel("AirportName");
		cityLabel = new JLabel("City");
		countryLabel = new JLabel("Country");
		IATALabel = new JLabel("IATA");	
		columnNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		modifiedValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		airportNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		countryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		IATALabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust TextFields
		//
		currentAirportNameTextField = new JTextField(20);
		currentCityTextField = new JTextField(20);
		currentCountryTextField = new JTextField(20);
		currentIATATextField = new JTextField(20);
		ModifyAirportNameTextField = new JTextField(20);
		ModifyCityTextField = new JTextField(20);
		ModifyCountryTextField = new JTextField(20);
		ModifyIATATextField = new JTextField(20);
		currentAirportNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentCityTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentCountryTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentIATATextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyAirportNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyCityTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyCountryTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyIATATextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyAirportNameTextField.setText(getAirportNameByID(intModifyID));
		ModifyCityTextField.setText(getCityNameByID(intModifyID));
		ModifyCountryTextField.setText(getCountryNameByID(intModifyID));
		ModifyIATATextField.setText(getIATAByID(intModifyID));

		//
		// Current values should not be editable
		//
		currentAirportNameTextField.setEditable(false);
		currentAirportNameTextField.setText(getAirportNameByID(intModifyID));
		currentCityTextField.setEditable(false);
		currentCityTextField.setText(getCityNameByID(intModifyID));
		currentCountryTextField.setEditable(false);
		currentCountryTextField.setText(getCountryNameByID(intModifyID));
		currentIATATextField.setEditable(false);
		currentIATATextField.setText(getIATAByID(intModifyID));

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
				modifyAirportsDialog.setVisible(false);
			}
		});

		//
		// Modify Airport button
		//
		modifyButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String airportName = ModifyAirportNameTextField.getText();
				String cityName = ModifyCityTextField.getText();
				String countryName = ModifyCountryTextField.getText();
				String IATACode = ModifyIATATextField.getText();

				//
				// TextField items should be valid
				//
				if (validateAirportParameters(airportName, cityName, countryName, IATACode)) {
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
						String query = "UPDATE Airports "
									 + "SET AirportName = \"" + airportName + "\""
									 + ", City = \"" + cityName + "\""
									 + ", Country = \"" + countryName + "\""
									 + ", IATA = \"" +  IATACode + "\""
									 + " WHERE (AirportID = " + intModifyID + ")";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					modifyAirportsDialog.setVisible(false);
				}
			}
		});

		//
		// Finalize Modify Airport Page Panel and Frame
		//
		modifyAirportsPagePanel = new JPanel(new GridLayout(6, 3, 10, 10));
		modifyAirportsPagePanel.add(columnNameLabel);
		modifyAirportsPagePanel.add(currentValueLabel);
		modifyAirportsPagePanel.add(modifiedValueLabel);
		modifyAirportsPagePanel.add(airportNameLabel);
		modifyAirportsPagePanel.add(currentAirportNameTextField);
		modifyAirportsPagePanel.add(ModifyAirportNameTextField);
		modifyAirportsPagePanel.add(cityLabel);
		modifyAirportsPagePanel.add(currentCityTextField);
		modifyAirportsPagePanel.add(ModifyCityTextField);
		modifyAirportsPagePanel.add(countryLabel);
		modifyAirportsPagePanel.add(currentCountryTextField);
		modifyAirportsPagePanel.add(ModifyCountryTextField);
		modifyAirportsPagePanel.add(IATALabel);
		modifyAirportsPagePanel.add(currentIATATextField);
		modifyAirportsPagePanel.add(ModifyIATATextField);
		modifyAirportsPagePanel.add(backButton);
		modifyAirportsPagePanel.add(new JLabel(""));
		modifyAirportsPagePanel.add(modifyButton);

		modifyAirportsPageFrame.add(modifyAirportsPagePanel);

		// 
		// Finalize the Modify Airport Dialog
		//
		modifyAirportsDialog = new JDialog(modifyAirportsPageFrame, "Modify Airport", true);
		modifyAirportsDialog.getContentPane().add(modifyAirportsPagePanel);
		modifyAirportsDialog.pack();
		modifyAirportsDialog.setSize(WIDTH,HEIGHT);
		modifyAirportsDialog.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		modifyAirportsDialog.setLocation(LocationX, LocationY);
		modifyAirportsDialog.setVisible(true);
    }
    
    //
    // validate modified Airport parameters
    //
	protected boolean validateAirportParameters(String airportName, String cityName, String countryName,
			String IATACode) {
		//
		// Names validation
		//
		if (airportName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Airport Name should not be empty!", "Airport Name Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (cityName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "City Name should not be empty!", "City Name Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (countryName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Country Name should not be empty!", "Country Name Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//
		// IATA Code length validation. IATA code should be 3 letters.
		//
		if (IATACode.length() != 3) {
			JOptionPane.showMessageDialog(null, "IATA code should be 3 letters", "IATA Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//
		// Validation passed
		//
		JOptionPane.showMessageDialog(null, "Airport modified succesfully!", "Airport Modified", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	//
	// Get airport name by ID
	//
	protected String getAirportNameByID (int AirportID)
	{
		String airportName = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT AirportName "
						 + "FROM Airports "
						 + "WHERE AirportID = " + AirportID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	airportName = rs.getString("AirportName");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return airportName;
	}
	
	//
	// Get city name by ID
	//
	protected String getCityNameByID (int AirportID)
	{
		String cityName = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT City "
						 + "FROM Airports "
						 + "WHERE AirportID = " + AirportID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	cityName = rs.getString("City");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return cityName;
	}

	//
	// Get country name by ID
	//
	protected String getCountryNameByID (int AirportID)
	{
		String countryName = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT Country "
						 + "FROM Airports "
						 + "WHERE AirportID = " + AirportID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	countryName = rs.getString("Country");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return countryName;
	}
	
	//
	// Get IATA Code by ID
	//
	protected String getIATAByID (int AirportID)
	{
		String IATA = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT IATA "
						 + "FROM Airports "
						 + "WHERE AirportID = " + AirportID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	IATA = rs.getString("IATA");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return IATA;
	}
}
