package com.z.photos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.z.photos.business.entities.Photo
import com.z.photos.ui.feed.FeedViewModel
import com.z.photos.ui.theme.HomeworkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeworkTheme {
                Greeting(
                    viewModel,
                )
            }
        }
    }
}

@Composable
fun Greeting(viewModel: FeedViewModel) {
    val photos = viewModel.pager.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(220.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(photos.itemCount) { index ->
            photos[index]?.let {
                PhotoItem(photo = it)
            }
        }

        photos.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }

                loadState.refresh is LoadState.Loading -> {
                    item { LoadingItem() }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PhotoItem(photo: Photo) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                model = photo.photoThumbnailUrl,
                contentDescription = photo.artist,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.None
            )
            Text(
                text = photo.artist,
                modifier = Modifier.padding(8.dp),
                maxLines = 1
            )
        }
    }
}
