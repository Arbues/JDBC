package uni.aed.example_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Example_db {

    private static final Scanner scanner = new Scanner(System.in);
    private static Connection connection;
    private static Statement stmt;

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost/postgres";
        try {
            connection = DriverManager.getConnection(url, "postgres", "postgres");
                stmt = connection.createStatement();

            boolean continuar = true;
            while (continuar) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Consultar datos");
                System.out.println("2. Insertar datos");
                System.out.println("3. Actualizar datos");
                System.out.println("4. Eliminar datos");
                System.out.println("5. Salir");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consume el salto de línea

                switch (opcion) {
                    case 1:
                        select();
                        break;
                    case 2:
                        insert();
                        break;
                    case 3:
                        update();
                        break;
                    case 4:
                        delete();
                        break;
                    case 5:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
                scanner.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void select() {
        System.out.print("Ingrese su consulta SQL personalizada: ");
        String consulta = scanner.nextLine();

        try (ResultSet rs = stmt.executeQuery(consulta)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + rs.getString(i) + " ");
                }
                System.out.println(); // Nueva línea después de cada fila
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    private static void insert() {
        System.out.print("Ingrese el ID del cliente: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume el salto de línea
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese la edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine(); // Consume el salto de línea

        String query = "INSERT INTO clientes (id, nombre, apellido, edad) VALUES (" + id + ", '" + nombre + "', '" + apellido + "', " + edad + ")";

        try {
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Datos insertados correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
        }
    }

    private static void update() {
        System.out.print("Ingrese el ID del cliente a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume el salto de línea
        System.out.print("Ingrese el nuevo nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el nuevo apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese la nueva edad: ");
        int edad = scanner.nextInt();

        String query = "UPDATE clientes SET nombre = '" + nombre + "', apellido = '" + apellido + "', edad = " + edad + " WHERE id = " + id;

        try {
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Datos actualizados correctamente.");
            } else {
                System.out.println("No se encontró el registro.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar datos: " + e.getMessage());
        }
    }

    private static void delete() {
        System.out.print("Ingrese el ID del cliente a eliminar: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM clientes WHERE id = " + id;

        try {
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Datos eliminados correctamente.");
            } else {
                System.out.println("No se encontró el registro.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar datos: " + e.getMessage());
        }
    }

}
