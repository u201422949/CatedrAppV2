package pe.com.creamos.catedrappv2.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, AdditionalInformation::class], version = 2)
abstract class CatedrappDatabase : RoomDatabase() {

    abstract fun catedrappDao(): CatedrappDao

    companion object {
        @Volatile
        private var instance: CatedrappDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CatedrappDatabase::class.java,
            "catedrapp_database"
        ).build()
    }
}