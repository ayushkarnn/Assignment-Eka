package ayush.assignment.ekacare.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database for this application. This database holds the [User] entity and provides
 * access to [UserDao] for data operations.
 */
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Returns an instance of [UserDao] to perform database operations.
     */
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns a singleton instance of [AppDatabase].
         *
         * If the instance is null, it creates a new instance using Room's databaseBuilder
         * and sets it to the INSTANCE variable. It ensures that only one instance of the database
         * is created throughout the application.
         *
         * @param context The application context.
         * @return The singleton instance of [AppDatabase].
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
