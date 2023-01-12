package yt.ftnl.spotify_tools.spotifyData

import com.google.gson.annotations.SerializedName


data class Disallows (

  @SerializedName("resuming"      ) var resuming     : Boolean? = null,
  @SerializedName("skipping_prev" ) var skippingPrev : Boolean? = null

)