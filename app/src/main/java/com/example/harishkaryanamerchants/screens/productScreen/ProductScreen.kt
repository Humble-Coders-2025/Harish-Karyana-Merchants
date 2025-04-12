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
            // Top navigation bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(0f)
                    .padding(top = 28.dp),
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
                        contentDescription = "Favourite",
                        tint = Color.Black
                    )
                }
            }

            // Product image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(3f),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.productimage),
                    contentDescription = "Tata Sampann Unpolished Rajma",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Content card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
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
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            // Add to Cart Section
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Quantity Selector
                    QuantitySelector(
                        quantity = quantity,
                        onQuantityChanged = { quantity = it }
                    )

                    // Add to Cart Button
                    Button(
                        onClick = { /* Handle add to cart */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .padding(start = 16.dp),
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
            .width(200.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(80.dp)
                .background(backgroundColor)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // Product Info
        Column(
            modifier = Modifier.padding(start = 8.dp),
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
                .border(1.dp, darkPurpleColor, RoundedCornerShape(8.dp))
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
                .background(color = lightPurpleColor, RoundedCornerShape(8.dp))
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