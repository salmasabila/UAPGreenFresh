package com.example.uapgreenfresh

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uapgreenfresh.databinding.ActivityAddItemBinding
import kotlinx.coroutines.launch

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            addPlantItem()
        }
    }

    private fun addPlantItem() {
        val name = binding.etPlantName.text.toString().trim()
        val price = binding.etPlantPrice.text.toString().trim()
        val description = binding.etPlantDescription.text.toString().trim()

        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val plantRequest = PlantRequest(
            plantName = name,
            price = price,
            description = description
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.addPlant(plantRequest)
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AddItemActivity,
                        response.body()?.message ?: "Tanaman berhasil ditambahkan!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        this@AddItemActivity,
                        "Gagal menambahkan: $errorCode",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("AddItemError", "Code: $errorCode, Body: $errorBody")
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddItemActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("AddItemException", "Exception: ${e.message}")
            }
        }
    }
}