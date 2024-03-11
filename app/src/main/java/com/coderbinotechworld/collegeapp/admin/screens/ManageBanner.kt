package com.coderbinotechworld.collegeapp.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.coderbinotechworld.collegeapp.R
import com.coderbinotechworld.collegeapp.itemview.BannerItemView
import com.coderbinotechworld.collegeapp.ui.theme.MColor3
import com.coderbinotechworld.collegeapp.utills.Constant.BANNER
import com.coderbinotechworld.collegeapp.viewmodel.BannerViewModel
import com.coderbinotechworld.collegeapp.widget.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBanner(navController: NavController) {

    // banner view model
    val bannerViewModel: BannerViewModel = viewModel()

    bannerViewModel.getBanner()

    val isUploaded by bannerViewModel.isPosted.observeAsState(false)
    val isDeleted by bannerViewModel.isDeleted.observeAsState(false)
    val bannerList by bannerViewModel.bannerList.observeAsState(null)


    // for loading dialog
    val showLoader = remember {
        mutableStateOf(false)
    }

    if (showLoader.value) {
        LoadingDialog(onDismissRequest = {
            showLoader.value = false
        })
    }


    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    LaunchedEffect(isUploaded) {
        if (isUploaded) {
            showLoader.value = false
            Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
            imageUri = null
        }
    }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            showLoader.value = false
            Toast.makeText(context, "Image Deleted Successfully", Toast.LENGTH_SHORT).show()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Manage Banner")
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MColor3),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.back), contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    launcher.launch("image/*")
                },
                containerColor = MColor3
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = null
                )
            }
        }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            if (imageUri != null) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Image(
                        rememberAsyncImagePainter(model = imageUri),
                        contentDescription = BANNER,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Row {
                        Button(
                            onClick = {
                                showLoader.value = true
                                bannerViewModel.saveImage(imageUri!!)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(text = "Add Image")
                        }
                        OutlinedButton(
                            onClick = {
                                imageUri = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
            LazyColumn {
                items(bannerList?: emptyList()) {
                    BannerItemView(
                        bannerModel = it,
                        delete = {docId ->
                            showLoader.value = true
                            bannerViewModel.deleteBanner(docId)
                        }
                    )
                }
            }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun MangeBannerView() {
    ManageBanner(navController = rememberNavController())
}