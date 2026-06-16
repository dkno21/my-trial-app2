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

        // Panggil semua fungsi startup
        requestAllPermissions()
        createNotificationChannel()
        checkAndCreateFolder()

        // Firebase Logic
        val db = FirebaseFirestore.getInstance()
        val inputTeks = findViewById<EditText>(R.id.inputTeks)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnHapus = findViewById<Button>(R.id.btnHapus)

        btnSimpan.setOnClickListener {
            val isiTeks = inputTeks.text.toString()
            if (isiTeks.isNotEmpty()) {
                db.collection("users").document("YpabdicodRZLzZ1vVRS5")
                    .update("usage_history", isiTeks)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Berhasil!", Toast.LENGTH_SHORT).show()
                        inputTeks.setText("")
                    }
                    .addOnFailureListener { e -> 
                        Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show() 
                    }
            }
        }

        btnHapus.setOnClickListener { inputTeks.setText("") }
    }

    private fun requestAllPermissions() {
        val permissions = mutableListOf<String>()
        
        // Izin Notifikasi (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        // Izin Penyimpanan (Android 9 ke bawah)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 100)
        }
    }

    private fun checkAndCreateFolder() {
        // Folder privat aplikasi selalu ada di getExternalFilesDir
        val folder = getExternalFilesDir(null)
        if (folder != null && !folder.exists()) {
            val created = folder.mkdirs()
            Log.d("AppFolder", "Folder berhasil dibuat: $created")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID_AERA", 
                "Update Info", 
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
