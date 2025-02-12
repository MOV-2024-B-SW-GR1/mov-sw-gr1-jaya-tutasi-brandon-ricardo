package com.example.sistemasolar_planetas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView

class SistemaSolarAdapter (
    private val listaSistemas: List<SistemaSolar>,
    private val clickListener: (SistemaSolar, View) -> Unit
) : RecyclerView.Adapter<SistemaSolarAdapter.SistemaSolarViewHolder>() {

    class SistemaSolarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombreSistemaSolar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SistemaSolarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sistema_solar, parent, false)
        return SistemaSolarViewHolder(view)
    }

    override fun onBindViewHolder(holder: SistemaSolarViewHolder, position: Int) {
        val sistema = listaSistemas[position]
        holder.nombre.text = sistema.nombre

        holder.itemView.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.menu_sistema_solar, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.verMapa -> clickListener(sistema, view)
                }
                true
            }
            popupMenu.show()
        }
    }


    override fun getItemCount() = listaSistemas.size
}