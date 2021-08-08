package ows.kotlinstudy.githubrepo.utility

import android.content.Context
import android.preference.PreferenceManager

class AuthTokenProvider(private val context: Context) {
    companion object {
        private const val GITHUB_KEY = "github_key"
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    fun updateToken(token: String) {
        context.getSharedPreferences(GITHUB_KEY, Context.MODE_PRIVATE).edit().apply {
            putString(KEY_AUTH_TOKEN, token)
            apply()
        }
    }

    val token: String?
        get() = context.getSharedPreferences(GITHUB_KEY, Context.MODE_PRIVATE)
            .getString(KEY_AUTH_TOKEN,null)
}