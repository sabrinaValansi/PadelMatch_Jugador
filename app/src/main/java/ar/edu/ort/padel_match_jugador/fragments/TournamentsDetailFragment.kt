package ar.edu.ort.padel_match_jugador.fragments

import TournamentsDetailViewModel
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.entities.Club
import ar.edu.ort.padel_match_jugador.entities.Tournament
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


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
    private lateinit var btnInfo: AppCompatImageButton
    private lateinit var btnWhatsapp: AppCompatImageButton
    private lateinit var btnMapa: AppCompatImageButton

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        btnInfo = v.findViewById(R.id.btnInfo)
        btnWhatsapp = v.findViewById(R.id.btnWhatsapp)
        btnMapa = v.findViewById(R.id.btnMapa)

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

        Log.w("TORNEO SELECCIONADO", tournamentSelected.toString())
        lifecycleScope.launch {
            val clubSelected: Club? = viewModel.getClubById(tournamentSelected.idClub)
            if (clubSelected != null) {
                setValues(tournamentSelected, clubSelected)
                btnMapa.setOnClickListener {
                    openLocationOnMap(clubSelected.direccionCompleta)
                }

                btnWhatsapp.setOnClickListener {
                    enviarMensajeWhatsapp()
                }
            }
        }

        btnInfo.setOnClickListener {
            val tournamentSelected: Tournament =
                TournamentsDetailFragmentArgs.fromBundle(requireArguments()).tournamentSelected
            viewModel.mostrarInformacion(requireContext(), tournamentSelected.imagenTorneo)
        }
    }

    private fun setValues(tournamentSelected: Tournament, clubSelected: Club) {
        Log.w("Torneo selecionado", tournamentSelected.toString())
        detailNombre.text = clubSelected.nombre
        detailTitulo.text = tournamentSelected.titulo
        detailFechaTorneo.text = tournamentSelected.fecha
        detailCategorias.text = tournamentSelected.categoría
        detailHorario.text = tournamentSelected.hora
        detailDireccion.text = clubSelected.direccionCompleta
        detailLocalidad.text = clubSelected.localidad
        detailCupos.text = tournamentSelected.cupos.toString()
        detailCancha.text = tournamentSelected.materialCancha
    }

    private fun openLocationOnMap(direccionCompleta: String) {
        try {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${direccionCompleta}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }catch ( e: Exception ) {
            Toast.makeText(this.requireContext(), "Ocurrio un error al abrir Google Maps o la aplicacion no se encuentra instalada!", Toast.LENGTH_SHORT)
                .show();
        }
    }

    private fun enviarMensajeWhatsapp() {

        try{
            val tournamentSelected: Tournament = TournamentsDetailFragmentArgs.fromBundle(requireArguments()).tournamentSelected
            val numeroTelefono = tournamentSelected.telefonoCoordinador

            val uri = Uri.parse("https://api.whatsapp.com/send?phone=${numeroTelefono}")
            val intent = Intent(Intent.ACTION_VIEW, uri)


            startActivity(intent)
        } catch ( e: Exception ) {
            Toast.makeText(this.requireContext(), "Ocurrio un error al abrir WSP o la aplicacion no se encuentra instalada!", Toast.LENGTH_SHORT)
                .show();
        }


    }
}
