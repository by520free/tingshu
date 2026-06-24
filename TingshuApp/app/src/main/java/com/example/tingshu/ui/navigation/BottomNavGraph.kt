package com.example.tingshu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tingshu.ui.screen.bookshelf.BookshelfScreen
import com.example.tingshu.ui.screen.book.BookDetailScreen
import com.example.tingshu.ui.screen.book.BookDetailViewModel
import com.example.tingshu.ui.screen.category.CategoryScreen
import com.example.tingshu.ui.screen.home.HomeScreen
import com.example.tingshu.ui.screen.player.PlayerScreen
import com.example.tingshu.ui.screen.profile.SettingsScreen
import com.example.tingshu.ui.screen.search.SearchScreen
import com.example.tingshu.ui.screen.sources.SourceManagementScreen

object Routes {
    const val BOOK_DETAIL = "book_detail/{bookId}"
    const val PLAYER = "player"
    const val SOURCE_MANAGEMENT = "source_management"

    fun bookDetail(bookId: String) = "book_detail/$bookId"
    fun player() = PLAYER
    fun sourceManagement() = SOURCE_MANAGEMENT
}

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(
                onSearchClick = {
                    navController.navigate(BottomNavItem.Search.route)
                },
                onBookClick = { bookId ->
                    navController.navigate(Routes.bookDetail(bookId))
                },
                onViewAllRecommended = {
                    navController.navigate(BottomNavItem.Category.route)
                },
                onViewAllHot = {
                    navController.navigate(BottomNavItem.Category.route)
                },
                onCategoryClick = { category ->
                    navController.navigate(BottomNavItem.Category.route)
                }
            )
        }
        composable(route = BottomNavItem.Search.route) {
            SearchScreen(
                onBookClick = { bookId ->
                    navController.navigate(Routes.bookDetail(bookId))
                },
                onCategoryClick = { category ->
                    navController.navigate(BottomNavItem.Category.route)
                }
            )
        }
        composable(route = BottomNavItem.Category.route) {
            CategoryScreen(
                onSubcategoryClick = { subcategory ->
                    navController.navigate(BottomNavItem.Search.route)
                }
            )
        }
        composable(route = BottomNavItem.Bookshelf.route) {
            BookshelfScreen(
                onBookClick = { bookId ->
                    navController.navigate(Routes.bookDetail(bookId))
                }
            )
        }
        composable(route = BottomNavItem.Profile.route) {
            SettingsScreen(
                onSourceManagementClick = {
                    navController.navigate(Routes.sourceManagement())
                }
            )
        }
        composable(
            route = Routes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) {
            val bookDetailViewModel: BookDetailViewModel = hiltViewModel()
            BookDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPlayEpisode = { episodeId ->
                    bookDetailViewModel.playEpisode(episodeId)
                    navController.navigate(Routes.player())
                },
                onPlayAll = {
                    bookDetailViewModel.playAll()
                    navController.navigate(Routes.player())
                }
            )
        }
        composable(route = Routes.PLAYER) {
            PlayerScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Routes.SOURCE_MANAGEMENT) {
            SourceManagementScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
