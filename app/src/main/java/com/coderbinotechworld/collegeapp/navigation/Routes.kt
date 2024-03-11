package com.coderbinotechworld.collegeapp.navigation

sealed class Routes(val route: String) {

    data object Home: Routes("home")
    data object Faculty: Routes("faculty")
    data object Gallery: Routes("gallery")
    data object AboutUs: Routes("about_us")
    data object BottomNav: Routes("bottom_nav")

    data object AdminDashboard: Routes("admin_dashboard")
    data object ManageBanner: Routes("manage_banner")
    data object ManageFaculty: Routes("manage_faculty")
    data object ManegeGallery: Routes("manage_gallery")
    data object ManageCollegeInfo: Routes("manage_college_info")
    data object ManageNotice: Routes("manage_notice")
    data object FacultyDetails: Routes("faculty_details/{catName}")

}