package ar.edu.ort.padel_match_jugador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ar.edu.ort.padel_match_jugador.fragments.TournamentsViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //borrado de torneos anteriores al dia de la fecha
        val TournamentsViewModel = ViewModelProvider(this).get(TournamentsViewModel::class.java)
        TournamentsViewModel.deleteOldTournaments()
    }
}