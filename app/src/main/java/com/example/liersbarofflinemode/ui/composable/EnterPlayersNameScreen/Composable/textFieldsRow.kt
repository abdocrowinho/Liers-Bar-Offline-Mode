package com.example.liersbarofflinemode.ui.composable.EnterPlayersNameScreen.Composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.liersbarofflinemode.ui.Utltiy.CustomOutLineBorder
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.models.TextFieldModel
import com.example.liersbarofflinemode.ui.theme.red_orange

@Composable
fun TextFieldsRow(
    modifier: Modifier,
    fields: List<TextFieldModel>
) {
    Row (modifier = modifier.padding(bottom = GetHeightConf() *.1f)){
        fields.forEachIndexed { index, textFieldModel ->

            CustomOutLineBorder(
                width = GetWidthConf() * 0.15f,
                radius = 16.dp,
                text = textFieldModel.text,
                onTextChange = textFieldModel.onTextChange,
                isError = textFieldModel.isError,
                supportingText = textFieldModel.supportingText,

            )
            if (index != fields.size.minus(1)) {
                Spacer(modifier = modifier.width(GetWidthConf() * .02f))

            }
        }
    }
}


