package pe.com.creamos.catedrappv2.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pe.com.creamos.catedrappv2.util.Converters

@Database(
    entities = [
        User::class,
        Score::class,
        AdditionalInformation::class,
        Question::class,
        Option::class,
        Challenge::class,
        ActionLog::class], version = 3
)
@TypeConverters(Converters::class)
abstract class CatedrAppDatabase : RoomDatabase() {

    abstract fun catedrappDao(): CatedrAppDao

    companion object {
        @Volatile
        private var instance: CatedrAppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CatedrAppDatabase::class.java,
            "catedrapp_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}