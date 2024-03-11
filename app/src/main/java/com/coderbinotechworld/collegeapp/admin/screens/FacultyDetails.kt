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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.coderbinotechworld.collegeapp.itemview.TeacherItemView
import com.coderbinotechworld.collegeapp.ui.theme.MColor3
import com.coderbinotechworld.collegeapp.utills.Constant
import com.coderbinotechworld.collegeapp.viewmodel.FacultyViewModel
import com.coderbinotechworld.collegeapp.viewmodel.NoticeViewModel
import com.coderbinotechworld.collegeapp.widget.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyDetails(navController: NavController, catName: String) {

    // faculty view model
    val facultyViewModel: FacultyViewModel = viewModel()

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

    val facultyList by facultyViewModel.facultyList.observeAsState(null)

    val isDeletedTeacher by facultyViewModel.isDeletedTeacher.observeAsState(false)

    LaunchedEffect(isDeletedTeacher) {
        if (isDeletedTeacher) {
            showLoader.value = false

            Toast.makeText(context, "Teacher Deleted Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    facultyViewModel.getFaculty(catName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = catName)
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding)
        ) {
            items(facultyList?: emptyList()) {
                TeacherItemView(
                    facultyModel = it,
                    delete = {facultyModel ->
                        showLoader.value = true

                        facultyViewModel.deleteFaculty(facultyModel)
                    }
                )
            }
        }

    }

}