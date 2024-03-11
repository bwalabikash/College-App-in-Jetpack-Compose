package com.coderbinotechworld.collegeapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coderbinotechworld.collegeapp.R
import com.coderbinotechworld.collegeapp.models.BottomNavItem
import com.coderbinotechworld.collegeapp.models.NavItem
import com.coderbinotechworld.collegeapp.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavController) {

    val navController1 = rememberNavController()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val context = LocalContext.current

    val list = listOf(
        NavItem(
            "Website",
            R.drawable.website
        ),
        NavItem(
            "Notice",
            R.drawable.notice
        ),
        NavItem(
            "Notes",
            R.drawable.notes
        ),
        NavItem(
            "Contact us",
            R.drawable.contact_us
        )
    )


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Image(painter = painterResource(id = R.drawable.photo), contentDescription = null,
                    modifier = Modifier.height(220.dp),
                    contentScale = ContentScale.FillBounds)

                Divider()
                
                Text(text = "")

                list.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                modifier = Modifier.height(24.dp))
                        })
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "College App")
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(painter = painterResource(id = R.drawable.menu), contentDescription = null)
                            }
                        }
                    )
                },
                bottomBar = {
                    MyBottomNav(navController = navController1)
                }
            ) {padding ->
                NavHost(
                    navController = navController1,
                    startDestination = Routes.Home.route,
                    modifier = Modifier.padding(padding)
                ) {
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
                }
            }
        }
    )

}

@Composable
fun MyBottomNav(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            "Home",
            R.drawable.home,
            Routes.Home.route
        ),
        BottomNavItem(
            "Faculty",
            R.drawable.faculty,
            Routes.Faculty.route
        ),
        BottomNavItem(
            "Gallery",
            R.drawable.gallery,
            Routes.Gallery.route
        ),
        BottomNavItem(
            "About Us",
            R.drawable.contact_us,
            Routes.AboutUs.route
        )
    )

    BottomAppBar {
        list.forEach {

            val currentRoute = it.route
            val otherRoute =
                try {
                    backStackEntry.value!!.destination.route
                }catch (e: Exception) {
                    Routes.Home.route
                }

            val selected = currentRoute == otherRoute

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = it.icon), contentDescription = it.title)
                }
            )
        }
    }

}