package com.percobaan.me

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
        setContentView(R.layout.tampilan)

        // 1. Inisialisasi Fitur
        checkAndCreateFolder()
        createNotificationChannel()

        // 2. Minta Izin Notifikasi (Untuk Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // 3. Inisialisasi Firebase
        val db = FirebaseFirestore.getInstance()

        // 4. UI Elements
        val inputTeks = findViewById<EditText>(R.id.inputTeks)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnHapus = findViewById<Button>(R.id.btnHapus)

        btnSimpan.setOnClickListener {
            val isiTeks = inputTeks.text.toString()
            if (isiTeks.isNotEmpty()) {
                db.collection("users").document("YpabdicodRZLzZ1vVRS5")
                    .update("usage_history", isiTeks)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Berhasil simpan!", Toast.LENGTH_SHORT).show()
                        inputTeks.setText("")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Teks kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        btnHapus.setOnClickListener {
            inputTeks.setText("")
        }
    }

    // --- FUNGSI FOLDER ---
    private fun checkAndCreateFolder() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                return
            }
        }
        val folder = getExternalFilesDir(null)
        if (folder != null && !folder.exists()) {
            folder.mkdirs()
        }
        try {
            val testFile = File(folder, "init_check.txt")
            if (!testFile.exists()) testFile.createNewFile()
        } catch (e: IOException) {
            Log.e("FolderTest", "Error: ${e.message}")
        }
    }

    // --- FUNGSI NOTIFIKASI CHANNEL ---
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Update Info"
            val channel = NotificationChannel("CHANNEL_ID_AERA", name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notifikasi dari Cloud"
            }
            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkAndCreateFolder()
        }
    }
}
