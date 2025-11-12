package org.barberia.usuarios.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection getConnection() throws SQLException {
      

        String url = "jdbc:postgresql://www.tecnoweb.org.bo:5432/db_grupo11sa";
        String user = "grupo11sa";
        String pass = "grup011grup011*";
        
         /*  String url = env("DB_URL", "jdbc:postgresql://localhost:5432/barberia");
          String user = env("DB_USER", "postgres");
         String pass = env("DB_PASSWORD", "ale12345678");
         */

        return DriverManager.getConnection(url, user, pass);
    }

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return v != null && !v.isBlank() ? v : def;
    }
}




  /*
         * String url = env("DB_URL", "jdbc:postgresql://localhost:5432/barberia");
         * String user = env("DB_USER", "postgres");
         * String pass = env("DB_PASSWORD", "ale12345678");
         */