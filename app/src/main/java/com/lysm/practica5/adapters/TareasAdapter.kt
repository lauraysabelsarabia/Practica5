package com.lysm.practica5.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lysm.practica5.R
import com.lysm.practica5.databinding.ItemTareaBinding
import com.lysm.practica5.model.Tarea

class TareasAdapter() :
    RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {
    lateinit var listaTareas: List<Tarea>
    var onTareaClickListener: OnTareaClickListener? = null

    fun setLista(lista: List<Tarea>) {
        listaTareas = lista
        //notifica al adaptador que hay cambios y tiene que redibujar el ReciclerView
        notifyDataSetChanged()
    }

    inner class TareaViewHolder(val binding: ItemTareaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            //inicio del click de icono borrar
            binding.ivBorrar.setOnClickListener() {
                //recuperamos la tarea de la lista
                val tarea = listaTareas.get(this.adapterPosition)
                //llamamos al evento borrar que estará definido en el fragment
                onTareaClickListener?.onTareaBorrarClick(tarea)
            }
            //inicio el click sobre el layout (constraintLayout)
            binding.root.setOnClickListener() {
                val tarea = listaTareas.get(this.adapterPosition)
                onTareaClickListener?.onTareaClick(tarea)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        //utilizamos binding, en otro caso hay que indicar el item.xml. Para más detalles puedes verlo en la documentación
        val binding = ItemTareaBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }

    //Tamaño de la lista
    override fun getItemCount(): Int = listaTareas?.size ?: 0
    override fun onBindViewHolder(tareaViewHolder: TareaViewHolder, pos: Int) {
        //Nos pasan la posición del  item a mostrar en el viewHolder
        with(tareaViewHolder) {
            //cogemos la tarea a mostrar y rellenamos los campos del ViewHolder
            with(listaTareas.get(pos)) {
                binding.tvId.text = id.toString()
                binding.tvDescripcion.text = descripcion
                binding.tvTecnico.text = tecnico
                binding.rbValoracion.rating = valoracionCliente
                //mostramos el icono en función del estado
                binding.ivEstado.setImageResource(
                    when (estado) {
                        0 -> R.drawable.ic_abierto
                        1 -> R.drawable.ic_encurso
                        else -> R.drawable.ic_cerrado
                    }
                )
                //cambiamos el color de fondo si la prioridad es alta
                binding.cvItem.setBackgroundResource(
                    if (prioridad == 2)//prioridad alta
                        R.color.prioridad_alta
                    else
                        Color.TRANSPARENT
                )
            }
        }
    }

    interface OnTareaClickListener {
        //editar tarea que contiene el ViewHolder
        fun onTareaClick(tarea: Tarea)

        //Borrar tarea que contiene el ViewHolder
        fun onTareaBorrarClick(tarea: Tarea)
    }

}