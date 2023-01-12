package yt.ftnl.spotify_tools.plugins

import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import yt.ftnl.spotify_tools.CONFIG
import yt.ftnl.spotify_tools.spotifyData.SongData

fun Application.configureSecurity() {
    
    authentication {
        oauth("auth-oauth-spotify") {
            urlProvider = { CONFIG.redirectUri }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "Spotify",
                    authorizeUrl = "https://accounts.spotify.com/authorize",
                    accessTokenUrl = "https://accounts.spotify.com/api/token",
                    requestMethod = HttpMethod.Post,
                    clientId = CONFIG.clientId,
                    clientSecret = CONFIG.clientSecret,
                    defaultScopes = listOf("user-read-currently-playing")
                )
            }
            client = HttpClient(Apache)
        }
    }
    data class SpotifySession(
        val accessToken: String,
        val refreshToken: String,
        val tokenType: String,
        val expiresAt: Long,
    ): Principal
    install(Sessions) {
        cookie<SpotifySession>("SpotifySession") {
            cookie.extensions["SameSite"] = "lax"
        }
        
        cookie<SongData>("UserSongs") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    
    routing {
        authenticate("auth-oauth-spotify") {
            get("login") {
                call.respondRedirect("/callback")
            }
            
            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
                val session = SpotifySession(
                    principal?.accessToken ?: "",
                    principal?.refreshToken ?: "",
                    principal?.tokenType ?: "",
                    System.currentTimeMillis() + (principal?.expiresIn ?: 0)
                )
                call.sessions.set(session)
                call.respondRedirect(("/app" +
                        "?access_token=${session.accessToken}" +
                        "?refresh_token=${session.refreshToken}" +
                        "?token_type=${session.tokenType}" +
                        "?expires_at=${session.expiresAt}"))
            }
        }
        
        get("/app") {
            val accessToken = call.request.queryParameters["access_token"]
                ?: call.sessions.get<SpotifySession>()?.accessToken
                ?: return@get call.respondRedirect("/login")
            val refreshToken = call.request.queryParameters["refresh_token"]
                ?: call.sessions.get<SpotifySession>()?.refreshToken
                ?: return@get call.respondRedirect("/login")
            val tokenType = call.request.queryParameters["token_type"]
                ?: call.sessions.get<SpotifySession>()?.tokenType
                ?: return@get call.respondRedirect("/login")
            val expiresIn = call.request.queryParameters["expires_at"]
                ?.toLongOrNull() ?: call.sessions.get<SpotifySession>()?.expiresAt
                ?: return@get call.respondRedirect("/login")
            
            if (System.currentTimeMillis() > expiresIn) {
                return@get call.respond("Token expired")
            }
            
            call.respondText("Hello, ${accessToken}!")
        }
        
        suspend fun refreshCurrentSong(call: ApplicationCall): SongData{
            val accessToken = call.request.queryParameters["access_token"]
                ?: call.sessions.get<SpotifySession>()?.accessToken
            
            // call spotify API to get current song
            val url = "https://api.spotify.com/v1/me/player/currently-playing"
            val client = HttpClient(Apache)
            val response = client.get(url){
                header("Accept", "application/json")
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $accessToken")
            }
            
            val text = response.bodyAsText()
            println("******************************************************************\n"
                            + text +
                            "\n******************************************************************")
            val song = Gson().fromJson(text, SongData::class.java)
            
            call.sessions.set(song)
            return song
        }
        
        // Current playing song name
        get("/api/songName"){
            val result = refreshCurrentSong(call)
            call.respondText(result.item?.album?.name ?: "No song playing")
        }
        // Current playing song artist
        get("/api/songArtist"){
            val result = refreshCurrentSong(call)
            call.respondText(result.item?.artists?.map { it.name }?.joinToString() ?: "No song playing")
        }
        // Current playing song uri
        get("/api/songUri"){
            val result = refreshCurrentSong(call)
            var uri = result.item?.uri
            if (uri != null) {
                uri = "https://open.spotify.com/track/" + uri.replace("spotify:track:", "")
            }
            call.respondText(uri ?: "No song playing")
        }
        
        // all song info
        get("/api/songInfo"){
            val result = refreshCurrentSong(call)
            call.respond(result ?: "No song playing")
        }
        
    }
}

class UserSession(accessToken: String)
