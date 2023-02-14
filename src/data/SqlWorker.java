package src.data;

import src.entity.transport.Bike;
import src.entity.transport.Car;
import src.entity.transport.Transport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlWorker {
    private static final String NAME_USER = "nikolay";
    private static final String PASSWORD = "root";
    private static final String TABLE = "transports";
    private static final String URL = "jdbc:mysql://localhost:3306/" + TABLE;
    private static Connection connection;


    public static void save(ArrayList<Transport> list, String selectedType) {
        connect();

        try {
            connection
                    .createStatement()
                    .executeUpdate("DELETE FROM " + TABLE + " WHERE type = '" + selectedType + "' AND id > 0");

            for (var tr: list) {
                if (tr.getClass().getSimpleName().equals(selectedType)) {
                    StringBuilder command = new StringBuilder("INSERT ");
                    command.append(TABLE);
                    command.append(" (id, type, x, y, lifetime) ");
                    String lifetime = Float.toString(tr.getLifetime()).replace(",", ".");
                    command.append(String.format("VALUES (%d, '%s', %d, %d, %s)",
                            tr.getId(), selectedType, tr.getX(), tr.getY(), lifetime));

                    connection.createStatement().executeUpdate(command.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        close();
    }

    public static ArrayList<Transport> load(String selectedType) {
        connect();

        ArrayList<Transport> transports = new ArrayList<Transport>();

        try {
            ResultSet resultSet = connection
                    .createStatement()
                    .executeQuery("SELECT * FROM " + TABLE + " WHERE type = '" + selectedType + "'");

            while (resultSet.next())
            {
                int id = resultSet.getInt(1);
                String type = resultSet.getString(2);
                int x = resultSet.getInt(3);
                int y = resultSet.getInt(4);
                int lifeTime = resultSet.getInt(5);

                if (type.equals("Car")) {
                    Car car = new Car(x, y, 0, lifeTime);
                    transports.add(car);
                } else {
                    Bike bike = new Bike(x, y, 0, lifeTime);
                    transports.add(bike);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        close();
        return transports;
    }

    private static void connect() {
        try {
            connection = DriverManager.getConnection(URL, NAME_USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
