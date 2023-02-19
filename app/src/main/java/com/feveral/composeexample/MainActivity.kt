package com.feveral.composeexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.feveral.composeexample.models.Meme
import com.feveral.composeexample.services.MemeService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val memeViewModel = TrendingMemeViewModel()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row {
                                    Text("Memery Trending")
                                }
                            })
                    },
                    content = {
                        MemeListView(memeViewModel)
                    }
                )
            }
        }
    }

    @Composable
    fun MemeListView(vm: TrendingMemeViewModel) {

        LaunchedEffect(Unit, block = {
            vm.getMemes()
        })

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(vm.memeList) { meme ->
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(16.dp)
                ) {
                    Row {
                        AsyncImage(
                            model = meme.user.avatarUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .padding(5.dp)
                        )
                        Text(
                            meme.user.name,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        meme.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = meme.image.url,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

class TrendingMemeViewModel: ViewModel() {

    private val _memeList = mutableStateListOf<Meme>()
    val memeList: List<Meme>
        get() = _memeList

    fun getMemes() {
        viewModelScope.launch {
            val memeService = MemeService.getInstance()
            try {
                _memeList.clear()
                _memeList.addAll(memeService.getTrendingMemes())
            } catch (e: Exception) {
                Log.d("MemeViewModel", "fetch trending meme failed")
                Log.e("MemeViewModel", e.toString())
            }
        }
    }
}