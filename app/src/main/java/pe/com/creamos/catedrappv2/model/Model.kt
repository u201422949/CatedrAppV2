package pe.com.creamos.catedrappv2.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "additional_information")
data class AdditionalInformation(

    var key: String?,

    @ColumnInfo(name = "id_image")
    val idImage: String?,

    val title: String?,

    val description: String?
) {
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
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = 0
}

@Entity
data class Score(
    @ColumnInfo(name = "key_score")
    val keyScore: Int? = 0,

    @ColumnInfo(name = "score_value")
    val scoreValue: String?
) {}

@Entity
data class Question(
    @ColumnInfo(name = "id_question")
    val idQuestion: String?,

    val title: String?,

    val answer: String?,

    val options: List<Option>?
){
    @PrimaryKey(autoGenerate = true)
    var qid: Int? = 0
}

@Entity
data class Option(
    val key: String?,

    val value: String?
){}