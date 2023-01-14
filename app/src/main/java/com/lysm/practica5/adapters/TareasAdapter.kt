package com.lysm.practica5.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lysm.practica5.databinding.ItemTareaBinding
import com.lysm.practica5.model.Tarea

class TareasAdapter(private val listaTareas: List<Tarea>) :
    RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {
    var listaTareas: List<Tarea>? = null

    fun setLista(lista: List<Tarea>) {
        listaTareas = lista
        //y notificamos al adaptador que hay cambios y que tiene que redibujar el RecycleView
        notifyDataSetChange()
    }

    inner class TareaViewHolder(val binding: ItemTareaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}