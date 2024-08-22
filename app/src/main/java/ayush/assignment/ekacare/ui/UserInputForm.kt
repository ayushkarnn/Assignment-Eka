package ayush.assignment.ekacare.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.assignment.ekacare.viewmodel.UserViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserInputForm(userViewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isAgeVisible by remember { mutableStateOf(false) }

    val calendar = remember { Calendar.getInstance() }
    val datePickerState = rememberSheetState()

    val focusRequester = remember { FocusRequester() }
    val addressfocusRequester = remember { FocusRequester() }

    val context = LocalContext.current

    fun onDateSelected(localDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        dob = localDate.format(formatter)

        val today = LocalDate.now()
        val period = Period.between(localDate, today)
        age = period.years.toString()

        isAgeVisible = true
        addressfocusRequester.requestFocus()
    }

    CalendarDialog(
        state = datePickerState,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date { localDate ->
            onDateSelected(localDate)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        UserInputCard(
            modifier = Modifier.fillMaxWidth(),
            content = {
                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Name",
                    imeAction = ImeAction.Next,
                    focusRequester = focusRequester
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isAgeVisible) {
                    CustomTextField(
                        value = age,
                        onValueChange = {},
                        label = "Age",
                        readOnly = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        value = dob,
                        onValueChange = { dob = it },
                        label = "DOB",
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { datePickerState.show() }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Pick Date")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = "Address",
                    imeAction = ImeAction.Done,
                    focusRequester = addressfocusRequester

                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && age.isNotBlank() && dob.isNotBlank() && address.isNotBlank()) {
                    userViewModel.insertUser(name, age.toIntOrNull() ?: 0, calendar.toString(), address)
                    name = ""
                    age = ""
                    dob = ""
                    address = ""
                    isAgeVisible = false
                    focusRequester.requestFocus()
                    Toast.makeText(context, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = name.isNotBlank() && age.isNotBlank() && dob.isNotBlank() && address.isNotBlank(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save")
        }
    }
}


@Composable
fun UserInputCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    focusRequester: FocusRequester? = null,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        modifier = modifier
            .background(Color.White)
            .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier)
    )
}
