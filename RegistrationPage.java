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

public class RegistrationPage {
	JFrame registrationPageFrame;

	JLabel firstNameLabel;
	JLabel lastNameLabel;
	JLabel loginLabel;
	JLabel passwordLabel;
	JTextField firstNameTextField;
	JTextField lastNameTextField;
	JTextField loginTextField;
	JPasswordField passwordTextField;
	JButton backButton;
	JButton registerButton;

    JPanel registrationPagePanel;
    JDialog registrationDialog;

	RegistrationPage()
	{
		final int HEIGHT = 210;
		final int WIDTH = 480;
		final int LocationX = 400;
		final int LocationY = 200;
		
		registrationPageFrame = new JFrame();

		//
		// Adjust labels
		//
		firstNameLabel = new JLabel("Enter your first name:");
		lastNameLabel = new JLabel("Enter your last name:");
		loginLabel = new JLabel("Enter login:");
		passwordLabel = new JLabel("Enter password:");
		firstNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lastNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Finalize TextFields
		//
		firstNameTextField = new JTextField(20);
		lastNameTextField = new JTextField(20);
		loginTextField = new JTextField(20);
		passwordTextField = new JPasswordField(20);
		passwordTextField.setEchoChar('*');

		firstNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		lastNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		loginTextField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordTextField.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Add buttons
		//
		backButton = new JButton("Back");
		registerButton = new JButton("Register");

		//
		// Back button should go back to Login page
		//
		backButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				registrationDialog.setVisible(false);
			}
		});

		//
		// Registration button
		//
		registerButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String firstName = firstNameTextField.getText();
				String lastName = lastNameTextField.getText();
				String userLogin = loginTextField.getText();
				@SuppressWarnings("deprecation")
				String userPassword = passwordTextField.getText();

				//
				// TextField items should be valid
				//
				if (validateRegistrationParameters(firstName, lastName, userLogin, userPassword)) {
					final String url = "jdbc:mysql://localhost:3306/airfare";
					final String username = "root";
					final String password = "s#MF!xh9";
					Connection connection = null;
					
					//
					// Register the user into DB
					//
					try {
						connection = DriverManager.getConnection(url, username, password);
						
						Statement statement = null;
						String query = "INSERT INTO Users (Username, Password, FirstName, LastName)" 
									 + "VALUES"
									 + "('" + userLogin + "','" + userPassword + "','" + firstName + "','" + lastName + "');";
						statement = connection.createStatement();
						statement.executeUpdate(query);
						statement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					registrationDialog.setVisible(false);
				}
			}
		});

		//
		// Finalize Registration Page Panel and Frame
		//
		registrationPagePanel = new JPanel(new GridLayout(5, 2, 10, 10));
		registrationPagePanel.add(firstNameLabel);
		registrationPagePanel.add(firstNameTextField);
		registrationPagePanel.add(lastNameLabel);
		registrationPagePanel.add(lastNameTextField);
		registrationPagePanel.add(loginLabel);
		registrationPagePanel.add(loginTextField);
		registrationPagePanel.add(passwordLabel);
		registrationPagePanel.add(passwordTextField);
		registrationPagePanel.add(backButton);
		registrationPagePanel.add(registerButton);

		registrationPageFrame.add(registrationPagePanel);

		// 
		// Finalize the Registration Dialog
		//
		registrationDialog = new JDialog(registrationPageFrame, "Registration", true);
		registrationDialog.getContentPane().add(registrationPagePanel);
		registrationDialog.pack();
		registrationDialog.setSize(WIDTH, HEIGHT);
		registrationDialog.setLocation(LocationX, LocationY);
		registrationDialog.setVisible(true);
	}

	//
	//	Validate Registration Parameters: Names, login user name, Password
	//
	protected boolean validateRegistrationParameters(String firstName, String lastName, String userLogin, String userPassword) {
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
		// Login Existing validation
		//
		boolean loginExists = false;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT Username "
						 + "FROM Users "
						 + "WHERE EXISTS (SELECT Username FROM Users WHERE Users.Username = \"" + userLogin + "\")";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            loginExists = rs.next();
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if (loginExists) {
			JOptionPane.showMessageDialog(null, "Login already in use", "Validation Error", JOptionPane.ERROR_MESSAGE);
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
}
