package com.example.fronteravictor_enterthecar

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.system.Os
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.fronteravictor_enterthecar.databinding.ActivityMainBinding
import kotlinx.coroutines.newFixedThreadPoolContext

/*
    author Victor Frontera
 */
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    private var item: String = ""

    private var vehiculo: String = ""
    private var combustible: String = ""
    private var precio: Int = 0
    private var precioDiesel: Int = 25
    private var precioGasolina: Int = 20
    private var precioElectrico: Int = 15
    private var alquilerLista: ArrayList<Alquiler> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)



        binding.spinnerVehiculo.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            this,
            R.array.arrayVehiculos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerVehiculo.adapter = adapter
        }

        binding.dias.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, star: Int, before: Int, count: Int) {
                if(!binding.dias.text.isEmpty()) {
                    binding.precioTotal.text = (precio *  Integer.parseInt(binding.dias.text.toString())).toString()
                } else {
                    binding.precioTotal.text = "0"
                }
            }
        })

        binding.botonAlquilar.setOnClickListener {

            if (binding.diesel.isChecked) {
                combustible = getString(R.string.dieselSinPrecio)
            } else if(binding.gasolina.isChecked) {
                combustible = getString(R.string.gasolinaSinPrecio)
            } else if(binding.electrico.isChecked) {
                combustible = getString(R.string.electricoSinPrecio)
            } else {
                combustible = getString(R.string.noDisponible)
            }
            val alquiler = Alquiler(binding.txtNombre.text.toString(),binding.txtApellidos.text.toString(),
                            binding.spinnerVehiculo.selectedItem.toString(), combustible,
                            binding.gps.isChecked, binding.dias.text.toString(), binding.precioTotal.text.toString())

            val bundle = Bundle()

            bundle.putParcelable("Alquiler", alquiler)
            bundle.putParcelableArrayList("AlquilerLista", alquilerLista)

            val intent = Intent(this, ResumenAlquiler::class.java).apply {
                putExtra(Intent.EXTRA_TEXT, bundle)
            }

            startActivity(intent)
        }

        if (intent.hasExtra(Intent.EXTRA_TEXT))  {
            val bundle = intent.getBundleExtra(Intent.EXTRA_TEXT)

            val alquiler:Alquiler? = bundle?.getParcelable("Alquiler")


            binding.txtNombre.setText(alquiler?.nombre)
            binding.txtApellidos.setText(alquiler?.apellido)
            //spinner vehiculo
            binding.spinnerVehiculo.setSelection(
                when(alquiler?.vehiculo) {
                    getString(R.string.turismo) -> 0
                    getString(R.string.moto) -> 1
                    else -> 2
                }
            )

            when (alquiler?.combustible) {
                getString(R.string.dieselSinPrecio) -> {
                    binding.rGpCombustible.check(binding.diesel.id)
                    precio = 25
                }
                getString(R.string.gasolinaSinPrecio) -> {
                    binding.rGpCombustible.check(binding.gasolina.id)
                    precio = 20
                }
                getString(R.string.electricoSinPrecio) -> {
                    binding.rGpCombustible.check(binding.electrico.id)
                    precio = 15
                }
            }

            binding.gps.isChecked = alquiler?.gps == true
            binding.dias.setText(alquiler?.dias)

            if(!binding.dias.text.isEmpty()) {
                binding.precioTotal.text = (precio *  Integer.parseInt(binding.dias.text.toString())).toString()
            } else {
                binding.precioTotal.text = "0"
            }
        }
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        item = parent.getItemAtPosition(pos).toString()

        when(pos){
            0 -> vehiculo = "Turismo"
            1 -> vehiculo = "Moto"
            2 -> vehiculo = "Patinete"
        }

        if (vehiculo != "Turismo") {
            binding.rGpCombustible.clearCheck()
        }
        if (vehiculo == "Moto") {
            precio = 10
        } else if (vehiculo == "Patinete") {
            precio = 5
        }

        binding.diesel.isEnabled = vehiculo == "Turismo"
        binding.gasolina.isEnabled = vehiculo == "Turismo"
        binding.electrico.isEnabled = vehiculo == "Turismo"
        if(!binding.dias.text.isEmpty()) {
            binding.precioTotal.text = (precio *  Integer.parseInt(binding.dias.text.toString())).toString()
        } else {
            binding.precioTotal.text = "0"
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

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
            R.id.viewRecyclingMenu -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun radioButtonClick(view: View) {
        val radio: RadioButton = findViewById(binding.rGpCombustible.checkedRadioButtonId)

        when(radio.text) {
            getString(R.string.diesel) -> precio = precioDiesel
            getString(R.string.gasolina) -> precio = precioGasolina
            getString(R.string.electrico) -> precio = precioElectrico
        }
        if(!binding.dias.text.isEmpty()) {
            binding.precioTotal.text = (precio *  Integer.parseInt(binding.dias.text.toString())).toString()
        } else {
            binding.precioTotal.text = "0"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
