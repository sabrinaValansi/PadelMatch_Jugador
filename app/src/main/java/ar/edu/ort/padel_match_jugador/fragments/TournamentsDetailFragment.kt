package ar.edu.ort.padel_match_jugador.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.edu.ort.padel_match_jugador.R

class TournamentsDetailFragment : Fragment() {

    companion object {
        fun newInstance() = TournamentsDetailFragment()
    }

    private lateinit var viewModel: TournamentsDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tournaments_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TournamentsDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}