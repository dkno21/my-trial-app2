package com.percobaan.me

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilan)

        // 1. Pemicu Pembuatan Folder Otomatis
        // Folder akan otomatis terbuat di: Android/data/com.percobaan.me/files/
        checkAndCreateFolder()

        // 2. Inisialisasi Database
        val db = FirebaseFirestore.getInstance()

        // 3. Definisi elemen UI
        val inputTeks = findViewById<EditText>(R.id.inputTeks)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnHapus = findViewById<Button>(R.id.btnHapus)

        // 4. Aksi Tombol Simpan
        btnSimpan.setOnClickListener {
            val isiTeks = inputTeks.text.toString()

            if (isiTeks.isNotEmpty()) {
                db.collection("users").document("YpabdicodRZLzZ1vVRS5")
                    .update("usage_history", isiTeks)
                    .addOnSuccessListener {
                        Log.d("FirebaseTest", "Berhasil terhubung ke Cloud!")
                        Toast.makeText(this, "Berhasil simpan ke Cloud!", Toast.LENGTH_SHORT).show()
                        inputTeks.setText("")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirebaseTest", "Gagal: ${e.message}")
                        Toast.makeText(this, "Gagal simpan ke Cloud", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Teks tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. Aksi Tombol Hapus
        btnHapus.setOnClickListener {
            inputTeks.setText("")
            Toast.makeText(this, "Teks telah dihapus", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndCreateFolder() {
        // Android 11+ (API 30+) tidak butuh izin runtime untuk folder privat aplikasi
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                return
            }
        }
        // Membuat folder privat aplikasi
        getExternalFilesDir(null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getExternalFilesDir(null)
        }
    }
}
