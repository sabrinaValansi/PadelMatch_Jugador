package ar.edu.ort.padel_match_jugador.fragments

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import ar.edu.ort.padel_match_jugador.entities.Club
import ar.edu.ort.padel_match_jugador.entities.Tournament
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class TournamentsDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val db = Firebase.firestore
    val storage = Firebase.storage(Firebase.app)


    suspend fun getClubById(clubId: String): Club? {
        val documentSnapshot = db.collection("clubs").document(clubId).get().await()

        var id= documentSnapshot.data!!.get("id") as String
        var nombre= documentSnapshot.data!!.get("id") as String
        var cuit= documentSnapshot.data!!.get("id") as String
        var provincia= documentSnapshot.data!!.get("id") as String
        var partido= documentSnapshot.data!!.get("id") as String
        var localidad= documentSnapshot.data!!.get("id") as String
        var domicilio= documentSnapshot.data!!.get("id") as String
        var email= documentSnapshot.data!!.get("id") as String
        var telefonos= documentSnapshot.data!!.get("id") as String
        var userId= documentSnapshot.data!!.get("id") as String

        val club = Club(id,nombre,cuit,provincia,partido,localidad,domicilio,email,telefonos,userId)
        return club
    }


}