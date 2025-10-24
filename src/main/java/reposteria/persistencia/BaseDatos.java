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
        
        try {
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            System.out.println("Error al habilitar claves foráneas: " + e.getMessage());
        }

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
                "precio REAL," +
                "unidad TEXT NOT NULL CHECK (UPPER(unidad) IN ('KG', 'DOCENA', 'CM'))," +
                "medida REAL DEFAULT 0," +
                "activo INTEGER DEFAULT 1" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS variante (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "producto_id INTEGER NOT NULL, " +
                "descripcion TEXT NOT NULL," +
                "precio_extra REAL DEFAULT 0," +
                "activo BOOLEAN DEFAULT TRUE," +
                "FOREIGN KEY (producto_id) REFERENCES producto(id)" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS pedidos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cliente_id INTEGER," +
                "fecha TEXT," +
                "total REAL," +
                "FOREIGN KEY(cliente_id) REFERENCES clientes(id)" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS detalles_pedidos (" +
                "id_detalle_pedido INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pedido_id INTEGER," +
                "producto_id INTEGER," +
                "variante_id INTEGER," +
                "cantidad INTEGER," +
                "subtotal REAL," +
                "FOREIGN KEY(pedido_id) REFERENCES pedidos(id)," +
                "FOREIGN KEY(producto_id) REFERENCES productos(id)" +
                "FOREIGN KEY(variante_id) REFERENCES variante(id)" +
                ")");
        stmt.execute("CREATE TABLE IF NOT EXISTS transacciones (" +
                "id_transaccion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo_transaccion TEXT," +
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