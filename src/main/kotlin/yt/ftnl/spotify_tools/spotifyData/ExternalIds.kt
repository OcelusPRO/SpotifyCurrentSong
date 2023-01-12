package yt.ftnl.spotify_tools.spotifyData

import com.google.gson.annotations.SerializedName


data class ExternalIds (

  @SerializedName("isrc" ) var isrc : String? = null

)