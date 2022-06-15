package DAO

import Grupo
import java.sql.Connection
import java.sql.SQLException

class GRUPOS (val c: Connection) {
    private val tiendas = mutableListOf<Grupo>()


    companion object {
        private const val SCHEMA = "default"
        private const val TABLE = "GRUPOS"
        private const val TRUNCATE_TABLE_GRUPOS_SQL = "TRUNCATE TABLE GRUPOS"
        private const val CREATE_TABLE_GRUPOS_SQL = "CREATE TABLE GRUPOS (grupoid INT NOT NULL AUTO_INCREMENT,grupodesc VARCHAR(100) NOT NULL,mejorposCTFid INT,PRIMARY KEY (grupoid))"
        private const val INSERT_GRUPOS_SQL = "INSERT INTO GRUPOS" + "  (grupoid,grupodesc,mejorposCTFid) VALUES " + " (?, ?, ?)"
        private const val SELECT_GRUPOS_BY_ID = "select grupoid,grupodesc,mejorposCTFid from GRUPOS where grupoid =?"
        private const val SELECT_ALL_GRUPOS = "select * from GRUPOS"
        private const val DELETE_GRUPOS_SQL = "delete from GRUPOS where grupoid = ?"
        private const val UPDATE_GRUPOS_SQL = "update GRUPOS set grupodesc = ?,mejorposCTFid= ? where grupoid = ?"
        private const val ALTER_TABLE_GRUPOS = "ADD FOREIGN KEY (mejorposCTFid, grupoid)\n" + "REFERENCES CTFS(CTFid,grupoid);"
    }

    fun prepareTable() {
        val metaData = c.metaData
        c.setAutoCommit(false)
        val rs = metaData.getTables(null, SCHEMA, TABLE, null)

        if (rs.next()) truncateTable() else createTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_GRUPOS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_GRUPOS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_GRUPOS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_GRUPOS_SQL)
            }
            //Commit the change to the database
            //linea de abajo eliminar?
            c.setAutoCommit(false)
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(grupo: Grupo) {
        println(INSERT_GRUPOS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_GRUPOS_SQL).use { st ->
                st.setInt(1, grupo.grupoid)
                st.setString(2, grupo.grupodesc)
                st.setInt(3, grupo.mejorposCTFid)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun selectById(grupoid: Int): Grupo? {
        var grupo: Grupo? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_GRUPOS_BY_ID).use { st ->
                st.setInt(1, grupoid)
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val grupoid = rs.getInt("grupoid")
                    val grupodesc = rs.getString("grupodesc")
                    val mejorposCTFid = rs.getInt("mejorposCTFid")
                    grupo = Grupo(grupoid,grupodesc,mejorposCTFid)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return grupo
    }

    fun selectAll(): List<Grupo> {

        // using try-with-resources to avoid closing resources (boiler plate code)
        val GRUPO: MutableList<Grupo> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_GRUPOS).use { st ->
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val grupoid = rs.getInt("grupoid")
                    val grupodesc = rs.getString("grupodesc")
                    val mejorposCTFid = rs.getInt("mejorposCTFid")
                    GRUPO.add(Grupo(grupoid,grupodesc,mejorposCTFid))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return GRUPO
    }

    fun deleteById(grupoid: Int): Boolean {
        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_GRUPOS_SQL).use { st ->
                st.setInt(1, grupoid)
                rowDeleted = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowDeleted
    }

    fun update(grupo: Grupo): Boolean {
        var rowUpdated = false

        try {
            c.prepareStatement(UPDATE_GRUPOS_SQL).use { st ->
                st.setInt(1, grupo.grupoid)
                st.setString(2, grupo.grupodesc)
                st.setInt(3, grupo.mejorposCTFid)
                rowUpdated = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowUpdated
    }

    private fun printSQLException(ex: SQLException) {
        for (e in ex) {
            if (e is SQLException) {
                e.printStackTrace(System.err)
                System.err.println("SQLState: " + e.sqlState)
                System.err.println("Error Code: " + e.errorCode)
                System.err.println("Message: " + e.message)
                var t = ex.cause
                while (t != null) {
                    println("Cause: $t")
                    t = t.cause
                }
            }
        }
    }

}