package com.example.uapgreenfresh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uapgreenfresh.databinding.ItemPlantBinding
import java.text.NumberFormat
import java.util.Locale

class PlantAdapter(
    private var plants: List<Plant>,
    private val onDetailClick: (Plant) -> Unit,
    private val onDeleteClick: (Plant) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        with(holder.binding) {
            tvPlantName.text = plant.plantName

            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            val formattedPrice = try {
                formatter.format(plant.price.toLong())
                    .replace("Rp", "Rp ")
                    .replace(",00", "")
            } catch (e: NumberFormatException) {
                "Rp ${plant.price}"
            }
            tvPlantPrice.text = formattedPrice

            ivPlantImage.setImageResource(R.drawable.ic_placeholder_image)

            btnDetail.setOnClickListener { onDetailClick(plant) }
            btnDelete.setOnClickListener { onDeleteClick(plant) }
        }
    }

    override fun getItemCount() = plants.size

    fun updateData(newPlants: List<Plant>) {
        plants = newPlants
        notifyDataSetChanged()
    }
}