import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class modifyUserPage {
	JFrame modifyUserPageFrame;

	JLabel columnNameLabel;
	JLabel currentValueLabel;
	JLabel modifiedValueLabel;
	JLabel usernameLabel;
	JLabel passwordLabel;
	JLabel firstNameLabel;
	JLabel lastNameLabel;
	JTextField currentUsernameLabelTextField;
	JTextField currentPasswordTextField;
	JTextField currentFirstNameTextField;
	JTextField currentLastNameTextField;
	JTextField ModifyUsernameTextField;
	JPasswordField ModifyPasswordPasswordField;
	JTextField ModifyFirstNameTextField;
	JTextField ModifyLastNameTextField;
	JButton backButton;
	JButton modifyButton;

    JPanel modifyUserPagePanel;
    JDialog modifyUserPageDialog;
    
    modifyUserPage(int intModifyID) {
		final int HEIGHT = 350;
		final int WIDTH = 600;
		final int LocationX = 400;
		final int LocationY = 200;
		
		modifyUserPageFrame = new JFrame();

		//
		// Adjust Labels
		//
		columnNameLabel = new JLabel("Column Name");
		currentValueLabel = new JLabel("Current Value");
		modifiedValueLabel = new JLabel("Modified Value");
		usernameLabel = new JLabel("Username");
		passwordLabel = new JLabel("Password");
		firstNameLabel = new JLabel("FirstName");
		lastNameLabel = new JLabel("LastName");	
		columnNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		modifiedValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		firstNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lastNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust TextFields
		//
		currentUsernameLabelTextField = new JTextField(20);
		currentPasswordTextField = new JTextField(20);
		currentFirstNameTextField = new JTextField(20);
		currentLastNameTextField = new JTextField(20);
		ModifyUsernameTextField = new JTextField(20);
		ModifyPasswordPasswordField = new JPasswordField(20);
		ModifyFirstNameTextField = new JTextField(20);
		ModifyLastNameTextField = new JTextField(20);
		currentUsernameLabelTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentPasswordTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentFirstNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		currentLastNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyUsernameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyPasswordPasswordField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyFirstNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyLastNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		ModifyUsernameTextField.setText(getUserNameByID(intModifyID));
		ModifyPasswordPasswordField.setText(getPasswordByID(intModifyID));
		ModifyFirstNameTextField.setText(getFirstNameByID(intModifyID));
		ModifyLastNameTextField.setText(getLastNameByID(intModifyID));

		//
		// Current values should not be editable
		//
		currentUsernameLabelTextField.setEditable(false);
		currentUsernameLabelTextField.setText(getUserNameByID(intModifyID));
		currentPasswordTextField.setEditable(false);
		currentPasswordTextField.setText(getPasswordByID(intModifyID));
		currentFirstNameTextField.setEditable(false);
		currentFirstNameTextField.setText(getFirstNameByID(intModifyID));
		currentLastNameTextField.setEditable(false);
		currentLastNameTextField.setText(getLastNameByID(intModifyID));

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
				modifyUserPageDialog.setVisible(false);
			}
		});

		//
		// Modify User button
		//
		modifyButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String userName = ModifyUsernameTextField.getText();
				String user_password = ModifyPasswordPasswordField.getText();
				String firstName = ModifyFirstNameTextField.getText();
				String lastName = ModifyLastNameTextField.getText();

				//
				// TextField items should be valid
				//
				if (validateUserParameters(firstName, lastName, userName, user_password)) {
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
						String query = "UPDATE Users "
									 + "SET Username = \"" + userName + "\""
									 + ", Password = \"" + user_password + "\""
									 + ", FirstName = \"" + firstName + "\""
									 + ", LastName = \"" +  lastName + "\""
									 + " WHERE (UserID = " + intModifyID + ")";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					modifyUserPageDialog.setVisible(false);
				}
			}
		});

		//
		// Finalize Modify Airport Page Panel and Frame
		//
		modifyUserPagePanel = new JPanel(new GridLayout(6, 3, 10, 10));
		modifyUserPagePanel.add(columnNameLabel);
		modifyUserPagePanel.add(currentValueLabel);
		modifyUserPagePanel.add(modifiedValueLabel);
		modifyUserPagePanel.add(usernameLabel);
		modifyUserPagePanel.add(currentUsernameLabelTextField);
		modifyUserPagePanel.add(ModifyUsernameTextField);
		modifyUserPagePanel.add(passwordLabel);
		modifyUserPagePanel.add(currentPasswordTextField);
		modifyUserPagePanel.add(ModifyPasswordPasswordField);
		modifyUserPagePanel.add(firstNameLabel);
		modifyUserPagePanel.add(currentFirstNameTextField);
		modifyUserPagePanel.add(ModifyFirstNameTextField);
		modifyUserPagePanel.add(lastNameLabel);
		modifyUserPagePanel.add(currentLastNameTextField);
		modifyUserPagePanel.add(ModifyLastNameTextField);
		modifyUserPagePanel.add(backButton);
		modifyUserPagePanel.add(new JLabel(""));
		modifyUserPagePanel.add(modifyButton);

		modifyUserPageFrame.add(modifyUserPagePanel);

		// 
		// Finalize the Modify User Dialog
		//
		modifyUserPageDialog = new JDialog(modifyUserPageFrame, "Modify User", true);
		modifyUserPageDialog.getContentPane().add(modifyUserPagePanel);
		modifyUserPageDialog.pack();
		modifyUserPageDialog.setSize(WIDTH,HEIGHT);
		modifyUserPageDialog.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		modifyUserPageDialog.setLocation(LocationX, LocationY);
		modifyUserPageDialog.setVisible(true);
    }
    
	//
	//	Validate Modified User Parameters: Names, login user name, Password
	//
	protected boolean validateUserParameters(String firstName, String lastName, String userLogin, String userPassword) {
		//
		// Names validation
		//
		if (!firstName.chars().allMatch(Character::isLetter)) {
			JOptionPane.showMessageDialog(null, "First name should only contain letters!", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!lastName.chars().allMatch(Character::isLetter)) {
			JOptionPane.showMessageDialog(null, "Last name should only contain letters!", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//
		// Login length validation
		//
		if (userLogin.length() < 5) {
			JOptionPane.showMessageDialog(null, "Login should contain at least 5 characters", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//
		// Password validation
		//
	    String passwordRegExpn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,25}$";
	    Pattern passwordPattern = Pattern.compile(passwordRegExpn, Pattern.CASE_INSENSITIVE);
	    Matcher passwordMatcher = passwordPattern.matcher(userPassword);

		if (!passwordMatcher.matches()) {
			JOptionPane.showMessageDialog(null, "Password should contain at least 8 characters, 1 small letter, 1 capital letter, 1 digit, 1 special symbol and max 25 characters", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		//
		// Validation passed
		//
		JOptionPane.showMessageDialog(null, "Registration completed succesfully!", "Registration Finish", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	//
	// Get user name by ID
	//
	protected String getUserNameByID (int userID)
	{
		String userName = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT Username "
						 + "FROM Users "
						 + "WHERE UserID = " + userID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	userName = rs.getString("Username");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return userName;
	}
	
	//
	// Get password by ID
	//
	protected String getPasswordByID (int userID)
	{
		String pass = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT Password "
						 + "FROM Users "
						 + "WHERE UserID = " + userID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	pass = rs.getString("Password");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return pass;
	}

	//
	// Get first name by ID
	//
	protected String getFirstNameByID (int userID)
	{
		String firstName = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT FirstName "
						 + "FROM Users "
						 + "WHERE UserID = " + userID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	firstName = rs.getString("FirstName");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return firstName;
	}
	
	//
	// Get Last Name by ID
	//
	protected String getLastNameByID (int userID)
	{
		String lastName = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT LastName "
						 + "FROM Users "
						 + "WHERE UserID = " + userID;
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
            	lastName = rs.getString("LastName");
            }
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return lastName;
	}
}
