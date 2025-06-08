package com.example.uapgreenfresh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uapgreenfresh.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var plantAdapter: PlantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.btnAddItem.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        fetchPlants()
    }

    override fun onResume() {
        super.onResume()
        // Muat ulang data setiap kali kembali ke activity ini
        fetchPlants()
    }

    private fun setupRecyclerView() {
        plantAdapter = PlantAdapter(
            plants = emptyList(),
            onDetailClick = { plant ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("PLANT_NAME", plant.plantName)
                    // Anda bisa mengirim seluruh objek jika membuatnya Parcelable
                }
                startActivity(intent)
            },
            onDeleteClick = { plant ->
                deletePlant(plant.plantName)
            }
        )
        binding.rvPlants.apply {
            adapter = plantAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun fetchPlants() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getAllPlants()
                if (response.isSuccessful) {
                    val plantList = response.body()?.data
                    plantAdapter.updateData(plantList ?: emptyList())
                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@MainActivity, "Gagal memuat data. Kode: $errorCode", Toast.LENGTH_SHORT).show()
                    Log.e("FetchPlantsError", "Code: $errorCode, Body: $errorBody")
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deletePlant(plantName: String) {
        lifecycleScope.launch {
            try {
                // DIUBAH: kirim plantName ke fungsi API
                val response = RetrofitClient.instance.deletePlant(plantName)
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, response.body()?.message ?: "Item dihapus", Toast.LENGTH_SHORT).show()
                    fetchPlants() // Muat ulang daftar setelah hapus
                } else {
                    Toast.makeText(this@MainActivity, "Gagal menghapus item", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}