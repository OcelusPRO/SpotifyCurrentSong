package yt.ftnl.spotify_tools.spotifyData

import com.google.gson.annotations.SerializedName


data class SongData (

  @SerializedName("timestamp"              ) var timestamp            : Long?     = null,
  @SerializedName("context"                ) var context              : Context? = Context(),
  @SerializedName("progress_ms"            ) var progressMs           : Long?     = null,
  @SerializedName("item"                   ) var item                 : Item?    = Item(),
  @SerializedName("currently_playing_type" ) var currentlyPlayingType : String?  = null,
  @SerializedName("actions"                ) var actions              : Actions? = Actions(),
  @SerializedName("is_playing"             ) var isPlaying            : Boolean? = null

)