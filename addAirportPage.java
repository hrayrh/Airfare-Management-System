import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class addAirportPage {
	JFrame addAirportPageFrame;

	JLabel airportNameLabel;
	JLabel cityLabel;
	JLabel countryLabel;
	JLabel IATALabel;
	JTextField airportNameTextField;
	JTextField cityTextField;
	JTextField countryTextField;
	JTextField IATATextField;
	JButton backButton;
	JButton addButton;

    JPanel addAirportPagePanel;
    JDialog addAirportDialog;
    
    addAirportPage()
    {
		final int HEIGHT = 210;
		final int WIDTH = 480;
		final int LocationX = 400;
		final int LocationY = 200;
		
		addAirportPageFrame = new JFrame();

		//
		// Adjust Labels
		//
		airportNameLabel = new JLabel("Enter airport name:");
		cityLabel = new JLabel("Enter city name:");
		countryLabel = new JLabel("Enter country name:");
		IATALabel = new JLabel("Enter IATA code:");	
		airportNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		countryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		IATALabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust TextFields
		//
		airportNameTextField = new JTextField(20);
		cityTextField = new JTextField(20);
		countryTextField = new JTextField(20);
		IATATextField = new JTextField(20);
		airportNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		cityTextField.setHorizontalAlignment(SwingConstants.CENTER);
		countryTextField.setHorizontalAlignment(SwingConstants.CENTER);
		IATATextField.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Add buttons
		//
		backButton = new JButton("Back");
		addButton = new JButton("Add Airport");

		//
		// Back button should go back to Main page
		//
		backButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				addAirportDialog.setVisible(false);
			}
		});

		//
		// Add Airport button
		//
		addButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String airportName = airportNameTextField.getText();
				String cityName = cityTextField.getText();
				String countryName = countryTextField.getText();
				String IATACode = IATATextField.getText();

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
						String query = "INSERT INTO Airports (AirportName, City, Country, IATA)" 
									 + "VALUES"
									 + "('" + airportName + "','" + cityName + "','" + countryName + "','" + IATACode + "');";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					addAirportDialog.setVisible(false);
				}
			}
		});

		//
		// Finalize Add Airport Page Panel and Frame
		//
		addAirportPagePanel = new JPanel(new GridLayout(5, 2, 10, 10));
		addAirportPagePanel.add(airportNameLabel);
		addAirportPagePanel.add(airportNameTextField);
		addAirportPagePanel.add(cityLabel);
		addAirportPagePanel.add(cityTextField);
		addAirportPagePanel.add(countryLabel);
		addAirportPagePanel.add(countryTextField);
		addAirportPagePanel.add(IATALabel);
		addAirportPagePanel.add(IATATextField);
		addAirportPagePanel.add(backButton);
		addAirportPagePanel.add(addButton);

		addAirportPageFrame.add(addAirportPagePanel);

		// 
		// Finalize the Add Airport Dialog
		//
		addAirportDialog = new JDialog(addAirportPageFrame, "Add Airport", true);
		addAirportDialog.getContentPane().add(addAirportPagePanel);
		addAirportDialog.pack();
		addAirportDialog.setSize(WIDTH,HEIGHT);
		addAirportDialog.setLocation(LocationX, LocationY);
		addAirportDialog.setVisible(true);
    }
    
	//
	//	Validate Airport Parameters: airport name, city name, country name, IATA code
	//
	protected boolean validateAirportParameters(String airportName, String cityName, String countryName, String IATACode) {
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
		JOptionPane.showMessageDialog(null, "Airport added succesfully!", "Airport Added", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
}
