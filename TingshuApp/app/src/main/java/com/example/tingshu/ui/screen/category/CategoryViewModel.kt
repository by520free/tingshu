package com.example.tingshu.ui.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryGroup(
    val name: String,
    val subcategories: List<String>
)

data class CategoryUiState(
    val categoryGroups: List<CategoryGroup> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class CategoryViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CategoryUiState(
                categoryGroups = listOf(
                    CategoryGroup(
                        name = "玄幻奇幻",
                        subcategories = listOf(
                            "东方玄幻", "西方奇幻", "异世大陆",
                            "高武世界", "修真文明", "魔法校园"
                        )
                    ),
                    CategoryGroup(
                        name = "都市言情",
                        subcategories = listOf(
                            "都市生活", "都市异能", "职场励志",
                            "现代言情", "豪门总裁", "青春校园"
                        )
                    ),
                    CategoryGroup(
                        name = "悬疑推理",
                        subcategories = listOf(
                            "悬疑探险", "侦探推理", "恐怖惊悚",
                            "灵异鬼怪", "盗墓探险", "法医悬疑"
                        )
                    ),
                    CategoryGroup(
                        name = "历史军事",
                        subcategories = listOf(
                            "历史穿越", "架空历史", "历史传记",
                            "军事战争", "抗战烽火", "谍战特工"
                        )
                    ),
                    CategoryGroup(
                        name = "科幻末世",
                        subcategories = listOf(
                            "未来世界", "星际文明", "科幻机甲",
                            "末世危机", "丧尸生存", "时空穿梭"
                        )
                    ),
                    CategoryGroup(
                        name = "武侠仙侠",
                        subcategories = listOf(
                            "传统武侠", "新派武侠", "古典仙侠",
                            "现代修真", "洪荒封神", "修仙种田"
                        )
                    )
                ),
                isLoading = false
            )
        }
    }
}
