package com.youkydesign.sakeca.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youkydesign.sakeca.designsystem.R as brandColors

@Composable
fun IngredientItem(
    modifier: Modifier = Modifier,
    name: String,
    toggleCheck: (state: Boolean) -> Unit,
) {
    var state by rememberSaveable { mutableStateOf<Boolean>(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                state = !state
                toggleCheck(state)
            }
            .background(colorResource(id = brandColors.color.tw_slate_100))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(onClick = {
            state = !state
            toggleCheck(state)
        }) {
            Icon(
                imageVector = if (state) Icons.Filled.CheckCircle else Icons.Outlined.Check,
                contentDescription = "Add",
                tint = if (state) colorResource(brandColors.color.tw_emerald_500) else colorResource(
                    brandColors.color.tw_slate_400
                )
            )
        }
        Text(
            name,
            style = TextStyle(
                color = if (state) colorResource(brandColors.color.tw_emerald_500) else colorResource(
                    brandColors.color.tw_slate_400
                ),
                fontWeight = if (state) FontWeight.Bold else FontWeight.Normal,
            )
        )
    }
}

@Preview(showBackground = false)
@Composable
fun IngredientItemPreview() {
    IngredientItem(
        name = "Sugar",
        toggleCheck = {}
    )
}
