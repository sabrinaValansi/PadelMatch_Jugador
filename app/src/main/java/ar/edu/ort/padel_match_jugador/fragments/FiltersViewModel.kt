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
    ): List<Tournament> {
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

        val list = arrayListOf<Tournament>()

        result.forEach{ doc ->
            val data = doc.data
            val id = data["id"] as String
            val titulo = data["titulo"] as String
            val club = data["club"] as String
            val fecha = data["fecha"] as String
            val hora = data["hora"] as String
            val cat = data["categoría"] as String
            val cupos = data["cupos"] as Number
            val costoInscripción = data["costoInscripción"] as Number
            val material = data["materialCancha"] as String
            val premios = data["premios"] as String
            val imagenTorneo = data["imagenTorneo"] as String
            val userId = data["uid"] as String
            val idClub = data["idClub"] as String
            var nombreCoor = data["nombreCoordinador"] as String
            var telefonoCood =data["telefonoCoordinador"] as String

            val torneo = Tournament(id, titulo, fecha, hora, cat, material, cupos, costoInscripción, premios, imagenTorneo, userId, idClub, nombreCoor, telefonoCood)
            list.add(torneo)
        }

        Log.w("TORNEOS FILTRADOS", list.toString())

        return list
    }



}