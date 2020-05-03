package pe.com.creamos.catedrappv2.model

import com.google.gson.annotations.SerializedName

data class ResponseInformation(
    @SerializedName("error")
    val error: String?,

    @SerializedName("information")
    val additionalInformationList: List<AdditionalInformation>?
) {}

data class ResponseUser(
    @SerializedName("error")
    val error: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("visitorId")
    val visitorId: Int?
) {}