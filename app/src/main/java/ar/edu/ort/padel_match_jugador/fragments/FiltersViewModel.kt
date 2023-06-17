package ar.edu.ort.padel_match_jugador.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import ar.edu.ort.padel_match_jugador.entities.Tournament
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

class FiltersViewModel : ViewModel() {

    val db = Firebase.firestore

    fun createDatePicker(): MaterialDatePicker<Long> {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccione una fecha")
            .setSelection(today)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.from(today))
                    .build()
            )
            .build();
    }

    fun createTimePicker() : MaterialTimePicker {
        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Seleccione una hora")
            .build()
    }

    suspend fun getPartidosList(): QuerySnapshot {
        val query =  db.collection("partidos")
        val clubs = query.get().await();

        return clubs;
    }

    suspend fun getCategoriasList(): Array<String> {
        val query =  db.collection("categorias")
        val categorias = query.get().await();
        val data = categorias.map { t -> t.data["nombreCategoria"] }[0] as List<String>
        var list = data.toTypedArray()
        return list;
    }

    // En FiltersViewModel
    suspend fun getFilteredMatches(
        fechaDesde: String?,
        fechaHasta: String?,
        horario: String?,
        partido: String?,
        localidad: String?,
        categoria: String?
    ) {
        val query = db.collection("tournaments")

        fechaDesde?.let {
            query.whereGreaterThanOrEqualTo("fecha", fechaDesde)
        }
        fechaHasta?.let {
            query.whereLessThanOrEqualTo("fecha", fechaHasta)
        }
        horario?.let {
            query.whereEqualTo("hora", horario)
        }
       /*  partido?.let {
            query.whereEqualTo("partido", partido)
        }
        localidad?.let {
            query.whereEqualTo("localidad", localidad)
        } */
        categoria?.let {
            query.whereEqualTo("categoria", categoria)
        }

        val result = query.get().await()
        // val matches = result.toObjects(Tournament::class.java)

        Log.w("TORNEOS FILTRADOS", result.documents.toString())

       //  return matches
    }


}