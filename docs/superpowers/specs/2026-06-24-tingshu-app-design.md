# 听书 App 设计规范

**版本：** v1.0
**日期：** 2026-06-24
**状态：** 待审阅

---

## 1. 项目概述

### 项目目标
重新开发一款免费、无广告的听书 App，复刻原「我的听书」功能，并全面升级 UI 设计和用户体验。

### 核心特性
- 有声书搜索与分类浏览
- 在线播放 + 下载缓存
- 书架管理（收藏、播放历史）
- 自定义源支持（jar 导入）
- 睡眠定时 / 倍速播放
- 深色模式 / 主题切换

---

## 2. 技术架构

### 技术栈
| 类别 | 技术选型 |
|------|----------|
| 平台 | Android |
| 语言 | Kotlin |
| UI 框架 | Jetpack Compose + Material 3 |
| 架构 | MVVM + Clean Architecture |
| 依赖注入 | Hilt |
| 网络 | Retrofit + OkHttp |
| HTML 解析 | Jsoup |
| 本地数据库 | Room |
| 异步处理 | Kotlin Coroutines + Flow |
| 音频播放 | Media3 ExoPlayer |
| 最小 SDK | 24 (Android 7.0) |
| 目标 SDK | 34 (Android 14) |

### 项目结构
```
app/
├── src/main/
│   ├── java/com/example/tingshu/
│   │   ├── data/           # 数据层
│   │   │   ├── local/       # Room 数据库、DataStore
│   │   │   ├── remote/      # API 服务、网络请求
│   │   │   └── repository/  # 仓库实现
│   │   ├── domain/          # 领域层
│   │   │   ├── model/       # 领域模型
│   │   │   ├── repository/  # 仓库接口
│   │   │   └── usecase/     # 用例
│   │   ├── presentation/     # 表现层
│   │   │   ├── ui/          # Compose UI
│   │   │   ├── viewmodel/   # ViewModel
│   │   │   └── navigation/  # 导航
│   │   ├── player/          # 播放器模块
│   │   ├── source/          # 有声书源
│   │   └── di/              # 依赖注入
│   └── res/
└── build.gradle.kts
```

---

## 3. UI/UX 设计

### 设计语言
- **设计系统：** Material Design 3 (Material You)
- **主题色：** 固定紫色 (#6750A4) 为主色
- **圆角风格：** 大量使用圆角卡片，圆角半径 16dp
- **动效：** 平滑的过渡动画，Material 3 标准动效

### 颜色方案

#### 浅色主题
| 用途 | 颜色 |
|------|------|
| 主色 | #6750A4 |
| 次要色 | #625B71 |
| 表面 | #FFFBFE |
| 背景 | #FFFBFE |
| 错误 | #B3261E |
| 文字主色 | #1C1B1F |
| 文字次要色 | #49454F |

#### 深色主题
| 用途 | 颜色 |
|------|------|
| 主色 | #D0BCFF |
| 次要色 | #CCC2DC |
| 表面 | #1C1B1F |
| 背景 | #1C1B1F |
| 错误 | #F2B8B5 |
| 文字主色 | #E6E1E5 |
| 文字次要色 | #CAC4D0 |

### 导航结构

**底部 Tab 导航（5 个标签）：**

| Tab | 图标 | 页面 |
|-----|------|------|
| 首页 | 🏠 | 首页推荐、Banner 轮播、分类入口 |
| 搜索 | 🔍 | 全局搜索、搜索历史、热门推荐 |
| 分类 | 📚 | 分类浏览、排行榜 |
| 书架 | ❤️ | 收藏、下载、历史记录 |
| 我的 | 👤 | 个人设置、主题切换、源管理 |

### 首页布局

```
┌─────────────────────────────┐
│  搜索栏 + 用户头像          │
├─────────────────────────────┤
│  Banner 轮播 (自动/手动)    │
├─────────────────────────────┤
│  🎧 正在播放 (迷你播放器)   │ ← 仅播放时显示
├─────────────────────────────┤
│  今日推荐 ────── 查看全部 > │
│  [横向滑动专辑卡片]         │
├─────────────────────────────┤
│  热门榜单 ────── 查看全部 > │
│  [横向滑动专辑卡片]         │
├─────────────────────────────┤
│  分类导航                   │
│  [玄幻] [都市] [悬疑] ...   │
├─────────────────────────────┤
│  新书上架 ────── 查看全部 > │
│  [横向滑动专辑卡片]         │
└─────────────────────────────┘
```

### 播放器设计

#### 迷你播放器（底部常驻）
```
┌─────────────────────────────┐
│ [封面] 书名 - 作者   ▶️/⏸️  │
│         ████████░░░ 2:30   │
└─────────────────────────────┘
```
- 播放时在底部 Tab 上方显示
- 点击展开全屏播放器
- 支持拖拽收起

#### 全屏沉浸式播放器
```
┌─────────────────────────────┐
│           ←  返回           │
│                             │
│        ┌─────────┐          │
│        │  封面   │          │
│        │  大图   │          │
│        └─────────┘          │
│                             │
│     书名：凡人修仙传         │
│     作者：忘语 | 播音：王牌  │
│                             │
│  ━━━━━●━━━━━━━━━━━━━━━━━   │ ← 进度条
│  01:23 / 45:30              │
│                             │
│    ⏮️   ▶️/⏸️   ⏭️           │ ← 控制按钮
│                             │
│  0.5x  1x  1.5x  2x        │ ← 倍速
│                             │
│  📃章节  ⏰定时  📥下载     │ ← 功能按钮
└─────────────────────────────┘
```

### 页面列表

| 页面 | 路由 | 说明 |
|------|------|------|
| 首页 | `/` | 推荐内容、Banner、分类入口 |
| 搜索页 | `/search` | 搜索框、热门推荐、搜索历史 |
| 搜索结果 | `/search/:keyword` | 搜索结果列表 |
| 分类页 | `/category` | 大分类 Tab + 子分类列表 |
| 分类列表 | `/category/:id` | 该分类下的书籍列表 |
| 书籍详情 | `/book/:id` | 书籍信息、章节列表 |
| 全屏播放器 | `/player` | 沉浸式播放界面 |
| 书架页 | `/shelf` | 收藏/下载/历史 Tab |
| 我的页 | `/profile` | 设置、源管理 |
| 源管理 | `/sources` | 查看/导入/管理自定义源 |

---

## 4. 功能模块

### 4.1 首页模块
- Banner 轮播（自动 5s 切换，支持手动滑动）
- 推荐专辑横向滑动
- 分类快捷入口
- 迷你播放器常驻

### 4.2 搜索模块
- 实时搜索建议
- 搜索历史（本地存储，最多 20 条）
- 热门搜索词
- 搜索结果分页加载

### 4.3 分类模块
- 大分类 Tab（如：玄幻、言情、都市、悬疑等）
- 子分类列表
- 排行榜入口

### 4.4 书籍详情
- 封面、书名、作者、播音、简介
- 章节列表（支持分页）
- 播放/下载按钮
- 收藏/取消收藏

### 4.5 播放器
- 在线播放
- 后台播放（Service）
- 锁屏/通知栏控制
- 播放进度保存/恢复
- 倍速播放（0.5x, 0.75x, 1x, 1.25x, 1.5x, 2x, 3x）
- 跳过片头/片尾时长设置
- 睡眠定时（15min, 30min, 45min, 60min, 定时关闭）
- 上一章/下一章
- 自动播放下一章

### 4.6 书架模块
- **收藏：** 收藏的书籍列表
- **下载：** 已下载的书籍（支持删除）
- **历史：** 播放历史记录（自动记录播放进度）

### 4.7 设置模块
- 深色模式切换（跟随系统 / 浅色 / 深色）
- 清理缓存
- 跳过片头/片尾时长
- 默认播放倍速
- 关于页面

### 4.8 自定义源模块
- 查看已加载的源列表
- 导入 jar 源包
- 启用/禁用特定源
- 源更新检测（支持 HTTP 订阅接口）

---

## 5. 有声书源设计

### 5.1 源接口抽象

每个有声书源需要实现统一的接口：

```kotlin
interface AudioSource {
    fun getSourceId(): String          // 唯一标识
    fun getSourceName(): String        // 源名称
    fun search(keyword: String, page: Int): SearchResult
    fun getCategories(): List<Category>
    fun getCategoryBooks(categoryId: String, page: Int): BookList
    fun getBookDetail(bookId: String): BookDetail
    fun getAudioUrl(episodeId: String): String
}
```

### 5.2 内置源规划

**优先级 1 - 稳定可用：**
- LibriVox（公共领域经典文学，英文）
- 喜马拉雅（第三方 API，仅学习交流）

**优先级 2 - 待调研：**
- 酷我畅听（API 可能已失效，需验证）
- 其他国内平台

**优先级 3 - 自建源：**
- 提供一个用户可配置的 RSS/URL 源模板

### 5.3 自定义源加载

```kotlin
// 源加载器支持：
// 1. 内置源（编译时注册）
// 2. 本地 jar 包（从指定目录加载）
// 3. HTTP 订阅（下载远程 jar）

class SourceLoader {
    fun loadLocalJars()
    fun downloadRemoteJar(url: String)
    fun registerSource(source: AudioSource)
}
```

---

## 6. 数据模型

### Book（书籍）
```kotlin
data class Book(
    val id: String,
    val title: String,
    val author: String,
    val narrator: String,
    val coverUrl: String,
    val description: String,
    val category: String,
    val episodeCount: Int,
    val sourceId: String,
    val isCompleted: Boolean = false
)
```

### Episode（章节）
```kotlin
data class Episode(
    val id: String,
    val title: String,
    val index: Int,
    val duration: Long,       // 时长（毫秒）
    val audioUrl: String
)
```

### PlayProgress（播放进度）
```kotlin
data class PlayProgress(
    val bookId: String,
    val episodeId: String,
    val position: Long,      // 播放位置（毫秒）
    val lastPlayedAt: Long   // 最后播放时间戳
)
```

---

## 7. 数据库设计（Room）

### 表结构

| 表名 | 说明 |
|------|------|
| books | 书籍基本信息 |
| episodes | 章节信息 |
| favorites | 收藏记录 |
| downloads | 下载记录 |
| play_history | 播放历史 |
| play_progress | 播放进度 |
| custom_sources | 自定义源配置 |
| search_history | 搜索历史 |
| app_settings | 应用设置 |

---

## 8. 播放器服务

### MediaSession 配置
- 支持 Media3 ExoPlayer
- 实现 MediaSessionService
- 提供 MediaSession 用于系统音频控制

### 通知栏
- 显示播放通知（封面、书名、章节名）
- 通知栏控制按钮（播放/暂停、上一章、下一章）

### 耳机线控
- 支持蓝牙耳机、有线耳机的线控操作
- 耳机插入/拔出时自动处理播放状态

---

## 9. 开发里程碑

### Phase 1：基础框架（MVP）
- [ ] 项目初始化，依赖配置
- [ ] Material 3 主题配置
- [ ] 底部导航框架
- [ ] 首页布局
- [ ] 搜索页面
- [ ] 分类页面
- [ ] 书架页面
- [ ] 设置页面

### Phase 2：核心功能
- [ ] Room 数据库集成
- [ ] 书籍详情页
- [ ] 迷你播放器
- [ ] 全屏播放器
- [ ] 后台播放服务
- [ ] 通知栏控制

### Phase 3：播放器增强
- [ ] 倍速播放
- [ ] 睡眠定时
- [ ] 播放历史记录
- [ ] 收藏/取消收藏
- [ ] 下载功能

### Phase 4：数据源
- [ ] LibriVox 源接入
- [ ] 喜马拉雅源接入
- [ ] 自定义源加载器
- [ ] 源管理界面

### Phase 5：完善与优化
- [ ] 深色模式
- [ ] 深色模式切换
- [ ] 性能优化
- [ ] 异常处理
- [ ] 测试

---

## 10. 待确认事项

以下事项需在开发过程中进一步确认：

1. **喜马拉雅 API 使用** — 第三方 API 仅供学习交流，需评估风险
2. **具体内置源数量** — 初期 MVP 需要接入多少个源？
3. **下载功能范围** — 是否需要支持断点续传？
4. **用户账号系统** — 是否需要登录功能？
5. **数据同步** — 是否需要云端同步功能？

---

## 附录：参考设计

- Material Design 3 官方文档：https://m3.material.io/
- Jetpack Compose 官方文档：https://developer.android.com/compose
- Media3 ExoPlayer：https://developer.android.com/media3/exoplayer
