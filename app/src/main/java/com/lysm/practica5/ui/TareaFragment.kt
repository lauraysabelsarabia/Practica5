package com.lysm.practica5.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.lysm.practica5.R
import com.lysm.practica5.databinding.FragmentTareaBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TareaFragment : Fragment() {

    private var _binding: FragmentTareaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTareaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
        iniciaSpCategoria()
        iniciaSpPrioridad()
    }

    private fun iniciaSpCategoria() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categoria,
            android.R.layout.simple_spinner_item).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnCategoria.adapter = adapter

        }

        binding.spnCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                v: View?,
                posicion: Int,
                id: Long
            ) {
                //recuperamos el valor
                val valor = binding.spnCategoria.getItemAtPosition(posicion)
                //creamos el mensaje desde el recurso string parametrizado
                val mensaje = getString(R.string.mensaje_categoria, valor)
                //mostramos el mensaje donde "binding.root" es el ContrainLayout principal
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }


    private fun iniciaSpPrioridad() {
        ArrayAdapter.createFromResource(
            //Contexto suele ser la Activity
            requireContext(),
            //array de string
            R.array.prioridad,
            //layout para mostrar el elemento seleccionado
            android.R.layout.simple_spinner_item).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnPrioridad.adapter = adapter
            //Cuando se selecciona prioridad Alta, se cambia el color
            binding.spnPrioridad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, v: View?, posicion: Int, id: Long) {
                    //el array son 3 elementos y "alta" ocupa la tercera posición
                    if(posicion==2){
                        binding.clyTarea.setBackgroundColor(requireContext().getColor(R.color.prioridad_alta))
                    }else{
                        //si no es prioridad alta quitamos el color
                        binding.clyTarea.setBackgroundColor(Color.TRANSPARENT)
                    }

                }
                override fun onNothingSelected(p0: AdapterView<*>?) {


                    binding.clyTarea.setBackgroundColor(Color.TRANSPARENT)
                }

            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
