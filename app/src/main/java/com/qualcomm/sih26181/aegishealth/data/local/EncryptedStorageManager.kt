package com.qualcomm.sih26181.aegishealth.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedStorageManager(context: Context) {

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = try {
        EncryptedSharedPreferences.create(
            context,
            "aegis_encrypted_vault",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        // Fallback for simulation or unencrypted fallback key
        context.getSharedPreferences("aegis_vault_fallback", Context.MODE_PRIVATE)
    }

    fun saveEncryptedString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getEncryptedString(key: String, default: String = ""): String {
        return sharedPreferences.getString(key, default) ?: default
    }

    fun saveEncryptedBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getEncryptedBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun purgeVault() {
        sharedPreferences.edit().clear().apply()
    }
}
