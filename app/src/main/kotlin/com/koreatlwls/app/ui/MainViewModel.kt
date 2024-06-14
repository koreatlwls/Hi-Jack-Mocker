package com.koreatlwls.app.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatlwls.app.remote.PokemonService
import com.koreatlwls.app.remote.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val pokemonService: PokemonService,
) : ViewModel() {

    val pokemons = mutableStateListOf<Item>()

    init {
        getPokemons()
    }

    fun getPokemons() {
        pokemons.clear()

        viewModelScope.launch {
            pokemons.addAll(pokemonService.getPokemons(0, 20).items)
        }
    }
}