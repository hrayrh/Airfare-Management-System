import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class viewBookingsPage {
	JFrame viewBookingsPageFrame;

	JScrollPane tableScrollPane;
    JPanel viewBookingsPagePanel;
    JDialog viewBookingsDialog;
    
    viewBookingsPage(int userID)
    {
		final int HEIGHT = 270;
		final int WIDTH = 480;
		final int LocationX = 400;
		final int LocationY = 200;
		viewBookingsPageFrame = new JFrame();
    	
    	//
    	// Display Airports from DB into JTable items
    	//
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT * FROM bookings WHERE UserID = " + userID;
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			JTable itemsTable = new JTable(buildTableModel(resultSet));
			tableScrollPane = new JScrollPane(itemsTable);
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//
		// Finalize View Airports Page Panel and Frame
		//
		viewBookingsPagePanel = new JPanel(new GridLayout(1, 1, 0, 0));
		viewBookingsPagePanel.add(tableScrollPane);
		viewBookingsPageFrame.add(viewBookingsPagePanel);

		// 
		// Finalize the View Airports Dialog
		//
		viewBookingsDialog = new JDialog(viewBookingsPageFrame, "My Bookings", true);
		viewBookingsDialog.getContentPane().add(viewBookingsPagePanel);
		viewBookingsDialog.pack();
		viewBookingsDialog.setSize(WIDTH,HEIGHT);
		viewBookingsDialog.setLocation(LocationX, LocationY);
		viewBookingsDialog.setVisible(true);
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
