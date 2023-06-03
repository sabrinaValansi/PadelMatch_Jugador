package ar.edu.ort.padel_match_jugador.fragments


import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.entities.Tournament
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ar.edu.ort.padel_match_jugador.entities.Club
import com.google.firebase.firestore.ktx.firestore
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*



class TournamentsDetailFragment : Fragment() {


    private lateinit var v: View
    private lateinit var detailNombre: TextView
    private lateinit var detailTitulo: TextView
    private lateinit var detailFechaTorneo: TextView
    private lateinit var detailCategorias: TextView
    private lateinit var detailHorario: TextView
    private lateinit var detailDireccion: TextView
    private lateinit var detailLocalidad: TextView
    private lateinit var detailCupos: TextView
    private lateinit var detailCancha: TextView
    private lateinit var detailImagen: TextView
    private lateinit var viewModel: TournamentsDetailViewModel


    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_tournaments_detail, container, false)

        detailNombre = v.findViewById(R.id.textNombre)

        detailTitulo = v.findViewById(R.id.textTitulo)

        detailFechaTorneo = v.findViewById(R.id.fechaTorneo)

        detailCategorias = v.findViewById(R.id.categoria)

        detailHorario = v.findViewById(R.id.horario)

        detailDireccion = v.findViewById(R.id.direccion)

        detailLocalidad = v.findViewById(R.id.localidad)

        detailCupos = v.findViewById(R.id.cupos)

        detailCancha = v.findViewById(R.id.cancha)



        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TournamentsDetailViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        val tournamentSelected: Tournament =
            TournamentsDetailFragmentArgs.fromBundle(requireArguments()).tournamentSelected


        lifecycleScope.launch {

            val clubSelected: Club? = viewModel.getClubById(tournamentSelected.idClub)
            if(clubSelected!= null){
                setValues( tournamentSelected,clubSelected)
            }
        }

        detailImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

    }

    private fun setValues(tournamentSelected: Tournament, clubSelected: Club) {
        Log.w("Torneo selecionado", tournamentSelected.toString())

        detailNombre.setText(tournamentSelected.club)
        detailTitulo.setText(tournamentSelected.titulo)
        detailFechaTorneo.setText(tournamentSelected.fecha)
        detailCategorias.setText(tournamentSelected.categoría)
        detailHorario.setText(tournamentSelected.hora)
        detailDireccion.setText(clubSelected.domicilio)
        detailLocalidad.setText(clubSelected.localidad)
        detailCupos.setText(tournamentSelected.cupos.toString())
        detailCancha.setText(tournamentSelected.materialCancha)

    }

}
