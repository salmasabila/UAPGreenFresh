package com.example.uapgreenfresh

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("plant/all")
    suspend fun getAllPlants(): Response<PlantListResponse>

    @GET("plant/{name}")
    suspend fun getPlantByName(@Path("name") plantName: String): Response<SinglePlantResponse>

    @POST("plant/new")
    suspend fun addPlant(@Body plantRequest: PlantRequest): Response<SinglePlantResponse>

    @PUT("plant/{name}")
    suspend fun updatePlant(
        @Path("name") plantName: String,
        @Body plantRequest: PlantRequest
    ): Response<SinglePlantResponse>

    @DELETE("plant/{name}")
    suspend fun deletePlant(@Path("name") plantName: String): Response<GenericResponse>
}