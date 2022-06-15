package DAO

import Ctf
import java.sql.Connection
import java.sql.SQLException

class CTFS(val c: Connection) {
    private val ctfs = mutableListOf<Ctf>()


    companion object {
        private const val SCHEMA = "default"
        private const val TABLE = "CTFS"
        private const val TRUNCATE_TABLE_CTFS_SQL = "TRUNCATE TABLE CTFS"
        private const val CREATE_TABLE_CTFS_SQL =
            "CREATE TABLE CTFS (CTFid INT NOT NULL,grupoId INT NOT NULL,puntuacion INT NOT NULL,PRIMARY KEY (CTFid,grupoid))"
        private const val INSERT_CTFS_SQL = "INSERT INTO CTFS" + "  (CTFid,grupoId,puntuacion) VALUES " + " (?, ?, ?)"
        private const val SELECT_CTFS_BY_ID = "select CTFid,grupoId,puntuacion from CTFS where CTFid =?"
        private const val SELECT_ALL_CTFS = "select * from CTFS"
        private const val DELETE_CTFS_SQL = "delete from CTFS where CTFid = ?"
        private const val UPDATE_CTFS_SQL = "update CTFS set grupoId = ?,puntuacion= ? where CTFid = ?"
    }

    fun prepareTable() {
        val metaData = c.metaData
        c.setAutoCommit(false)
        val rs = metaData.getTables(null, SCHEMA, TABLE, null)

        if (rs.next()) truncateTable() else createTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_CTFS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_CTFS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_CTFS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_CTFS_SQL)
            }
            //Commit the change to the database
            //linea de abajo eliminar?
            c.setAutoCommit(false)
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(ctff: Ctf) {
        println(INSERT_CTFS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_CTFS_SQL).use { st ->
                st.setInt(1, ctff.CTFid)
                st.setInt(2, ctff.grupoId)
                st.setInt(3, ctff.puntuacion)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun selectById(CTFid: Int): Ctf? {
        var ctff: Ctf? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_CTFS_BY_ID).use { st ->
                st.setInt(1, CTFid)
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val CTFid = rs.getInt("CTFid")
                    val grupoId = rs.getInt("grupoId")
                    val puntuacion= rs.getInt("puntuacion")
                    ctff = Ctf(CTFid,grupoId,puntuacion)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return ctff
    }

    fun selectAll(): List<Ctf> {

        // using try-with-resources to avoid closing resources (boiler plate code)
        val CTF: MutableList<Ctf> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_CTFS).use { st ->
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val CTFid = rs.getInt("CTFid")
                    val grupoId = rs.getInt("grupoId")
                    val puntuacion = rs.getInt("puntuacion")
                    CTF.add(Ctf(CTFid,grupoId,puntuacion))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return CTF
    }

    fun deleteById(CTFid: Int): Boolean {
        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_CTFS_SQL).use { st ->
                st.setInt(1, CTFid)
                rowDeleted = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowDeleted
    }

    fun update(ctff: Ctf): Boolean {
        var rowUpdated = false

        try {
            c.prepareStatement(UPDATE_CTFS_SQL).use { st ->
                st.setInt(1, ctff.CTFid)
                st.setInt(2, ctff.grupoId)
                st.setInt(3, ctff.puntuacion)
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