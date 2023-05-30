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
import com.google.firebase.firestore.ktx.firestore
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*



class TournamentsDetailFragment : Fragment() {


    private lateinit var v: View
    private lateinit var titulo: TextView
    private lateinit var detailNombre: TextView
    private lateinit var detailFecha: TextView
    private lateinit var detailCategorias: TextView
    private lateinit var detailHorario: TextView
    private lateinit var detailCupos: TextView
    private lateinit var detailMateriales: TextView
    private lateinit var detailCostoInscripcion: TextView
    private lateinit var detailClub: TextView
    private lateinit var detailPremio: TextView
    private lateinit var detailImagen: TextView
    private lateinit var imageDisplay : ImageView
    private lateinit var viewModel: TournamentsDetailViewModel
    private lateinit var imageUri: Uri

    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_tournaments_detail, container, false)

        titulo = v.findViewById(R.id.detail_tituloNombre)

        detailNombre = v.findViewById(R.id.detail_name)

        detailFecha = v.findViewById(R.id.detail_date)

        detailClub = v.findViewById(R.id.detail_club)

        detailCategorias = v.findViewById(R.id.detail_categorias)

        detailHorario = v.findViewById(R.id.detail_hour)

        detailCupos = v.findViewById(R.id.detail_cupos)

        detailMateriales = v.findViewById(R.id.detail_materiales)

        detailCostoInscripcion = v.findViewById(R.id.detail_costoInscripcion)

        detailPremio = v.findViewById(R.id.detail_premio)

        detailImagen = v.findViewById(R.id.ImagenEditTextDetail)

        imageDisplay = v.findViewById(R.id.imagenDetailTorneo)

        blockFields()

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

        setValues( tournamentSelected )


        lifecycleScope.launch {

            var data = viewModel.getClubsList()
            ( detailClub as? MaterialAutoCompleteTextView)?.setSimpleItems(data);

            var data_cat = viewModel.getCategoriasList()
            ( detailCategorias as? MaterialAutoCompleteTextView)?.setSimpleItems(data_cat)

            var data_material = viewModel.getMaterialesList();
            ( detailMateriales as? MaterialAutoCompleteTextView)?.setSimpleItems(data_material)
        }

        detailImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

    }

    private fun setValues(tournamentSelected: Tournament) {
        Log.w("Torneo selecionado", tournamentSelected.toString())
        titulo.text = tournamentSelected.titulo
        detailNombre.setText(tournamentSelected.titulo)
        detailClub.setText(tournamentSelected.club)
        detailFecha.setText(tournamentSelected.fecha)
        detailCategorias.setText(tournamentSelected.categoría)
        detailHorario.setText(tournamentSelected.hora)
        detailCupos.setText(tournamentSelected.cupos.toString())
        detailCostoInscripcion.setText(tournamentSelected.costoInscripción.toString())
        detailPremio.setText(tournamentSelected.premios)
        detailMateriales.setText(tournamentSelected.materialCancha)

    }


    private fun blockFields() {
        titulo.isEnabled = false
        detailNombre.isEnabled = false
        detailFecha.isEnabled = false
        detailCategorias.isEnabled = false
        detailHorario.isEnabled = false
        detailCupos.isEnabled = false
    }

    private fun createTournament(): Tournament {

        val nombre = detailNombre.text.toString();
        val club = detailClub.text.toString();
        val date = detailFecha.text.toString();
        val hour = detailHorario.text.toString();
        val category = detailCategorias.text.toString();
        val material = detailMateriales.text.toString();
        val cupo = detailCupos.text.toString().toInt()
        val cost = detailCupos.text.toString().toInt();
        val premio = detailPremio.text.toString();
        val udi = auth.currentUser!!.uid
        var idClub = ""

        lifecycleScope.launch {
            idClub = viewModel.getIdClubByName(club)
        }

        val retorno = Tournament( nombre, club, date, hour, category, material, cupo, cost, premio,
            "loading...", udi, idClub )

        return retorno
    }

}
