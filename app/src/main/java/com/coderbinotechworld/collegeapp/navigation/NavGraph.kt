package com.coderbinotechworld.collegeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coderbinotechworld.collegeapp.admin.screens.AdminDashboard
import com.coderbinotechworld.collegeapp.admin.screens.FacultyDetails
import com.coderbinotechworld.collegeapp.admin.screens.ManageBanner
import com.coderbinotechworld.collegeapp.admin.screens.ManageCollegeInfo
import com.coderbinotechworld.collegeapp.admin.screens.ManageFaculty
import com.coderbinotechworld.collegeapp.admin.screens.ManageGallery
import com.coderbinotechworld.collegeapp.admin.screens.ManageNotice
import com.coderbinotechworld.collegeapp.screens.AboutUs
import com.coderbinotechworld.collegeapp.screens.BottomNav
import com.coderbinotechworld.collegeapp.screens.Faculty
import com.coderbinotechworld.collegeapp.screens.Gallery
import com.coderbinotechworld.collegeapp.screens.Home
import com.coderbinotechworld.collegeapp.utills.Constant.IS_ADMIN

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = if (IS_ADMIN) Routes.AdminDashboard.route else Routes.BottomNav.route
    ) {

        composable(Routes.BottomNav.route) {
            BottomNav(navController)
        }

        composable(Routes.Home.route) {
            Home()
        }

        composable(Routes.Faculty.route) {
            Faculty(navController)
        }

        composable(Routes.Gallery.route) {
            Gallery()
        }

        composable(Routes.AboutUs.route) {
            AboutUs()
        }



        composable(Routes.AdminDashboard.route) {
            AdminDashboard(navController)
        }

        composable(Routes.ManageBanner.route) {
            ManageBanner(navController)
        }

        composable(Routes.ManageFaculty.route) {
            ManageFaculty(navController)
        }

        composable(Routes.ManegeGallery.route) {
            ManageGallery(navController)
        }

        composable(Routes.ManageCollegeInfo.route) {
            ManageCollegeInfo(navController)
        }

        composable(Routes.ManageNotice.route) {
            ManageNotice(navController)
        }

        composable(Routes.FacultyDetails.route) {
            val data = it.arguments!!.getString("catName")
            FacultyDetails(navController, data!!)
        }

    }

}