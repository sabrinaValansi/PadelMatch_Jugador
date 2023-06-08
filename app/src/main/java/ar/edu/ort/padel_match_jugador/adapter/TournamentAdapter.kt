package ar.edu.ort.padel_match_jugador.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.padel_match_jugador.R
import ar.edu.ort.padel_match_jugador.entities.Tournament

data class TournamentAdapter(
    private var tournamentList: MutableList<Tournament>,
    val context: Context,
    val onItemCLick: (Int) -> Unit
): RecyclerView.Adapter<TournamentAdapter.TournamentHolder >()  {

    class TournamentHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }

        fun setTitle(name: String) {
            val txt: TextView = view.findViewById(R.id.tournament_title )
            txt.text = name
        }

        fun setCategory(name: String) {
            val txt: TextView = view.findViewById(R.id.tournament_category )
            txt.text = name
        }

        fun setDate(name: String) {
            val txt: TextView = view.findViewById(R.id.tournament_date )
            txt.text = name
        }

        fun setHour(name: String) {
            val txt: TextView = view.findViewById(R.id.tournament_hour)
            txt.text = name
        }

        fun setOrganizador(name: String) {

            val txt: TextView = view.findViewById(R.id.tournament_name)
            txt.text = name
        }

        fun setTelefono(name: String) {
            val txt: TextView = view.findViewById(R.id.tournament_phone)
            txt.text = name
        }



        fun getCardLayout ():CardView{
            return view.findViewById(R.id.item_torneo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.tournament_item,parent,false)

        return (TournamentHolder(view))
    }

    override fun getItemCount(): Int {
        return tournamentList.size
    }

    override fun onBindViewHolder(holder: TournamentHolder, position: Int) {

        holder.setTitle(tournamentList[position].titulo )
        holder.setCategory(tournamentList[position].categoría )
        holder.setDate(tournamentList[position].fecha )
        holder.setHour(tournamentList[position].hora )
        holder.setOrganizador(tournamentList[position].nombreCoordinador)
        holder.setTelefono(tournamentList[position].telefonoCoordinador)

        holder.getCardLayout().setOnClickListener{
            onItemCLick(position)
        }

    }
}


