package com.example.tingshu.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tingshu.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "home",
        title = "首页",
        icon = Icons.Filled.Home
    )

    data object Search : BottomNavItem(
        route = "search",
        title = "搜索",
        icon = Icons.Filled.Search
    )

    data object Category : BottomNavItem(
        route = "category",
        title = "分类",
        icon = Icons.Filled.Category
    )

    data object Bookshelf : BottomNavItem(
        route = "bookshelf",
        title = "书架",
        icon = Icons.Filled.Book
    )

    data object Profile : BottomNavItem(
        route = "profile",
        title = "我的",
        icon = Icons.Filled.Person
    )

    companion object {
        val items = listOf(Home, Search, Category, Bookshelf, Profile)
    }
}
