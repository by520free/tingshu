# 听书 App 完整实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 开发一款完整的免费无广告听书 App，包含首页、搜索、分类、书架、播放器、设置等功能，支持自定义源导入。

**Architecture:** 采用 Clean Architecture + MVVM 架构，分层设计（UI层/业务层/数据层），使用 Hilt 进行依赖注入，Room 数据库存储本地数据，Media3 ExoPlayer 处理音频播放。

**Tech Stack:** Kotlin + Jetpack Compose + Material 3 + Hilt + Room + Retrofit + OkHttp + Jsoup + Media3 ExoPlayer

---

## 项目结构

```
app/
├── src/main/
│   ├── java/com/example/tingshu/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── db/                    # Room 数据库
│   │   │   │   ├── dao/
│   │   │   │   └── entity/
│   │   │   └── repository/               # 仓库实现
│   │   ├── domain/
│   │   │   ├── model/                    # 领域模型
│   │   │   ├── repository/               # 仓库接口
│   │   │   └── usecase/                  # 用例
│   │   ├── presentation/
│   │   │   ├── ui/
│   │   │   │   ├── theme/                # Material 3 主题
│   │   │   │   ├── components/           # 通用组件
│   │   │   │   ├── home/                 # 首页
│   │   │   │   ├── search/               # 搜索
│   │   │   │   ├── category/             # 分类
│   │   │   │   ├── book/                 # 书籍详情
│   │   │   │   ├── shelf/                # 书架
│   │   │   │   ├── player/               # 播放器
│   │   │   │   ├── settings/             # 设置
│   │   │   │   └── sources/              # 源管理
│   │   │   └── navigation/               # 导航
│   │   ├── player/                       # 播放器核心
│   │   ├── source/                       # 有声书源
│   │   └── di/                           # 依赖注入
│   └── res/
└── build.gradle.kts
```

---

## 实现阶段

### Phase 1: 项目基础框架

#### 任务 1.1: 项目初始化

**Files:**
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/example/tingshu/TingshuApp.kt`

- [ ] **Step 1: 创建 Gradle 配置文件**

```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.tingshu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tingshu"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Activity & Navigation
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Jsoup
    implementation("org.jsoup:jsoup:1.17.2")

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-session:1.2.1")

    // Coil
    implementation("io.coil-kt:coil-compose:2.5.0")
}
```

- [ ] **Step 2: 创建 Application 类**

```kotlin
package com.example.tingshu

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TingshuApp : Application()
```

- [ ] **Step 3: 创建 AndroidManifest.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />

    <application
        android:name=".TingshuApp"
        android:allowBackup="true"
        android:theme="@style/Theme.Tingshu">

        <activity
            android:name=".presentation.MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".player.PlayerService"
            android:exported="false"
            android:foregroundServiceType="mediaPlayback">
            <intent-filter>
                <action android:name="androidx.media3.session.MediaSessionService" />
            </intent-filter>
        </service>

    </application>
</manifest>
```

- [ ] **Step 4: 提交代码**

```bash
git add app/build.gradle.kts app/src app/src/main/AndroidManifest.xml
git commit -m "chore: initial project setup"
```

---

#### 任务 1.2: Material 3 主题配置

**Files:**
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/theme/Color.kt`
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/theme/Theme.kt`

- [ ] **Step 1: 创建颜色定义**

```kotlin
package com.example.tingshu.presentation.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Purple40 = Color(0xFF6750A4)
val PurpleGrey40 = Color(0xFF625B71)

val SurfaceLight = Color(0xFFFFFBFE)
val SurfaceDark = Color(0xFF1C1B1F)
```

- [ ] **Step 2: 创建主题文件**

```kotlin
package com.example.tingshu.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    surface = SurfaceDark,
    background = SurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    surface = SurfaceLight,
    background = SurfaceLight
)

@Composable
fun TingshuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}
```

- [ ] **Step 3: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/presentation/ui/theme/
git commit -m "feat: add Material 3 theme"
```

---

#### 任务 1.3: 底部导航框架

**Files:**
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/navigation/Screen.kt`
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/navigation/MainNavigation.kt`
- Create: `app/src/main/java/com/example/tingshu/presentation/MainActivity.kt`

- [ ] **Step 1: 创建导航路由**

```kotlin
package com.example.tingshu.presentation.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Category : Screen("category")
    data object BookDetail : Screen("book/{bookId}") {
        fun createRoute(bookId: String) = "book/$bookId"
    }
    data object Player : Screen("player")
    data object Shelf : Screen("shelf")
    data object Settings : Screen("settings")
}
```

- [ ] **Step 2: 创建主导航**

```kotlin
package com.example.tingshu.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

data class BottomNavItem(
    val title: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
    val route: String
)

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("首页", { Icon(Icons.Filled.Home, null) }, { Icon(Icons.Outlined.Home, null) }, Screen.Home.route),
        BottomNavItem("搜索", { Icon(Icons.Filled.Search, null) }, { Icon(Icons.Outlined.Search, null) }, Screen.Search.route),
        BottomNavItem("分类", { Icon(Icons.Filled.Category, null) }, { Icon(Icons.Outlined.Category, null) }, Screen.Category.route),
        BottomNavItem("书架", { Icon(Icons.Filled.Bookmarks, null) }, { Icon(Icons.Outlined.Bookmarks, null) }, Screen.Shelf.route),
        BottomNavItem("我的", { Icon(Icons.Filled.Person, null) }, { Icon(Icons.Outlined.Person, null) }, Screen.Settings.route)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = { if (selected) item.selectedIcon() else item.unselectedIcon() },
                        label = { Text(item.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { HomeScreenPlaceholder() }
            composable(Screen.Search.route) { SearchScreenPlaceholder() }
            composable(Screen.Category.route) { CategoryScreenPlaceholder() }
            composable(Screen.Shelf.route) { ShelfScreenPlaceholder() }
            composable(Screen.Settings.route) { SettingsScreenPlaceholder() }
        }
    }
}

// 占位页面
@Composable fun HomeScreenPlaceholder() = Text("首页", style = MaterialTheme.typography.headlineMedium)
@Composable fun SearchScreenPlaceholder() = Text("搜索", style = MaterialTheme.typography.headlineMedium)
@Composable fun CategoryScreenPlaceholder() = Text("分类", style = MaterialTheme.typography.headlineMedium)
@Composable fun ShelfScreenPlaceholder() = Text("书架", style = MaterialTheme.typography.headlineMedium)
@Composable fun SettingsScreenPlaceholder() = Text("设置", style = MaterialTheme.typography.headlineMedium)
```

- [ ] **Step 3: 创建 MainActivity**

```kotlin
package com.example.tingshu.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tingshu.presentation.ui.navigation.MainNavigation
import com.example.tingshu.presentation.ui.theme.TingshuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TingshuTheme {
                MainNavigation()
            }
        }
    }
}
```

- [ ] **Step 4: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/presentation/
git commit -m "feat: add navigation framework"
```

---

### Phase 2: 首页与搜索模块

#### 任务 2.1: 首页布局实现

**Files:**
- Create: `app/src/main/java/com/example/tingshu/domain/model/Book.kt`
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/components/BookCard.kt`
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/home/HomeScreen.kt`

- [ ] **Step 1: 创建领域模型**

```kotlin
package com.example.tingshu.domain.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val narrator: String,
    val coverUrl: String,
    val description: String = "",
    val category: String = "",
    val episodeCount: Int = 0,
    val sourceId: String
)

data class Episode(
    val id: String,
    val title: String,
    val audioUrl: String,
    val duration: Long = 0
)
```

- [ ] **Step 2: 创建书籍卡片组件**

```kotlin
package com.example.tingshu.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tingshu.domain.model.Book

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = book.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(book.title, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(book.narrator, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
    }
}
```

- [ ] **Step 3: 创建首页**

```kotlin
package com.example.tingshu.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tingshu.domain.model.Book
import com.example.tingshu.presentation.ui.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBookClick: (String) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的听书") },
                actions = { IconButton(onClick = onSearchClick) { Icon(Icons.Default.Search, "搜索") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("今日推荐", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sampleBooks) { book ->
                        BookCard(book = book, onClick = { onBookClick(book.id) })
                    }
                }
            }
            item {
                Text("热门榜单", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sampleBooks) { book ->
                        BookCard(book = book, onClick = { onBookClick(book.id) })
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

private val sampleBooks = listOf(
    Book("1", "凡人修仙传", "忘语", "蜂鸟", "https://picsum.photos/200", sourceId = "test"),
    Book("2", "斗破苍穹", "天蚕土豆", "阑珊", "https://picsum.photos/200", sourceId = "test"),
    Book("3", "庆余年", "猫腻", "张遥", "https://picsum.photos/200", sourceId = "test"),
    Book("4", "全职高手", "蝴蝶蓝", "蝴蝶", "https://picsum.photos/200", sourceId = "test")
)
```

- [ ] **Step 4: 更新导航使用新首页**

修改 MainNavigation.kt 中的 composable 调用

- [ ] **Step 5: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/domain/ app/src/main/java/com/example/tingshu/presentation/ui/home/ app/src/main/java/com/example/tingshu/presentation/ui/components/
git commit -m "feat: implement home screen"
```

---

### Phase 3: 播放器模块

#### 任务 3.1: 播放服务与控制器

**Files:**
- Create: `app/src/main/java/com/example/tingshu/player/PlayerState.kt`
- Create: `app/src/main/java/com/example/tingshu/player/PlayerController.kt`
- Create: `app/src/main/java/com/example/tingshu/player/PlayerService.kt`

- [ ] **Step 1: 创建播放器状态**

```kotlin
package com.example.tingshu.player

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode

data class PlayerState(
    val currentBook: Book? = null,
    val currentEpisode: Episode? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val playbackSpeed: Float = 1f
) {
    val progress: Float get() = if (duration > 0) currentPosition.toFloat() / duration else 0f
}
```

- [ ] **Step 2: 创建播放器控制器**

```kotlin
package com.example.tingshu.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.exoplayer.ExoPlayer
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var player: ExoPlayer? = null
    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state

    private var currentPlaylist: List<Episode> = emptyList()

    fun initialize() {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().apply {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _state.value = _state.value.copy(isPlaying = isPlaying)
                    }
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) {
                            _state.value = _state.value.copy(duration = player?.duration ?: 0L)
                        }
                    }
                })
            }
        }
    }

    fun playBook(book: Book, episodes: List<Episode>, startEpisode: Episode? = null) {
        currentPlaylist = episodes
        val episode = startEpisode ?: episodes.firstOrNull() ?: return
        playEpisode(book, episode)
    }

    fun playEpisode(book: Book, episode: Episode) {
        player?.apply {
            setMediaItem(MediaItem.fromUri(episode.audioUrl))
            prepare()
            play()
        }
        _state.value = PlayerState(currentBook = book, currentEpisode = episode)
    }

    fun play() { player?.play() }
    fun pause() { player?.pause() }
    fun seekTo(position: Long) { player?.seekTo(position) }
    fun playNext() { /* TODO */ }
    fun playPrevious() { /* TODO */ }
    fun setSpeed(speed: Float) { player?.setPlaybackSpeed(speed) }
    fun updatePosition() {
        player?.let { p ->
            _state.value = _state.value.copy(currentPosition = p.currentPosition)
        }
    }
    fun release() { player?.release(); player = null }
}
```

- [ ] **Step 3: 创建播放服务**

```kotlin
package com.example.tingshu.player

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.tingshu.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_SPEECH).setUsage(C.USAGE_MEDIA).build(), true)
            .build()
        val sessionActivityPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        mediaSession = MediaSession.Builder(this, player).setSessionActivity(sessionActivityPendingIntent).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onDestroy() {
        mediaSession?.run { player.release(); release(); mediaSession = null }
        super.onDestroy()
    }
}
```

- [ ] **Step 4: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/player/
git commit -m "feat: implement player service and controller"
```

---

### Phase 4: 数据层与源模块

#### 任务 4.1: Room 数据库

**Files:**
- Create: `app/src/main/java/com/example/tingshu/data/local/db/entity/BookEntity.kt`
- Create: `app/src/main/java/com/example/tingshu/data/local/db/TingshuDatabase.kt`
- Create: `app/src/main/java/com/example/tingshu/di/DatabaseModule.kt`

- [ ] **Step 1: 创建实体**

```kotlin
package com.example.tingshu.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val narrator: String,
    val coverUrl: String,
    val description: String,
    val category: String,
    val episodeCount: Int,
    val sourceId: String
)
```

- [ ] **Step 2: 创建数据库**

```kotlin
package com.example.tingshu.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tingshu.data.local.db.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class TingshuDatabase : RoomDatabase()
```

- [ ] **Step 3: 创建依赖注入模块**

```kotlin
package com.example.tingshu.di

import android.content.Context
import androidx.room.Room
import com.example.tingshu.data.local.db.TingshuDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, TingshuDatabase::class.java, "tingshu_db").build()
}
```

- [ ] **Step 4: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/data/ app/src/main/java/com/example/tingshu/di/
git commit -m "feat: add Room database configuration"
```

---

#### 任务 4.2: 源接口与实现

**Files:**
- Create: `app/src/main/java/com/example/tingshu/source/AudioSource.kt`
- Create: `app/src/main/java/com/example/tingshu/source/impl/LibriVoxSource.kt`

- [ ] **Step 1: 创建源接口**

```kotlin
package com.example.tingshu.source

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode

interface AudioSource {
    fun getSourceId(): String
    fun getSourceName(): String
    suspend fun search(keyword: String, page: Int = 1): List<Book>
    suspend fun getBookDetail(bookId: String): Pair<Book, List<Episode>>
}

data class SourceResult(
    val books: List<Book>,
    val hasMore: Boolean
)
```

- [ ] **Step 2: 创建 LibriVox 源**

```kotlin
package com.example.tingshu.source.impl

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode
import com.example.tingshu.source.AudioSource
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibriVoxSource @Inject constructor() : AudioSource {
    override fun getSourceId() = "librivox"
    override fun getSourceName() = "LibriVox"

    override suspend fun search(keyword: String, page: Int): List<Book> {
        return try {
            val url = "https://librivox.org/search?q=$keyword"
            val doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get()
            doc.select(".catalog-result").map { el ->
                val title = el.select(".title-link").text()
                val author = el.select(".author").text()
                val link = el.select(".title-link").attr("href")
                Book(id = link, title = title, author = author, narrator = "Volunteer", coverUrl = "", sourceId = getSourceId())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getBookDetail(bookId: String): Pair<Book, List<Episode>> {
        val book = Book(id = bookId, title = "Sample", author = "Author", narrator = "Narrator", coverUrl = "", sourceId = getSourceId())
        return book to emptyList()
    }
}
```

- [ ] **Step 3: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/source/
git commit -m "feat: implement audio source interface and LibriVox source"
```

---

### Phase 5: 书架与设置模块

#### 任务 5.1: 书架页面

**Files:**
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/shelf/ShelfScreen.kt`

- [ ] **Step 1: 创建书架页面**

```kotlin
package com.example.tingshu.presentation.ui.shelf

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tingshu.domain.model.Book
import com.example.tingshu.presentation.ui.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfScreen(onBookClick: (String) -> Unit = {}) {
    var selectedTab by mutableIntStateOf(0)
    val tabs = listOf("收藏", "下载", "历史")
    val sampleBooks = listOf(
        Book("1", "凡人修仙传", "忘语", "蜂鸟", "", sourceId = "test"),
        Book("2", "斗破苍穹", "天蚕土豆", "阑珊", "", sourceId = "test")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("我的书架") })
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(sampleBooks) { book -> BookCard(book = book, onClick = { onBookClick(book.id) }) }
        }
    }
}
```

- [ ] **Step 2: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/presentation/ui/shelf/
git commit -m "feat: implement shelf screen"
```

---

#### 任务 5.2: 设置页面

**Files:**
- Create: `app/src/main/java/com/example/tingshu/presentation/ui/settings/SettingsScreen.kt`

- [ ] **Step 1: 创建设置页面**

```kotlin
package com.example.tingshu.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onSourceManagementClick: () -> Unit = {}) {
    var showThemeDialog by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(false) }
    var autoPlayNext by remember { mutableStateOf(true) }

    Scaffold(topBar = { TopAppBar(title = { Text("设置") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            SettingsSection("播放设置")
            SettingsItem(icon = { Icon(Icons.Default.PlayArrow, null) }, title = "默认播放倍速", subtitle = "1.0x", onClick = {})
            SettingsItem(icon = { Icon(Icons.Default.PlayArrow, null) }, title = "自动播放下一章", trailing = { Switch(checked = autoPlayNext, onCheckedChange = { autoPlayNext = it }) })

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsSection("外观")
            SettingsItem(icon = { Icon(Icons.Default.DarkMode, null) }, title = "深色模式", subtitle = if (isDarkMode) "开" else "关", onClick = { isDarkMode = !isDarkMode })

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsSection("数据")
            SettingsItem(icon = { Icon(Icons.Default.Storage, null) }, title = "源管理", subtitle = "管理有声书源", onClick = onSourceManagementClick, showArrow = true)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsSection("关于")
            SettingsItem(icon = { Icon(Icons.Default.Info, null) }, title = "版本", subtitle = "1.0.0")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSection(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
}

@Composable
private fun SettingsItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String = "",
    onClick: () -> Unit = {},
    showArrow: Boolean = false,
    trailing: @Composable (() -> Unit)? = null
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { if (subtitle.isNotEmpty()) Text(subtitle) },
        leadingContent = icon,
        trailingContent = { if (showArrow) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) else trailing?.invoke() },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
```

- [ ] **Step 2: 提交代码**

```bash
git add app/src/main/java/com/example/tingshu/presentation/ui/settings/
git commit -m "feat: implement settings screen"
```

---

## 开发检查清单

- [x] Phase 1: 项目初始化、主题、导航框架
- [ ] Phase 2: 首页与搜索模块
- [ ] Phase 3: 播放器模块
- [ ] Phase 4: 数据层与源模块
- [ ] Phase 5: 书架与设置模块

---

## 执行选项

**1. Subagent-Driven (推荐)** - 我将分配专门的子代理来处理每个任务，并在任务之间进行审查

**2. Inline Execution** - 在当前会话中按批次执行任务，并在关键节点进行检查

你选择哪种执行方式？