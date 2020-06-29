package pe.com.creamos.catedrappv2.model

import androidx.room.*

@Dao
interface CatedrAppDao {

    //User Table
    @Insert
    suspend fun insertUser(vararg user: User): List<Long>

    @Transaction
    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): UserAndScore

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    //Score Table
    @Insert
    suspend fun insertScore(vararg score: Score): List<Long>

    @Update
    suspend fun updateScore(vararg score: Score)

    @Query("DELETE FROM score")
    suspend fun deleteScore()

    //Information Table
    @Insert
    suspend fun insertInformation(vararg infoList: AdditionalInformation): List<Long>

    @Update
    suspend fun updateInformation(vararg information: AdditionalInformation)

    @Query("SELECT * FROM additional_information")
    suspend fun getInformationList(): List<AdditionalInformation>

    @Query("DELETE FROM additional_information")
    suspend fun deleteAdditionalInformation()

    //Question Table
    @Insert
    suspend fun insertQuestion(vararg questionList: Question): List<Long>

    @Update
    suspend fun updateQuestion(vararg question: Question)

    @Transaction
    @Query("SELECT * FROM question")
    suspend fun getQuestionList(): List<QuestionAndOptions>

    @Query("DELETE FROM question")
    suspend fun deleteQuestion()

    //Option Table
    @Insert
    suspend fun insertOption(vararg optionList: Option): List<Long>

    @Query("DELETE FROM option")
    suspend fun deleteOption()

    //Challenge Table
    @Insert
    suspend fun insertChallenge(vararg challenge: Challenge): List<Long>

    @Query("SELECT * FROM challenge")
    suspend fun getChallengeList(): List<Challenge>

    @Query("SELECT * FROM challenge WHERE type LIKE :type LIMIT 1")
    suspend fun getChallengeList(type: String): Challenge

    @Update
    suspend fun updateChallenge(vararg challenge: Challenge)

    @Query("DELETE FROM challenge")
    suspend fun deleteChallenge()

    //ActionLog Table
    @Insert
    suspend fun insertLog(vararg actionLog: ActionLog): List<Long>

    @Query("DELETE FROM action_log")
    suspend fun deleteLog()
}