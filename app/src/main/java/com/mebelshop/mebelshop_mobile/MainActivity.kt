@file:OptIn(ExperimentalTvMaterial3Api::class)

package com.mebelshop.mebelshop_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.mebelshop.mebelshop_mobile.ar.AR2
import com.mebelshop.mebelshop_mobile.ui.theme.AppTheme
import com.mebelshop.mebelshop_mobile.ui.theme.AppTypography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
             AppTheme {
                var ar by remember{
                    mutableStateOf(false)
                }
                if(ar){
                    enableEdgeToEdge()
                    AR2()
                }
                else{
                    Button(onClick = {ar = true}) {
                        Text("Toggle AR")
                    }
                }
            }
        }
    }

    enum class CardType(){
        Common, Liked, Discount, LikedDicsount
    }

    @Composable
    fun PhotoCarousel(photos: List<Int>, modifier: Modifier = Modifier){
        val pagerState = rememberPagerState(initialPage = 0){photos.size}
        HorizontalPager(
            state = pagerState,
            modifier = modifier.width(256.dp)
        ){ page: Int ->
            Image(painterResource(photos[page]), contentDescription = "example photo", modifier.size(256.dp, 256.dp))
        }
    }

    @Composable
    fun MainCardItem(photos: List<Int>, cost: Int, name: String, id: Int, type: CardType){
        AppTheme {
            val colors = when(type){
                CardType.Common -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
                CardType.Liked -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
                CardType.LikedDicsount, CardType.Discount-> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
            }
            Card(shape = RoundedCornerShape(16), colors = colors){
                Box(modifier = Modifier.padding(16.dp)){
                    Column {
                        Box{
                            Box(contentAlignment = Alignment.Center) {
                                PhotoCarousel(
                                    photos, modifier = Modifier
                                        .padding(bottom = 5.dp)
                                        .clip(
                                            RoundedCornerShape(16)
                                        )
                                )
                            }
                            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.size(256.dp)){
                                val (icon, color) = when(type){
                                    CardType.Common, CardType.Discount -> listOf(Icons.Outlined.FavoriteBorder, Color.White)
                                    CardType.Liked, CardType.LikedDicsount -> listOf(Icons.Filled.Favorite, Color.Red)
                                }
                                Icon(imageVector = icon as ImageVector, tint = color as Color, contentDescription = null, modifier = Modifier.padding(16.dp))
                            }
                        }
                        Column {
                            Text("$cost Р", fontWeight = AppTypography.titleSmall.fontWeight)
                            Text(name, modifier = Modifier.width(256.dp), fontWeight = AppTypography.bodySmall.fontWeight, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

    }

    @Preview
    @Composable
    fun MainCardPreview(){
        MainCardItem(
            listOf(
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3),
            7200,
            "Стол для инвалидов-колясочников",
            0,
            CardType.Common
        )
    }

}

