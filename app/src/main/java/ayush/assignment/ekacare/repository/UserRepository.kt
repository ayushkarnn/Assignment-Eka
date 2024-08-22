package ayush.assignment.ekacare.repository

import androidx.lifecycle.LiveData
import ayush.assignment.ekacare.local.User
import ayush.assignment.ekacare.local.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for managing [User] data operations.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Inserts a user into the database on an IO thread.
     */
    suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    /**
     * Retrieves all users from the database.
     */
    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }
}
