package ie.coconnor.mobileappdev.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val PREF_DARK_MODE = "dark_mode"
    }

    private val context: Context = context.applicationContext

    @Volatile
    private var sharedPref: SharedPreferences? = null

    private fun getSharedPerf(): SharedPreferences {
        return sharedPref ?: synchronized(this) {
            context.getSharedPreferences(
                "${context.packageName}.main",
                Context.MODE_PRIVATE
            )
        }
    }

    fun reset() {
        getSharedPerf().edit().clear().apply()
    }

    fun setDarkMode(isDark: Boolean) {
        getSharedPerf()
            .edit()
            .apply {
                putBoolean(PREF_DARK_MODE, isDark)
                apply()
            }
    }

    fun getDarkMode(): Boolean {
        return getSharedPerf().getBoolean(PREF_DARK_MODE, false)
    }
}