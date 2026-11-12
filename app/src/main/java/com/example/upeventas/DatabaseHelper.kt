package com.example.upeventas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Databasehelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABSE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABSE_NAME = "music.db"
        private const val DATABASE_VERSION = 2

        // Tabla usuarios
        private const val TABLE_USUARIOS = "usuarios"
        private const val COL_ID = "id"
        private const val COL_NOMBRE = "nombre"
        private const val COL_CORREO = "correo"
        private const val COL_CONTRASEÑA = "contraseña"

        // Tabla favoritos
        private const val TABLE_FAVORITOS = "favoritos"
        private const val COL_ID_FAVORITO = "id"
        private const val COL_PLATILLO_ID = "platillo_id"
        private const val COL_NOMBRE_PLATILLO = "nombre_platillo"
        private const val COL_CATEGORIA = "categoria"
        private const val COL_INSTRUCCIONES = "instrucciones"
        private const val COL_IMAGEN = "imagen"

        // Creación de tablas
        private const val CREATE_TABLE_USUARIOS = """
            CREATE TABLE $TABLE_USUARIOS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_CORREO TEXT NOT NULL,
                $COL_CONTRASEÑA TEXT NOT NULL
            )
        """

        private const val CREATE_TABLE_FAVORITOS = """
            CREATE TABLE $TABLE_FAVORITOS (
                $COL_ID_FAVORITO INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PLATILLO_ID INTEGER UNIQUE NOT NULL,
                $COL_NOMBRE_PLATILLO TEXT NOT NULL,
                $COL_CATEGORIA TEXT,
                $COL_INSTRUCCIONES TEXT,
                $COL_IMAGEN TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_USUARIOS)
        db?.execSQL(CREATE_TABLE_FAVORITOS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Si la versión anterior era 1 y la nueva es 2, creamos la tabla favoritos que faltaba
        if (oldVersion < 2) {
            db?.execSQL(CREATE_TABLE_FAVORITOS)
        }
    }

    fun registrarUsuario(nombre: String, correo: String, password: String): Boolean {
        val db = writableDatabase
        val datos = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_CORREO, correo)
            put(COL_CONTRASEÑA, password)
        }
        val resultado = db.insert(TABLE_USUARIOS, null, datos)
        db.close()
        return resultado != -1L
    }

    fun iniciarSesion(correo: String, password: String): Boolean {
        val db = readableDatabase
        val querySql = """
            SELECT * FROM $TABLE_USUARIOS
            WHERE $COL_CORREO = ? AND $COL_CONTRASEÑA = ?
        """.trimIndent()

        val cursor = db.rawQuery(querySql, arrayOf(correo, password))
        val usuarioEncontrado = cursor.count > 0
        cursor.close()
        db.close()
        return usuarioEncontrado
    }

    fun guardarPlatilloFavoritos(
        platilloId: Long,
        nombrePlatillo: String,
        categ: String,
        instruc: String,
        img: String
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put(COL_PLATILLO_ID, platilloId)
            put(COL_NOMBRE_PLATILLO, nombrePlatillo)
            put(COL_CATEGORIA, categ)
            put(COL_INSTRUCCIONES, instruc)
            put(COL_IMAGEN, img)
        }

        val resultado = db.insertWithOnConflict(
            TABLE_FAVORITOS,
            null,
            valores,
            SQLiteDatabase.CONFLICT_IGNORE
        )
        db.close()
        return resultado != -1L
    }

    fun eliminarPlatilloFavorito(platilloId: Long): Boolean {
        val db = writableDatabase
        val filasAfectadas = db.delete(
            TABLE_FAVORITOS,
            "$COL_PLATILLO_ID = ?",
            arrayOf(platilloId.toString())
        )
        db.close()
        return filasAfectadas > 0
    }

    fun existePlatilloFavorito(platilloId: Long): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_PLATILLO_ID FROM $TABLE_FAVORITOS WHERE $COL_PLATILLO_ID = ?",
            arrayOf(platilloId.toString())
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }
}