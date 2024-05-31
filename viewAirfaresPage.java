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

public class viewAirfaresPage {
	JFrame viewAirfaresPageFrame;

	JScrollPane tableScrollPane;
    JPanel viewAirfaresPagePanel;
    JDialog viewAirfaresDialog;
    
    viewAirfaresPage()
    {
		final int HEIGHT = 270;
		final int WIDTH = 480;
		final int LocationX = 400;
		final int LocationY = 200;
		viewAirfaresPageFrame = new JFrame();
		
		//
    	// Display Air fares from DB into JTable items
    	//
		final String url = "jdbc:mysql://localhost:3306/airfare";
		final String username = "root";
		final String password = "s#MF!xh9";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			Statement statement = null;
			String query = "SELECT * FROM Airfares";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			JTable itemsTable = new JTable(buildTableModel(resultSet));
			tableScrollPane = new JScrollPane(itemsTable);
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		//
		// Finalize View Air fares Page Panel and Frame
		//
		viewAirfaresPagePanel = new JPanel(new GridLayout(1, 1, 0, 0));
		viewAirfaresPagePanel.add(tableScrollPane);
		viewAirfaresPageFrame.add(viewAirfaresPagePanel);
		
		// 
		// Finalize the View Air fares Dialog
		//
		viewAirfaresDialog = new JDialog(viewAirfaresPageFrame, "Airfares", true);
		viewAirfaresDialog.getContentPane().add(viewAirfaresPagePanel);
		viewAirfaresDialog.pack();
		viewAirfaresDialog.setSize(WIDTH,HEIGHT);
		viewAirfaresDialog.setLocation(LocationX, LocationY);
		viewAirfaresDialog.setVisible(true);
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
