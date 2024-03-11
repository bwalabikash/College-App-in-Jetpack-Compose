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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.coderbinotechworld.collegeapp.R
import com.coderbinotechworld.collegeapp.itemview.NoticeItemView
import com.coderbinotechworld.collegeapp.ui.theme.MColor3
import com.coderbinotechworld.collegeapp.utills.Constant
import com.coderbinotechworld.collegeapp.viewmodel.CollegeInfoViewModel
import com.coderbinotechworld.collegeapp.widget.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCollegeInfo(navController: NavController) {

    // College Info view model
    val collegeInfoViewModel: CollegeInfoViewModel = viewModel()

    collegeInfoViewModel.getCollegeInfo()

    val isUploaded by collegeInfoViewModel.isPosted.observeAsState(false)
    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)

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

    var imageUrl by remember {
        mutableStateOf("")
    }

    var name by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var desc by remember {
        mutableStateOf("")
    }
    var websiteLink by remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    LaunchedEffect(isUploaded) {
        if (isUploaded) {
            showLoader.value = false

            Toast.makeText(context, "College Info Updated", Toast.LENGTH_SHORT).show()
            imageUri = null
        }
    }

    LaunchedEffect(collegeInfo) {
        if (collegeInfo != null) {
            imageUrl = collegeInfo!!.imageUrl!!
            name = collegeInfo!!.name!!
            address = collegeInfo!!.address!!
            desc = collegeInfo!!.desc!!
            websiteLink = collegeInfo!!.websiteLink!!
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Manage College Info")
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
        }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            ElevatedCard(
                modifier = Modifier
                    .padding(8.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    placeholder = {
                        Text(text = "College Name...")
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                    },
                    placeholder = {
                        Text(text = "College Address...")
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = {
                        desc = it
                    },
                    placeholder = {
                        Text(text = "College Description...")
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = websiteLink,
                    onValueChange = {
                        websiteLink = it
                    },
                    placeholder = {
                        Text(text = "College Website...")
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )

                Image(
                    if (imageUrl != "") {
                        rememberAsyncImagePainter(model = imageUrl)
                    }else {
                        if (imageUri == null)
                            painterResource(id = R.drawable.image)
                        else
                            rememberAsyncImagePainter(model = imageUri)
                    },
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

                            if (name == "") {
                                Toast.makeText(
                                    context,
                                    "Please enter name!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else if (address == "") {
                                Toast.makeText(
                                    context,
                                    "Please enter address!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else if (desc == "") {
                                Toast.makeText(
                                    context,
                                    "Please enter description!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else if (websiteLink == "") {
                                Toast.makeText(
                                    context,
                                    "Please enter Website LInk!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else if (imageUrl == "") {
                                collegeInfoViewModel.saveImageUrl(imageUrl, name, address, desc, websiteLink)
                            }else if (imageUri == null) {
                                Toast.makeText(
                                    context,
                                    "Please select image!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else {
                                showLoader.value = true

                                collegeInfoViewModel.saveImage(imageUri!!, name, address, desc, websiteLink)
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(text = "Update College Info")
                    }
                }

            }
        }
    }
}