package ru.neuron.sportapp.home

import android.view.RoundedCorner
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.neuron.sportapp.R
import ru.neuron.sportapp.data.VideoRecordFileSource
import ru.neuron.sportapp.geofindbutton.GeoFindButton

@Composable
fun SportHome(homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { videoUri ->
        if (videoUri != null) {
            context.contentResolver.openInputStream(videoUri)
                ?.let { homeViewModel.onVideoSelected(context, it) }
        }
    }
    Column {
        SportGeoFindButton()
        Row {
            Button(onClick = {
                pickFileLauncher.launch("video/*")
            }) {
                Text(stringResource(R.string.upload_video))
            }

            Button(onClick = {
                Toast.makeText(
                    context,
                    VideoRecordFileSource.videoRecordsFolder.absoluteFile.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Text(stringResource(R.string.open_folder_button))
            }
        }

    }
}

@Preview
@Composable
fun SportGeoFindButton() {
    Box(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 40.dp)
            .requiredHeight(140.dp)
            .fillMaxWidth()
        ,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.geo_find_button),
            contentScale = ContentScale.Crop,
            contentDescription = null)

        GeoFindButton(
            modifier = Modifier.padding(20.dp),
            label = stringResource(R.string.geo_find_button)
        )
    }

}