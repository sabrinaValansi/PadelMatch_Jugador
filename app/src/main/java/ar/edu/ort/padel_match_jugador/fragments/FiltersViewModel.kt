package ar.edu.ort.padel_match_jugador.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FiltersViewModel : ViewModel() {
    // Propiedades MutableLiveData para almacenar los valores de los filtros
    private val _fechaDesde = MutableLiveData<String?>()
    val fechaDesde: LiveData<String?> = _fechaDesde

    private val _fechaHasta = MutableLiveData<String?>()
    val fechaHasta: LiveData<String?> = _fechaHasta

    private val _horario = MutableLiveData<String?>()
    val horario: LiveData<String?> = _horario

    private val _partido = MutableLiveData<String?>()
    val partido: LiveData<String?> = _partido

    private val _localidad = MutableLiveData<String?>()
    val localidad: LiveData<String?> = _localidad

    private val _categoria = MutableLiveData<String?>()
    val categoria: LiveData<String?> = _categoria

    // Funciones para actualizar los valores de los filtros
    fun setFechaDesde(fecha: String?) {
        _fechaDesde.value = fecha
    }

    fun setFechaHasta(fecha: String?) {
        _fechaHasta.value = fecha
    }

    fun setHorario(hora: String?) {
        _horario.value = hora
    }

    fun setPartido(partido: String?) {
        _partido.value = partido
    }

    fun setLocalidad(localidad: String?) {
        _localidad.value = localidad
    }

    fun setCategoria(categoria: String?) {
        _categoria.value = categoria
    }

    // Función para aplicar los filtros
    fun applyFilters() {
        // Aquí puedes implementar la lógica para aplicar los filtros según los valores almacenados
        // en las propiedades MutableLiveData. Puedes utilizar los valores directamente o pasarlos a una
        // función en tu capa de datos para realizar la consulta filtrada.
    }

    // Función para borrar los filtros
    fun clearFilters() {
        _fechaDesde.value = null
        _fechaHasta.value = null
        _horario.value = null
        _partido.value = null
        _localidad.value = null
        _categoria.value = null
    }
}
