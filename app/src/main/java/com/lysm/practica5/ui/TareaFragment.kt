package com.lysm.practica5.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.lysm.practica5.R
import com.lysm.practica5.databinding.FragmentTareaBinding
import com.lysm.practica5.model.Tarea
import com.lysm.practica5.viewmodel.AppViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TareaFragment : Fragment() {

    private var _binding: FragmentTareaBinding? = null
    val args: TareaFragmentArgs by navArgs()
    private val viewModel:AppViewModel by activityViewModels()
    // será una tarea nueva si no hay argumento
    //lazy permite la iniciación del objeto cuando es utilizado por primera vez
    val esNuevo by lazy { args.tarea==null }


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
        iniciaSwPagado ()
        iniciaRgEstado()
        iniciaSbHoras()

        //si es nueva tarea o es una edición
       if (esNuevo) //nueva tarea
            //cambiamos el título de la ventana
            (requireActivity() as AppCompatActivity).supportActionBar?.title="Nueva tarea"
        else //editar tarea
            iniciaTarea(args.tarea!!)
            iniciaFabGuardar()

    }

    private fun iniciaTarea(tarea: Tarea) {
        binding.spnCategoria.setSelection(tarea.categoria)
        binding.spnPrioridad.setSelection(tarea.prioridad)
        binding.swPagado.isChecked=tarea.pagado
        binding.rgEstado.check(
            when (tarea.estado) {
                0->R.id.rbAbierta
                1->R.id.rbEnCurso
                else -> R.id.rbCerrada
            }
        )
        binding.sbHoras.progress = tarea.horasTrabajo
        binding.rbValoracion.rating=tarea.valoracionCliente
        binding.etTecnico.setText(tarea.tecnico)
        binding.etDescripcion.setText(tarea.descripcion)
        //cambiamos el titulo
        (requireActivity() as AppCompatActivity).supportActionBar?.title="Tarea ${tarea.id}"
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

    private fun iniciaSwPagado () {
        binding.swPagado.setOnCheckedChangeListener { _, isChecked ->
            //cambiamos el icono si está marcado o no el switch
            val imagen=if (isChecked) R.drawable.ic_pagado
            else R.drawable.ic_nopagado
            //asignamos la imagen desde recursos
            binding.ivPagado.setImageResource(imagen)
        }
        //iniciamos a valor false
        binding.swPagado.isChecked=false
        binding.ivPagado.setImageResource(R.drawable.ic_nopagado)
    }

    private fun iniciaRgEstado() {
        //listener de radioGroup
        binding.rgEstado.setOnCheckedChangeListener{_,checkedId ->
            val imagen = when (checkedId) {
                //el id del RadioButton seleccionado
                R.id.rbAbierta -> R.drawable.ic_abierto
                R.id.rbEnCurso -> R.drawable.ic_encurso
                else -> R.drawable.ic_cerrado
            }
            binding.ivEstado.setImageResource(imagen)
        }
        //iniciamos a abierto
        binding.rgEstado.check(R.id.rbAbierta)
    }

    private fun iniciaSbHoras() {
        //asignamos el evento
        binding.sbHoras.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progreso: Int, p2: Boolean) {
                //Mostramos el progreso en el textview
                binding.tvHoras.text=getString(R.string.horas_trabajadas,progreso)

            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        //inicio del progreso
        binding.sbHoras.progress=0
        binding.tvHoras.text=getString(R.string.horas_trabajadas,0)

    }

    private fun iniciaFabGuardar() {
        binding.fabGuardar.setOnClickListener {
            if (binding.etTecnico.text.toString().isNullOrEmpty() || binding.etDescripcion.text.toString().isNullOrEmpty())
                muestraMensajeError()
            else
                guardaTarea()
        }
    }

    private fun muestraMensajeError() {
        Snackbar.make(binding.root, "Es necesario rellenar todos los campos", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun guardaTarea() {
        //recuperamos los datos
        val categoria=binding.spnCategoria.selectedItemPosition
        val prioridad=binding.spnPrioridad.selectedItemPosition
        val pagado=binding.swPagado.isChecked
        val estado=when (binding.rgEstado.checkedRadioButtonId) {
            R.id.rbAbierta -> 0
            R.id.rbEnCurso -> 1
            else -> 2
        }
        val horas=binding.sbHoras.progress
        val valoracion=binding.rbValoracion.rating
        val tecnico=binding.etTecnico.text.toString()
        val descripcion=binding.etDescripcion.text.toString()

        //creamos la tarea: si es nueva, generamos un id, en otro caso le asignamos su id
        val tarea = if (esNuevo)
            Tarea(categoria, prioridad, pagado, estado, horas, valoracion, tecnico, descripcion)
        else
            Tarea(args.tarea!!.id,categoria,prioridad,pagado,estado,horas,valoracion,tecnico,descripcion)
        //guardamos la tarea desde el viewmodel
        viewModel.addTarea(tarea)
        //salimos de editarFragment
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
