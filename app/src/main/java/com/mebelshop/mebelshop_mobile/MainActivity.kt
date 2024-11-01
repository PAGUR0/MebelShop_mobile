package com.mebelshop.mebelshop_mobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mebelshop.mebelshop_mobile.ar.AR2
import com.mebelshop.mebelshop_mobile.ui.theme.AppTheme
import com.mebelshop.mebelshop_mobile.ui.theme.AppTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
             AppTheme {
                 MainScreen()
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
            modifier = modifier.width(256.dp)
        ){ page: Int ->
            val bitmapState = getImageFromAssets(photos[page])
            if (null != bitmapState) {
                val bitmap = bitmapState.asImageBitmap()
                Image(
                    bitmap,
                    contentDescription = "example photo",
                    modifier.size(150.dp, 150.dp)
                )
            }
        }
    }

    @Composable
    fun MainCardItem(product: Product, type: CardType, modifier: Modifier, onClick: () -> Unit){
        AppTheme {
            val colors = when(type){
                CardType.Common -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
                CardType.Liked -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
                CardType.LikedDiscount, CardType.Discount-> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
            }
            Card(shape = RoundedCornerShape(16), colors = colors, modifier = modifier.clickable { onClick() }){
                Box(modifier = Modifier.padding(8.dp)){
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
                            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.size(150.dp)){
                                val (icon, color) = when(type){
                                    CardType.Common, CardType.Discount -> listOf(Icons.Outlined.FavoriteBorder, Color.White)
                                    CardType.Liked, CardType.LikedDiscount -> listOf(Icons.Filled.Favorite, Color.Red)
                                }
                                Icon(imageVector = icon as ImageVector, tint = color as Color, contentDescription = null, modifier = Modifier.padding(8.dp))
                            }
                        }
                        Column {
                            Text("${product.price} Р", fontWeight = AppTypography.titleSmall.fontWeight)
                            Text(product.name, modifier = Modifier.width(256.dp), fontWeight = AppTypography.bodySmall.fontWeight, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

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
    fun CategoryItem(category: CategoryProduct, showedCategory: MutableState<CategoryProduct>, showCategory: MutableState<Boolean>){
        val bitmapState = getImageFromAssets(category.image)
        AppTheme{
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest), onClick = {
                showedCategory.value = category
                showCategory.value = true
            }){
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



    @Composable
    fun CategoryBar(categoryList: List<CategoryProduct>, showedCategory: MutableState<CategoryProduct>, showCategory: MutableState<Boolean>){
        AppTheme {
            Card(modifier = Modifier.padding(8.dp)){
                Text("Категории", fontSize = AppTypography.titleLarge.fontSize, modifier = Modifier.padding(top = 2.dp, start = 16.dp))
                LazyRow(contentPadding = PaddingValues(10.dp)) {
                    items(categoryList) { category ->
                        CategoryItem(category, showedCategory, showCategory)
                    }
                }
            }
        }
    }


    @Composable
    fun MainScreen(){
        var selectedModelPath by rememberSaveable { mutableStateOf<String?>(null) }

        AppTheme{
            val showedCategory = remember { mutableStateOf(CategoryProduct("0", "0")) }
            val showCategory = remember { mutableStateOf(false) }
            Scaffold(
                topBar = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.Red))
                },
                bottomBar = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.Red))
                }
            ){ padding ->
                Column(modifier = Modifier.padding(padding)) {
                    CategoryBar(DataMobile().listCategoryProduct!!, showedCategory, showCategory)
                    Card(modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)){
                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(DataMobile().listProduct!!) { product ->
                                MainCardItem(product, CardType.Common, modifier = Modifier.padding(8.dp)) {
                                    selectedModelPath = product.model
                                    Log.d("=PATH=", "$selectedModelPath")
                                }
                            }
                        }
                    }
                }
            }
            if(showCategory.value){
                CategoryView(showedCategory.value, showCategory)
            }
            if (selectedModelPath != null) {
                AR2(selectedModelPath) {
                    selectedModelPath = null
                }
            }
        }

    }


    @Composable
    fun CategoryView(category: CategoryProduct, isActive: MutableState<Boolean>){
        var selectedModelPath by rememberSaveable { mutableStateOf<String?>(null) }

        Box(modifier = Modifier.background(Color(0f,0f,0f,0.5f)).fillMaxSize(), contentAlignment = Alignment.Center){
            Card(modifier = Modifier.padding(16.dp)){
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        ElevatedButton(
                            onClick = { isActive.value = false }
                        ) {
                            Text("Назад")
                        }

                        Text(category.name, fontSize = AppTypography.titleLarge.fontSize, modifier = Modifier.padding(end = 8.dp))
                    }
                    HorizontalDivider()
                    val products by remember { mutableStateOf(
                        DataMobile().listProduct!!.filter { product -> category in product.categoryProduct }
                    ) }
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(products) { product ->
                            MainCardItem(product, CardType.Common, modifier = Modifier.padding(8.dp)){
                                selectedModelPath = product.model
                            }
                        }
                    }
                }
            }
        }

        if (selectedModelPath != null) {
            AR2(selectedModelPath) {
                selectedModelPath = null
            }
        }
    }
}

