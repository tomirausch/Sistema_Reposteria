package reposteria.persistencia.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import reposteria.logica.Variante;
import reposteria.persistencia.dao.VarianteDAO;

public class VarianteDAOImpl implements VarianteDAO {
    private Connection conn;

    public VarianteDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void agregar(Variante v) throws SQLException {
        String sql = "INSERT INTO variante (id_producto, descripcion, precio_extra, activo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, v.getIdProducto());
            ps.setString(2, v.getDescripcion());
            ps.setDouble(3, v.getPrecioExtra());
            ps.setBoolean(4, v.isActivo());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Variante> listarPorProducto(int idProducto) throws SQLException {
        List<Variante> variantes = new ArrayList<>();
        String sql = "SELECT * FROM variante WHERE id_producto = ? AND activo = true";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Variante v = new Variante();
                v.setIdVariante(rs.getInt("id_variante"));
                v.setIdProducto(rs.getInt("id_producto"));
                v.setDescripcion(rs.getString("descripcion"));
                v.setPrecioExtra(rs.getDouble("precio_extra"));
                v.setActivo(rs.getBoolean("activo"));
                variantes.add(v);
            }
        }
        return variantes;
    }

    @Override
    public void modificar(Variante v) throws SQLException {
        String sql = "UPDATE variante SET descripcion = ?, precio_extra = ?, activo = ? WHERE id_variante = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getDescripcion());
            ps.setDouble(2, v.getPrecioExtra());
            ps.setBoolean(3, v.isActivo());
            ps.setInt(4, v.getIdVariante());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int idVariante) throws SQLException {
        String sql = "UPDATE variante SET activo = false WHERE id_variante = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVariante);
            ps.executeUpdate();
        }
    }
}
