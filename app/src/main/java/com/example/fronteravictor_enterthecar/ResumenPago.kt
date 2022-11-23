package com.example.fronteravictor_enterthecar

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.fronteravictor_enterthecar.databinding.ActivityResumenPagoBinding

class ResumenPago : AppCompatActivity() {
    private lateinit var binding: ActivityResumenPagoBinding

    private var nombre: String = ""
    private var apellidos: String = ""
    private var destinatario = arrayOf("victorfrontera3@gmail.com")
    private var vehiculo: String = ""
    private var combustible: String = ""
    private var gps: String = ""
    private var dias: String = ""
    private var precioTotal: String = ""
    private var alquilerLista: ArrayList<Alquiler> = ArrayList()
    private var alquiler: Alquiler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResumenPagoBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            val bundle = intent.getBundleExtra(Intent.EXTRA_TEXT)

            val pago:Pago? = bundle?.getParcelable("Pago")

            alquiler = bundle?.getParcelable("Alquiler")

            alquilerLista = bundle?.getParcelableArrayList("AlquilerLista")!!

            nombre = alquiler?.nombre.toString()
            apellidos = alquiler?.apellido.toString()
            if(alquiler?.vehiculo.toString() == getString(R.string.moto_Sin_Precio)) {
                vehiculo = getString(R.string.moto_Sin_Precio)
            } else if(alquiler?.vehiculo.toString() == getString((R.string.patinete))) {
                vehiculo = getString(R.string.patinete_Sin_Precio)
            } else {
                vehiculo = getString(R.string.turismo)
            }
            combustible = alquiler?.combustible.toString()
            gps = if(alquiler?.gps.toString() == "true") "Sí" else "No"
            dias = alquiler?.dias.toString()
            precioTotal = alquiler?.precioTotal.toString()
            binding.tipoTarjeta.text = pago?.tipoTarjeta
            binding.numeroTarjeta.text = pago?.numeroTarjeta
            binding.fechaCaducidad.text = pago?.fechaCaducidad

            alquilerLista.add(alquiler!!)

        }

        binding.fabEnviar.setOnClickListener{

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                data = Uri.parse("mailto:")
                type = "text/plain"

                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.justificanteCorreo) + " " + nombre + " " + apellidos)
                putExtra(Intent.EXTRA_EMAIL, destinatario)
                putExtra(Intent.EXTRA_TEXT, getString(R.string.tipoTarjetaCorreo) + " " +  binding.tipoTarjeta.text + "\n" +
                                                    getString(R.string.numeroTarjetaCorreo) + " " + binding.numeroTarjeta.text + "\n" +
                                                    getString(R.string.fechaCaducidadCorreo) + " " + binding.fechaCaducidad.text + "\n" +
                                                    getString(R.string.vehiculoCorreo) + " " + vehiculo + "\n" +
                                                    getString(R.string.combustibleCorreo) + " " + combustible + "\n" +
                                                    getString(R.string.gpsCorreo) + " " + gps + "\n" +
                                                    getString(R.string.diasCorreo) + " " + dias + "\n" +
                                                    getString(R.string.precioTotalCorreo) + " " + precioTotal + "\n")
            }
            val envIntent = Intent.createChooser(intent, null)
            startActivity(envIntent)

        }



        binding.botonAceptar.setOnClickListener {
            Toast.makeText(this, getString(R.string.pagoRealizado), Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(Intent.EXTRA_TEXT, alquilerLista)
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
