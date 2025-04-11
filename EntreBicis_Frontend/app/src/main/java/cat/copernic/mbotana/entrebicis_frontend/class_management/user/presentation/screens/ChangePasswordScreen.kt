package cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import cat.copernic.mbotana.entrebicis_frontend.class_management.user.presentation.viewmodels.ChangePasswordViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.ToastMessage
import kotlinx.coroutines.launch


@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val email by viewModel.email.collectAsState()
    val tokenCode by viewModel.tokenCode.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val repNewPassword by viewModel.repNewPassword.collectAsState()

    val currentFormStep by viewModel.currentFormStep.collectAsState()

    val sendEmailSuccess by viewModel.sendEmailSuccess.collectAsState()
    val tokenCodeSuccess by viewModel.tokenCodeSuccess.collectAsState()
    val changePasswordSuccess by viewModel.changePasswordSuccess.collectAsState()

    var isButtonEnabled by remember { mutableStateOf(true) }

    fun change() {
        isButtonEnabled = !isButtonEnabled
    }

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    val emptyEmailError by viewModel.emptyEmailError.collectAsState()
    val emptyTokenCodeError by viewModel.emptyTokenCodeError.collectAsState()
    val emptyNewPasswordError by viewModel.emptyNewPasswordError.collectAsState()
    val emptyRepNewPasswordError by viewModel.emptyRepNewPasswordError.collectAsState()

    val emailNotFoundError by viewModel.emailNotFoundError.collectAsState()
    val tokenCodeNotFoundError by viewModel.tokenCodeNotFoundError.collectAsState()

    val emailError by viewModel.emailError.collectAsState()
    val tokenCodeError by viewModel.tokenCodeError.collectAsState()
    val newPasswordError by viewModel.newPasswordError.collectAsState()
    val repNewPasswordError by viewModel.repNewPasswordError.collectAsState()

    val passwordNotMatchError by viewModel.passwordNotMatchError.collectAsState()

    val isPasswordChanged by viewModel.isPasswordChanged.collectAsState()

    LaunchedEffect(isPasswordChanged) {
        if (isPasswordChanged) {
            viewModel.resetPasswordChanged()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(sendEmailSuccess) {
        if (sendEmailSuccess) {
            viewModel.nextFormStep()
            isButtonEnabled = true
        } else {
            isButtonEnabled = true
        }
    }

    LaunchedEffect(tokenCodeSuccess) {
        if (tokenCodeSuccess) {
            viewModel.nextFormStep()
            isButtonEnabled = true
        } else {
            isButtonEnabled = true
        }
    }

    LaunchedEffect(changePasswordSuccess) {
        if (changePasswordSuccess) {
            viewModel.nextFormStep()
            isButtonEnabled = true
        } else {
            isButtonEnabled = true
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
                Text("Canviar Contrasenya", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                        .padding(16.dp)
                ) {

                    ContinuousStepProgressBar(
                        currentStep = currentFormStep,
                        navController
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedContent(
                        targetState = currentFormStep,
                        transitionSpec = {
                            fadeIn()
                                .togetherWith(fadeOut())
                        },
                        label = "StepAnimation"
                    ) { step ->
                        when (step) {
                            1 -> {
                                ShowEmailForm(
                                    email,
                                    viewModel,
                                    emptyEmailError,
                                    emailNotFoundError,
                                    emailError,
                                    isButtonEnabled,
                                    buttonOnClick = { change() }
                                )
                            }

                            2 -> {
                                ShowTokenForm(
                                    tokenCode,
                                    viewModel,
                                    emptyTokenCodeError,
                                    tokenCodeNotFoundError,
                                    tokenCodeError,
                                    isButtonEnabled,
                                    buttonOnClick = { change() }
                                )
                            }

                            3 -> {
                                ShowPasswordForm(
                                    newPassword,
                                    repNewPassword,
                                    viewModel,
                                    emptyNewPasswordError,
                                    emptyRepNewPasswordError,
                                    passwordNotMatchError,
                                    newPasswordError,
                                    repNewPasswordError,
                                    isButtonEnabled,
                                    buttonOnClick = { change() }
                                )
                            }

                            4 -> {
                                FinishFormScreen(
                                    viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowEmailForm(
    email: String,
    viewModel: ChangePasswordViewModel,
    emptyEmailError: String?,
    emailNotFoundError: String?,
    emailError: String?,
    isButtonEnabled: Boolean,
    buttonOnClick: () -> Unit
) {

    Column {
        Text(
            text = "Correu:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            isError = emptyEmailError != null || emailNotFoundError != null || emailError != null,
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
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        emailNotFoundError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        emailError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            enabled = isButtonEnabled,
            onClick = {
                buttonOnClick()
                viewModel.viewModelScope.launch {
                    viewModel.sendEmail()
                }
            }) {
            Text("Seguent")
        }
    }
}


@Composable
fun ShowTokenForm(
    tokenCode: String,
    viewModel: ChangePasswordViewModel,
    emptyTokenCodeError: String?,
    tokenCodeNotFoundError: String?,
    tokenCodeError: String?,
    isButtonEnabled: Boolean,
    buttonOnClick: () -> Unit
) {

    Column {
        Text(
            text = "Codi:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = tokenCode,
            onValueChange = { viewModel.updateTokenCode(it) },
            isError = emptyTokenCodeError != null || tokenCodeNotFoundError != null || tokenCodeError != null,
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
        emptyTokenCodeError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        tokenCodeNotFoundError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        tokenCodeError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            enabled = isButtonEnabled,
            onClick = {
                buttonOnClick()
                viewModel.viewModelScope.launch {
                    viewModel.validateTokenCode()
                }
            }) {
            Text("Seguent")
        }
    }
}

@Composable
fun ShowPasswordForm(
    newPassword: String,
    repNewPassword: String,
    viewModel: ChangePasswordViewModel,
    emptyNewPasswordError: String?,
    emptyRepNewPasswordError: String?,
    passwordNotMatchError: String?,
    newPasswordError: String?,
    repNewPasswordError: String?,
    isButtonEnabled: Boolean,
    buttonOnClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(
            text = "Nova contrasenya:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = newPassword,
            onValueChange = { viewModel.updateNewPassword(it) },
            isError = newPasswordError != null || emptyNewPasswordError != null || passwordNotMatchError != null,
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
        emptyNewPasswordError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        newPasswordError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        passwordNotMatchError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Repeteix contrasenya:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = repNewPassword,
            onValueChange = { viewModel.updateRepNewPassword(it) },
            isError = repNewPasswordError != null || emptyRepNewPasswordError != null || passwordNotMatchError != null,
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
        emptyRepNewPasswordError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        repNewPasswordError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        passwordNotMatchError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
            if (!isButtonEnabled) {
                buttonOnClick()
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            enabled = isButtonEnabled,
            onClick = {
                buttonOnClick()
                viewModel.viewModelScope.launch {
                    viewModel.updatePasswordUser()
                }
            }) {
            Text("Canviar")
        }
    }
}

@Composable
fun FinishFormScreen(
    viewModel: ChangePasswordViewModel
) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Text(
            text = "Contrasenya modificada amb èxit!",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.finishStep()
                }
            }) {
            Text("Sortir")
        }
    }
}

@Composable
fun ContinuousStepProgressBar(
    currentStep: Int,
    navController: NavController
) {
    val progress = when (currentStep) {
        1 -> 0.1f
        2 -> 0.5f
        3 -> 0.9f
        4 -> 1.0f
        else -> 0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "ProgressBarAnimation"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navController.navigate("splash") {
                        popUpTo(0) { inclusive = true }
                    }
                }
        )

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = animatedProgress)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF2196F3))
            )
        }
    }
}


