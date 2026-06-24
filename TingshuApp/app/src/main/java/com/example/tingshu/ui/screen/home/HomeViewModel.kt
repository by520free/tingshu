package com.example.tingshu.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingshu.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recommendedBooks: List<Book> = emptyList(),
    val hotBooks: List<Book> = emptyList(),
    val categories: List<String> = emptyList(),
    val bannerImageUrl: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(
                recommendedBooks = sampleBooks,
                hotBooks = sampleBooks.shuffled(),
                categories = listOf(
                    "玄幻", "都市", "言情", "悬疑",
                    "历史", "军事", "科幻", "武侠"
                ),
                bannerImageUrl = "https://picsum.photos/seed/banner/800/300",
                isLoading = false
            )
        }
    }

    companion object {
        val sampleBooks = listOf(
            Book(
                id = "1",
                title = "凡人修仙传",
                author = "忘语",
                narrator = "大灰狼",
                coverUrl = "https://picsum.photos/seed/book1/240/320",
                description = "一个普通的山村穷小子，偶然之下，进入到当地的江湖小门派，成了一名记名弟子。他以这样的身份，开始了自己的修仙之路。",
                category = "玄幻",
                episodeCount = 2000,
                sourceId = "source1",
                isCompleted = true
            ),
            Book(
                id = "2",
                title = "斗破苍穹",
                author = "天蚕土豆",
                narrator = "方寸无衣",
                coverUrl = "https://picsum.photos/seed/book2/240/320",
                description = "这里是属于斗气的世界，没有花俏艳丽的魔法，有的，仅仅是繁衍到巅峰的斗气！",
                category = "玄幻",
                episodeCount = 1600,
                sourceId = "source1",
                isCompleted = true
            ),
            Book(
                id = "3",
                title = "庆余年",
                author = "猫腻",
                narrator = "昊翔",
                coverUrl = "https://picsum.photos/seed/book3/240/320",
                description = "积善之家，必有余庆，留余庆，留余庆，忽遇恩人；幸娘亲，幸娘亲，积得阴功。",
                category = "历史",
                episodeCount = 800,
                sourceId = "source2",
                isCompleted = true
            ),
            Book(
                id = "4",
                title = "全职高手",
                author = "蝴蝶蓝",
                narrator = "北炎",
                coverUrl = "https://picsum.photos/seed/book4/240/320",
                description = "网游荣耀中被誉为教科书级别的顶尖高手叶修，因为种种原因遭到俱乐部的驱逐，离开职业圈的他重新回到游戏赛场的故事。",
                category = "都市",
                episodeCount = 1200,
                sourceId = "source3",
                isCompleted = true
            ),
            Book(
                id = "5",
                title = "鬼吹灯",
                author = "天下霸唱",
                narrator = "周建龙",
                coverUrl = "https://picsum.photos/seed/book5/240/320",
                description = "胡八一上山下乡来到东北地区，在一个叫做岗岗营子的村庄插队时，遇到了一系列诡异的事情。",
                category = "悬疑",
                episodeCount = 500,
                sourceId = "source4",
                isCompleted = true
            ),
            Book(
                id = "6",
                title = "盗墓笔记",
                author = "南派三叔",
                narrator = "青雪",
                coverUrl = "https://picsum.photos/seed/book6/240/320",
                description = "五十年前，一群长沙土夫子挖到了一部战国古墓，但是他们几乎全部死在了古墓中。",
                category = "悬疑",
                episodeCount = 600,
                sourceId = "source4",
                isCompleted = true
            ),
            Book(
                id = "7",
                title = "三体",
                author = "刘慈欣",
                narrator = "王明军",
                coverUrl = "https://picsum.photos/seed/book7/240/320",
                description = "文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划'红岸工程'取得了突破性进展。",
                category = "科幻",
                episodeCount = 200,
                sourceId = "source5",
                isCompleted = true
            ),
            Book(
                id = "8",
                title = "明朝那些事儿",
                author = "当年明月",
                narrator = "王更新",
                coverUrl = "https://picsum.photos/seed/book8/240/320",
                description = "《明朝那些事儿》主要讲述的是从1344年到1644年这三百年间关于明朝的一些事情。",
                category = "历史",
                episodeCount = 300,
                sourceId = "source6",
                isCompleted = true
            )
        )
    }
}
