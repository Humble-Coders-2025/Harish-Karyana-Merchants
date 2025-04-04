package com.example.harishkaryanamerchants.screens.productScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.harishkaryanamerchants.R
import com.example.harishkaryanamerchants.ui.theme.darkPurpleColor
import com.example.harishkaryanamerchants.ui.theme.lightPurpleColor

@Composable
fun ProductDetailScreen() {
    var quantity by remember { mutableStateOf(2) }
    var isFavourite by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightPurpleColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top icons (fixed at the top)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightPurpleColor)
                    .padding(start = 8.dp, top = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* Handle back navigation */ }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                IconButton(onClick = { isFavourite = !isFavourite }) {
                    Icon(
                        if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Black
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightPurpleColor)
                    .padding(top = 0.dp, bottom = 10.dp)
                    .zIndex(0f) // Lower zIndex
            ) {
                Image(
                    painter = painterResource(id = R.drawable.productimage),
                    contentDescription = "Tata Sampann Unpolished Rajma",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Using Box for the scrollable content with the Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Scrollable content section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 40.dp,
                            shape = RoundedCornerShape(topStart = 44.dp, topEnd = 24.dp),
                            spotColor = Color.Black.copy(alpha = 0.2f),
                            ambientColor = Color.Black.copy(alpha = 0.2f)
                        ),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    // Use LazyColumn inside the Card for scrollable content
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                // Category
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(lightPurpleColor)
                                        .padding(horizontal = 6.dp)
                                ) {
                                    Text(
                                        text = "Pulses & Cereals",
                                        color = darkPurpleColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }

                                // Product Name
                                Text(
                                    text = "Tata Sampann\nunpolished Rajma",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                // Price
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = darkPurpleColor
                                                )
                                            ) {
                                                append("Rs 88")
                                            }
                                            append(" / kg ")
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 16.sp,
                                                    textDecoration = TextDecoration.LineThrough,
                                                    color = Color.Gray
                                                )
                                            ) {
                                                append("Rs 93")
                                            }
                                        }
                                    )
                                }

                                // Description Section
                                Text(
                                    text = "Description",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )

                                // Bullet Points
                                BulletPoint("Rajma from Tata Sampann is naturally rich in protein and high in dietary fibre")
                                BulletPoint("Unpolished: Does not undergo any artificial polishing with water, oil or leather thereby retaining its goodness and wholesomeness")
                                BulletPoint("Tata Sampann has rigorous quality checks: Ensures that the grains of dal are uniform in size & colour")

                                // Related Products Section
                                Text(
                                    text = "Related Product",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )

                                // Related Products Row
                                LazyRow (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    item {
                                        RelatedProductItem(
                                            name = "Premium Millet",
                                            price = "Rs 88",
                                            imageRes = R.drawable.productimage,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    item {
                                        RelatedProductItem(
                                            name = "Organic Rice",
                                            price = "Rs 98",
                                            imageRes = R.drawable.productimage,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Add to Cart Section - Fixed at bottom
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color.DarkGray.copy(alpha = 0.2f)
                    ),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity Control
                    QuantitySelector(
                        quantity = quantity,
                        onQuantityChanged = { quantity = it },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Add to Cart Button
                    Button(
                        onClick = { /* Handle add to cart */ },
                        modifier = Modifier
                            .weight(0.5f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkPurpleColor
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Add to Cart",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// Keep the rest of the functions unchanged
@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "• ",
            fontSize = 14.sp,
            color = Color.DarkGray
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun RelatedProductItem(
    name: String,
    price: String,
    imageRes: Int,
    backgroundColor: Color = Color(0xCCC8A6D8),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(15.dp))
            .width(200.dp)
            .padding(end = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 8.dp)
                .background(backgroundColor)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product Info
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Left,
            )

            Text(
                text = price,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = darkPurpleColor,
                modifier = Modifier.padding(top = 2.dp),
                textAlign = TextAlign.Left
            )
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Minus button
        Box(
            modifier = Modifier
                .size(30.dp)
                .border(1.dp, lightPurpleColor, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { if (quantity > 1) onQuantityChanged(quantity - 1) },
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = "-",
                    color = darkPurpleColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Quantity display
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF2F2F7), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quantity.toString(),
                color = darkPurpleColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Plus button
        Box(
            modifier = Modifier
                .size(30.dp)
                .border(1.dp, darkPurpleColor, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { onQuantityChanged(quantity + 1) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase Quantity",
                    tint = darkPurpleColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}