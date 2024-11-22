package com.example.tap2024b.models;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    static private String DB = "spotify";
    static private String USER = "root";
    static private String PASS = "sistemaPlan";
    static private String HOST = "localhost";
    static private String PORT = "3306";
    public static Connection connection;

    // Método para crear la conexión
    public static void CrearConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DB, USER, PASS);
            System.out.println("Conexión establecida a la base de datos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
