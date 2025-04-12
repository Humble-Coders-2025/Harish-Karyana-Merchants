package com.example.harishkaryanamerchants.screens.homeScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.with
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.harishkaryanamerchants.R
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.abs
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen() {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Transparent
    val useDarkIcons = false

    DisposableEffect(systemUiController, statusBarColor) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }

    val scrollState = rememberScrollState()
    val headerHeightPx = remember { mutableStateOf(0) }
    val defaultHeaderHeight = 160.dp
    val headerHeight = with(LocalDensity.current) {
        if (headerHeightPx.value > 0) headerHeightPx.value.toDp() else defaultHeaderHeight
    }

    // State for bottom bar visibility
    var isBottomBarVisible by remember { mutableStateOf(true) }
    var lastScrollPosition by remember { mutableStateOf(0) }

    // Handle scroll events to show/hide bottom bar
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollPosition ->
                val scrollDelta = scrollPosition - lastScrollPosition
                when {
                    // Scrolling down - hide the bar
                    scrollDelta > 10 && isBottomBarVisible -> {
                        isBottomBarVisible = false
                    }
                    // Scrolling up - show the bar
                    scrollDelta < -10 && !isBottomBarVisible -> {
                        isBottomBarVisible = true
                    }
                    // At top - always show
                    scrollPosition <= 0 -> {
                        isBottomBarVisible = true
                    }
                }
                lastScrollPosition = scrollPosition
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Layer 1 (bottom): Scrollable background image starting after the header
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = headerHeight)
                .verticalScroll(scrollState)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.homescreenbackground),
                    contentDescription = "Background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .offset(y = (-headerHeight).value.dp),
                    contentScale = ContentScale.FillBounds
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(800.dp)
                        .background(Color.Transparent)
                )
            }
        }

        // Layer 2: Main content without bottom navigation
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars,
            containerColor = Color.Transparent,
            bottomBar = {} // Empty bottom bar - we'll add our custom one later
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = contentPadding.calculateBottomPadding())
            ) {
                // Layer 2A: Scrollable content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = headerHeight)
                        .verticalScroll(scrollState)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DiscountCarousel()

                        Text(
                            text = "Categories",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            )
                        )
                        CategoriesSection()

                        Divider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = Color.LightGray,
                            thickness = 1.dp
                        )

                        SpecialDealSection()

                        Spacer(modifier = Modifier.height(16.dp))

                        DiscountCarousel()

                        Text(
                            text = "Categories",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            )
                        )
                        CategoriesSection()

                        Divider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = Color.LightGray,
                            thickness = 1.dp
                        )

                        SpecialDealSection()

                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }

                // Layer 3 (top): Fixed header with background image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(defaultHeaderHeight)
                        .onSizeChanged { size ->
                            headerHeightPx.value = size.height
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.homescreenbackground),
                        contentDescription = "Header Background",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RectangleShape)
                            .align(Alignment.TopStart),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                    ) {
                        TopSection()
                        LocationBar()
                    }
                }
            }
        }



        // Layer 4: Animated Floating Bottom Navigation Bar
        AnimatedVisibility(
            visible = isBottomBarVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
                .zIndex(1f)
        ) {
            Box {
                // Blurred background only (no transparency)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp) // Height of bar + padding
                        .blur(radius = 8.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .background(Color.Transparent)
                )

                // Opaque navigation bar
                BottomNavigationBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(35.dp),
                            clip = false
                        )
                )
            }
        }
    }
}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search for pulses, cereals, brea...") },
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFF7B2CBF),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Account Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Account",
                tint = Color.White
            )
        }
    }
}


@Composable
fun LocationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = Color.White
        )

        Text(
            text = "Sent to",
            color = Color.White,
            modifier = Modifier.padding(start = 4.dp)
        )

        Text(
            text = "Pamulang Barat Residence No.5, RT 05/...",
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Expand",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DiscountCarousel() {
    var currentPage by remember { mutableStateOf(0) }
    val pageCount = 5
    var isAutoScrolling by remember { mutableStateOf(true) }
    var swipeDirection by remember { mutableStateOf(SwipeDirection.NONE) }
    var offsetX by remember { mutableStateOf(0f) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val animatedOffset by animateFloatAsState(
        targetValue = if (swipeDirection == SwipeDirection.NONE) 0f else offsetX,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "offsetAnimation"
    )

    val carouselItems = remember {
        listOf(
            CarouselItem("Discount", "25%", "All Snacks and Drinks", Color(0xFF7B2CBF), "Snacks"),
            CarouselItem("Special Offer", "40%", "Fresh Produce", Color(0xFF2C7BBF), "Vegetables"),
            CarouselItem("Sale", "30%", "Kitchen Appliances", Color(0xFF2CBF7B), "Appliances"),
            CarouselItem("Limited Time", "50%", "Personal Care Items", Color(0xFFBF2C7B), "Care Items"),
            CarouselItem("Clearance", "60%", "Home Decor", Color(0xFFBF7B2C), "Home Items")
        )
    }

    // Auto-scrolling effect
    LaunchedEffect(key1 = isAutoScrolling) {
        if (isAutoScrolling) {
            while (isAutoScrolling) {
                delay(3000)
                swipeDirection = SwipeDirection.LEFT
                currentPage = (currentPage + 1) % pageCount
            }
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(180.dp)
                .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.2f))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isAutoScrolling = false },
                        onDragEnd = {
                            val threshold = screenWidth.value * 0.2f
                            if (abs(offsetX) > threshold) {
                                if (offsetX > 0) {
                                    swipeDirection = SwipeDirection.RIGHT
                                    currentPage = (currentPage - 1 + pageCount) % pageCount
                                } else {
                                    swipeDirection = SwipeDirection.LEFT
                                    currentPage = (currentPage + 1) % pageCount
                                }
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX += dragAmount
                        }
                    )
                }
        ) {
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    when (swipeDirection) {
                        SwipeDirection.LEFT -> {
                            slideInHorizontally { it } with slideOutHorizontally { -it }
                        }
                        SwipeDirection.RIGHT -> {
                            slideInHorizontally { -it } with slideOutHorizontally { it }
                        }
                        else -> {
                            slideInHorizontally { it } with slideOutHorizontally { -it }
                        }
                    }
                }
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(carouselItems[page].backgroundColor)
                        .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                ) {
                    CarouselContent(carouselItems[page])
                }
            }
        }

        // Carousel Indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { index ->
                val isSelected = index == currentPage
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF7B2CBF) else Color.LightGray)
                        .clickable {
                            swipeDirection = if (index > currentPage) SwipeDirection.LEFT else SwipeDirection.RIGHT
                            isAutoScrolling = false
                            currentPage = index
                        }
                )
            }
        }
    }
}
enum class SwipeDirection {
    LEFT, RIGHT, NONE
}


@Composable
fun CarouselContent(item: CarouselItem) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Discount Text Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp)
        ) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = item.highlightText,
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-2).sp,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = item.subtitle,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif
            )

            Button(
                onClick = { /* No action */ },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(36.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD966)
                )
            ) {
                Text(
                    text = "See Detail",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        // Product Images
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            // Placeholder for product image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFD966)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.imageDescription,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7B2CBF)
                )
            }
        }
    }
}

// Data class for carousel items
data class CarouselItem(
    val title: String,
    val highlightText: String,
    val subtitle: String,
    val backgroundColor: Color,
    val imageDescription: String
)

data class Category(
    val name: String,
    val imageResId: Int // This would be your R.drawable.image_id
)

@Composable
fun CategoriesSection() {
    // For demo purposes, we'll use placeholder categories
    val categories = listOf(
        Category("Pulses & Cereals", 0),
        Category("Breads & Bakery", 0),
        Category("Beauty & Personal care", 0),
        Category("Household essentials", 0),
        Category("Snacks & Drinks", 0)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category)
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        // Category Icon - Rounded Square
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp)) // Rounded corners instead of circle
                .background(Color(0xFFF3E5F5)) // Light purple background
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Replace with actual image resource
            // Image(
            //     painter = painterResource(id = category.imageResId),
            //     contentDescription = category.name,
            //     modifier = Modifier.fillMaxSize()
            // )

            // Placeholder Icon
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = category.name,
                tint = Color(0xFF7B2CBF),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Name
        Text(
            text = category.name,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
fun SpecialDealSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SpecialDealHeader()
        SpecialDealItems() // This calls the SpecialDealItems() function below
    }
}

@Composable
fun SpecialDealHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Special Deal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        TextButton(onClick = { /* No action */ }) {
            Text(
                text = "See more",
                color = Color(0xFF7B2CBF),
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "See more",
                tint = Color(0xFF7B2CBF)
            )
        }
    }
}

@Composable
fun SpecialDealItems() {
    // Sample deal items
    val dealItems = listOf(
        "Beauty Products",
        "Home Essentials",
        "Medications",
        "Electronics",
        "Fresh Food"
    )

    // Special Deal Products in LazyRow
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dealItems) { dealItem ->
            SpecialDealItem(dealItem)
        }
    }
}

@Composable
fun SpecialDealItem(title: String) {
    // Special Deal Item
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(35.dp))
            .background(Color.White.copy(alpha = 1f)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Default.Home,
            label = "Home",
            isSelected = true
        )

        BottomNavItem(
            icon = Icons.Default.KeyboardArrowDown,
            label = "Order Again",
            isSelected = false
        )

        BottomNavItem(
            icon = Icons.Default.Menu,
            label = "Categories",
            isSelected = false
        )

        BottomNavItem(
            icon = Icons.Default.ShoppingCart,
            label = "Cart",
            isSelected = false,
            badgeCount = 1
        )
    }
}
@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) Color(0xFF7B2CBF) else Color.Gray
            )

            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .offset(x = 8.dp, y = (-8).dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7B2CBF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = if (isSelected) Color(0xFF7B2CBF) else Color.Gray,
            fontSize = 12.sp
        )
    }
}