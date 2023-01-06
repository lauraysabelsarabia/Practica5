package com.lysm.practica5.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lysm.practica5.R
import com.lysm.practica5.databinding.FragmentListaBinding
import com.lysm.practica5.model.Tarea
import com.lysm.practica5.viewmodel.AppViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null
    private val viewModel: AppViewModel by activityViewModels()

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

        //importar import androidx.lifecycle.observe

        viewModel.tareasLiveData.observe(viewLifecycleOwner, Observer<List<Tarea>>{ lista ->
            actualizaLista(lista)
        })

     /*   binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
        binding.fabNuevo.setOnClickListener {
            findNavController().navigate(R.id.action_editar)
        }
    }

    private fun actualizaLista (lista:List<Tarea>?) {
        var listaString=""
        lista?.forEach(){
            listaString="$listaString ${it.id}-${it.tecnico}-${it.descripcion}-${if(it.pagado) "pagado" else "no pagado"}\n"
        }
        binding.tvLista.setText(listaString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}