package ar.edu.ort.padel_match_jugador.fragments

import android.icu.text.CaseMap.Lower
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.padel_match_jugador.entities.Club
import ar.edu.ort.padel_match_jugador.entities.Tournament
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class TournamentsViewModel : ViewModel() {
    val db = Firebase.firestore

    private var auth: FirebaseAuth = Firebase.auth

    suspend fun getTournament(): MutableList<Tournament> {

        var list = mutableListOf<Tournament>();
        //var uid = auth.currentUser!!.uid
        val documents = db.collection("tournaments").get().await()
        documents.forEach { data ->
            val id = data["id"] as? String?: "ID"
            val titulo = data["titulo"] as? String ?: "Torneo default"
            val club = data["club"] as? String ?: "Torneo default"
            val fecha = data["fecha"] as? String ?: "No se proporciono fecha"
            val hora = data["hora"] as? String ?: "No se proporciono hora"
            val cat = data["categoría"] as? String ?: "No se proporciono categoria"
            val cupos = data["cupos"] as? Number ?: 0
            val costoInscripción = data["costoInscripción"] as? Number ?: 0
            val material = data["materialCancha"] as? String ?: "No se proporciono premios"
            val premios = data["premios"] as? String ?: "No se proporciono premios"
            val imagenTorneo = data["imagenTorneo"] as? String ?: "No se proporciono imagenTorneo"
            val userId = data["uid"] as? String ?: ""
            val idClub = data["idClub"] as? String ?: ""
            var nombreCoor = data["nombreCoordinador"] as? String ?: ""
            var telefonoCood =data["telefonoCoordinador"] as? String ?: ""


            val torneo =  Tournament(id, titulo, fecha, hora, cat, material, cupos,  costoInscripción, premios, imagenTorneo, userId, idClub, nombreCoor, telefonoCood)

            list.add(torneo);
        }

        return list
    }

    fun deleteOldTournaments() {
        val currentDate = Date()
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Cambia el formato aquí

        db.collection("tournaments")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val dateString = document.getString("fecha")
                    val tournamentDate = format.parse(dateString)
                    if (tournamentDate != null && tournamentDate.before(currentDate)) {
                        db.collection("tournaments").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("DeleteSuccess","Successfully deleted old tournament with id: ${document.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("DeleteError", "Error deleting old tournament", e)
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents: ", exception)
            }
    }

    suspend fun getClub(id: String): Club {

        lateinit var club: Club
        val document = db.collection("clubs").whereEqualTo("id", id).get().await()
        var data = document.documents.get(0);
        var id = data!!["id"] as String
        var nombre= data!!["nombre"] as String
        var cuit= data!!["cuit"] as String
        var provincia= data!!["provincia"] as String
        var partido= data!!["partido"] as String
        var localidad= data!!["localidad"] as String
        var domicilio= data!!["domicilio"] as String
        var email= data!!["email"] as String
        var telefonos= data!!["telefonos"] as String
        var userId= data!!["userId"] as String

        club = Club(id,nombre,cuit,provincia,partido,localidad,domicilio,email,telefonos,userId,"INFO")

        return club
    }
}

