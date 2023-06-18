package ar.edu.ort.padel_match_jugador.fragments

import TournamentsDetailViewModel
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.adapter.TournamentAdapter
import ar.edu.ort.padel_match_jugador.entities.Tournament
import ar.edu.ort.padel_match_jugador.fragments.TournamentsFragmentDirections
import ar.edu.ort.padel_match_jugador.fragments.TournamentsViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import android.widget.TextView.OnEditorActionListener
import android.widget.TextView
import android.view.KeyEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import org.checkerframework.common.subtyping.qual.Bottom

class TournamentsFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewModel: TournamentsViewModel
    private lateinit var adapter: TournamentAdapter
    private lateinit var recyclerView: RecyclerView
    private var list: MutableList<Tournament> = mutableListOf()
    private lateinit var boton: View
    private var clubNames: MutableMap<String, String> = mutableMapOf()
    private lateinit var detailViewModel: TournamentsDetailViewModel

    // Create connection with the database
    val db = Firebase.firestore

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

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            list = viewModel.getTournament()
            // Load all club names
            for (tournament in list) {
                val clubName = detailViewModel.getClubById(tournament.idClub)?.nombre
                clubNames[tournament.idClub] = clubName ?: ""
            }
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = TournamentAdapter(requireContext()) { pos ->
                onItemClick(pos)
            }
            adapter.updateTournaments(list) // Agregar esta línea para mostrar todas las tarjetas inicialmente
            recyclerView.adapter = adapter
        }
    }

    private fun onItemClick(position: Int) {
        Log.w("POSICION", position.toString())
        Log.w("LISTA", list.toString())
        val action =
            TournamentsFragmentDirections.actionMyTournamentsFragmentToTournamentDetailFragment(list[position])
        findNavController().navigate(action)
    }
}
