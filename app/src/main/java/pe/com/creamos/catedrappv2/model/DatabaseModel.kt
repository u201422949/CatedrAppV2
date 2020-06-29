package pe.com.creamos.catedrappv2.model

import androidx.room.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(tableName = "additional_information")
data class AdditionalInformation(
    @PrimaryKey
    val iid: Int? = 0,

    @ColumnInfo(name = "id_image")
    val idImage: String?,

    val title: String?,

    val description: String?,

    @ColumnInfo(name = "was_read")
    var wasRead: Boolean?,

    @ColumnInfo(name = "update_date")
    var updateDate: LocalDateTime?
) : Serializable

@Entity
data class Question(
    @PrimaryKey
    val qid: Int? = 0,

    @ColumnInfo(name = "id_image")
    val idImage: String?,

    val title: String?,

    val answer: String?,

    @ColumnInfo(name = "answer_key")
    val answerKey: Int?,

    @ColumnInfo(name = "question_was_read")
    var questionWasRead: Boolean?,

    @ColumnInfo(name = "answer_was_read")
    var answerWasRead: Boolean?,

    @ColumnInfo(name = "update_date")
    var updateDate: LocalDateTime?
) : Serializable

@Entity
data class Option(
    @PrimaryKey
    val oid: Int? = 0,

    @ColumnInfo(name = "question_id")
    val questionId: Int,

    val key: String?,

    val value: String?
) : Serializable

@Entity
data class QuestionAndOptions(
    @Embedded
    val question: Question,

    @Relation(
        parentColumn = "qid",
        entityColumn = "question_id"
    )
    val options: List<Option>
)

@Entity
data class User(
    @ColumnInfo(name = "visitor_id")
    var visitorId: Int?,

    val mail: String?,

    val nickname: String?,

    val age: Int?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = 0
}

@Entity
data class Score(
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "score_value")
    var scoreValue: Int?,

    var level: Int?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var sid: Int? = 0
}

@Entity
data class UserAndScore(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "uid",
        entityColumn = "user_id"
    )
    val score: Score
)

@Entity
data class Challenge(
    @PrimaryKey
    var cid: Int? = 0,

    val type: String?,

    val bonus: Int?,

    var completed: Boolean?,

    val title: String?,

    val description: String?,

    val total: Int?,

    var progress: Int?
) : Serializable

data class Rating(
    val userId: Int,

    var ratedValue: Int?,

    var message: String?,

    var ratedDate: LocalDateTime
)

@Entity(tableName = "action_log")
data class ActionLog(
    @ColumnInfo(name = "element_id")
    val elementId: Int?,

    @ColumnInfo(name = "element_type")
    val elementType: String?,

    @ColumnInfo(name = "register_date")
    val registerDate: LocalDateTime?
) {
    @PrimaryKey(autoGenerate = true)
    var aid: Int? = 0
}