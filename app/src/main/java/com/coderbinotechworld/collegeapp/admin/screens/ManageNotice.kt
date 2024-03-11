package com.coderbinotechworld.collegeapp.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.coderbinotechworld.collegeapp.itemview.NoticeItemView
import com.coderbinotechworld.collegeapp.ui.theme.MColor3
import com.coderbinotechworld.collegeapp.utills.Constant
import com.coderbinotechworld.collegeapp.viewmodel.NoticeViewModel
import com.coderbinotechworld.collegeapp.widget.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageNotice(navController: NavController) {
    // notice view model
    val noticeViewModel: NoticeViewModel = viewModel()

    noticeViewModel.getNotice()

    val isUploaded by noticeViewModel.isPosted.observeAsState(false)
    val isDeleted by noticeViewModel.isDeleted.observeAsState(false)
    val noticeList by noticeViewModel.noticeList.observeAsState(null)

    val context = LocalContext.current

    // for loading dialog
    val showLoader = remember {
        mutableStateOf(false)
    }

    if (showLoader.value) {
        LoadingDialog(onDismissRequest = {
            showLoader.value = false
        })
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var isNotice by remember {
        mutableStateOf(false)
    }

    var title by remember {
        mutableStateOf("")
    }
    var link by remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    LaunchedEffect(isUploaded) {
        if (isUploaded) {
            showLoader.value = false

            Toast.makeText(context, "Notice Uploaded Successfully", Toast.LENGTH_SHORT).show()
            imageUri = null
            isNotice = false
        }
    }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            showLoader.value = false

            Toast.makeText(context, "Notice Deleted Successfully", Toast.LENGTH_SHORT).show()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Manage Notice")
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
                    isNotice = true
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
            if (isNotice) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp)
                ) {

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        placeholder = {
                            Text(text = "Notice Title.")
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = link,
                        onValueChange = {
                            link = it
                        },
                        placeholder = {
                            Text(text = "Notice Link.")
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )

                    Image(
                        if (imageUri == null)
                            painterResource(id = R.drawable.image)
                        else
                            rememberAsyncImagePainter(model = imageUri),
                        contentDescription = Constant.NOTICE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentScale = if (imageUri == null) ContentScale.Fit else ContentScale.FillBounds
                    )

                    Row {
                        Button(
                            onClick = {

                                if (imageUri == null) {
                                    Toast.makeText(
                                        context,
                                        "Please select image!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else if (title == "") {
                                    Toast.makeText(
                                        context,
                                        "Please enter title!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    showLoader.value = true

                                    noticeViewModel.saveNotice(imageUri!!, title, link)
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(text = "Add Notice")
                        }
                        OutlinedButton(
                            onClick = {
                                imageUri = null
                                isNotice = false
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
                items(noticeList?: emptyList()) {
                    NoticeItemView(
                        noticeModel = it,
                        delete = {docId ->
                            showLoader.value = true
                            noticeViewModel.deleteNotice(docId)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun NoticeView() {
    ManageNotice(navController = rememberNavController())
}