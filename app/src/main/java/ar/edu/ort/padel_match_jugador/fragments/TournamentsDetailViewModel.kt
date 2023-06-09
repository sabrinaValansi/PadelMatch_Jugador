import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.LocusId
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.entities.Club
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.bumptech.glide.Glide
import kotlinx.coroutines.tasks.await

class TournamentsDetailViewModel : ViewModel() {

    private var club: Club? = null
    private val db = Firebase.firestore
    val storage = Firebase.storage(Firebase.app)
    val storageRef = storage.reference

    suspend fun getClubById(clubId: String): Club? {
        val documentSnapshot = db.collection("clubs").document(clubId).get().await()

        val id = documentSnapshot.data!!.get("id") as String
        val nombre = documentSnapshot.data!!.get("nombre") as String
        val cuit = documentSnapshot.data!!.get("cuit") as String
        val provincia = documentSnapshot.data!!.get("provincia") as String
        val partido = documentSnapshot.data!!.get("partido") as String
        val localidad = documentSnapshot.data!!.get("localidad") as String
        val domicilio = documentSnapshot.data!!.get("domicilio") as String
        val email = documentSnapshot.data!!.get("email") as String
        val telefonos = documentSnapshot.data!!.get("telefonos") as String
        val userId = documentSnapshot.data!!.get("userId") as String

        val direccionCompleta = "${domicilio}, ${partido}"

        club = Club(id, nombre, cuit, provincia, partido, localidad, domicilio, email, telefonos, userId)
        club?.direccionCompleta = direccionCompleta

        return club
    }


    fun mostrarInformacion(context: Context, userId: String) {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_flyer, null)
        val imageViewFlyer = dialogView.findViewById<ImageView>(R.id.imageViewFlyer)

        Glide.with(context).load(userId).into(imageViewFlyer)

        // Resto del código para configurar el diálogo
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

}
