package com.mebelshop.mebelshop_mobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mebelshop.mebelshop_mobile.ar.AR2
import com.mebelshop.mebelshop_mobile.ui.theme.AppTheme
import com.mebelshop.mebelshop_mobile.ui.theme.AppTypography

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
             AppTheme {
                 var isARScreenVisible by remember { mutableStateOf(false) }
                 var selectedModelPath by remember { mutableStateOf<String?>(null) }

                 if(isARScreenVisible && selectedModelPath != null){
                     AR2(selectedModelPath)
                 } else if (isARScreenVisible && selectedModelPath == null) {
                     AR2()
                 }
                 else{
                     Box(
                         modifier = Modifier
                         .padding(top = 30.dp)
                     ) {
                         Column {
                             CategoryBar(DataMobile().listCategoryProduct!!)

                             Card(
                                 modifier = Modifier.padding(16.dp),
                                 colors = CardDefaults.cardColors(
                                     containerColor = MaterialTheme.colorScheme.surfaceContainer
                                 )
                             ) {
                                 LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                                     items(DataMobile().listProduct!!) { product ->
                                         MainCardItem(
                                             product,
                                             CardType.Common,
                                             modifier = Modifier.padding(16.dp),
                                             onClick = {
                                             selectedModelPath = product.model
                                             isARScreenVisible = true
                                         })
                                     }
                                 }
                             }
                         }

                         Button(
                             onClick = { isARScreenVisible = true }
                         ) {
                             Text("Toggle AR")
                         }
                     }
                 }
            }
        }
    }

    @Composable
    fun MainScreen(onToggleAR: (String?) -> Unit) {
        Column {
            Column {
                CategoryBar(DataMobile().listCategoryProduct!!)

                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(DataMobile().listProduct!!) { product ->
                            MainCardItem(product, CardType.Common, modifier = Modifier.padding(16.dp), onClick = {
                                onToggleAR(product.model)
                            })
                        }
                    }
                }
            }

            Button(
                onClick = { onToggleAR(null) }
            ) {
                Text("Toggle AR")
            }
        }
    }

    enum class CardType {
        Common, Liked, Discount, LikedDiscount
    }

    @Composable
    fun PhotoCarousel(photos: List<String>, modifier: Modifier = Modifier){
        val pagerState = rememberPagerState(initialPage = 0){photos.size}
        HorizontalPager(
            state = pagerState,
            modifier = modifier.width(128.dp)
        ){ page: Int ->
            val bitmapState = getImageFromAssets(photos[page])
            if (null != bitmapState) {
                val bitmap = bitmapState.asImageBitmap()
                Image(
                    bitmap,
                    contentDescription = "example photo",
                    modifier.size(128.dp, 128.dp)
                )
            }
        }
    }

    @Composable
    fun MainCardItem(product: Product, type: CardType, modifier: Modifier, onClick: () -> Unit = {}){
        AppTheme {
            val colors = when(type){
                CardType.Common -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
                CardType.Liked -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
                CardType.LikedDiscount, CardType.Discount-> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
            }
            Card(shape = RoundedCornerShape(16), colors = colors, modifier = modifier, onClick = onClick){
                Box(modifier = Modifier.padding(16.dp)){
                    Column {
                        Box{
                            Box(contentAlignment = Alignment.Center) {
                                PhotoCarousel(
                                    product.images, modifier = Modifier
                                        .padding(bottom = 5.dp)
                                        .clip(
                                            RoundedCornerShape(16)
                                        )
                                )
                            }
                            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.size(128.dp)){
                                val (icon, color) = when(type){
                                    CardType.Common, CardType.Discount -> listOf(Icons.Outlined.FavoriteBorder, Color.White)
                                    CardType.Liked, CardType.LikedDiscount -> listOf(Icons.Filled.Favorite, Color.Red)
                                }
                                Icon(imageVector = icon as ImageVector, tint = color as Color, contentDescription = null, modifier = Modifier.padding(16.dp))
                            }
                        }
                        Column {
                            Text("${product.price} Р", fontWeight = AppTypography.titleSmall.fontWeight)
                            Text(product.name, modifier = Modifier.width(128.dp), fontWeight = AppTypography.bodySmall.fontWeight, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

    }

    //@Preview
    @Composable
    fun MainCardPreview(){
        MainCardItem(
            DataMobile().listProduct!![0],
            CardType.LikedDiscount,
            modifier = Modifier
        )
    }

    @Composable
    fun getImageFromAssets(name: String): Bitmap? {
        var bitmapState by remember{ mutableStateOf<Bitmap?>(null) }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            bitmapState = BitmapFactory.decodeStream(context.assets.open(name))
        }
        return bitmapState
    }

    @Composable
    fun CategoryItem(category: CategoryProduct){
        val bitmapState = getImageFromAssets(category.image)
        AppTheme{
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ){
                Box(modifier = Modifier.padding(4.dp)) {
                    Column {
                        if (null != bitmapState) {
                            val bitmap = bitmapState.asImageBitmap()
                            Image(
                                bitmap = bitmap,
                                "assetsImage",
                                modifier = Modifier.size(100.dp),
                                colorFilter = null
                            )
                            Text(category.name, modifier = Modifier.width(140.dp))
                        }
                    }
                }
            }
        }
    }

    //@Preview
    @Composable
    fun CategoryPreview(){
        CategoryItem(DataMobile().listCategoryProduct!![0])
    }

    @Composable
    fun CategoryBar(categoryList: List<CategoryProduct>){
        AppTheme {
            Card(modifier = Modifier.padding(10.dp)){
                Text("Категории", fontSize = AppTypography.titleLarge.fontSize, modifier = Modifier.padding(top = 16.dp, start = 16.dp))
                LazyRow(contentPadding = PaddingValues(10.dp)) {
                    items(categoryList) { category ->
                        CategoryItem(category)
                    }
                }
            }
        }
    }

    //@Preview
    @Composable
    fun CategoryBarPreview(){
        CategoryBar(DataMobile().listCategoryProduct!!)
    }

    @Preview(showSystemUi = true)
    @Composable
    fun MainScreenPreview(){
        AppTheme {
            Column {
                CategoryBar(DataMobile().listCategoryProduct!!)
                Card(modifier = Modifier.padding(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)){
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(DataMobile().listProduct!!) { product ->
                            MainCardItem(product, CardType.Common, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}

