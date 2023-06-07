package ar.edu.ort.padel_match_jugador.fragments

import TournamentsDetailViewModel
import android.content.Intent
import android.net.Uri
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.entities.Tournament
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ar.edu.ort.padel_match_jugador.entities.Club
import com.google.firebase.firestore.ktx.firestore
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
    private lateinit var viewModel: TournamentsDetailViewModel
    private lateinit var btnInfo: AppCompatImageButton
    private lateinit var btnWhatsapp: AppCompatImageButton
    private lateinit var btnMapa: AppCompatImageButton

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

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
        btnInfo = v.findViewById(R.id.btnInfo)
        btnWhatsapp = v.findViewById(R.id.btnWhatsapp)
        btnMapa = v.findViewById(R.id.btnMapa)


        btnInfo.setOnClickListener {
            val tournamentSelected: Tournament =
                TournamentsDetailFragmentArgs.fromBundle(requireArguments()).tournamentSelected
            viewModel.mostrarInformacion(requireContext(), tournamentSelected.imagenTorneo)
        }


        btnMapa.setOnClickListener {
            val direccionCompleta = "${detailDireccion.text}, ${detailLocalidad.text}"
                openLocationOnMap(direccionCompleta)
        }

        btnWhatsapp.setOnClickListener {
            enviarMensajeWhatsapp()
        }

        return v
    }


    private fun enviarMensajeWhatsapp() {
        val tournamentSelected: Tournament = TournamentsDetailFragmentArgs.fromBundle(requireArguments()).tournamentSelected
        val numeroTelefono = tournamentSelected.telefonoCoordinador

        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$numeroTelefono")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
        }
    }


    private fun openLocationOnMap(direccionCompleta: String) {
        val uri = Uri.parse("geo:0,0?q=$direccionCompleta")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "No se puede abrir Google Maps", Toast.LENGTH_SHORT).show()
        }
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
            }
        }



    }



    private fun setValues(tournamentSelected: Tournament, clubSelected: Club) {
        Log.w("Torneo selecionado", tournamentSelected.toString())


        detailNombre.setText(tournamentSelected.club)
        detailTitulo.setText(tournamentSelected.titulo)
        detailFechaTorneo.setText(tournamentSelected.fecha)
        detailCategorias.setText(tournamentSelected.categoria)
        detailHorario.setText(tournamentSelected.hora)
        detailDireccion.setText(clubSelected.domicilio)
        detailLocalidad.setText(clubSelected.localidad)
        detailCupos.setText(tournamentSelected.cupos.toString())
        detailCancha.setText(tournamentSelected.materialCancha)
        btnInfo = v.findViewById(R.id.btnInfo)
        btnWhatsapp = v.findViewById(R.id.btnWhatsapp)
        btnMapa = v.findViewById(R.id.btnMapa)


    }
}
