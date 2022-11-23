package com.example.fronteravictor_enterthecar

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.fronteravictor_enterthecar.databinding.ActivityResumenAlquilerBinding

class ResumenAlquiler : AppCompatActivity() {
    private lateinit var binding: ActivityResumenAlquilerBinding

    private var gps: Boolean = false
    private lateinit var alquiler: Alquiler
    private var alquilerLista: ArrayList<Alquiler> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResumenAlquilerBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            val bundle = intent.getBundleExtra(Intent.EXTRA_TEXT)

            alquiler = bundle?.getParcelable("Alquiler")!!

            binding.nombre.text = alquiler?.nombre
            binding.apellidos.text = alquiler?.apellido
            binding.vehiculo.text = alquiler?.vehiculo
            binding.combustible.text = alquiler?.combustible
            if(alquiler?.gps.toString() == "true") binding.gps.text = "Sí" else binding.gps.text = "No"
            binding.dias.text = alquiler?.dias
            binding.precioTotalResumen.text = alquiler?.precioTotal

            alquilerLista = bundle?.getParcelableArrayList("AlquilerLista")!!

        }

        binding.botonPagar.setOnClickListener {
            if(binding.nombre.text.isEmpty() || binding.apellidos.text.isEmpty() || binding.dias.text.isEmpty()) {
                Toast.makeText(this, "No pueden quedar datos sin completar", Toast.LENGTH_LONG).show()
            } else if (binding.vehiculo.text == getString(R.string.turismo) &&
                binding.combustible.text == getString(R.string.noDisponible)) {
                Toast.makeText(this, "No pueden quedar datos sin completar", Toast.LENGTH_LONG).show()
            } else {

                val bundle = Bundle()

                bundle.putParcelable("Alquiler", alquiler)
                bundle.putParcelableArrayList("AlquilerLista", alquilerLista)


                val intent = Intent(this, FormularioPago::class.java).apply {
                    putExtra(Intent.EXTRA_TEXT, bundle)
                }
                startActivity(intent)
            }
        }

        binding.botonCancelar.setOnClickListener {
            gps = binding.gps.text.toString() == getString(R.string.si)
            val alquiler = Alquiler(binding.nombre.text.toString(), binding.apellidos.text.toString(), binding.vehiculo.text.toString(),
                binding.combustible.text.toString(), gps, binding.dias.text.toString(),binding.precioTotalResumen.text.toString())

            val bundle = Bundle()

            bundle.putParcelable("Alquiler", alquiler)

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(Intent.EXTRA_TEXT, bundle)
            }

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.gmail -> {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("https://www.gmail.com")
                }
                if(intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                true
            }
            R.id.sobre -> {
                Toast.makeText(this, "Autor: Victor Frontera \n Version: 1.0", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}