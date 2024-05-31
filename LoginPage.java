import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoginPage{
	JFrame loginPageFrame;

	JButton loginButton;
	JButton registerButton;  
	JButton usersButton;
	JButton wholeDBButton;
	JButton exitButton;  

    JPanel loginPagePanel;
    JDialog loginDialog;

	LoginPage()
	{
		final int HEIGHT = 400;
		final int WIDTH = 330;
		final int LocationX = 400;
		final int LocationY = 200;

		loginPageFrame = new JFrame();

		//
		// Adjust buttons
		//
		loginButton = new JButton("Login");
		registerButton = new JButton("Register");
		usersButton = new JButton("Users");
		exitButton = new JButton("Exit");
		wholeDBButton = new JButton("View DB");

		//
		// Button should allow us to login
		//
		loginButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				LoginInformationPage loginInformationPage = new LoginInformationPage(loginDialog);
			}
		});

		//
		// Register button should open Registration Dialog
		//
		registerButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				RegistrationPage registrationPage = new RegistrationPage();
			}
		});

		//
		// wholeDBButton button should open DB contents
		//
		wholeDBButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				wholeDBPage wholeDBPage = new wholeDBPage();
			}
		});
		
		//
		// Users button should open Users from DB
		//
		usersButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				@SuppressWarnings("unused")
				usersPage usersPage = new usersPage();
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
		// Finalize Login Page Panel and Frame
		//
		loginPagePanel = new JPanel(new GridLayout(5, 1, 0, 15));

		loginPagePanel.add(loginButton);
		loginPagePanel.add(registerButton);
		loginPagePanel.add(usersButton);
		loginPagePanel.add(wholeDBButton);
		loginPagePanel.add(exitButton);

		loginPageFrame.add(loginPagePanel);

		//
		// Finalize Login Dialog
		//
		loginDialog = new JDialog(loginPageFrame, "Airfare Ticket Purchase System", true);
		loginDialog.getContentPane().add(loginPagePanel);
		loginDialog.pack();
		loginDialog.setSize(WIDTH, HEIGHT);
		loginDialog.setLocation(LocationX, LocationY);
		loginDialog.setVisible(true);
	}
}