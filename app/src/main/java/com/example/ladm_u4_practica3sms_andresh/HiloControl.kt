package com.example.ladm_u4_practica3sms_andresh

class HiloControl (p:MainActivity) : Thread() {
    private var iniciado = false
    private var puntero = p
    private var pausa = false

    override fun run() {
        super.run()
        iniciado = true
        while (iniciado) {
            sleep(200)
            if (!pausa) {
                puntero.runOnUiThread {
                    puntero.analizarMensajes()
                }
            }
        }

    }

    fun estaIniciado(): Boolean {
        return iniciado
    }

    fun pausar() {
        pausa = true
    }

    fun despausar() {
        pausa = false
    }

    fun detener() {
        iniciado = false
    }
}
