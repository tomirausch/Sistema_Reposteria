package reposteria.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDatos {
    private static BaseDatos instance;
    private Connection conn;

    private BaseDatos(String dbName) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            crearTablas();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la DB: " + e.getMessage());
        }
    }

    public static BaseDatos getInstance() {
        if (instance == null) {
            instance = new BaseDatos("reposteria.db");
        }
        return instance;
    }

    private void crearTablas() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "telefono TEXT," +
                "direccion TEXT," +
                "activo INTEGER DEFAULT 1" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS productos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "precio REAL" +
                "activo INTEGER DEFAULT 1" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS pedidos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cliente_id INTEGER," +
                "fecha TEXT," +
                "total REAL," +
                "FOREIGN KEY(cliente_id) REFERENCES clientes(id)" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS detalles_pedidos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pedido_id INTEGER," +
                "producto_id INTEGER," +
                "cantidad INTEGER," +
                "subtotal REAL," +
                "FOREIGN KEY(pedido_id) REFERENCES pedidos(id)," +
                "FOREIGN KEY(producto_id) REFERENCES productos(id)" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS transacciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo TEXT," +
                "monto REAL," +
                "descripcion TEXT," +
                "fecha TEXT" +
                ")");
    }

    public Connection getConnection() {
        return conn;
    }

    public void cerrar() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar DB: " + e.getMessage());
        }
    }
}