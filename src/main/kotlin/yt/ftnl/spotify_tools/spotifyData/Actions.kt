package yt.ftnl.spotify_tools.spotifyData

import com.google.gson.annotations.SerializedName


data class Actions (

  @SerializedName("disallows" ) var disallows : Disallows? = Disallows()

)