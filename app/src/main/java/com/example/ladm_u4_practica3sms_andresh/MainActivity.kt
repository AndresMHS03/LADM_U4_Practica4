package com.example.ladm_u4_practica3sms_andresh

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    var hiloControl : HiloControl?=null


    val siPermiso = 1
    val siPermisoReciever = 2
    val siPermisoLectura = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hiloControl = HiloControl(this)


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS),siPermisoReciever)
        }

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS),siPermisoLectura)
        } else {

        }

        button.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),siPermiso)
            } else{
                try {
                    if(hiloControl!!.estaIniciado()){
                      //  Mensaje("Error, hilo ya está iniciado")
                        return@setOnClickListener
                    }
                    hiloControl?.start()
                    setTitle("ESTADO: "+hiloControl!!.isAlive)
                }catch (e: Exception){
                    setTitle("ESTADO: "+hiloControl!!.isAlive)
                    //Mensaje("EXCEPCION: Hilo ya fue detenido")
                }
            }
        }
    }

    fun analizarMensajes(){
        var numero =""
        try {
            val cursor = BaseDatos(this,"entrantes",null,1)
                .readableDatabase
                .rawQuery("SELECT * FROM ENTRANTES",null)

            var ultimo  = ""
            if (cursor.moveToFirst()){
                numero = cursor.getString(0)
                do {
                    if(cursor.getString(1).split(" ")[0]=="ESTADO"){

                        val nombre = cursor.getString(1).split(" ")[1]
                        val nombreMascota = cursor.getString(1).split(" ")[2]
                        val cursor2 = BaseDatos(this,"entratamiento",null,1)
                            .readableDatabase
                            .rawQuery("SELECT * FROM ENTRATAMIENTO WHERE (NOMBRE = "+nombre+") AND (NOMBREMASCOTA = "+nombreMascota+")",null)

                        var texto = ""
                        if(cursor2.moveToFirst()){
                            var estado = cursor2.getString(2)
                            SmsManager.getDefault().sendTextMessage(cursor.getString(0),null,"Su perro ${estado} está listo",null,null)
                        } else {
                            SmsManager.getDefault().sendTextMessage(cursor.getString(0),null,"No se ha podido encontrar lo que solicita, verifique e intente nuevamente",null,null)
                        }

                    } else {
                        SmsManager.getDefault().sendTextMessage(cursor.getString(0),null,"Intente de nuevo con el formato requerido [ESTADO SUNOMBRE NOMBREMASCOTA]",null,null)
                    }
                } while (cursor.moveToNext())
            } else {
                ultimo = "Sin mensajes"
            }
            BaseDatos(this,"entrantes",null,1)
                .readableDatabase
                .rawQuery("DELETE * FROM ENTRANTES WHERE (CELULAR = ?)", arrayOf(numero))
            hiloControl!!.pausar()
           //textView.setText(ultimo)
        } catch (err : SQLiteException){
            Toast.makeText(this,err.message, Toast.LENGTH_LONG)
                .show()
        }
    }
}
