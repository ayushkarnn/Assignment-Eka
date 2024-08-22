package ayush.assignment.ekacare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.assignment.ekacare.local.User
import ayush.assignment.ekacare.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing [User] data.
 */
class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    // LiveData for observing users
    val users: LiveData<List<User>> = userRepository.getAllUsers()

    // LiveData for user added status
    private val _userAdded = MutableLiveData<Boolean>()
    val userAdded: LiveData<Boolean> get() = _userAdded

    /**
     * Inserts a new user and updates the status.
     */
    fun insertUser(name: String, age: Int, dob: String, address: String) {
        viewModelScope.launch {
            val user = User(name = name, age = age, dob = dob, address = address)
            userRepository.insertUser(user)
            _userAdded.postValue(true)
        }
    }
}
