package ar.edu.ort.padel_match_jugador.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.databinding.FragmentFiltersBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.launch
import java.util.*


class Filters : Fragment() {

    companion object {
        fun newInstance() = Filters()
    }

    private lateinit var viewModel: FiltersViewModel
    private lateinit var binding: FragmentFiltersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FiltersViewModel::class.java)
        val datePicker = viewModel.createDatePicker()
        datePickerHandler(datePicker, binding.editTextFiltroFechaDesde)
        datePickerHandler(datePicker, binding.editTextFiltroFechaHasta)

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

                    selecClubs.forEach{ t ->
                        val lista = t.data["localidades"] as List<String>
                        val localidades = lista.toTypedArray()
                        (binding.filListaLocalidades as? MaterialAutoCompleteTextView)?.setSimpleItems(localidades)
                    }
                }

            var data_cat = viewModel.getCategoriasList()
            ( binding.editTextAddTournamentCategorias as? MaterialAutoCompleteTextView)?.setSimpleItems(data_cat)
        }

        binding.btnFiltrar.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getFilteredMatches(
                    binding.editTextFiltroFechaDesde.text.toString(),
                    binding.editTextFiltroFechaHasta.text.toString(),
                    binding.editTextAddTournamentHorario.toString(),
                    binding.filListaPartidos.text.toString(),
                    binding.filListaLocalidades.text.toString(),
                    binding.editTextAddTournamentCategorias.text.toString()
                )
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
        item.setOnClickListener{
            datePicker.show(requireActivity().supportFragmentManager, "tag" )
            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateString = DateFormat.format("dd/MM/yyyy", Date(selection)).toString()
                item.setText(dateString)
            }
        }
    }

    fun timePickerHandler(timePicker: MaterialTimePicker, item: EditText){
        item.setOnClickListener {
            timePicker.show(requireActivity().supportFragmentManager, "tag")
            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute
                binding.editTextAddTournamentHorario.setText(String.format("%02d:%02d", hour, minute))
            }
        }
    }

}