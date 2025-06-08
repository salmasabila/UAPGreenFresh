package com.example.uapgreenfresh

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.uapgreenfresh.databinding.ActivityDetailBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var currentPlant: Plant? = null
    private var originalPlantName: String? = null

    // Launcher untuk menangani hasil dari UpdateItemActivity
    private val updateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedName = result.data?.getStringExtra("UPDATED_PLANT_NAME") ?: originalPlantName
            updatedName?.let { fetchPlantDetails(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalPlantName = intent.getStringExtra("PLANT_NAME")

        if (originalPlantName == null) {
            Toast.makeText(this, "Nama Tanaman tidak ditemukan", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        fetchPlantDetails(originalPlantName!!)

        binding.btnUpdate.setOnClickListener {
            currentPlant?.let {
                val intent = Intent(this, UpdateItemActivity::class.java)
                intent.putExtra("PLANT_DATA", it)
                updateResultLauncher.launch(intent) // Gunakan launcher untuk membuka activity
            } ?: Toast.makeText(this, "Data tanaman belum dimuat", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPlantDetails(name: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getPlantByName(name)
                if (response.isSuccessful) {
                    currentPlant = response.body()?.data
                    currentPlant?.let { populateUi(it) }
                } else {
                    Toast.makeText(this@DetailActivity, "Gagal memuat detail", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateUi(plant: Plant) {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val formattedPrice = formatter.format(plant.price.toLongOrNull() ?: 0)
            .replace("Rp", "Rp ")
            .replace(",00", "")

        binding.apply {
            tvPlantNameDetail.text = plant.plantName
            tvPlantPriceDetail.text = formattedPrice
            tvPlantDescriptionDetail.text = plant.description

        }
    }
}