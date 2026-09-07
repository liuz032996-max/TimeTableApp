package com.example.timetable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// ======================= 1. 課堂資料模型與 1151 課表資料 =======================
data class Course(
    val name: String,
    val classroom: String,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val period: String
)

val mySemesterSchedule = listOf(
    // 星期一 (Mon)
    Course("電子學(一)", "R70102", DayOfWeek.MONDAY, LocalTime.of(10, 10), LocalTime.of(11, 0), "第3節"),
    Course("電子學(一)", "R70102", DayOfWeek.MONDAY, LocalTime.of(11, 10), LocalTime.of(12, 0), "第4節"),
    Course("微電腦實驗", "R70405", DayOfWeek.MONDAY, LocalTime.of(13, 10), LocalTime.of(14, 0), "第6節"),
    Course("微電腦實驗", "R70405", DayOfWeek.MONDAY, LocalTime.of(14, 10), LocalTime.of(15, 0), "第7節"),
    Course("微電腦實驗", "R70405", DayOfWeek.MONDAY, LocalTime.of(15, 10), LocalTime.of(16, 0), "第8節"),
    Course("工程數學(一)", "R70206", DayOfWeek.MONDAY, LocalTime.of(16, 10), LocalTime.of(17, 0), "第9節"),
    Course("工程數學(一)", "R70206", DayOfWeek.MONDAY, LocalTime.of(17, 10), LocalTime.of(18, 0), "第10節"),

    // 星期二 (Tue)
    Course("微電腦系統", "R70304", DayOfWeek.TUESDAY, LocalTime.of(8, 10), LocalTime.of(9, 0), "第1節"),
    Course("微電腦系統", "R70304", DayOfWeek.TUESDAY, LocalTime.of(9, 10), LocalTime.of(10, 0), "第2節"),
    Course("電路學", "R70105", DayOfWeek.TUESDAY, LocalTime.of(10, 10), LocalTime.of(11, 0), "第3節"),
    Course("電路學", "R70105", DayOfWeek.TUESDAY, LocalTime.of(11, 10), LocalTime.of(12, 0), "第4節"),
    Course("工程數學(一)", "R70103", DayOfWeek.TUESDAY, LocalTime.of(13, 10), LocalTime.of(14, 0), "第6節"),
    Course("工程數學(一)", "R70103", DayOfWeek.TUESDAY, LocalTime.of(14, 10), LocalTime.of(15, 0), "第7節"),
    Course("桌球", "R體育場地", DayOfWeek.TUESDAY, LocalTime.of(15, 10), LocalTime.of(16, 0), "第8節"),
    Course("桌球", "R體育場地", DayOfWeek.TUESDAY, LocalTime.of(16, 10), LocalTime.of(17, 0), "第9節"),

    // 星期三 (Wed)
    Course("全民國防教育軍事訓練課程", "R1115", DayOfWeek.WEDNESDAY, LocalTime.of(8, 10), LocalTime.of(9, 0), "第1節"),
    Course("全民國防教育軍事訓練課程", "R1115", DayOfWeek.WEDNESDAY, LocalTime.of(9, 10), LocalTime.of(10, 0), "第2節"),
    Course("電路學", "R70102", DayOfWeek.WEDNESDAY, LocalTime.of(10, 10), LocalTime.of(11, 0), "第3節"),
    Course("資料結構", "R70206", DayOfWeek.WEDNESDAY, LocalTime.of(11, 10), LocalTime.of(12, 0), "第4節"),
    Course("健康、休閒與生活", "R1213", DayOfWeek.WEDNESDAY, LocalTime.of(14, 10), LocalTime.of(15, 0), "第7節"),
    Course("健康、休閒與生活", "R1213", DayOfWeek.WEDNESDAY, LocalTime.of(15, 10), LocalTime.of(16, 0), "第8節"),

    // 星期四 (Thu)
    Course("微電腦系統", "R70304", DayOfWeek.THURSDAY, LocalTime.of(8, 10), LocalTime.of(9, 0), "第1節"),
    Course("資料結構", "R70304", DayOfWeek.THURSDAY, LocalTime.of(10, 10), LocalTime.of(11, 0), "第3節"),
    Course("資料結構", "R70304", DayOfWeek.THURSDAY, LocalTime.of(11, 10), LocalTime.of(12, 0), "第4節"),
    Course("電子電路實驗(一)", "R70308", DayOfWeek.THURSDAY, LocalTime.of(14, 10), LocalTime.of(15, 0), "第7節"),
    Course("電子電路實驗(一)", "R70308", DayOfWeek.THURSDAY, LocalTime.of(15, 10), LocalTime.of(16, 0), "第8節"),
    Course("電子電路實驗(一)", "R70308", DayOfWeek.THURSDAY, LocalTime.of(16, 10), LocalTime.of(17, 0), "第9節"),

    // 星期五 (Fri)
    Course("英語筆譯(一)", "R1211", DayOfWeek.FRIDAY, LocalTime.of(8, 10), LocalTime.of(9, 0), "第1節"),
    Course("英語筆譯(一)", "R1211", DayOfWeek.FRIDAY, LocalTime.of(9, 10), LocalTime.of(10, 0), "第2節"),
    Course("電子學(一)", "R70102", DayOfWeek.FRIDAY, LocalTime.of(10, 10), LocalTime.of(11, 0), "第3節")
)

// ======================= 2. App 進入點 =======================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF6F8FA)
                ) {
                    ScheduleDashboard()
                }
            }
        }
    }
}

// ======================= 3. 主畫面與判定邏輯 =======================
@Composable
fun ScheduleDashboard() {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    // 每 5 秒自動檢測一次系統時間並刷新畫面
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            currentDate = LocalDate.now()
            delay(5000L)
        }
    }

    val todayOfWeek = currentDate.dayOfWeek
    val todayList = remember(todayOfWeek) {
        mySemesterSchedule.filter { it.dayOfWeek == todayOfWeek }.sortedBy { it.startTime }
    }

    // 判斷當前進行中或下一節課
    val ongoingCourse = todayList.find { currentTime >= it.startTime && currentTime <= it.endTime }
    val nextCourse = todayList.find { it.startTime > currentTime }

    val activeCourse = ongoingCourse ?: nextCourse
    val isOngoing = ongoingCourse != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 頂部星期與即時時間
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${todayOfWeek.name} (${currentDate})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 置頂卡片：優先顯示當前或下一節課的位置與時間
        if (activeCourse != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOngoing) Color(0xFF1E88E5) else Color(0xFF37474F)
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isOngoing) "● 正在上課" else "➜ 下一節課",
                        color = if (isOngoing) Color(0xFFFFEB3B) else Color(0xFF81D4FA),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = activeCourse.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(text = "教室位置", fontSize = 12.sp, color = Color.LightGray)
                            Text(
                                text = activeCourse.classroom,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "${activeCourse.period} (${activeCourse.startTime} - ${activeCourse.endTime})",
                            fontSize = 14.sp,
                            color = Color(0xFFE0E0E0)
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(text = "🎉 今日已無其他課程或非上課日", fontSize = 16.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "今日課表清單", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // 下方今日課程列表
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(todayList) { course ->
                val isSelected = course == activeCourse
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${course.period}  ${course.name}",
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color(0xFF1565C0) else Color.Black
                            )
                            Text(
                                text = "教室：${course.classroom}",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "${course.startTime} - ${course.endTime}",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
