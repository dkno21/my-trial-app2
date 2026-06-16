package com.percobaan.me 

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore // Import ditaruh di sini

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilan) 
        getEksternalFilesDir(null)

        // 1. Inisialisasi Database
        val db = FirebaseFirestore.getInstance()

        // 2. Definisi elemen
        val inputTeks = findViewById<EditText>(R.id.inputTeks)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnHapus = findViewById<Button>(R.id.btnHapus)

        // 3. Aksi Tombol Simpan (Menggunakan Update)
 btnSimpan.setOnClickListener {
    val isiTeks = inputTeks.text.toString()

    if (isiTeks.isNotEmpty()) {
        db.collection("users").document("YpabdicodRZLzZ1vVRS5")
            .update("usage_history", isiTeks)
            .addOnSuccessListener {
                // Berhasil: Gabungkan aksi Toast dan Log di sini
                Log.d("FirebaseTest", "Berhasil terhubung ke Cloud!")
                Toast.makeText(this, "Berhasil simpan ke Cloud!", Toast.LENGTH_SHORT).show()
                inputTeks.setText("") 
            }
            .addOnFailureListener { e ->
                // Gagal: Tampilkan error di Log dan Toast
                Log.e("FirebaseTest", "Gagal: ${e.message}")
                Toast.makeText(this, "Gagal simpan ke Cloud", Toast.LENGTH_SHORT).show()
            }
    } else {
        Toast.makeText(this, "Teks tidak boleh kosong!", Toast.LENGTH_SHORT).show()
    }
}


        // 4. Aksi Tombol Hapus
        btnHapus.setOnClickListener {
            inputTeks.setText("")
            Toast.makeText(this, "Teks telah dihapus", Toast.LENGTH_SHORT).show()
        }
    }
}
