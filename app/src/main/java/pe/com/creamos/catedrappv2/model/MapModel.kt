package pe.com.creamos.catedrappv2.model

import java.util.*

fun mapAdditionalInformation(responseInformationList: List<ResponseInformation>): List<AdditionalInformation> {
    val informationList: MutableList<AdditionalInformation> = ArrayList()

    for (info in responseInformationList) {
        informationList.add(
            AdditionalInformation(
                info.iid,
                info.idImage,
                info.title,
                info.description,
                false,
                null
            )
        )
    }

    return informationList
}

fun mapQuestion(responseQuestionList: List<ResponseQuestion>): List<Question> {
    val questionList: MutableList<Question> = ArrayList()

    for (question in responseQuestionList) {
        questionList.add(
            Question(
                question.qid,
                question.idImage,
                question.title,
                question.answer,
                question.answerKey,
                questionWasRead = false,
                answerWasRead = false,
                updateDate = null
            )
        )
    }

    return questionList
}

fun mapOption(responseQuestionList: List<ResponseQuestion>): List<Option> {
    val optionList: MutableList<Option> = ArrayList()

    for (question in responseQuestionList) {
        for (option in question.optionList!!) {
            question.qid?.let {
                optionList.add(Option(option.oid, it, option.key, option.value))
            }
        }
    }

    return optionList
}

fun mapChallenge(responseChallengeList: List<ResponseChallenge>): List<Challenge> {
    val challengeList: MutableList<Challenge> = ArrayList()

    for (challenge in responseChallengeList) {
        challengeList.add(
            Challenge(
                challenge.cid,
                challenge.type,
                challenge.bonus,
                false,
                challenge.title,
                challenge.description,
                challenge.total,
                challenge.progress
            )
        )
    }

    return challengeList
}