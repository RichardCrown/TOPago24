package com.example.tap2024b.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private int idCte;
    private String nomCte;
    private String telCte;
    private String emailCte;

    public int getIdCte() {
        return idCte;
    }

    public void setIdCte(int idCte) {
        this.idCte = idCte;
    }

    public String getNomCte() {
        return nomCte;
    }

    public void setNomCte(String nomCte) {
        this.nomCte = nomCte;
    }

    public String getTelCte() {
        return telCte;
    }

    public void setTelCte(String telCte) {
        this.telCte = telCte;
    }

    public String getEmailCte() {
        return emailCte;
    }

    public void setEmailCte(String emailCte) {
        this.emailCte = emailCte;
    }

    // Método para insertar un nuevo cliente
    public void INSERT() {
        String query = "INSERT INTO tbcliente(nomCte, telCte, emailCte) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            // Crear la conexión si no está ya creada
            if (Conexion.connection == null) {
                Conexion.CrearConexion();
            }
            conn = Conexion.connection;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, this.nomCte);
            pstmt.setString(2, this.telCte);
            pstmt.setString(3, this.emailCte);

            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Cliente insertado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un cliente existente
    public void UPDATE() {
        String query = "UPDATE tbcliente SET nomCte = ?, telCte = ?, emailCte = ? WHERE idCte = ?";
        Connection conn = null;

        try {
            if (Conexion.connection == null) {
                Conexion.CrearConexion();
            }
            conn = Conexion.connection;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, this.nomCte);
            pstmt.setString(2, this.telCte);
            pstmt.setString(3, this.emailCte);
            pstmt.setInt(4, this.idCte);

            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Cliente actualizado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un cliente
    public void DELETE() {
        String query = "DELETE FROM tbcliente WHERE idCte = ?";
        Connection conn = null;

        try {
            if (Conexion.connection == null) {
                Conexion.CrearConexion();
            }
            conn = Conexion.connection;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, this.idCte);

            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Cliente eliminado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obtener un cliente específico por su ID
    public ClienteDAO SELECT(int idCte) {
        String query = "SELECT * FROM tbcliente WHERE idCte = ?";
        ClienteDAO cliente = null;
        Connection conn = null;

        try {
            if (Conexion.connection == null) {
                Conexion.CrearConexion();
            }
            conn = Conexion.connection;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idCte);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                cliente = new ClienteDAO();
                cliente.setIdCte(rs.getInt("idCte"));
                cliente.setNomCte(rs.getString("nomCte"));
                cliente.setTelCte(rs.getString("telCte"));
                cliente.setEmailCte(rs.getString("emailCte"));
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cliente;
    }

    // Método para obtener todos los clientes
    public List<ClienteDAO> SELECT_ALL() {
        String query = "SELECT * FROM tbcliente";
        List<ClienteDAO> clientes = new ArrayList<>();
        Connection conn = null;

        try {
            if (Conexion.connection == null) {
                Conexion.CrearConexion();
            }
            conn = Conexion.connection;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ClienteDAO cliente = new ClienteDAO();
                cliente.setIdCte(rs.getInt("idCte"));
                cliente.setNomCte(rs.getString("nomCte"));
                cliente.setTelCte(rs.getString("telCte"));
                cliente.setEmailCte(rs.getString("emailCte"));
                clientes.add(cliente);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientes;
    }
}
