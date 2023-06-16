package ar.edu.ort.padel_match_jugador.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.padel_match_jugador.entities.Tournament
import ar.edu.ort.padel_match_jugador.R

class TournamentAdapter(
    private val context: Context,
    private val onItemClick: (Int) -> Unit
) : ListAdapter<Tournament, TournamentAdapter.TournamentViewHolder>(TournamentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tournament_item, parent, false)
        return TournamentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        val tournament = getItem(position)
        holder.bind(tournament)
        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    inner class TournamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tournament: Tournament) {
            // Bind data to views
            val nameTextView = itemView.findViewById<TextView>(R.id.tournament_title)
            nameTextView.text = tournament.titulo

            val categoryTextView = itemView.findViewById<TextView>(R.id.tournament_category)
            categoryTextView.text = tournament.categoría

            val fechaTextView = itemView.findViewById<TextView>(R.id.tournament_date)
            fechaTextView.text = tournament.fecha

            val horaTextView = itemView.findViewById<TextView>(R.id.tournament_hour)
            horaTextView.text = tournament.hora

            val coorNameTextView = itemView.findViewById<TextView>(R.id.tournament_name)
            coorNameTextView.text = tournament.nombreCoordinador

            val coorTelefonoTextView = itemView.findViewById<TextView>(R.id.tournament_phone)
            coorTelefonoTextView.text = tournament.telefonoCoordinador
        }
    }

    private class TournamentDiffCallback : DiffUtil.ItemCallback<Tournament>() {
        override fun areItemsTheSame(oldItem: Tournament, newItem: Tournament): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tournament, newItem: Tournament): Boolean {
            return oldItem == newItem
        }
    }

    // Método para actualizar la lista de torneos en el adaptador
    fun updateTournaments(tournaments: List<Tournament>) {
        submitList(tournaments)
    }
}
