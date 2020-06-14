package pe.com.creamos.catedrappv2.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "additional_information")
data class AdditionalInformation(

    var key: String?,

    @ColumnInfo(name = "id_image")
    val idImage: String?,

    val title: String?,

    val description: String?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var iid: Int? = 0
}

@Entity
data class User(
    @ColumnInfo(name = "visitor_id")
    var visitorId: Int?,

    val mail: String?,

    val nickname: String?,

    val age: Int?,

    @Embedded
    val score: Score?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = 0
}

@Entity
data class Score(
    @ColumnInfo(name = "key_score")
    val keyScore: Int? = 0,

    @ColumnInfo(name = "score_value")
    val scoreValue: String?,

    @ColumnInfo(name = "level")
    val level: Int?
) : Serializable {}

@Entity
data class Question(
    @ColumnInfo(name = "id_question")
    val idQuestion: String?,

    @ColumnInfo(name = "id_image")
    val idImage: String?,

    val title: String?,

    val answer: String?,

    val answerKey: Int?,

    val options: List<Option>?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var qid: Int? = 0
}

@Entity
data class Option(
    val key: String?,

    val value: String?
) : Serializable {}

@Entity
data class Challenge(
    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "total")
    val total: Int?,

    @ColumnInfo(name = "progress")
    val progress: Int?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var cid: Int? = 0
}