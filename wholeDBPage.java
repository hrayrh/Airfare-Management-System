import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class wholeDBPage {
	JButton deleteButton;
	JButton insertButton;
	JButton modifyButton;
	JButton searchButton;
	JLabel deleteIDLabel;
	JLabel modifyIDLabel;
	JLabel searchInDBLabel;
	JTextField deleteIDTextField;
	JTextField modifyIDTextField;
	JTextField searchTextField;
	JComboBox<String> tableNamesBox;
	JTable DBdataTable;
    JPanel manipulateDBPanel;
	JScrollPane tableScrollPane;
	JDialog wholeDBDialog;

	wholeDBPage()
	{
		final int HEIGHT = 500;
		final int WIDTH = 880;
		final int LocationX = 400;
		final int LocationY = 200;

		//
		// Setup the dialog
		//
		wholeDBDialog = new JDialog();
        wholeDBDialog.setSize(WIDTH,HEIGHT);
        wholeDBDialog.setLocation(LocationX, LocationY);
        wholeDBDialog.setTitle("Database");

		//
		// Adjust Labels
		//
    	deleteIDLabel = new JLabel("Delete ID:");
    	modifyIDLabel = new JLabel("Modify ID:");
    	searchInDBLabel = new JLabel("Search in table:");
    	deleteIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		modifyIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		searchInDBLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//
		// Adjust Buttons
		//
		deleteButton = new JButton("Delete");
		insertButton = new JButton("Insert");
		modifyButton = new JButton("Modify");
		searchButton = new JButton("Search");

		//
		// delete button should delete the item by id from the table
		//
		deleteButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String selectedComboItem = (String) tableNamesBox.getSelectedItem();
				String deleteID = deleteIDTextField.getText();
				deleteFromDB(deleteID, selectedComboItem);	
	            updateTable(selectedComboItem);
			}
		});

		//
		// Modify Button should open a page corresponding to the table
		//
		modifyButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				String selectedComboItem = (String) tableNamesBox.getSelectedItem();
				String modifyID = modifyIDTextField.getText();
				modify(modifyID, selectedComboItem);	
	            updateTable(selectedComboItem);
			}
		});
		//
		// Search button should search matching pattern from searchTextField and display only those rows
		// that contain the pattern
		//
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				String selectedComboItem = (String) tableNamesBox.getSelectedItem();
				String searchPattern = searchTextField.getText();
                search(searchPattern, selectedComboItem);
            }
        });
        
        //
        // Insert button should open a corresponding dialog based on table
        //
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				String selectedComboItem = (String) tableNamesBox.getSelectedItem();
	    		switch(selectedComboItem) {
    	    		case "airports":
    					@SuppressWarnings("unused")
    					addAirportPage addAirportPage = new addAirportPage();
    	    			break;
    	    		case "airfares":
    	    			@SuppressWarnings("unused") 
    	    			addAirfarePage addAirfarePage = new addAirfarePage();
    	    			break;
    	    		case "bookings":
    					@SuppressWarnings("unused")
    					bookAFlightPage bookAFlightPage = new bookAFlightPage(0);
    	    			break;
    	    		case "users":
    					@SuppressWarnings("unused")
    					RegistrationPage registrationPage = new RegistrationPage();
    	    			break;
    	    		default:
    	    			//
    	    			// We should never reach there
    	    			//
    	    			JOptionPane.showMessageDialog(null, "Bug in the code", "Call 911", JOptionPane.ERROR_MESSAGE);
	    		}	
	            updateTable(selectedComboItem);
            }
        });

		//
		// Adjust TextFields
		//
		deleteIDTextField = new JTextField(0);
		modifyIDTextField = new JTextField(0);
		searchTextField = new JTextField(0);
		deleteIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		modifyIDTextField.setHorizontalAlignment(SwingConstants.CENTER);
		searchTextField.setHorizontalAlignment(SwingConstants.CENTER);
		
		//
		// Delete and Modify ID text fields should only contain digits
		//
		Document document1 = deleteIDTextField.getDocument();
		if(document1 instanceof PlainDocument){
		    PlainDocument plainDocument = (PlainDocument)document1;
		    plainDocument.setDocumentFilter(new DigitOnlyDocumentFilter());
		}
		Document document2 = modifyIDTextField.getDocument();
		if(document2 instanceof PlainDocument){
		    PlainDocument plainDocument = (PlainDocument)document2;
		    plainDocument.setDocumentFilter(new DigitOnlyDocumentFilter());
		}

        //
        // Add DB table names into combo box
        //
        tableNamesBox = new JComboBox<>();
        addDataToCombobox(tableNamesBox);

        //
        // Populate table items from DB
        //
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			//
			// Get table name from combo box to display the right table items
			//
			String selectedComboItem = (String) tableNamesBox.getSelectedItem();
			String query = "SELECT * FROM " + selectedComboItem;
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			DBdataTable = new JTable(buildTableModel(resultSet));
			tableScrollPane = new JScrollPane(DBdataTable);
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		//
		// Add an item listener to combo box 
		// so that each time combo box item is changed, table is updated accordingly
		//
		tableNamesBox.addItemListener(new ItemListener() {
		    @Override
		    public void itemStateChanged(ItemEvent e) {
		        if (e.getStateChange() == ItemEvent.SELECTED) {
		            String selectedTableName = (String) tableNamesBox.getSelectedItem();
		            updateTable(selectedTableName);
		        }
		    }
		});
		
		//
		// Adjust data manipulate panel
		//
		manipulateDBPanel = new JPanel();
		manipulateDBPanel.setLayout(new GridLayout(4, 3, 0, 60));

		manipulateDBPanel.add(deleteIDLabel);
		manipulateDBPanel.add(deleteIDTextField);
		manipulateDBPanel.add(deleteButton);

		manipulateDBPanel.add(modifyIDLabel);
		manipulateDBPanel.add(modifyIDTextField);
		manipulateDBPanel.add(modifyButton);

		manipulateDBPanel.add(searchInDBLabel);
		manipulateDBPanel.add(searchTextField);
		manipulateDBPanel.add(searchButton);

		manipulateDBPanel.add(new JLabel(""));
		manipulateDBPanel.add(new JLabel(""));
		manipulateDBPanel.add(insertButton);

        //
        // Finalize the dialog
        //
        wholeDBDialog.setLayout(new BorderLayout());
        wholeDBDialog.add(tableNamesBox, BorderLayout.NORTH);
        wholeDBDialog.add(tableScrollPane, BorderLayout.EAST);
        wholeDBDialog.add(manipulateDBPanel, BorderLayout.CENTER);
        wholeDBDialog.setSize(WIDTH,HEIGHT);
        wholeDBDialog.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        wholeDBDialog.setLocation(LocationX, LocationY);
        wholeDBDialog.pack();
        wholeDBDialog.setModal(true);
        wholeDBDialog.setVisible(true);
	}

	protected void modify(String modifyID, String selectedComboItem) {
		//
		// modifyID cannot be empty
		//
		if (modifyID.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Modify ID is empty", "ID Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int intModifyID = Integer.parseInt(modifyID);
		if(IDExists(modifyID, selectedComboItem)) {
			switch(selectedComboItem) {
    		case "airports":
    			@SuppressWarnings("unused")
				modifyAirportsPage modifyAirportsPage = new modifyAirportsPage(intModifyID);
        		break;
    		case "airfares":
    			@SuppressWarnings("unused")
				modifyAirfaresPage modifyAirfaresPage = new modifyAirfaresPage(intModifyID);
    			break;
    		case "bookings":
    			@SuppressWarnings("unused")
				modifyBookingPage modifyBookingPage = new modifyBookingPage(intModifyID);
    			break;    	    
    		case "users":
    			@SuppressWarnings("unused")
    			modifyUserPage modifyUserPage = new modifyUserPage(intModifyID);
    			break;
    		default:
    			//
    			// We should never reach there
    			//
    			JOptionPane.showMessageDialog(null, "Bug in the code", "Call 911", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Specified ID " + intModifyID + " not found", "ID Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	//
	// Check if ID exists in the DB
	//
	private boolean IDExists(String modifyID, String selectedComboItem) {
		boolean IDExists = false;
	    try {
    		final String url = "jdbc:mysql://localhost:3306/airfare";
    		final String username = "root";
    		final String password = "s#MF!xh9";
    		Connection connection = DriverManager.getConnection(url, username, password);
    		String idName = null;
    		switch(selectedComboItem) {
    	    	case "airports":
    	        	idName = "AirportID";
    	        	break;
    	    	case "airfares":
    	        	idName = "FlightID";
    	        	break;
    	    	case "bookings":
    	        	idName = "BookingID";
    	        	break;    	    
    	    	case "users":
        	    	idName = "UserID";
        	    	break;
    	    	default:
    	    		//
    	    		// We should never reach there
    	    		//
    				JOptionPane.showMessageDialog(null, "Bug in the code", "Call 911", JOptionPane.ERROR_MESSAGE);
    		}	
    		Statement statement = null;
			String query = "SELECT " + idName
					 	 + " FROM " + selectedComboItem
					 	 + " WHERE EXISTS "
					 	 + "(SELECT " + idName + " FROM " + selectedComboItem + " WHERE " 
					 	 + selectedComboItem + "." + idName + " = " + modifyID + ");";
			statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            IDExists = rs.next();
			statement.close();
	    } catch (SQLException e1) {
			e1.printStackTrace();
		}
	    return IDExists;
	}

	//
	// Search pattern in database
	//
	protected void search(String searchPattern, String selectedComboItem) {
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
	        DatabaseMetaData metaData = connection.getMetaData();
	        ResultSet resultSet = metaData.getColumns(null, null, selectedComboItem, null);
	        
	        List<String> columnNames = new ArrayList<>();
	        
	        while (resultSet.next()) {
	            String columnName = resultSet.getString("COLUMN_NAME");
	            columnNames.add(columnName);
	        }
	        
	        String query = "SELECT * FROM " + selectedComboItem + " WHERE ";

	        //
	        // WARNING: This snippet should be removed as it's a bad solution
	        // Somehow users contains a lot of columns
	        //
	        int max_len = 0;
	        if (selectedComboItem.equals("users")) {
	        	max_len = 5;
	        } else {
	        	max_len = columnNames.size();	        	
	        }
	        for (int i = 0; i < max_len; i++) {
	            query += columnNames.get(i) + " LIKE ?";
	            if (i < max_len - 1) {
	                query += " OR ";
	            }
	        }
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        
	        searchPattern = "%" + searchPattern + "%";
	        for (int i = 1; i <= max_len; i++) {
	            preparedStatement.setString(i, searchPattern);
	        }
	        
	        resultSet = preparedStatement.executeQuery();

			DefaultTableModel tableModel = buildTableModel(resultSet);

	        while (resultSet.next()) {
	            Object[] row = new Object[columnNames.size()]; // Number of columns
	            tableModel.addRow(row);
	        }
	        DBdataTable.setModel(tableModel);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	//
	// Delete element from DB
	//
	protected void deleteFromDB(String deleteID, String selectedComboItem) {
	    try {
    		final String url = "jdbc:mysql://localhost:3306/airfare";
    		final String username = "root";
    		final String password = "s#MF!xh9";
    		Connection connection = DriverManager.getConnection(url, username, password);
    		String idName = null;
    		switch(selectedComboItem) {
    	    	case "airports":
    	        	idName = "AirportID";
    	        	break;
    	    	case "airfares":
    	        	idName = "FlightID";
    	        	break;
    	    	case "bookings":
    	        	idName = "BookingID";
    	        	break;    	    
    	    	case "users":
        	    	idName = "UserID";
        	    	break;
    	    	default:
    	    		//
    	    		// We should never reach there
    	    		//
    				JOptionPane.showMessageDialog(null, "Bug in the code", "Call 911", JOptionPane.ERROR_MESSAGE);
    		}
	        String deleteQuery = "DELETE FROM " + selectedComboItem + " WHERE " + idName + " = ?";
	        PreparedStatement statement = connection.prepareStatement(deleteQuery);
	        statement.setString(1, deleteID);
	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected > 0) {
	    		JOptionPane.showMessageDialog(null, 
	    				"Item deleted succesfully!", "Item Deleted", 
	    				JOptionPane.INFORMATION_MESSAGE);
	    	} else {
				JOptionPane.showMessageDialog(null, "No ID found with the specified ID in table", "ID Error", JOptionPane.ERROR_MESSAGE);
	        }
	        statement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	//
	// Adds DB table names into combo box
	//
	private void addDataToCombobox(JComboBox<String> comboBox) {
        try {
    		final String url = "jdbc:mysql://localhost:3306/airfare";
    		final String username = "root";
    		final String password = "s#MF!xh9";
    		Connection connection = null;
			connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                comboBox.addItem(resultSet.getString(1));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	//
	// update table items
	//
	private void updateTable(String comboboxItem) {
		DefaultTableModel tableModel = null;
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			//
			// Get table name from combo box to display the right table items
			//
			String query = "SELECT * FROM " + comboboxItem;
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
	        tableModel = buildTableModel(resultSet);
	        DBdataTable.setModel(tableModel);
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

    //
    // Get Table model for displaying items
    //
    protected static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getObject(i);
            }
            tableModel.addRow(rowData);
        }
        return tableModel;
    }
}
