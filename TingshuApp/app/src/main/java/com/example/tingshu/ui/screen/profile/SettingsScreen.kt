package com.example.tingshu.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tingshu.data.preferences.ThemeMode

@Composable
fun SettingsScreen(
    onSourceManagementClick: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showIntroDialog by remember { mutableStateOf(false) }
    var showOutroDialog by remember { mutableStateOf(false) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var showClearCacheDialog by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        UserHeader()

        SettingsGroup(title = "播放设置") {
            SettingsItem(
                title = "默认播放倍速",
                value = "${uiState.defaultPlaybackSpeed}x",
                onClick = { showSpeedDialog = true }
            )
            SettingsSwitchItem(
                title = "自动播放下一章",
                checked = uiState.autoPlayNext,
                onCheckedChange = { viewModel.setAutoPlayNext(it) }
            )
            SettingsItem(
                title = "跳过片头",
                value = if (uiState.skipIntroSeconds > 0) "${uiState.skipIntroSeconds}秒" else "关闭",
                onClick = { showIntroDialog = true }
            )
            SettingsItem(
                title = "跳过片尾",
                value = if (uiState.skipOutroSeconds > 0) "${uiState.skipOutroSeconds}秒" else "关闭",
                onClick = { showOutroDialog = true }
            )
            SettingsItem(
                title = "睡眠定时默认时长",
                value = "${uiState.sleepTimerDefault}分钟",
                onClick = { showSleepTimerDialog = true }
            )
        }

        SettingsGroup(title = "外观设置") {
            SettingsItem(
                title = "深色模式",
                value = when (uiState.themeMode) {
                    ThemeMode.FOLLOW_SYSTEM -> "跟随系统"
                    ThemeMode.LIGHT -> "浅色"
                    ThemeMode.DARK -> "深色"
                },
                onClick = { showThemeDialog = true }
            )
            SettingsItem(
                title = "主题色",
                value = "紫色",
                onClick = {}
            )
        }

        SettingsGroup(title = "数据设置") {
            SettingsItem(
                title = "源管理",
                onClick = onSourceManagementClick,
                showArrow = true
            )
            SettingsItem(
                title = "清除缓存",
                value = uiState.cacheSize,
                onClick = { showClearCacheDialog = true }
            )
            SettingsItem(
                title = "清除搜索历史",
                onClick = { showClearHistoryDialog = true },
                showArrow = false,
                isLast = true
            )
        }

        SettingsGroup(title = "关于") {
            SettingsItem(
                title = "版本号",
                value = "1.0.0"
            )
            SettingsItem(
                title = "开源许可",
                onClick = {},
                showArrow = true
            )
            SettingsItem(
                title = "隐私政策",
                onClick = {},
                showArrow = true,
                isLast = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showSpeedDialog) {
        SingleChoiceDialog(
            title = "默认播放倍速",
            options = listOf("0.5x", "0.75x", "1.0x", "1.25x", "1.5x", "2.0x", "2.5x", "3.0x"),
            selectedIndex = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 2.5f, 3.0f)
                .indexOf(uiState.defaultPlaybackSpeed).coerceAtLeast(0),
            onDismiss = { showSpeedDialog = false },
            onConfirm = { index ->
                val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 2.5f, 3.0f)
                viewModel.setDefaultPlaybackSpeed(speeds[index])
                showSpeedDialog = false
            }
        )
    }

    if (showThemeDialog) {
        SingleChoiceDialog(
            title = "深色模式",
            options = listOf("跟随系统", "浅色", "深色"),
            selectedIndex = uiState.themeMode.ordinal,
            onDismiss = { showThemeDialog = false },
            onConfirm = { index ->
                viewModel.setThemeMode(ThemeMode.entries[index])
                showThemeDialog = false
            }
        )
    }

    if (showIntroDialog) {
        NumberInputDialog(
            title = "跳过片头（秒）",
            value = uiState.skipIntroSeconds,
            minValue = 0,
            maxValue = 300,
            unit = "秒",
            onDismiss = { showIntroDialog = false },
            onConfirm = {
                viewModel.setSkipIntroSeconds(it)
                showIntroDialog = false
            }
        )
    }

    if (showOutroDialog) {
        NumberInputDialog(
            title = "跳过片尾（秒）",
            value = uiState.skipOutroSeconds,
            minValue = 0,
            maxValue = 300,
            unit = "秒",
            onDismiss = { showOutroDialog = false },
            onConfirm = {
                viewModel.setSkipOutroSeconds(it)
                showOutroDialog = false
            }
        )
    }

    if (showSleepTimerDialog) {
        NumberInputDialog(
            title = "睡眠定时默认时长",
            value = uiState.sleepTimerDefault,
            minValue = 5,
            maxValue = 180,
            unit = "分钟",
            onDismiss = { showSleepTimerDialog = false },
            onConfirm = {
                viewModel.setSleepTimerDefault(it)
                showSleepTimerDialog = false
            }
        )
    }

    if (showClearCacheDialog) {
        ConfirmDialog(
            title = "清除缓存",
            text = "确定要清除所有缓存文件吗？",
            onDismiss = { showClearCacheDialog = false },
            onConfirm = {
                viewModel.clearCache()
                showClearCacheDialog = false
            }
        )
    }

    if (showClearHistoryDialog) {
        ConfirmDialog(
            title = "清除搜索历史",
            text = "确定要清除所有搜索历史吗？",
            onDismiss = { showClearHistoryDialog = false },
            onConfirm = {
                viewModel.clearSearchHistory()
                showClearHistoryDialog = false
            }
        )
    }
}

@Composable
private fun UserHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "我的",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "管理你的设置与偏好",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun SettingsGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    value: String = "",
    onClick: (() -> Unit)? = null,
    showArrow: Boolean = false,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (value.isNotEmpty()) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (showArrow) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    if (!isLast) {
        Divider(
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
    if (!isLast) {
        Divider(
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun SingleChoiceDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var selected by remember { mutableStateOf(selectedIndex) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selected = index }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.RadioButton(
                            selected = selected == index,
                            onClick = { selected = index }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun NumberInputDialog(
    title: String,
    value: Int,
    minValue: Int,
    maxValue: Int,
    unit: String,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var currentValue by remember { mutableStateOf(value) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = {
                            if (currentValue > minValue) currentValue -= 5
                        }
                    ) {
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(
                        text = "$currentValue $unit",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    TextButton(
                        onClick = {
                            if (currentValue < maxValue) currentValue += 5
                        }
                    ) {
                        Text("+", style = MaterialTheme.typography.titleLarge)
                    }
                }
                androidx.compose.material3.Slider(
                    value = currentValue.toFloat(),
                    onValueChange = { currentValue = it.toInt() },
                    valueRange = minValue.toFloat()..maxValue.toFloat(),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(currentValue) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun ConfirmDialog(
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
