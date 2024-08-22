package ayush.assignment.ekacare

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import ayush.assignment.ekacare.local.AppDatabase
import ayush.assignment.ekacare.repository.UserRepository
import ayush.assignment.ekacare.ui.UserInputForm
import ayush.assignment.ekacare.viewmodel.UserViewModel
import ayush.assignment.ekacare.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the Room database
        val database = AppDatabase.getDatabase(this)

        // Create UserRepository with UserDao
        val userRepository = UserRepository(database.userDao())

        // Create UserViewModel with UserRepository
        val userViewModel = ViewModelProvider(
            this, UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]

        // Set the content to UserInputForm composable
        setContent {
            UserInputForm(userViewModel = userViewModel)
        }
    }
}
