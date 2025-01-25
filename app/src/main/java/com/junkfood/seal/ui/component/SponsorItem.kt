package com.junkfood.seal.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.AsyncImageImpl

fun gitHubAvatar(userLogin: String): String = "https://github.com/${userLogin}.png"

fun gitHubProfile(userLogin: String): String = "https://github.com/${userLogin}"

@Composable
fun SponsorItem(
    modifier: Modifier = Modifier,
    userName: String?,
    userLogin: String,
    avatarUrl: Any = gitHubAvatar(userLogin),
    profileUrl: String = gitHubProfile(userLogin),
    contentPadding: PaddingValues = PaddingValues(horizontal = 0.dp, vertical = 12.dp),
    onClick: () -> Unit = {},
) {
    Column() {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .clickable(onClick = onClick)
                    .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImageImpl(
                modifier =
                    Modifier.widthIn(min = 48.dp, max = 108.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f, true)
                        .clip(CircleShape),
                model = avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                userName?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = "@$userLogin",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
@Preview
fun SponsorItemPreview() {
    SponsorItem(
        userName = "junkfood",
        userLogin = "JunkFood02",
        avatarUrl = R.drawable.sample1,
        profileUrl = "",
    ) {}
}
