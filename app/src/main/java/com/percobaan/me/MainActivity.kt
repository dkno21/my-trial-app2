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
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan nama file layout di bawah ini sama dengan file di res/layout/ kamu
        setContentView(R.layout.tampilan)

        // 1. Panggil fungsi folder otomatis saat aplikasi pertama dibuka
        checkAndCreateFolder()

        // 2. Inisialisasi Firebase
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
                        Log.d("FirebaseTest", "Berhasil terhubung!")
                        Toast.makeText(this, "Berhasil simpan ke Cloud!", Toast.LENGTH_SHORT).show()
                        inputTeks.setText("")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirebaseTest", "Gagal: ${e.message}")
                        Toast.makeText(this, "Gagal simpan: ${e.message}", Toast.LENGTH_SHORT).show()
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

    // --- FUNGSI PEMBUAT FOLDER OTOMATIS ---
    private fun checkAndCreateFolder() {
        // Untuk Android 11 ke bawah, butuh cek izin manual
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                return
            }
        }
        
        // Membuat folder privat aplikasi
        val folder = getExternalFilesDir(null)
        if (folder != null && !folder.exists()) {
            folder.mkdirs()
        }
        
        // Uji coba membuat file untuk memastikan folder aktif
        try {
            val testFile = File(folder, "init_check.txt")
            if (!testFile.exists()) {
                testFile.createNewFile()
            }
        } catch (e: IOException) {
            Log.e("FolderTest", "Gagal buat file cek: ${e.message}")
        }
    }

    // Menangani respon dari user saat izin diminta
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkAndCreateFolder()
        }
    }
}
