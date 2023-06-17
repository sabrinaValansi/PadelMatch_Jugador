package ar.edu.ort.padel_match_jugador.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import ar.edu.ort.padel_match_jugador.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class Filters : DialogFragment() {

    companion object {
        fun newInstance() = Filters()
    }

    private lateinit var viewModel: FiltersViewModel
    private lateinit var fechaDesde: TextInputEditText
    private lateinit var fechaHasta: TextInputEditText
    private lateinit var horario: TextInputEditText
    private lateinit var listaPartidos: AutoCompleteTextView
    private lateinit var listaLocalidades: AutoCompleteTextView
    private lateinit var categorias: AutoCompleteTextView
    private lateinit var btnFiltrar: Button
    private lateinit var btnBorrar: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filters, container, false)

        fechaDesde = view.findViewById(R.id.editTextFiltro_fechaDesde)
        fechaHasta = view.findViewById(R.id.editTextFiltro_fechaHasta)
        horario = view.findViewById(R.id.editTextAddTournament_horario)
        listaPartidos = view.findViewById(R.id.fil_listaPartidos)
        listaLocalidades = view.findViewById(R.id.fil_listaLocalidades)
        categorias = view.findViewById(R.id.editTextAddTournament_categorias)
        btnFiltrar = view.findViewById(R.id.btn_filtrar)
        btnBorrar = view.findViewById(R.id.btn_borrar)

        setupListeners()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton = view.findViewById<MaterialButton>(R.id.closeButton)
        closeButton.setOnClickListener {
            // Realiza la acción que deseas al hacer clic en el botón "Cerrar"
        }

        val filterIcon = view.findViewById<ImageView>(R.id.filters)
        filterIcon.setOnClickListener {
            // Crear una instancia del fragmento Filters
            findNavController().navigate(R.id.action_tournamentsFragment_to_filters)
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    private fun setupListeners() {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val current = Calendar.getInstance()

        fechaDesde.setOnClickListener {
            DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, day: Int ->
                val selected = Calendar.getInstance()
                selected.set(year, month, day)
                fechaDesde.setText(format.format(selected.time))
            }, current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH)).show()
        }

        fechaHasta.setOnClickListener {
            DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, day: Int ->
                val selected = Calendar.getInstance()
                selected.set(year, month, day)
                fechaHasta.setText(format.format(selected.time))
            }, current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH)).show()
        }

        horario.setOnClickListener {
            TimePickerDialog(requireContext(), { _: TimePicker, hourOfDay: Int, minute: Int ->
                val selected = Calendar.getInstance()
                selected.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selected.set(Calendar.MINUTE, minute)
                horario.setText(timeFormat.format(selected.time))
            }, current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE), true).show()
        }

        btnFiltrar.setOnClickListener {
            applyFilters()
        }

        btnBorrar.setOnClickListener {
            clearFilters()
        }
    }

    /*El botón "Filtrar" llama a la función applyFilters(),
    que actualmente no hace nada, pero es donde debes implementar
    la lógica para aplicar los filtros según tu aplicación.
    El botón "Borrar" borra todos los campos.*/
    private fun applyFilters() {
        val filtersApplied = StringBuilder()

        if (fechaDesde.text?.isNotEmpty() == true) filtersApplied.append("Fecha Desde: ${fechaDesde.text}\n")
        if (fechaHasta.text?.isNotEmpty() == true) filtersApplied.append("Fecha Hasta: ${fechaHasta.text}\n")
        if (horario.text?.isNotEmpty() == true) filtersApplied.append("Horario: ${horario.text}\n")
        if (listaPartidos.text?.isNotEmpty() == true) filtersApplied.append("Partido: ${listaPartidos.text}\n")
        if (listaLocalidades.text?.isNotEmpty() == true) filtersApplied.append("Localidad/Barrio: ${listaLocalidades.text}\n")
        if (categorias.text?.isNotEmpty() == true) filtersApplied.append("Categoria: ${categorias.text}")

        if (filtersApplied.isEmpty()) {
            Toast.makeText(requireContext(), "No se han aplicado filtros", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Filtros aplicados:\n$filtersApplied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFilters() {
        fechaDesde.text?.clear()
        fechaHasta.text?.clear()
        horario.text?.clear()
        listaPartidos.text?.clear()
        listaLocalidades.text?.clear()
        categorias.text?.clear()
    }
}
