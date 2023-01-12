package yt.ftnl.spotify_tools

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import kotlin.reflect.KProperty

class ConfigurationException(message: String) : Exception(message)
data class Configuration(
    val clientId : String = "",
    val clientSecret : String = "",
    val port: Int = 8080,
    val redirectUri: String = "http://localhost:8080/callback",
) {    companion object {
        lateinit var loadedConfig: Configuration
        fun loadConfiguration(file: File): Configuration {
            if (file.createNewFile()) {
                val config = Configuration()
                file.writeText(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(config))
                throw ConfigurationException("Veuillez remplir le fichier de configuration")
            }
            return try {
                val cfg = Gson().fromJson(file.readText(), Configuration::class.java)
                file.writeText(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(cfg))
                cfg
            } catch (e: Exception) {
                throw ConfigurationException("La configuration n'est pas valide")
            }
        }
    }
}
