package com.lysm.practica5.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lysm.practica5.model.Tarea
import kotlin.random.Random

object ModelTempTareas {
    //lista de tareas
    private val tareas = ArrayList<Tarea>()

    //LiveData para observar en la vista los cambios en la lista
    private val tareasLiveData = MutableLiveData<ArrayList<Tarea>>(tareas)

    //el context que suele ser neceario en acceso a datos
    private lateinit var application: Application

    //el método invoke permite iniciar el objeto Singlenton
    operator fun invoke(context:Context) {
        this.application=context.applicationContext as Application
        iniciaPruebaTareas()
    }

    /**
     * devuelve un LiveData en vez de MutableLiveData
     * para evitar su modificación en las capas superiores
     */

    fun getAllTareas(): LiveData<ArrayList<Tarea>> {
        tareasLiveData.value = tareas
        return tareasLiveData
    }

    /*********
     * añade una tarea, si existe (id iguales) la sustituye
     * y si no la añade. Posteriormente actualiza el LiveData
     * que permitirá avisar a quien está observando
     */

    fun addTarea(tarea: Tarea) {
        val pos = tareas.indexOf(tarea)
        if (pos < 0) { //Si no existe
            tareas.add(tarea)
        } else {
            //si existe se sustituye
            tareas.set(pos,tarea)
        }

        // se actualiza LiveData
        tareasLiveData.value = tareas
    }

    /**
     * Borra una tarea y actualilza el LiveData
     * para avisar a los observadores
     */

    fun delTarea (tarea:Tarea) {
        tareas.remove(tarea)
        tareasLiveData.value = tareas
    }

    /**
     * Crea unas tareas de prueba aleatoria
     */
    fun iniciaPruebaTareas() {
        val tecnicos = listOf(
            "Pepe Gotero",
            "Sacarino Pómez",
            "Mortadelo Fernández",
            "Filemon López",
            "Zipe Clement",
            "Zape Gómez"
        )
        lateinit var tarea:Tarea
        (1..10).forEach({
            tarea = Tarea(
                (0..4).random(),
                (0..2).random(),
                Random.nextBoolean(),
                (0..2).random(),
                (0..30).random(),
                (0..5).random().toFloat(),
                tecnicos.random(),
                "tarea $it realizada por el técnico \n Texto de ejemplo.Lorem ip"
            )
            tareas.add(tarea)
        })
        // actualizamos el LiveData
        tareasLiveData.value=tareas
    }

    fun getTareasFiltroSinPagar(soloSinPagar:Boolean): LiveData<ArrayList<Tarea>> {
        //devuelve el LiveData con la  lista filtrada o entera
        tareasLiveData.value=if(soloSinPagar)
            tareas.filter { !it.pagado } as ArrayList<Tarea>
        else
            tareas
        return tareasLiveData
    }

    fun getTareasFiltroEstado(estado:Int): LiveData<ArrayList<Tarea>> {
        //devuelve el LiveData con la  lista filtrada o entera
        tareasLiveData.value=
            tareas.filter { it.estado == estado} as ArrayList<Tarea>
        return tareasLiveData
    }


}