package com.lysm.practica5.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lysm.practica5.model.Tarea
import com.lysm.practica5.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    //repositorio
    private val repositorio: Repository

    //liveData de lista de tareas
    val tareasLiveData: LiveData<ArrayList<Tarea>>

    // creamos el liveData de tipo Booleano. Representa nuestro filtro
    //private val soloSinPagarLiveData = MutableLiveData<Boolean>(false)
    //private val estadoLiveData= MutableLiveData<Int>(0)
    //LiveData que cuando se modifique un filtro cambia el tareasLiveData
    val SOLO_SIN_PAGAR = "SOLO_SIN_PAGAR"
    val ESTADO = "ESTADO"
    private val filtrosLiveData by lazy {//inicio tardío
        val mutableMap = mutableMapOf<String, Any?>(
            SOLO_SIN_PAGAR to false,
            ESTADO to 3
        )
        MutableLiveData(mutableMap)
    }

    //inicio ViewModel
    init {
        //inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio = Repository
        //tareasLiveData=repositorio.getAllTareas()
        //asocioamos el tareasLiveData en soloSinPagarLiveData
        //tareasLiveData=Transformations.switchMap(soloSinPagarLiveData)
        //{soloSinPagar->Repository.getTareasFiltroSinPagar(soloSinPagar)}
        // tareasLiveData= Transformations.switchMap(estadoLiveData)
        //  {estadoLiveData->Repository.getTareasFiltroEstado(estadoLiveData)}
        tareasLiveData = Transformations.switchMap(filtrosLiveData)
        { mapFiltro ->
            val aplicarSinPagar = mapFiltro!![SOLO_SIN_PAGAR] as Boolean
            val estado = mapFiltro!![ESTADO] as Int
            //Devuelve el resultado del when
            when {//trae toda la lista de tareas
                (!aplicarSinPagar && (estado == 3)) -> repositorio.getAllTareas()
                //Sólo filtra por ESTADO
                (!aplicarSinPagar && (estado != 3)) -> repositorio.getTareasFiltroEstado(estado)
                //Sólo filtra SINPAGAR
                (aplicarSinPagar && (estado == 3)) -> repositorio.getTareasFiltroSinPagar(
                    aplicarSinPagar
                )//Filtra por ambos
                else -> repositorio.getTareasFiltroSinPagarEstado(aplicarSinPagar, estado)
            }
        }
    }

    fun addTarea(tarea: Tarea) = repositorio.addTarea(tarea)
    fun delTarea(tarea: Tarea) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.delTarea(tarea)
    }
    //LiveData que cuando se modifique un filtro cambia el tareaLiveData

    private val soloSinPagarLiveData by lazy {  //creamos el LiveData de tipo Booleano. Repesenta nuestro filtro
        val livedata = MutableLiveData<Boolean>()
        //iniciamos a false
        livedata.value = false
        //devolvemos el LiveData
        livedata
    }

    /**
     * activa el LiveData del filtro
     */
    //fun setSoloSinPagar(soloSinPagar:Boolean){soloSinPagarLiveData.value=soloSinPagar}

    /**
     * Modifica el Map filtrosLiveData el elemento "SOLO_SIN_PAGAR"
     * que activará el Transformations de TareasLiveData
     */

    fun setSoloSinPagar(soloSinPagar: Boolean) {
        //recuperamos el map
        val mapa = filtrosLiveData.value
        //modificamos el filtro
        mapa!![SOLO_SIN_PAGAR] = soloSinPagar
        //activamos el LiveData
        filtrosLiveData.value = mapa
    }

    /**
     * Modifica el Map filtrosLiveData el elemento "ESTADO"
     * que activará el Transformations de TareasLiveData lo
     *llamamos cuando cambia el RadioButton
     */
    fun setEstado(estado: Int) {
        //recuperamos el map
        val mapa = filtrosLiveData.value
        //modificamos el filtro
        mapa!![ESTADO] = estado
        //activamos el LiveData
        filtrosLiveData.value = mapa
    }


}
