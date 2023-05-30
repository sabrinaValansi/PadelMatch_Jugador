package ar.edu.ort.padel_match_jugador.fragments

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class TournamentsFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewModel: TournamentsViewModel
    private lateinit var adapter: TournamentAdapter
    private lateinit var recyclerView: RecyclerView
    private var list: MutableList<Tournament> = mutableListOf()

    // Create connection with the database
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_tournaments, container, false)
        recyclerView = v.findViewById(R.id.tournament_list)

        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TournamentsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            list = viewModel.getTournament();
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = TournamentAdapter(list, requireContext()) { pos ->
                onItemClick(pos)
            }
            recyclerView.adapter = adapter
        }

    }

    fun onItemClick ( position : Int )  {
        Log.w("POSICION", position.toString())
        Log.w("LISTA", list.toString())
        val action = TournamentsFragmentDirections.actionMyTournamentsFragmentToTournamentDetailFragment()
        findNavController().navigate(action)
    }
}