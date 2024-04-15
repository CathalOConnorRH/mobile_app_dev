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
        private const val LOCATION_ID = ""
        private const val LOCATION_TO_SEARCH = "Waterford, Ireland"

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

    fun setLocationId(location_id: String) {
        getSharedPerf()
            .edit()
            .apply {
                putString(LOCATION_ID, location_id)
                apply()
            }
    }

    fun getLocationId(): String {
        return getSharedPerf().getString(LOCATION_ID, "").toString()
    }

    fun setLocationToSearch(location: String) {
        getSharedPerf()
            .edit()
            .apply {
                putString(LOCATION_TO_SEARCH, location)
                apply()
            }
    }

    fun getLocationToSearch(): String {
        return getSharedPerf().getString(LOCATION_TO_SEARCH, LOCATION_TO_SEARCH).toString()
    }
}
