package ar.edu.ort.padel_match_jugador.entities
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tournament(
    var titulo: String,
    var club: String,
    var fecha: String,
    var hora: String,
    var categoria: String,
    var materialCancha: String,
    var cupos: Number,
    var costoInscripción: Number,
    var premios: String,
    var imagenTorneo: String,
    var uid: String,
    var idClub: String,
    var nombreCoordinador: String,
    val telefonoCoordinador: String
) : Parcelable
