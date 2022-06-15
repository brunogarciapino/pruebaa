package DAO

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectionBuilder {
    // TODO Auto-generated catch block
    lateinit var connection: Connection

    // La URL de conexión. Tendremos que cambiarsa según el SGBD que se use.
    private val jdbcURL = "jdbc:h2:mem:default"
    private val jdbcUsername = ""
    private val jdbcPassword = ""

    init {
        try {
            // Aqui construimos la conexión
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)
        } catch (e: SQLException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
    // Si termina sin excepción, habrá creado la conexión.

}