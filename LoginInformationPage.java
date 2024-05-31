import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginInformationPage{
	JFrame loginInformationPageFrame;

	JLabel loginLabel;
	JLabel passwordLabel;
	JTextField loginTextField;
	JPasswordField passwordTextField;
	JButton backButton;
	JButton loginButton;

    JPanel loginInformationPagePanel;
    JDialog loginInformationDialog;

    LoginInformationPage(JDialog loginDialog)
	{
		final int HEIGHT = 200;
		final int WIDTH = 300;
		final int LocationX = 400;
		final int LocationY = 200;

		loginInformationPageFrame = new JFrame();

		//
		// Adjust labels
		//
		loginLabel = new JLabel("Enter login:");
		passwordLabel = new JLabel("Enter password:");
		loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust textFields
		//
		loginTextField = new JTextField(20);
		passwordTextField = new JPasswordField(20);
		passwordTextField.setEchoChar('*');
		
		//
		// Adjust buttons
		//
		backButton = new JButton("Back");
		loginButton = new JButton("Login");

		//
		// Back button should go back to Login page
		//
		backButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				loginInformationDialog.setVisible(false);
			}
		});

		//
		// login Button should login into main application window
		//
		loginButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String userLogin = loginTextField.getText();
				@SuppressWarnings("deprecation")
				String userPassword = passwordTextField.getText();

				//
				// TextField items should be valid
				//
				if (validateUser(userLogin, userPassword)) {
					loginInformationDialog.setVisible(false);
					loginDialog.setVisible(false);
					@SuppressWarnings("unused")
					MainPage mainPage = new MainPage(userLogin);
				}			
			}
		});
		
		//
		// Enter on passwordTextField will trigger the login button
		//
		passwordTextField.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		            @SuppressWarnings("unused")
					ActionEvent actionEvent = new ActionEvent(e.getSource(), e.getID(), "EnterPressed");
		            for (ActionListener listener : loginButton.getActionListeners()) {
		                listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "command"));
		            }
		        }
		    }
		});
		
		//
		// Adjust Panel and Frame
		//
		loginInformationPagePanel = new JPanel(new GridLayout(3, 2, 20, 30));

		loginInformationPagePanel.add(loginLabel);
		loginInformationPagePanel.add(loginTextField);
		loginInformationPagePanel.add(passwordLabel);
		loginInformationPagePanel.add(passwordTextField);
		loginInformationPagePanel.add(backButton);
		loginInformationPagePanel.add(loginButton);

		loginInformationPageFrame.add(loginInformationPagePanel);

		//
		// Adjust Dialog
		//
		loginInformationDialog = new JDialog(loginInformationPageFrame, "Airfare Ticket Purchase System", true);
		loginInformationDialog.getContentPane().add(loginInformationPagePanel);
		loginInformationDialog.pack();
		loginInformationDialog.setSize(WIDTH,HEIGHT);
		loginInformationDialog.setLocation(LocationX, LocationY);
		loginInformationDialog.setVisible(true);
	}

	protected boolean validateUser(String userLogin, String userPassword) {
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		boolean userExists = true;
		//
		// check if user exists
		//
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT Username "
						 + "FROM Users "
						 + "WHERE EXISTS (SELECT Username FROM Users WHERE Users.Username LIKE BINARY \"" + userLogin 
						 + "\" AND Users.Password LIKE BINARY \"" + userPassword + "\")";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            userExists = rs.next();
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(!userExists)
		{
			JOptionPane.showMessageDialog(null, "Username or Password invalid!", "Login Error", JOptionPane.ERROR_MESSAGE);
		}
		return userExists;
	}
}
