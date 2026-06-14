# Aplikasi Bukti Konsep

Ini adalah aplikasi Android sederhana yang berbasis pada "Empty Views Activity".

**Detail:**
- Base original: https://github.com/hextreeio/android-poc-app

# Penyelesaian Masalah (Troubleshooting)

## ActivityNotFoundException

Jika Anda menemui kesalahan berikut saat mencoba memulai aktivitas yang Anda tahu ada, coba tambahkan `<queries>` tag pada manifest Android.
```
android.content.ActivityNotFoundException: Tidak dapat menemukan kelas aktivitas eksplisit {...}; apakah Anda sudah mendeklarasikan aktivitas ini di AndroidManifest.xml Anda, atau apakah intent Anda tidak sesuai dengan deklarasinya? <intent-filter>?
```
                                                                                                    
```xml
<queries>
    <package android:name="<package_name>" />
</queries>
```
