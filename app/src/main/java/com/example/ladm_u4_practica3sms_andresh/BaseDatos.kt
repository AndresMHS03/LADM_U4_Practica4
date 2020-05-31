package com.example.ladm_u4_practica3sms_andresh

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ENTRANTES(CELULAR VARCHAR (200), MENSAJE VARCHAR (2000))")
        db.execSQL("CREATE TABLE ENTRATAMIENTO(NOMBRE VARCHAR (200),NOMBREMASCOTA VARCHAR (200),LISTO VARCHAR(10))")
        db.execSQL("INSERT INTO ENTRATAMIENTO VALUES ('Pedro','Roky','NO')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}