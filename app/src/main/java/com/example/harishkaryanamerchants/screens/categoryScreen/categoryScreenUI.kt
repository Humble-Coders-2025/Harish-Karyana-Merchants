package com.example.harishkaryanamerchants.screens.categoryscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.abs

private val PrimaryPurple = Color(0xFF6D2773)
private val LightPurple = Color(0xFFE6DAEB)
private val DarkGrayText = Color(0xFF5A5A5A)
private val LightGrayDivider = Color(0xFFDDDDDD)

data class Category(val name: String)
data class Product(val name: String, val category: String)

@Composable
fun GroceryScreen() {
    val categories = listOf(
        Category("Grocery"), Category("Dairy & Beverages"), Category("Packaged Food"),
        Category("Fruits & Vegetables"), Category("Home & Kitchen"),
        Category("Personal Care & Beauty"), Category("Baby Care"),
        Category("Home Utility & Organisers"), Category("Electronics & Appliances"),
        Category("Footwear"), Category("School Supplies")
    )

    val products = listOf(
        Product("Dals", "Grocery"), Product("Pulses", "Grocery"),
        Product("Milk", "Dairy & Beverages"), Product("Cheese", "Dairy & Beverages"),
        Product("Instant Noodles", "Packaged Food"), Product("Chips", "Packaged Food"),
        Product("Fresh Vegetables", "Fruits & Vegetables"), Product("Fruits", "Fruits & Vegetables"),
        Product("Cleaning Supplies", "Home & Kitchen"), Product("Cookware", "Home & Kitchen")
    )

    var selectedCategory by remember { mutableStateOf(categories.first()) }
    val categoryListState = rememberLazyListState()
    val productListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // State to control scroll animation speed and prevent feedback loops
    var isScrollingCategories by remember { mutableStateOf(false) }
    var isScrollingProducts by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxHeight()
        ) {
            CategorySidebar(
                categories = categories,
                selectedCategory = selectedCategory,
                scrollState = categoryListState,
                onCategorySelected = { category ->
                    val categoryIndex = categories.indexOf(category)
                    selectedCategory = category

                    // Set flag to avoid feedback loop
                    isScrollingCategories = true

                    coroutineScope.launch {
                        // Slow, smooth scroll to product section
                        val targetIndex = categoryIndex * 2
                        val currentIndex = productListState.firstVisibleItemIndex
                        val distance = abs(targetIndex - currentIndex)

                        // Adjust duration based on distance - longer distance = slower scroll
                        val scrollDuration = (150L * distance).coerceAtLeast(300L)

                        animateScrollToItem(
                            lazyListState = productListState,
                            targetIndex = targetIndex,
                            duration = scrollDuration
                        )

                        // Reset flag after scrolling completes with a delay
                        delay(500)
                        isScrollingCategories = false
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .weight(2.25f)
                .fillMaxHeight()
        ) {
            CategorizedProductList(
                products = products,
                categories = categories,
                scrollState = productListState,
                isScrollingFromCategoryClick = isScrollingCategories,
                onFirstVisibleCategoryChange = { index ->
                    // Only update category selection if not currently scrolling from a category click
                    if (!isScrollingCategories && index in categories.indices) {
                        isScrollingProducts = true
                        selectedCategory = categories[index]

                        coroutineScope.launch {
                            // Slower scroll for category list
                            animateScrollToItem(
                                lazyListState = categoryListState,
                                targetIndex = index,
                                duration = 600L // Slower scroll animation
                            )

                            // Reset flag after scrolling completes
                            delay(500)
                            isScrollingProducts = false
                        }
                    }
                }
            )
        }
    }
}

// Custom animation function using Kotlin Coroutines
suspend fun animateScrollToItem(
    lazyListState: LazyListState,
    targetIndex: Int,
    duration: Long = 500L
) {
    val startIndex = lazyListState.firstVisibleItemIndex
    val startOffset = lazyListState.firstVisibleItemScrollOffset
    val startTime = System.currentTimeMillis()

    // Basic physics-based easing
    fun ease(x: Float): Float {
        return (1 - (1 - x) * (1 - x)) // Ease out quad
    }

    while (true) {
        val elapsedTime = System.currentTimeMillis() - startTime
        val progress = (elapsedTime.toFloat() / duration).coerceIn(0f, 1f)
        val easedProgress = ease(progress)

        // Calculate current position based on progress
        val currentIndex = if (startIndex == targetIndex) {
            startIndex
        } else {
            startIndex + ((targetIndex - startIndex) * easedProgress).toInt()
        }

        // Calculate scroll offset (gradually reduce to 0)
        val currentOffset = (startOffset * (1 - easedProgress)).toInt()

        lazyListState.scrollToItem(index = currentIndex, scrollOffset = currentOffset)

        if (progress >= 1f) break
        delay(16) // ~60fps
    }

    // Make sure we land exactly at the target
    lazyListState.scrollToItem(targetIndex)
}

@Composable
fun CategorySidebar(
    categories: List<Category>,
    selectedCategory: Category,
    scrollState: LazyListState,
    onCategorySelected: (Category) -> Unit
) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxHeight()
    ) {
        items(categories) { category ->
            Text(
                text = category.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategorySelected(category) }
                    .padding(12.dp),
                fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal,
                color = if (category == selectedCategory) PrimaryPurple else DarkGrayText
            )
            Divider(color = LightGrayDivider, thickness = 1.dp)
        }
    }
}

@Composable
fun CategorizedProductList(
    products: List<Product>,
    categories: List<Category>,
    scrollState: LazyListState,
    isScrollingFromCategoryClick: Boolean,
    onFirstVisibleCategoryChange: (Int) -> Unit
) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize()
    ) {
        categories.forEachIndexed { index, category ->
            item {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(16.dp)
                )
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    items(products.filter { it.category == category.name }) { product ->
                        ProductGridItem(product)
                    }
                }
            }
        }
    }

    // Track scroll position changes with throttling to avoid too many updates
    var lastFirstVisibleCategoryTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        if (!isScrollingFromCategoryClick) {
            val currentTime = System.currentTimeMillis()

            // Throttle updates to avoid rapid changes (every 200ms)
            if (currentTime - lastFirstVisibleCategoryTime > 200) {
                lastFirstVisibleCategoryTime = currentTime
                val firstVisibleCategory = scrollState.firstVisibleItemIndex / 2
                onFirstVisibleCategoryChange(firstVisibleCategory)
            }
        }
    }
}

@Composable
fun ProductGridItem(product: Product) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = product.name,
                textAlign = TextAlign.Center
            )
        }
    }
}