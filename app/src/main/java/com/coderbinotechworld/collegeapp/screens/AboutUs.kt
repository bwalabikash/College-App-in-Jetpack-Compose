package com.coderbinotechworld.collegeapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.coderbinotechworld.collegeapp.viewmodel.CollegeInfoViewModel

@Composable
fun AboutUs() {

    // College Info view model
    val collegeInfoViewModel: CollegeInfoViewModel = viewModel()
    collegeInfoViewModel.getCollegeInfo()
    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        if (collegeInfo != null) {

            Image(
                painter = rememberAsyncImagePainter(model = collegeInfo!!.imageUrl),
                contentDescription = "college image",
                modifier = Modifier
                    .height(210.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = collegeInfo!!.name!!,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = collegeInfo!!.desc!!,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = collegeInfo!!.address!!,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = collegeInfo!!.websiteLink!!,
                fontWeight = FontWeight.Normal,
                color = Color.Blue,
                fontSize = 16.sp
            )

        }
    }

}