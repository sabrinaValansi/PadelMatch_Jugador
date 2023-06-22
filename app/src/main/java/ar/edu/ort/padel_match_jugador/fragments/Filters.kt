package ar.edu.ort.padel_match_jugador.fragments

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.databinding.FragmentFiltersBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class Filters : Fragment() {

    companion object {
        fun newInstance() = Filters()
    }

    private lateinit var viewModel: FiltersViewModel
    private lateinit var viewModel2: TournamentsViewModel
    private lateinit var binding: FragmentFiltersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FiltersViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(TournamentsViewModel::class.java)

        val datePickerDesde = viewModel.createDatePicker()
        datePickerHandler(datePickerDesde, binding.editTextFiltroFechaDesde)

        val datePickerHasta = viewModel.createDatePicker()
        datePickerHandler(datePickerHasta, binding.editTextFiltroFechaHasta)

        val timePicker = viewModel.createTimePicker()
        timePickerHandler(timePicker, binding.editTextAddTournamentHorario)

        lifecycleScope.launch {
            var clubs = viewModel.getPartidosList()
            val data = clubs.map { t -> t.data["nombre"] } as List<String>
            var list = data.toTypedArray();

            (binding.filListaPartidos as? MaterialAutoCompleteTextView)?.setSimpleItems(list)

            binding.filListaPartidos.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    binding.filListaLocalidades.setText("")
                    val selecPartido = binding.filListaPartidos.text.toString()
                    val selecClubs = clubs.filter { t -> t.data["nombre"] == selecPartido }

                    selecClubs.forEach { t ->
                        val lista = t.data["localidades"] as List<String>
                        val localidades = lista.toTypedArray()
                        (binding.filListaLocalidades as? MaterialAutoCompleteTextView)?.setSimpleItems(
                            localidades
                        )
                    }
                }

            var data_cat = viewModel.getCategoriasList()
            (binding.editTextAddTournamentCategorias as? MaterialAutoCompleteTextView)?.setSimpleItems(
                data_cat
            )
        }

        binding.btnFiltrar.setOnClickListener {
            lifecycleScope.launch {
                val lista = viewModel.getFilteredMatches(
                    binding.editTextFiltroFechaDesde.text.toString(),
                    binding.editTextFiltroFechaHasta.text.toString(),
                    binding.editTextAddTournamentHorario.text.toString(),
                    binding.filListaPartidos.text.toString(),
                    binding.filListaLocalidades.text.toString(),
                    binding.editTextAddTournamentCategorias.text.toString()
                )
                val action = FiltersDirections.actionFiltersToTournamentsFragment(lista.toTypedArray())
                findNavController().navigate(action)
            }
        }

        handlerBackToTournaments()

    }

    private fun handlerBackToTournaments() {
        binding.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun datePickerHandler(datePicker: MaterialDatePicker<Long>, item: EditText) {
        if (!datePicker.isAdded()) {
            item.setOnClickListener {
                datePicker.show(requireActivity().supportFragmentManager, "tag")
                datePicker.addOnPositiveButtonClickListener { selection ->
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = Date(selection)
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    val nuevaFecha = calendar.time
                    val nuevaFechaString = formatoFecha.format(nuevaFecha)
                    item.setText(nuevaFechaString)
                }

            }
        }
    }

    private fun timePickerHandler(timePicker: MaterialTimePicker, item: EditText) {
        item.setOnClickListener {
            timePicker.show(requireActivity().supportFragmentManager, "tag")
            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute
                binding.editTextAddTournamentHorario.setText(
                    String.format(
                        "%02d:%02d",
                        hour,
                        minute
                    )
                )
            }
        }
    }
}