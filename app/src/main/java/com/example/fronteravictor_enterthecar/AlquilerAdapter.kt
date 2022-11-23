package com.example.fronteravictor_enterthecar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlquilerAdapter (private val lista: ArrayList<Alquiler>): RecyclerView.Adapter<AlquilerAdapter.MiViewHolder>() {

    class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclernombreapellidosView: TextView
        val recyclervechiculoView: TextView
        val recyclerdiasalquiladoView: TextView
        val recyclerpreciototalView: TextView

        init {
            recyclernombreapellidosView = view.findViewById(R.id.recyclerNombreApellidos)
            recyclervechiculoView = view.findViewById(R.id.recyclerVechiculo)
            recyclerdiasalquiladoView = view.findViewById(R.id.recyclerDiasAlquilado)
            recyclerpreciototalView = view.findViewById(R.id.recyclerPrecioTotal)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.elementos_lista, viewGroup, false)

        return MiViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MiViewHolder, position: Int) {
        viewHolder.recyclernombreapellidosView.text = lista[position].nombre + " " +lista[position].apellido
        viewHolder.recyclervechiculoView.text = lista[position].vehiculo + " " + lista[position].combustible
        viewHolder.recyclerdiasalquiladoView.text = lista[position].dias
        viewHolder.recyclerpreciototalView.text = lista[position].precioTotal

    }

    override fun getItemCount() = lista.size

}