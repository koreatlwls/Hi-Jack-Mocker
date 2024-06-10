package com.koreatlwls.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatlwls.app.remote.PokemonService
import com.koreatlwls.app.remote.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val pokemonService: PokemonService,
) : ViewModel() {

    private val _pokemons: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
    val pokemons = _pokemons.asStateFlow()

    init {
        viewModelScope.launch {
            _pokemons.value = pokemonService.getPokemons(0, 20).items
        }
    }
}