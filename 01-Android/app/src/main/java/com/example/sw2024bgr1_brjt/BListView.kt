package com.example.sw2024bgr1_brjt

import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class BListView : AppCompatActivity() {
    val arreglo = BBaseDatosMemoria.arregloBEntrenador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blist_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listView =findViewById<ListView>(R.id.lv_list_view)
        val adaptador = ArrayAdapter(
            this, // contexto
            android.R.layout.simple_list_item_1, //XML que vamos a usar
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        val botonAnadirLstView = findViewById<Button>(R.id.btn_anadir_list_view)
        botonAnadirLstView
            .setOnClickListener { anadirEntrenador(adaptador) }
        registerForContextMenu(listView)
    }

    var posicionItemSeleccionado = -1 // VARIABLE GLOBAL
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // LLenamos opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        // obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar -> {
                mostrarSnackbar("$posicionItemSeleccionado")
                return true
            }
            R.id.mi_eliminar -> {
                mostrarSnackbar("$posicionItemSeleccionado")
                abrirDialogo()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar (texto: String){
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    fun abrirDialogo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                mostrarSnackbar("Eliminar Aceptado")
            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )
        val opciones = resources.getStringArray(
            R.array.string_array_opciones_dialogo
        )
        val seleccionPrevia = booleanArrayOf(
            true,false,false
        )
        builder.setMultiChoiceItems(
            opciones,
            seleccionPrevia,
            {
                dialog,
                which,
                isChecked -> mostrarSnackbar(
                    "${which} $isChecked"
                )

            }
        )
        val dialogo = builder.create()
        dialogo.show()
    }

    fun anadirEntrenador(adaptador: ArrayAdapter<BEntrenador>){
        arreglo.add(BEntrenador(4,"Wendy", "d@d.com"))
        adaptador.notifyDataSetChanged()
    }
}