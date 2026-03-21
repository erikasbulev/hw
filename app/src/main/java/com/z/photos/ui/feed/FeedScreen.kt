package com.z.photos.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.z.photos.business.entities.Photo

private const val FEED_GRID_COLUMNS = 2
private const val PREFETCH_THRESHOLD = 3
private const val LOADING_PADDING_DP = 16
private const val PHOTO_CARD_PADDING_DP = 8
private const val PHOTO_THUMBNAIL_HEIGHT_DP = 150
private const val ARTIST_TEXT_PADDING_DP = 8
private const val ARTIST_MAX_LINES = 1

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onPhotoClick: (Photo) -> Unit,
) {
    val photos by viewModel.photos.collectAsState(initial = emptyList())

    LazyVerticalGrid(
        columns = GridCells.Fixed(FEED_GRID_COLUMNS)
    ) {
        itemsIndexed(photos) { index, photo ->
            if (index >= photos.size - PREFETCH_THRESHOLD) {
                LaunchedEffect(Unit) {
                    viewModel.onRequestMoreItems()
                }
            }
            PhotoItem(
                photo = photo,
                onClick = { onPhotoClick(photo) },
                onFavoriteClick = { viewModel.toggleFavorite(photo) },
            )
        }

        item {
            LoadingItem()
        }
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(LOADING_PADDING_DP.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PhotoItem(
    photo: Photo,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(PHOTO_CARD_PADDING_DP.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = photo.photoThumbnailUrl,
                    contentDescription = photo.artist,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PHOTO_THUMBNAIL_HEIGHT_DP.dp),
                    contentScale = ContentScale.Crop,
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    Icon(
                        imageVector = if (photo.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (photo.isFavorite) Color.Red else Color.White,
                    )
                }
            }
            Text(
                text = photo.artist,
                modifier = Modifier.padding(ARTIST_TEXT_PADDING_DP.dp),
                maxLines = ARTIST_MAX_LINES,
            )
        }
    }
}
