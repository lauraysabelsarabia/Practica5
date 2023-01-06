package com.lysm.practica5.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tarea(
    var id:Long? = null,
    val categoria:Int,
    val prioridad:Int,
    val pagado:Boolean,
    val estado:Int,
    val horasTrabajo:Int,
    val valoracionCliente:Float,
    val tecnico:String,
    val descripcion:String
):Parcelable
{
constructor( categoria:Int,
             prioridad:Int,
             pagado:Boolean,
             estado:Int,
             horasTrabajo:Int,
             valoracionCliente:Float,
             tecnico:String,
             descripcion:String):this(generateId(),categoria,prioridad,pagado,estado,horasTrabajo,valoracionCliente,tecnico, descripcion){}

    companion object  {
    var idContador=1L
    private fun generateId(): Long {
        return idContador++
    }
}

    override fun equals(other: Any?): Boolean {
        return (other is Tarea) && (this.id == other?.id)
    }
}

