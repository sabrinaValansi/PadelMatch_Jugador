package ar.edu.ort.padel_match_jugador.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Club(
    var id: String,
    var nombre:String,
    var cuit:String,
    var provincia:String,
    var partido:String,
    var localidad:String,
    var domicilio:String,
    var email: String,
    var telefonos:String,
    var userId: String,
): Parcelable
