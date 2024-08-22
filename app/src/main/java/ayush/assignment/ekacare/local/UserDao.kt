package ayush.assignment.ekacare.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object for the [User] entity.
 */
@Dao
interface UserDao {

    /**
     * Inserts a user into the database.
     */
    @Insert
    suspend fun insertUser(user: User)

    /**
     * Retrieves all users from the database.
     */
    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>
}
