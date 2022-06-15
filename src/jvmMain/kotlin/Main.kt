// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import DAO.CTFS
import DAO.ConnectionBuilder
import DAO.GRUPOS
import gui.AppGui

data class Ctf(val CTFid: Int, val grupoId: Int, val puntuacion: Int)
data class Grupo(val grupoid: Int,val grupodesc:String, val mejorposCTFid: Int = 0)
fun main() = AppGui()

val gruposs = GRUPOS(ConnectionBuilder().connection)
val ctfss = CTFS(ConnectionBuilder().connection)

/*
gruposs.prepareTable()
with(gruposs) {
    insert into Grupo(grupoid, grupodesc) values(1, 'dam3');
    insert into Grupo(grupoid, grupodesc) values(2, 'dam1');
    insert into Grupo(grupoid, grupodesc) values(3, 'masters');
    insert into Grupo(grupoid, grupodesc) values(4, '1DAW-G1');
    insert into Grupo(grupoid, grupodesc) values(5, '1DAW-G2');
    insert into Grupo(grupoid, grupodesc) values(6, '1DAW-G3');
}
*/
