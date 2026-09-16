package main.java.com.programadoreschidos.abarroteria.kinal.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.config.DataBaseConnection;
import main.java.com.programadoreschidos.abarroteria.kinal.dto.response.FacturaDetalleDTOResponse;
import main.java.com.programadoreschidos.abarroteria.kinal.model.DetalleFactura;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Factura;

public class FacturaRepository {

    // Solo encabezados, útil para un listado simple de facturas
    public ObservableList<Factura> findAll() {
        String sql = "SELECT * FROM facturas ORDER BY fecha DESC;";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            ObservableList<Factura> lista = FXCollections.observableArrayList();

            while (rs.next()) {
                lista.add(new Factura(
                    rs.getString("id_factura"),
                    rs.getString("id_cliente"),
                    rs.getBigDecimal("monto"),
                    rs.getTimestamp("fecha").toLocalDateTime()
                ));
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error en la consulta de facturas.");
        }
    }

    public boolean existeCliente(String idCliente) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE id_cliente = ?";
        // Usamos DataBaseConnection que es la clase que ya tienes en tu proyecto
        try (Connection conn = DataBaseConnection.getDataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Guarda la factura y todos sus detalles en una sola transacción:
     * si algo falla, no queda ni el encabezado ni ninguna línea a medias.
     */
    public void save(Factura factura, List<DetalleFactura> detalles) {
        String sqlFactura = "INSERT INTO facturas (id_factura, id_cliente, monto, fecha) VALUES (?, ?, ?, ?);";
        String sqlDetalle = "INSERT INTO detalles_facturas (id_detalle_factura, id_producto, id_factura, cantidad_comprada) VALUES (?, ?, ?, ?);";

        Connection conn = null;
        try {
            conn = DataBaseConnection.getDataBaseConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmFactura = conn.prepareStatement(sqlFactura)) {
                pstmFactura.setString(1, factura.getIdFactura());
                pstmFactura.setString(2, factura.getIdCliente());
                pstmFactura.setBigDecimal(3, factura.getMonto());
                pstmFactura.setTimestamp(4, Timestamp.valueOf(factura.getFecha()));
                pstmFactura.executeUpdate();
            }

            try (PreparedStatement pstmDetalle = conn.prepareStatement(sqlDetalle)) {
                for (DetalleFactura detalle : detalles) {
                    pstmDetalle.setString(1, detalle.getIdDetalleFactura());
                    pstmDetalle.setString(2, detalle.getIdProducto());
                    pstmDetalle.setString(3, detalle.getIdFactura());
                    pstmDetalle.setInt(4, detalle.getCantidadComprada());
                    pstmDetalle.addBatch();
                }
                pstmDetalle.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la factura.");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Une facturas + detalles_facturas + productos para UNA factura específica
    public ObservableList<FacturaDetalleDTOResponse> findDetallesByFactura(String idFactura) {
        String sql = "SELECT f.id_factura, f.id_cliente, f.monto, f.fecha, "
                + "df.id_detalle_factura, df.id_producto, p.nombre_producto, p.precio, df.cantidad_comprada "
                + "FROM facturas f "
                + "INNER JOIN detalles_facturas df ON f.id_factura = df.id_factura "
                + "INNER JOIN productos p ON df.id_producto = p.id_productos "
                + "WHERE f.id_factura = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            pstm.setString(1, idFactura);
            ResultSet rs = pstm.executeQuery();
            return mapearResultado(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar el detalle de la factura.");
        }
    }

    // Une facturas + detalles_facturas + productos para TODO el historial (reportes)
    public ObservableList<FacturaDetalleDTOResponse> findAllUnificado() {
        String sql = "SELECT f.id_factura, f.id_cliente, f.monto, f.fecha, "
                + "df.id_detalle_factura, df.id_producto, p.nombre_producto, p.precio, df.cantidad_comprada "
                + "FROM facturas f "
                + "INNER JOIN detalles_facturas df ON f.id_factura = df.id_factura "
                + "INNER JOIN productos p ON df.id_producto = p.id_productos "
                + "ORDER BY f.fecha DESC;";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            return mapearResultado(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar el historial de facturas.");
        }
    }

    private ObservableList<FacturaDetalleDTOResponse> mapearResultado(ResultSet rs) throws SQLException {
        ObservableList<FacturaDetalleDTOResponse> lista = FXCollections.observableArrayList();

        while (rs.next()) {
            LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
            lista.add(new FacturaDetalleDTOResponse(
                rs.getString("id_factura"),
                rs.getString("id_cliente"),
                rs.getBigDecimal("monto"),
                fecha,
                rs.getString("id_detalle_factura"),
                rs.getString("id_producto"),
                rs.getString("nombre_producto"),
                rs.getBigDecimal("precio"),
                rs.getInt("cantidad_comprada")
            ));
        }
        return lista;
    }
}