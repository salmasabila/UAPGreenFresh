package com.example.uapgreenfresh

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.uapgreenfresh.databinding.ActivityUpdateItemBinding
import kotlinx.coroutines.launch

class UpdateItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateItemBinding
    private var plantToUpdate: Plant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        plantToUpdate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("PLANT_DATA", Plant::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("PLANT_DATA")
        }

        if (plantToUpdate == null) {
            Toast.makeText(this, "Data tanaman tidak ditemukan", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        populateForm(plantToUpdate!!)

        binding.btnSaveUpdate.setOnClickListener {
            performUpdate()
        }
    }

    private fun populateForm(plant: Plant) {
        binding.apply {
            etPlantNameUpdate.setText(plant.plantName)
            etPlantPriceUpdate.setText(plant.price)
            etPlantDescriptionUpdate.setText(plant.description)
        }
    }

    private fun performUpdate() {
        val originalPlantName = plantToUpdate?.plantName ?: return

        val updatedName = binding.etPlantNameUpdate.text.toString().trim()
        val updatedPrice = binding.etPlantPriceUpdate.text.toString().trim()
        val updatedDescription = binding.etPlantDescriptionUpdate.text.toString().trim()

        if (updatedName.isEmpty() || updatedPrice.isEmpty() || updatedDescription.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val plantRequest = PlantRequest(
            plantName = updatedName,
            price = updatedPrice,
            description = updatedDescription
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.updatePlant(originalPlantName, plantRequest)
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateItemActivity, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK) // Beri sinyal ke DetailActivity bahwa update berhasil
                    finish()
                } else {
                    Toast.makeText(this@UpdateItemActivity, "Gagal memperbarui: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@UpdateItemActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}