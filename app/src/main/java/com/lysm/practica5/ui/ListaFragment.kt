package com.lysm.practica5.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lysm.practica5.R
import com.lysm.practica5.adapters.TareasAdapter
import com.lysm.practica5.databinding.FragmentListaBinding
import com.lysm.practica5.model.Tarea
import com.lysm.practica5.viewmodel.AppViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var tareasAdapter: TareasAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciaRecycleView()
        iniciaCRUD()


        //importar import androidx.lifecycle.observe
        viewModel.tareasLiveData.observe(viewLifecycleOwner, Observer<List<Tarea>> { lista ->
            //actualizaLista(lista)
            tareasAdapter.setLista(lista)
        })

        /*   binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/

        //para prueba, editamos una tarea aleatoria
        /*binding.btPruebaEdicion.setOnClickListener {
            //cogemos la lista actual de Tareas que tenemos en el ViewModel.
            val lista = viewModel.tareasLiveData.value
            //buscamos una tarea aleatoriamente
            val tarea = lista?.get((0..lista.lastIndex).random())
            //se la enviamos a TareaFragment para su edición
            val action = ListaFragmentDirections.actionEditar(tarea)
            findNavController().navigate(action)
        }*/
        iniciaFiltros()
    }

    private fun iniciaRecycleView() {
        //creamos el adaptador
        tareasAdapter = TareasAdapter()
        with(binding.rvTareas) {
            //Creamos el layoutManager
            layoutManager = LinearLayoutManager(activity)
            //le asignamos el adaptador
            adapter = tareasAdapter
        }
    }

    private fun iniciaCRUD(){
        //************Nueva Tarea****************
        binding.fabNuevo.setOnClickListener {
            //creamos acción enviamos argumento nulo porque queremos crear NuevaTarea
            val action=ListaFragmentDirections.actionEditar(null)
            findNavController().navigate(action)
        }
        tareasAdapter.onTareaClickListener = object : TareasAdapter.OnTareaClickListener {
            //**************Editar  Tarea*************

            override fun onTareaClick(tarea: Tarea) {
                //creamos acción enviamos argumento la tarea para editarla
                val action = ListaFragmentDirections.actionEditar(tarea)
                findNavController().navigate(action)
            }
            //***********Borrar Tarea************
            override fun onTareaBorrarClick(tarea: Tarea) {
                //borramos directamente la tarea
                //viewModel.delTarea(tarea!!)
                borrarTarea(tarea)

            }

        }
    }


    private fun actualizaLista(lista: List<Tarea>?) {
        var listaString = ""
        lista?.forEach() {
            listaString =
                "$listaString ${it.id}-${it.tecnico}-${it.descripcion}-${if (it.pagado) "pagado" else "no pagado"}\n"
        }
        //  binding.tvLista.setText(listaString)
    }

    private fun iniciaFiltros() {
        binding.swSinPagar.setOnCheckedChangeListener() { _, isChecked ->
            //actualiza el LiveData SoloSinPagarliveData que a su vez modifica tareasLiveData
            //mediante el Tranformation
            viewModel.setSoloSinPagar(isChecked)
        }
        binding.rgEstado.setOnCheckedChangeListener { _, checkedId ->
            val estado = when (checkedId) {//el id del RadioButton seleccionado
                //id de cada RadioButon
                R.id.rbAbierta -> 0
                R.id.rbEnCurso -> 1
                R.id.rbCerrada -> 2
                else -> 3
            }
            viewModel.setEstado(estado)
        }
    }

    fun borrarTarea (tarea:Tarea){
        AlertDialog.Builder(activity as Context)
            .setTitle(android.R.string.dialog_alert_title)
            //recuerda:todo el texto en el string.xml
            .setMessage("Desea borrar la Tarea ${tarea.id}?")
            //acción si pulsa si
            .setPositiveButton(android.R.string.ok){v,_->
                //borramos la tarea
                viewModel.delTarea(tarea)
                //cerramos el dialogo
                v.dismiss()
            }
            //accion si pulsa no
            .setNegativeButton(android.R.string.cancel){v,_->v.dismiss()}
            .setCancelable(false)
            .create()
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
