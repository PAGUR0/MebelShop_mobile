package com.mebelshop.mebelshop_mobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mebelshop.mebelshop_mobile.ar.ARScreen
import com.mebelshop.mebelshop_mobile.model.MainModel
import com.mebelshop.mebelshop_mobile.model.SelectedModels
import com.mebelshop.mebelshop_mobile.ui.theme.AppTheme
import com.mebelshop.mebelshop_mobile.ui.theme.AppTypography
import com.mebelshop.mebelshop_mobile.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = MainViewModel(MainModel())
        viewModel.getProducts()
        viewModel.filterProducts(CategoryProduct("Все категории", image = "image/all.png"))
        setContent {
            AppTheme(dynamicColor = false){
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main_screen") {
                    composable("main_screen") {
                        MainScreen(viewModel, navController)
                    }
                    composable("ar_screen") {
                        ARScreen()
                    }
                }
            }
        }
    }

    enum class CardType {
        Common, Liked, Discount, LikedDiscount
    }

    @Composable
    fun PhotoCarousel(photos: List<String>, modifier: Modifier = Modifier) {
        val pagerState = rememberPagerState(initialPage = 0) { photos.size }
        HorizontalPager(
            state = pagerState,
            modifier = modifier.width(256.dp)
        ) { page: Int ->
            val bitmapState = getImageFromAssets(photos[page])
            bitmapState?.let {
                val bitmap = it.asImageBitmap()
                Image(
                    bitmap,
                    contentDescription = "example photo",
                    modifier = Modifier.size(150.dp)
                )
            }
        }
    }

    @Composable
    fun MainCardItem(product: Product, type: CardType, modifier: Modifier, showCard: MutableState<Boolean>, showedCard: MutableState<Product>) {
        AppTheme {
            val colors = when (type) {
                CardType.Common -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
                CardType.Liked -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
                CardType.LikedDiscount, CardType.Discount -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
            }
            Card(shape = RoundedCornerShape(16), colors = colors, modifier = modifier, onClick = {
                showedCard.value = product
                showCard.value = true
            }) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Column {
                        Box {
                            Box(contentAlignment = Alignment.Center) {
                                PhotoCarousel(
                                    product.images, modifier = Modifier
                                        .padding(bottom = 5.dp)
                                        .clip(RoundedCornerShape(16))
                                )
                            }
                            Box(
                                contentAlignment = Alignment.TopEnd,
                                modifier = Modifier.size(150.dp)
                            ) {
                                val (icon, color) = when (type) {
                                    CardType.Common, CardType.Discount -> listOf(Icons.Outlined.FavoriteBorder, Color.White)
                                    CardType.Liked, CardType.LikedDiscount -> listOf(Icons.Filled.Favorite, Color.Red)
                                }
                                Icon(
                                    imageVector = icon as ImageVector,
                                    tint = color as Color,
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                "${product.price} Р",
                                fontWeight = AppTypography.titleSmall.fontWeight
                            )
                            Text(
                                product.name,
                                modifier = Modifier.width(256.dp),
                                fontWeight = AppTypography.bodySmall.fontWeight,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun getImageFromAssets(name: String): Bitmap? {
        var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
        val context = LocalContext.current

        LaunchedEffect(name) {
            bitmapState = BitmapFactory.decodeStream(context.assets.open(name))
        }
        return bitmapState
    }

    @Composable
    fun CategoryItem(category: CategoryProduct, viewModel: MainViewModel) {
        val bitmapState = getImageFromAssets(category.image)
        AppTheme {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                onClick = {
                    viewModel.filterProducts(category)
                }
            ) {
                Box(modifier = Modifier.padding(4.dp)) {
                    Column {
                        bitmapState?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "assetsImage",
                                modifier = Modifier.size(100.dp)
                            )
                            Text(category.name, modifier = Modifier.width(140.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CategoryBar(categoryList: List<CategoryProduct>, viewModel: MainViewModel) {
        AppTheme {
            Card(modifier = Modifier.padding(8.dp)) {
                Text(
                    "Категории",
                    fontSize = AppTypography.titleLarge.fontSize,
                    modifier = Modifier.padding(top = 2.dp, start = 16.dp)
                )
                LazyRow(contentPadding = PaddingValues(10.dp)) {
                    items(listOf(CategoryProduct("Все категории", image = "image/all.png")) + categoryList) { category ->
                        CategoryItem(category, viewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen(viewModel: MainViewModel, navController: NavController) {
        val filteredProducts by viewModel.filteredProducts.collectAsState(emptyList())
        val products = remember { mutableStateListOf<Product>() }

        LaunchedEffect(filteredProducts) {
            products.clear()
            products.addAll(filteredProducts)
        }

        AppTheme {
            val showedCard = remember { mutableStateOf(DataMobile().listProduct!![0]) }
            val showCard = remember { mutableStateOf(false) }
            Scaffold(
                floatingActionButton = {
                    Button(onClick = { navController.navigate("ar_screen") }) {
                        Text("AR")
                    }
                },
                floatingActionButtonPosition = FabPosition.Center
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    CategoryBar(DataMobile().listCategoryProduct!!,viewModel)
                    Card(
                        modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(products) { product ->
                                MainCardItem(
                                    product,
                                    CardType.Common,
                                    modifier = Modifier.padding(8.dp),
                                    showCard,
                                    showedCard
                                )
                            }
                        }
                    }
                }
            }
            if (showCard.value) {
                ProductCard(showedCard.value, showCard)
            }
        }
    }


    @Composable
    fun ProductCard(product: Product, isActive: MutableState<Boolean>) {
        val bitmapState = getImageFromAssets(product.images[0])
        var showAr by remember { mutableStateOf(false) }

        AppTheme {
            if (showAr) {

            } else {
                Card() {
                    Scaffold(
                        topBar = {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ElevatedButton(onClick = {
                                        isActive.value = false
                                    }) { Text("Назад") }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.padding(end = 16.dp)
                                    ) {
                                        Icon(Icons.Filled.FavoriteBorder, null,)
                                        Icon(Icons.Filled.Share, null)
                                    }
                                }
                                HorizontalDivider()
                            }
                        },
                        bottomBar = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = {},
                                    modifier = Modifier.fillMaxWidth(0.45f)
                                ) {
                                    Text("Купить", fontSize = AppTypography.bodyLarge.fontSize)
                                }
                                Button(onClick = {
                                    SelectedModels.addModel(product)
                                }, modifier = Modifier.fillMaxWidth(0.9f)) {
                                    Text(
                                        "Добавить в AR",
                                        fontSize = AppTypography.bodyLarge.fontSize
                                    )
                                }
                            }
                        }
                    ) { padding ->
                        Box(
                            modifier = Modifier.padding(padding),
                            contentAlignment = Alignment.TopStart
                        ) {
                            LazyColumn {
                                if (null != bitmapState) {
                                    val bitmap = bitmapState.asImageBitmap()
                                    item {
                                        Image(
                                            bitmap = bitmap,
                                            "assetsImage",
                                            modifier = Modifier.size(393.dp),
                                            colorFilter = null
                                        )
                                    }
                                }
                                item {
                                    ElevatedCard(
                                        modifier = Modifier.padding(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                                    ) {
                                        Column {

                                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                                                Text(
                                                    product.shop.name_shop,
                                                    modifier = Modifier.padding(2.dp)
                                                )
                                            }
                                            Text(
                                                product.name,
                                                fontSize = AppTypography.headlineMedium.fontSize,
                                                modifier = Modifier.padding(2.dp)
                                            )
                                        }
                                    }
                                }
                                item {
                                    ElevatedCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            ExpandableText(
                                                text = product.description,
                                                showMoreText = "...->",
                                                showLessText = "<-",
                                                fontStyle = AppTypography.bodySmall.fontStyle
                                            )
                                            Text(
                                                "Характеристики",
                                                fontStyle = AppTypography.bodyLarge.fontStyle,
                                                modifier = Modifier.padding(vertical = 6.dp)
                                            )
                                            Column {
                                                for (attr in product.attributes) {
                                                    Column {
                                                        Row(
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                            Text(attr.key)
                                                            Text(attr.value)
                                                        }
                                                        HorizontalDivider()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun ExpandableText(
        modifier: Modifier = Modifier,
        textModifier: Modifier = Modifier,
        style: TextStyle = LocalTextStyle.current,
        fontStyle: FontStyle? = null,
        text: String,
        collapsedMaxLine: Int = 2,
        showMoreText: String = "... Show More",
        showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
        showLessText: String = " Show Less",
        showLessStyle: SpanStyle = showMoreStyle,
        textAlign: TextAlign? = null
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        var clickable by remember { mutableStateOf(false) }
        var lastCharIndex by remember { mutableStateOf(0) }
        Box(modifier = Modifier
            .clickable(clickable) {
                isExpanded = !isExpanded
            }
            .then(modifier)
        ) {
            Text(
                modifier = textModifier
                    .fillMaxWidth()
                    .animateContentSize(),
                text = buildAnnotatedString {
                    if (clickable) {
                        if (isExpanded) {
                            append(text)
                            withStyle(style = showLessStyle) { append(showLessText) }
                        } else {
                            val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                                .dropLast(showMoreText.length)
                                .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                            append(adjustText)
                            withStyle(style = showMoreStyle) { append(showMoreText) }
                        }
                    } else {
                        append(text)
                    }
                },
                maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
                fontStyle = fontStyle,
                onTextLayout = { textLayoutResult ->
                    if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                        clickable = true
                        lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                    }
                },
                style = style,
                textAlign = textAlign
            )
        }

    }
}

