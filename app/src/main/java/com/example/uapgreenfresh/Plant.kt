package com.example.uapgreenfresh
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GenericResponse(
    @SerializedName("message")
    val message: String
)

data class SinglePlantResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Plant?
)

data class PlantListResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<Plant>
)

@Parcelize
data class Plant(
    @SerializedName("id")
    val id: Int,

    @SerializedName("plant_name")
    val plantName: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable

data class PlantRequest(
    @SerializedName("plant_name")
    val plantName: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: String,

    )
