package yt.ftnl.spotify_tools.spotifyData

import com.google.gson.annotations.SerializedName


data class Item (

  @SerializedName("album"         ) var album        : Album?             = Album(),
  @SerializedName("artists"       ) var artists      : ArrayList<Artists> = arrayListOf(),
  @SerializedName("disc_number"   ) var discNumber   : Int?               = null,
  @SerializedName("duration_ms"   ) var durationMs   : Long?                = null,
  @SerializedName("explicit"      ) var explicit     : Boolean?           = null,
  @SerializedName("external_ids"  ) var externalIds  : ExternalIds?       = ExternalIds(),
  @SerializedName("external_urls" ) var externalUrls : ExternalUrls?      = ExternalUrls(),
  @SerializedName("href"          ) var href         : String?            = null,
  @SerializedName("id"            ) var id           : String?            = null,
  @SerializedName("is_local"      ) var isLocal      : Boolean?           = null,
  @SerializedName("is_playable"   ) var isPlayable   : Boolean?           = null,
  @SerializedName("linked_from"   ) var linkedFrom   : LinkedFrom?        = LinkedFrom(),
  @SerializedName("name"          ) var name         : String?            = null,
  @SerializedName("popularity"    ) var popularity   : Int?               = null,
  @SerializedName("preview_url"   ) var previewUrl   : String?            = null,
  @SerializedName("track_number"  ) var trackNumber  : Long?                = null,
  @SerializedName("type"          ) var type         : String?            = null,
  @SerializedName("uri"           ) var uri          : String?            = null

)