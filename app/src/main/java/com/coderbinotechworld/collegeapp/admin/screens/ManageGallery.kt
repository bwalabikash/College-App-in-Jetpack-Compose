package com.coderbinotechworld.collegeapp.admin.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.coderbinotechworld.collegeapp.R
import com.coderbinotechworld.collegeapp.itemview.FacultyItemView
import com.coderbinotechworld.collegeapp.itemview.GalleryItemView
import com.coderbinotechworld.collegeapp.navigation.Routes
import com.coderbinotechworld.collegeapp.ui.theme.MColor3
import com.coderbinotechworld.collegeapp.utills.Constant
import com.coderbinotechworld.collegeapp.viewmodel.FacultyViewModel
import com.coderbinotechworld.collegeapp.viewmodel.GalleryViewModel
import com.coderbinotechworld.collegeapp.widget.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageGallery(navController: NavController) {

    // gallery view model
    val galleryViewModel: GalleryViewModel = viewModel()

    galleryViewModel.getGallery()

    val isUploaded by galleryViewModel.isPosted.observeAsState(false)
    val isDeleted by galleryViewModel.isDeleted.observeAsState(false)
    val galleryList by galleryViewModel.galleryList.observeAsState(null)

    val option = arrayListOf<String>()

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

    var isExpended by remember {
        mutableStateOf(false)
    }

    var isCategory by remember {
        mutableStateOf(false)
    }

    var isImage by remember {
        mutableStateOf(false)
    }

    var category by remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    LaunchedEffect(isUploaded) {
        if (isUploaded) {
            showLoader.value = false
            Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
            imageUri = null
            isCategory = false
            category = ""
        }
    }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            showLoader.value = false
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
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
        }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            Row(
                modifier = Modifier.padding(8.dp)
            ) {

                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                        .clickable {
                            isCategory = true
                            isImage = false
                        }
                ) {
                    Text(text = "Add Category",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                        .clickable {
                            isCategory = false
                            isImage = true
                        }
                ) {
                    Text(text = "Add Image",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            }

            if (isCategory) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {
                            category = it
                        },
                        placeholder = {
                            Text(text = "Category")
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
                        contentDescription = Constant.GALLERY,
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
                                        "Please select image first",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else if (category == "") {
                                    Toast.makeText(
                                        context,
                                        "Please enter category name!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    showLoader.value = true

                                    galleryViewModel.saveGalleryImage(imageUri!!, category, true)
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(text = "Add Category")
                        }
                        OutlinedButton(
                            onClick = {
                                imageUri = null
                                isCategory = false
                                isImage = false
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

            if (isImage) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopStart)
                        ) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {
                                    category = it
                                },
                                readOnly = true,
                                placeholder = {
                                    Text(text = "Select Gallery Category")
                                },
                                label = {
                                    Text(text = "Category Name")
                                },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended)
                                }
                            )

                            DropdownMenu(
                                expanded = isExpended,
                                onDismissRequest = { isExpended = false }
                            ) {

                                if (galleryList != null && galleryList!!.isNotEmpty()) {
                                    option.clear()
                                    for (data in galleryList!!) {
                                        option.add(data.category!!)
                                    }
                                }

                                option.forEach { selectedOption ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = selectedOption)
                                        },
                                        onClick = {
                                            category = selectedOption
                                            isExpended = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                }

                            }

                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .padding(10.dp)
                                    .clickable {
                                        isExpended = !isExpended
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Image(
                            if (imageUri == null)
                                painterResource(id = R.drawable.image)
                            else
                                rememberAsyncImagePainter(model = imageUri),
                            contentDescription = Constant.GALLERY,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clickable {
                                    launcher.launch("image/*")
                                },
                            contentScale = if (imageUri == null) ContentScale.Fit else ContentScale.FillBounds
                        )

                        Spacer(modifier = Modifier.height(6.dp))


                        Row {
                            Button(
                                onClick = {

                                    if (imageUri == null) {
                                        Toast.makeText(
                                            context,
                                            "Please Choose image from device!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }else if (category == "") {
                                        Toast.makeText(
                                            context,
                                            "Please select category!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        showLoader.value = true

                                        galleryViewModel.saveGalleryImage(imageUri!!, category, false)
                                    }

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
                                    isCategory = false
                                    isImage = false
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
            }


            LazyColumn {

                items(galleryList?: emptyList()) {
                    GalleryItemView(it,
                        delete = {category ->
                            galleryViewModel.deleteGallery(category)
                        },
                        deleteImage = {imageUrl, cat ->
                            showLoader.value = true

                            galleryViewModel.deleteImage(cat, imageUrl)
                        }
                    )
                }
            }
        }
    }

}