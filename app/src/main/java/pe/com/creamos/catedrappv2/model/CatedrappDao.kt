package pe.com.creamos.catedrappv2.model

import androidx.room.*

@Dao
interface CatedrappDao {

    //User Database
    @Insert
    suspend fun insertUser(vararg user: User): List<Long>

    @Query("SELECT * FROM user limit 1")
    suspend fun getUser(): User

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    //Information Database
    @Insert
    suspend fun insertInformation(vararg infoList: AdditionalInformation): List<Long>

    @Query("SELECT * FROM additional_information")
    suspend fun getInformationList(): List<AdditionalInformation>

    @Query("DELETE FROM additional_information")
    suspend fun deleteAdditionalInformation()

    //Challenge Database
    @Insert
    suspend fun insertChallenge(vararg challengue: Challenge): List<Long>

    @Query("SELECT * FROM challenge")
    suspend fun getChallengeList(): List<Challenge>

    @Query("UPDATE challenge SET progress = progress + 1")
    suspend fun updateChallenge()

    @Query("DELETE FROM challenge")
    suspend fun deleteChallenge()
}