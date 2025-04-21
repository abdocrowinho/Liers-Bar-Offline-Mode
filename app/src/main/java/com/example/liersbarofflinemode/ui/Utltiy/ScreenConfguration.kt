package com.example.liersbarofflinemode.ui.Utltiy

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp



@Composable
fun GetHeightConf ():Dp {
    val configuration = LocalConfiguration.current
return   configuration.screenHeightDp.dp

}
@Composable
fun GetWidthConf ():Dp {
    val configuration = LocalConfiguration.current
    return  configuration.screenWidthDp.dp


}
