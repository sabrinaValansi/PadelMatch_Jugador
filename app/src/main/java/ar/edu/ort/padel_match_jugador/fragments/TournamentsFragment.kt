package ar.edu.ort.padel_match_jugador.fragments

import TournamentsDetailViewModel
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.adapter.TournamentAdapter
import ar.edu.ort.padel_match_jugador.entities.Tournament
import kotlinx.coroutines.launch
import android.view.KeyEvent
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TournamentsFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewModel: TournamentsViewModel
    private lateinit var adapter: TournamentAdapter
    private lateinit var recyclerView: RecyclerView
    private var list: MutableList<Tournament> = mutableListOf()
    private lateinit var boton: View
    private var clubNames: MutableMap<String, String> = mutableMapOf()
    private lateinit var detailViewModel: TournamentsDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_tournaments, container, false)
        recyclerView = v.findViewById(R.id.tournament_list)
        boton = v.findViewById(R.id.btn_filtros)

        boton.setOnClickListener {
            val action =
                TournamentsFragmentDirections.actionTournamentsFragmentToFilters()
            findNavController().navigate(action)
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filtersButton: ImageButton = view.findViewById(R.id.filters)
        filtersButton.setOnClickListener {
            findNavController().navigate(R.id.action_tournamentsFragment_to_filters)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TournamentsViewModel::class.java)
        detailViewModel = ViewModelProvider(this).get(TournamentsDetailViewModel::class.java)

        // Get the search view from the layout
        val searchView = v.findViewById<SearchView>(R.id.search_view)

        // Set a listener for menu item click
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Perform search based on the query
                if (!query.isNullOrBlank()) {
                    filterData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Perform search as the text changes (optional)
                if (!newText.isNullOrBlank()) {
                    filterData(newText)
                } else {
                    // Si no hay texto, mostrar todos los datos sin filtrar
                    resetFilter()
                }
                return true
            }
        })

        // Set a listener for the Done/Enter key press on the keyboard
        searchView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Perform search based on the entered query
                val query = searchView.query.toString()
                if (!query.isNullOrBlank()) {
                    performSearch(query)
                }
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun filterData(query: String) {
        lifecycleScope.launch {
            val filteredList = list.filter { tournament ->
                val club = detailViewModel.getClubById(tournament.idClub)?.nombre ?: ""
                tournament.titulo.contains(query, ignoreCase = true) ||
                        club.contains(query, ignoreCase = true) ||
                        tournament.categoría.contains(query, ignoreCase = true) ||
                        tournament.fecha.contains(query, ignoreCase = true) ||
                        tournament.hora.contains(query, ignoreCase = true) ||
                        tournament.nombreCoordinador.contains(query, ignoreCase = true) ||
                        tournament.telefonoCoordinador.contains(query, ignoreCase = true)
            }
            adapter.updateTournaments(filteredList)
        }
    }

    private fun resetFilter() {
        adapter.updateTournaments(list)
    }

    private fun performSearch(query: String) {
        // TODO: Implement the logic for performing search with the given query
        Log.d("TournamentsFragment", "Perform search for query: $query")
    }

    // ordeno la lista de torneos por fecha
    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            val originalList = viewModel.getTournament()
            val sortedList = originalList.sortedBy { parseDate(it.fecha) }
            list.clear()
            list.addAll(sortedList)
            // Load all club names
            for (tournament in list) {
                val clubName = detailViewModel.getClubById(tournament.idClub)?.nombre
                clubNames[tournament.idClub] = clubName ?: ""
            }


            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = TournamentAdapter(requireContext()) { pos ->
                onItemClick(pos)
            }

            try{
                val tournamentSelected =
                    TournamentsFragmentArgs.fromBundle(requireArguments()).tournamentList
                adapter.updateTournaments(tournamentSelected!!.toList())

            } catch ( e: Exception ) {
                adapter.updateTournaments(list)
            }
            recyclerView.adapter = adapter
        }
    }

    // convierto la fecha de string a date
    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }

    // por la posicion del turno me redirige al detalle del mismo
    private fun onItemClick(position: Int) {
        val action =
            TournamentsFragmentDirections.actionMyTournamentsFragmentToTournamentDetailFragment(list[position])
        findNavController().navigate(action)
    }
}
