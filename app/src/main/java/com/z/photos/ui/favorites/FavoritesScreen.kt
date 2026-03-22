package com.z.photos.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.z.photos.R
import com.z.photos.business.entities.Photo

private const val GRID_COLUMNS = 2
private const val CARD_PADDING_DP = 8
private const val THUMBNAIL_HEIGHT_DP = 150
private const val ARTIST_TEXT_PADDING_DP = 8
private const val ARTIST_MAX_LINES = 1

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onPhotoClick: (Photo) -> Unit,
) {
    val favorites by viewModel.favorites.collectAsState()

    if (favorites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.empty_favorites),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMNS)
        ) {
            items(favorites) { photo ->
                FavoritePhotoItem(
                    photo = photo,
                    onClick = { onPhotoClick(photo) },
                    onUnfavoriteClick = { viewModel.unfavorite(photo) },
                )
            }
        }
    }
}

@Composable
private fun FavoritePhotoItem(
    photo: Photo,
    onClick: () -> Unit,
    onUnfavoriteClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(CARD_PADDING_DP.dp)
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
                        .height(THUMBNAIL_HEIGHT_DP.dp),
                    contentScale = ContentScale.Crop,
                )
                IconButton(
                    onClick = onUnfavoriteClick,
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
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
