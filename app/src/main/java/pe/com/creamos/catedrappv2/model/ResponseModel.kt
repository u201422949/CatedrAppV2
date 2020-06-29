package pe.com.creamos.catedrappv2.model

data class ResponseUser(
    val error: String?,

    val message: String?,

    val visitorId: Int?
) {}

data class ResponseDataStructure(
    val error: String?,

    val dataVersion: String?,

    val additionalInformationList: List<ResponseInformation>?,

    val questionList: List<ResponseQuestion>?,

    val challengeList: List<ResponseChallenge>?
) {}

data class ResponseInformation(
    val iid: Int? = 0,

    val idImage: String?,

    val title: String?,

    val description: String?
) {}

data class ResponseQuestion(
    val qid: Int? = 0,

    val idImage: String?,

    val title: String?,

    val answer: String?,

    val answerKey: Int?,

    var optionList: List<ResponseOption>?
) {}

data class ResponseOption(
    val oid: Int? = 0,

    val key: String?,

    val value: String?
) {}

data class ResponseChallenge(
    var cid: Int? = 0,

    val type: String?,

    val bonus: Int?,

    val title: String?,

    val description: String?,

    val total: Int?,

    val progress: Int?
) {}