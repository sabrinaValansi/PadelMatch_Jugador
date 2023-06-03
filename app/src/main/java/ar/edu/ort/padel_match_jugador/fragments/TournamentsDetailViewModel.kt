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

class TournamentsDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val db = Firebase.firestore
    val storage = Firebase.storage(Firebase.app)


    suspend fun getClubById(clubId: String): Club? {
        val documentSnapshot = db.collection("clubs").document(clubId).get().await()
        val club = documentSnapshot.toObject(Club::class.java)
        if (club != null) {
            club.id = documentSnapshot.id
        }
        return club
    }


}