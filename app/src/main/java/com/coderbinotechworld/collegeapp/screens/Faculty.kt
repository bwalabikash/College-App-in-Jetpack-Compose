package com.coderbinotechworld.collegeapp.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coderbinotechworld.collegeapp.itemview.FacultyItemView
import com.coderbinotechworld.collegeapp.navigation.Routes
import com.coderbinotechworld.collegeapp.viewmodel.FacultyViewModel

@Composable
fun Faculty(navController: NavController) {

    // faculty view model
    val facultyViewModel: FacultyViewModel = viewModel()
    facultyViewModel.getFacultyCategory()
    val categoryList by facultyViewModel.categoryList.observeAsState(null)

    LazyColumn {

        items(categoryList?: emptyList()) {

        }

        items(categoryList?: emptyList()) {
            FacultyItemView(it,
                delete = {category ->
                    facultyViewModel.deleteFacultyCategory(category)
                },
                onClick = {categoryName ->
                    val routes = Routes.FacultyDetails.route.replace("{catName}", categoryName)
                    navController.navigate(routes)
                }
            )
        }
    }

}