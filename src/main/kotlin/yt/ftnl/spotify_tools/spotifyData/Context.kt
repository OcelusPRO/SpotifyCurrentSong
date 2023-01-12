package yt.ftnl.spotify_tools.spotifyData

import com.google.gson.annotations.SerializedName


data class Context (

  @SerializedName("external_urls" ) var externalUrls : ExternalUrls? = ExternalUrls(),
  @SerializedName("href"          ) var href         : String?       = null,
  @SerializedName("type"          ) var type         : String?       = null,
  @SerializedName("uri"           ) var uri          : String?       = null

)