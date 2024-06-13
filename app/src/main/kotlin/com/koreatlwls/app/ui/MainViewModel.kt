package com.koreatlwls.app.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatlwls.app.remote.PokemonService
import com.koreatlwls.app.remote.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val pokemonService: PokemonService,
) : ViewModel() {

    val firstPokemons = mutableStateListOf<Item>()
    val secondPokemons = mutableStateListOf<Item>()
    val thirdPokemons = mutableStateListOf<Item>()

    init {
        viewModelScope.launch {
            firstPokemons.addAll(pokemonService.getPokemons(0, 20).items)
        }
        viewModelScope.launch {
            delay(5555)
            secondPokemons.addAll(pokemonService.getPokemons(20, 20).items)
        }
        viewModelScope.launch {
            thirdPokemons.addAll(pokemonService.getPokemons(40, 20).items)
        }
    }
}