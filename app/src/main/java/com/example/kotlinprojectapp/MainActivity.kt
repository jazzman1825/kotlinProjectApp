package com.example.kotlinprojectapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.kotlinprojectapp.ui.theme.KotlinProjectAppTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val db = Firebase.firestore

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


class ArticleViewModel: ViewModel() {
    var articles = mutableStateOf( listOf<String>())
    var contents = mutableStateOf( listOf<String>())
//    var ids = mutableStateOf( listOf<String>())

    init {
        Firebase.firestore
            .collection("articles")
            .addSnapshotListener{value, error ->
                if(error != null){
                } else if(value != null && !value.isEmpty){
                    val articlesValue = mutableListOf<String>()
                    val contentsValue = mutableListOf<String>()
//                    val idsValue = mutableListOf<String>()

                    for(d in value.documents){
                        articlesValue.add(d.get("title").toString())
                        contentsValue.add(d.get("contents").toString())
//                                        idsValue.add(d.get("id").toString())
                    }
                    articles.value = articlesValue
                    contents.value = contentsValue
//                    ids.value = idsValue
                }
            }
    }

    fun getTitle(id: String?) {
//        val title = Firebase.firestore.collection("articles").whereEqualTo("id", id).get()
        val title: String?
        Firebase.firestore.collection("articles")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
            }

//        return(title)
    } // unfinished, its purpose was to parse the collection in order to retrieve data for articleViewModel...


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
                    type = NavType.StringType
                }
            ))
        {
            ArticleView(navControl, it.arguments?.getString("articleId"))
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
    val msgVM: ArticleViewModel = viewModel()
    Scaffold(
        topBar = {
            TopAppBar { Text(text = "Article feed", fontSize = 18.sp) }
        },
        content = {
            Column(
            ) {
                msgVM.articles.value.forEach {
                    Text(
                        text = it,
                        fontSize = 30.sp,

                        modifier = Modifier.clickable { navControl.navigate("article/${msgVM.contents.value}")
                        //                                                               ^   BUG HERE
                        // It must route to a specific article but it renders all the list
                        }


                    )
                }

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
fun ArticleView(navControl: NavController, articleContents: String?) {
    Scaffold(
        topBar = {
            TopAppBar { Text(text = "Cool articles app", fontSize = 18.sp) }
        },
        content = {
            Text(
                text = "$articleContents"
            )
        }
    )

}

@Composable
fun WriteArticle(navControl: NavController) {
    val titleState =  remember { mutableStateOf("") }
    val articleState =  remember { mutableStateOf("") }
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
                    onClick = {
                        postArticle(titleState.value, articleState.value)
                        navControl.navigate("feed")
                              },
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

fun postArticle(title: String, contents: String) {

    val article = hashMapOf(
        "title" to title  ,
        "contents" to contents
    )

    db.collection("articles")
        .add(article)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
}