package com.example.kotlinprojectapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.kotlinprojectapp.ui.theme.KotlinProjectAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinProjectAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    setContent {
                        Navigation()
                    }
                }
            }
        }
    }
}



@Composable // Routing
fun Navigation(){
    val navControl = rememberNavController()

    NavHost(navController = navControl, startDestination = "login"){
        composable(route = "login"){
            LoginView(navControl)
        }
        composable(route = "feed"){
            ArticleFeedView(navControl)
        }

        composable(route = "article/{articleId}",
            arguments = listOf(
                navArgument("articleId") {
                    type = NavType.IntType
                }
            ))
        {
            ArticleView(navControl, it.arguments?.getInt("articleId"))
        }

        composable(route = "writeArticle"){
            WriteArticle(navControl)
        }
    }
}

@Composable  // Page for logging in
fun LoginView(navControl: NavController)  {

    Scaffold(
        topBar = {
            TopAppBar { Text(text = "Cool articles app", fontSize = 18.sp) }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(34.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Title()
                Email()
                Password()
                Button(
                    onClick = {navControl.navigate("feed")},
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Log in")
                }
            }
        }
    )
}

@Composable // Page for viewing the list of articles
fun ArticleFeedView(navControl: NavController) {
    Scaffold(
        topBar = {
            TopAppBar { Text(text = "Cool articles app", fontSize = 18.sp) }
        },
        content = {
            Column(
            ) {
                Text(text = "Article feed")
                Text(
                    text = "Article 1",
                    modifier = Modifier.clickable { navControl.navigate("article/1") }
                )

            }
        },
        bottomBar =  {
            Button(
                onClick = {navControl.navigate("writeArticle")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(text = "New article")
            }
    }
    )
}

@Composable
fun ArticleView(navControl: NavController, articleId: Int?) {
    Scaffold(
        topBar = {
            TopAppBar { Text(text = "Cool articles app", fontSize = 18.sp) }
        },
        content = {
            Text(
                text = "this is article with id = $articleId"
            )
        }
    )

}

@Composable
fun WriteArticle(navControl: NavController) {
    val titleState =  remember { mutableStateOf(TextFieldValue()) }
    val articleState =  remember { mutableStateOf(TextFieldValue()) }
    Scaffold(
        topBar = {
            TopAppBar { Text(text = "Cool articles app", fontSize = 18.sp) }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextField(
                    value = titleState.value,
                    onValueChange = { titleState.value = it},
                    modifier = Modifier.fillMaxWidth(),
                    label = {Text(text = "Article title")}
                )
                TextField(
                    value = articleState.value,
                    onValueChange = { articleState.value = it},
                    modifier = Modifier.fillMaxWidth(),
                    label = {Text(text = "Write your article here!")}
                )
                Button(
                    onClick = {navControl.navigate("feed")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(text = "Post your article")
                }
            }
        }
    )
}

@Composable
fun Title() {
    Text(
        text = "Log-in Screen",
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Email() {
    val emailState =  remember { mutableStateOf(TextFieldValue()) }
    TextField(
        value = emailState.value,
        onValueChange = { emailState.value = it},
        modifier = Modifier.fillMaxWidth(),
        label = {Text(text = "Email")}
    )
}

@Composable
fun Password() {
    val passwordState =  remember { mutableStateOf(TextFieldValue()) }
    TextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it},
        modifier = Modifier.fillMaxWidth(),
        label = {Text(text = "Password")}
    )
}

//@Composable
//fun SigninButton() {
//    Button(
//        onClick = {navControl.navigate("feed")},
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        Text(text = "Log in")
//    }
//}             It doesn't work as I want it to so it is all commented

