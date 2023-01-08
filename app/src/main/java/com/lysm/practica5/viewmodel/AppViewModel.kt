package com.lysm.practica5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.lysm.practica5.model.Tarea
import com.lysm.practica5.repository.Repository

class AppViewModel(application: Application):AndroidViewModel(application){
    //repositorio
    private val repositorio:Repository

    //liveData de lista de tareas
    val tareasLiveData :LiveData<ArrayList<Tarea>>
    // creamos el liveData de tipo Booleano. Representa nuestro filtro
    private val soloSinPagarLiveData = MutableLiveData<Boolean>(false)
    private val estadoLiveData= MutableLiveData<Int>(0)

    //inicio ViewModel

    init {
        //inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio=Repository
        //tareasLiveData=repositorio.getAllTareas()
        //asocioamos el tareasLiveData en soloSinPagarLiveData
        //tareasLiveData=Transformations.switchMap(soloSinPagarLiveData)
        //{soloSinPagar->Repository.getTareasFiltroSinPagar(soloSinPagar)}
        tareasLiveData= Transformations.switchMap(estadoLiveData)
        {estadoLiveData->Repository.getTareasFiltroEstado(estadoLiveData)}

    }
    fun addTarea(tarea: Tarea) = repositorio.addTarea(tarea)
    fun delTarea(tarea: Tarea) = repositorio.delTarea(tarea)

    /**
     * activa el LiveData del filtro
     */
    fun setSoloSinPagar(soloSinPagar:Boolean)
    {soloSinPagarLiveData.value=soloSinPagar}

    fun setEstado(estado:Int)
    {estadoLiveData.value=estado}



}
