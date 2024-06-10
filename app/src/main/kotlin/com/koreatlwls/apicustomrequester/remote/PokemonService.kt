package com.koreatlwls.apicustomrequester.remote

import com.koreatlwls.apicustomrequester.remote.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PokemonService {
    @GET("api/v2/pokemon/")
    suspend fun getPokemons(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20,
    ): PokemonResponse

}