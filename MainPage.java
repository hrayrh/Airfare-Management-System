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
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.text.SimpleDateFormat;  
import java.util.Date;

public class MainPage {
	JFrame mainPageFrame;

	JLabel namesLabel;
	JLabel dateLabel;

	JButton addAirportButton;
	JButton addAirfareButton;
	JButton viewAirportsButton;
	JButton viewAirfaresButton;
	JButton myBookingsButton;
	JButton bookFlightButton;
	JButton exitButton;

    JPanel mainPagePanel;
    JDialog mainDialog;

    MainPage(String userLogin)
	{
		final int HEIGHT = 400;
		final int WIDTH = 600;
		final int LocationX = 400;
		final int LocationY = 200;

		mainPageFrame = new JFrame();
		
		//
		// Simple timer to update current date for dateLabel
		//
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String currentDate = dateFormat.format(new Date());
		        dateLabel.setText(currentDate);
		    }
		});
		timer.start();
		
		//
		// Adjust Labels
		//
		namesLabel = new JLabel(getFirstName(userLogin) + " " + getLastName(userLogin));
		dateLabel = new JLabel(dateFormat.format(new Date()));
		namesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dateLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust Buttons
		//
        myBookingsButton = new JButton("My bookings");
		addAirportButton = new JButton("Add Airport");
		addAirfareButton = new JButton("Add Airfare");
		viewAirportsButton = new JButton("View Airports");
		viewAirfaresButton = new JButton("View Airfares");
		bookFlightButton = new JButton("Book a flight");
		exitButton = new JButton("Exit");

		//
		// My Bookings button should open My Bookings Dialog
		//
		myBookingsButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				viewBookingsPage viewBookingsPage = new viewBookingsPage(getUserID(userLogin));
			}
		});
		
		//
		// Add Air fare button should open Add Air fare Dialog
		//
		addAirfareButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				addAirfarePage addAirfarePage = new addAirfarePage();
			}
		});

		//
		// Add Airport button should open Add Airport Dialog
		//
		addAirportButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				addAirportPage addAirportPage = new addAirportPage();
			}
		});

		//
		// Book Flight button should open Add Booking Dialog
		//
		bookFlightButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				bookAFlightPage bookAFlightPage = new bookAFlightPage(getUserID(userLogin));
			}
		});

		//
		// View Airports button should open View Airports Dialog
		//
		viewAirfaresButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				viewAirfaresPage viewAirfaresPage = new viewAirfaresPage();
			}
		});

		//
		// View Air fares button should open View Air fares Dialog
		//
		viewAirportsButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				viewAirportsPage viewAirportsPage = new viewAirportsPage();
			}
		});
		
		//
		// Exit should leave the application
		//
		exitButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				System.exit(0);
			}
		});

		//
		// Finalize Main Page Panel and Frame
		//
		mainPagePanel = new JPanel(new GridLayout(3, 3, 10, 10));

		mainPagePanel.add(namesLabel);
		mainPagePanel.add(myBookingsButton);
		mainPagePanel.add(dateLabel);
		mainPagePanel.add(addAirportButton);
		mainPagePanel.add(addAirfareButton);
		mainPagePanel.add(bookFlightButton);
		mainPagePanel.add(viewAirportsButton);
		mainPagePanel.add(viewAirfaresButton);
		mainPagePanel.add(exitButton);

		mainPageFrame.add(mainPagePanel);

		//
		// Finalize Main Dialog
		//
		mainDialog = new JDialog(mainPageFrame, "Airfare Ticket Purchase System", true);
		mainDialog.getContentPane().add(mainPagePanel);
		mainDialog.pack();
		mainDialog.setSize(WIDTH,HEIGHT);
		mainDialog.setLocation(LocationX, LocationY);
		mainDialog.setVisible(true);
	}

	//
	// get First Name of the user from DB
	//
	protected String getFirstName(String userLogin) {
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		String firstName = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT FirstName "
						 + "FROM Users "
						 + "WHERE Username LIKE BINARY \"" + userLogin + "\";";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                firstName = rs.getString("FirstName");
            }
            statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return firstName;
	}

	//
	// get Last Name of the user from DB
	//
	protected String getLastName(String userLogin) {
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		String lastName = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT LastName "
						 + "FROM Users "
						 + "WHERE Username LIKE BINARY \"" + userLogin + "\";";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
            	lastName = rs.getString("LastName");
            }
            statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return lastName;
	}
	
	//
	// get ID of the user from DB
	//
	protected int getUserID(String userLogin) {
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		int getUserID = 0;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT UserID "
						 + "FROM Users "
						 + "WHERE Username LIKE BINARY \"" + userLogin + "\";";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
            	getUserID = rs.getInt("UserID");
            }
            statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return getUserID;
	}
}
