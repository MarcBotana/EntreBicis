package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewModels.LoginViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.ToastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.session.model.SessionUser
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val isUserLogged by viewModel.isUserLogged.collectAsState()

    val user by viewModel.user.collectAsState()

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    val emailError by viewModel.emailError.collectAsState()
    val emptyEmailError by viewModel.emptyEmailError.collectAsState()
    val emailNotFoundError by viewModel.emailNotFoundError.collectAsState()

    val passwordError by viewModel.passwordError.collectAsState()
    val emptyPasswordError by viewModel.emptyPasswordError.collectAsState()

    val unauthorizedError by viewModel.unauthorizedError.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()


    LaunchedEffect(isUserLogged) {
        if (isUserLogged && user != null) {
            sessionViewModel.updateSession(SessionUser(user!!.email, user!!.image, user!!.role, user!!.totalPoints, true))
            viewModel.resetUserLogged()
            val bottomNavIndex = "Map"
            navController.navigate("main/$bottomNavIndex") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(backendException) {
        backendException?.let { ToastMessage(context, it) }
    }

    LaunchedEffect(frontendException) {
        frontendException?.let { ToastMessage(context, it) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.entrebicis_logo),
                contentDescription = "EntreBicis_Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 12f)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Iniciar Sessió", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Correu",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { viewModel.updateEmail(it) },
                            isError = emptyEmailError != null || emailNotFoundError != null || unauthorizedError != null || emailError != null,
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp)
                                .height(46.dp)
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
                        emptyEmailError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        emailNotFoundError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        unauthorizedError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        emailError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Contrasenya",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.updatePassword(it) },
                            isError = emptyPasswordError != null || unauthorizedError != null || passwordError != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White),
                            shape = RoundedCornerShape(50.dp),
                            visualTransformation = PasswordVisualTransformation(),
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
                        emptyPasswordError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        unauthorizedError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        passwordError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            viewModel.viewModelScope.launch {
                                viewModel.loginUser()
                            }
                        }) {
                        Text("Entrar")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Canviar contrasenya",
                    modifier = Modifier.clickable {
                        navController.navigate("changePassword") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}


























