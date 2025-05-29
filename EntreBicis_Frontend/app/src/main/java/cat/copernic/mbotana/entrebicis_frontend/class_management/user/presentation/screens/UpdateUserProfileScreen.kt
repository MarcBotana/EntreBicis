package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewModels.UserProfileViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.common.ImageUtils
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import kotlinx.coroutines.launch

@Composable
fun UpdateUserProfileScreen(
    viewModel: UserProfileViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    email: String
) {
    val context = LocalContext.current

    val userData by sessionViewModel.userData.collectAsState()
    val userDetail by viewModel.userDetail.collectAsState()

    var showConfirmProfileUpdateDialog by remember { mutableStateOf(false) }

    val name by viewModel.name.collectAsState()
    val surname by viewModel.surname.collectAsState()
    val observation by viewModel.observation.collectAsState()
    val town by viewModel.town.collectAsState()
    val mobile by viewModel.mobile.collectAsState()

    val emptyNameError by viewModel.emptyNameError.collectAsState()
    val emptySurnameError by viewModel.emptySurnameError.collectAsState()
    val emptyObservationError by viewModel.emptyObservationError.collectAsState()
    val emptyTownError by viewModel.emptyTownError.collectAsState()
    val emptyMobileError by viewModel.emptyMobileError.collectAsState()

    val updateUserSuccess by viewModel.updateUserSuccess.collectAsState()


    val userNotFoundError by viewModel.userNotFoundError.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(email) {
        viewModel.loadUserProfile(email)
    }

    LaunchedEffect(updateUserSuccess) {
        if (updateUserSuccess) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(backendException) {
        backendException?.let { toastMessage(context, it) }
    }

    LaunchedEffect(frontendException) {
        frontendException?.let { toastMessage(context, it) }
    }

    Scaffold(
        topBar =
        { userData?.let { CustomTopBar("Modificar Perfil", it, false) } },
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            userNotFoundError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEFEFEF))
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nom",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.updateName(it) },
                        isError = emptyNameError != null,
                        shape = RoundedCornerShape(50.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = Color.Gray
                        )
                    )
                    emptyNameError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cognom",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { viewModel.updateSurname(it) },
                        isError = emptySurnameError != null,
                        shape = RoundedCornerShape(50.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = Color.Gray
                        )
                    )
                    emptySurnameError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Observacions",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = observation,
                        onValueChange = { viewModel.updateObservation(it) },
                        isError = emptyObservationError != null,
                        shape = RoundedCornerShape(50.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = Color.Gray
                        )
                    )
                    emptyObservationError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Localització",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = town,
                        onValueChange = { viewModel.updateTown(it) },
                        isError = emptyTownError != null,
                        shape = RoundedCornerShape(50.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = Color.Gray
                        )
                    )
                    emptyTownError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Mòbil",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = mobile.toString(),
                        onValueChange = {
                            val filtered = it.filter { char -> char.isDigit() }
                            if (filtered.isNotEmpty()) {
                                viewModel.updateMobile(filtered.toInt())
                            } else {
                                viewModel.updateMobile(0)
                            }
                        },
                        isError = emptyMobileError != null,
                        shape = RoundedCornerShape(50.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = Color.Gray
                        )
                    )
                    emptyMobileError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(60.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        showConfirmProfileUpdateDialog = true
                    }
                ) {
                    Text(
                        text = "Guardar",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    if (showConfirmProfileUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmProfileUpdateDialog = false },
            text = {
                Text("Vols guardar els canvis?")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.updateUser()
                    }
                    showConfirmProfileUpdateDialog = false
                }) {
                    Text("Sí, guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    navController.popBackStack()
                    showConfirmProfileUpdateDialog = false
                }) {
                    Text("No")
                }
            }
        )
    }
}