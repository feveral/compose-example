package com.feveral.composeexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                Column {
                    Row {
                        AsyncImage(
                            model = meme.user.avatarUrl,
                            contentDescription = null
                        )
                        Text(
                            meme.user.name,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    AsyncImage(
                        model = meme.image.url,
                        contentDescription = null
                    )
                    Text(
                        meme.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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