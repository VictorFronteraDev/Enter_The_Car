package com.example.fronteravictor_enterthecar

import android.R.attr
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fronteravictor_enterthecar.databinding.ActivityFormularioPagoBinding
import com.google.android.material.datepicker.DateValidatorPointBackward.before


class FormularioPago : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityFormularioPagoBinding
    private lateinit var alquiler: Alquiler
    private var item: String = ""
    private var tipoTarjeta: String = ""
    private var alquilerLista: ArrayList<Alquiler> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioPagoBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            val bundle = intent.getBundleExtra(Intent.EXTRA_TEXT)

            alquiler = bundle?.getParcelable("Alquiler")!!
            alquilerLista = bundle?.getParcelableArrayList("AlquilerLista")!!
        }

        binding.spinnerTarjeta.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            this,
            R.array.arrayTarjetas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTarjeta.adapter = adapter
        }

        binding.botonEnviar.setOnClickListener {
            var fecha: String = binding.fechaCaducidad.text.toString()
            var numTarjeta: String = binding.numeroTarjeta.text.toString()

            if(numTarjeta.isNotEmpty() && fecha!!.isNotEmpty() ) {
                if(numTarjeta.length == 19) {
                    if (fecha.length == 5 && fecha.substring(0,2).toInt() in 1..12
                        && fecha.substring(3).toInt() in 22..27) {
                        val pago = Pago(
                            binding.spinnerTarjeta.selectedItem.toString(),
                            binding.numeroTarjeta.text.toString(),
                            binding.fechaCaducidad.text.toString()
                        )

                        val bundle = Bundle()

                        bundle.putParcelable("Pago", pago)
                        bundle.putParcelable("Alquiler", alquiler)
                        bundle.putParcelableArrayList("AlquilerLista", alquilerLista)

                        val intent = Intent(this, ResumenPago::class.java).apply {
                            putExtra(Intent.EXTRA_TEXT, bundle)
                        }
                        startActivity(intent)
                     } else {
                        Toast.makeText(this, "Fecha de la tarjeta de credito erronea", Toast.LENGTH_LONG).show()
                     }
                } else {
                    Toast.makeText(this, "Número de la tarjeta de credito erroneo", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "No pueden quedar campos vacios", Toast.LENGTH_LONG).show()
            }


        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        item = parent.getItemAtPosition(pos).toString()

        when (pos) {
            0 -> tipoTarjeta = getString(R.string.visa)
            1 -> tipoTarjeta = getString(R.string.mastercard)
            2 -> tipoTarjeta = getString(R.string.euro6000)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

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