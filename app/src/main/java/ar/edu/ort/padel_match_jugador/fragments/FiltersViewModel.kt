package ar.edu.ort.padel_match_jugador.fragments

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getFilteredMatches(
        fechaDesde: String?,
        fechaHasta: String?,
        horario: String?,
        partido: String?,
        localidad: String?,
        categoria: String?
    ): ArrayList<Tournament> {
        var query = db.collection("tournaments")

        val result = query.get().await()

        var list = arrayListOf<Tournament>()

        result.forEach{ doc ->
            val data = doc.data!!
            val id = data["id"] as String
            val titulo = data["titulo"] as String
            val fecha = data["fecha"] as String
            val hora = data["hora"] as String
            val cat = data["categoría"] as String
            val cupos = data["cupos"] as Number
            val costoInscripción = data["costoInscripción"] as Number
            val material = data["materialCancha"] as String
            val premios = data["premios"] as String
            val imagenTorneo = data["imagenTorneo"] as String
            val userId = data["userId"] as String
            val idClub = data["idClub"] as String
            var nombreCoor = data["nombreCoordinador"] as String
            var telefonoCood =data["telefonoCoordinador"] as String

            val torneo = Tournament(id, titulo, fecha, hora, cat, material, cupos, costoInscripción, premios, imagenTorneo, userId, idClub, nombreCoor, telefonoCood)
            list.add(torneo)
        }


        if ( !fechaDesde.isNullOrBlank() ) {
            val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val f2 = LocalDate.parse(fechaDesde, formato)
            list = list.filter { d -> f2.isBefore(LocalDate.parse(d.fecha, formato)) }as ArrayList<Tournament>
        }

        if ( !fechaHasta.isNullOrBlank() ) {
            val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val f2 = LocalDate.parse(fechaHasta, formato)
            list = list.filter { d -> f2.isAfter(LocalDate.parse(d.fecha, formato)) }as ArrayList<Tournament>
        }


        if ( !partido.isNullOrBlank() ) {
            list = list.filter { d -> db.collection("clubs").document(d.idClub).get().await().data!!["partido"] == partido } as ArrayList<Tournament>

            if ( !localidad.isNullOrBlank() ) {
                list = list.filter { d -> db.collection("clubs").document(d.idClub).get().await().data!!["localidad"] == localidad } as ArrayList<Tournament>
            }
        }

        if ( !horario.isNullOrBlank() ) {
            list = list.filter { d -> d.hora == horario } as ArrayList<Tournament>
        }

        if ( !categoria.isNullOrBlank() ) {
            list = list.filter { d -> d.categoría == categoria } as ArrayList<Tournament>
        }

        Log.w("LISTA FILTRADA", list.toString())

        return list
    }



}