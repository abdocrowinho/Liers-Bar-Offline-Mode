@file:Suppress("UNUSED_EXPRESSION")

package com.example.liersbarofflinemode.ui.composable.PlayersNameScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.composable.PlayersNameScreen.Composable.ButtonsRow
import com.example.liersbarofflinemode.ui.composable.PlayersNameScreen.Composable.TextFieldsRow
import com.example.liersbarofflinemode.ui.theme.red_orange
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.liersbarofflinemode.ui.Intent.EnterScreenIntent
import com.example.liersbarofflinemode.ui.Utltiy.ArgumentsKeys
import com.example.liersbarofflinemode.ui.ViewModels.EnterUsersScreenViewModel
import com.example.liersbarofflinemode.ui.models.TextFieldModel
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import com.example.liersbarofflinemode.ui.theme.warm_peach

@Composable
fun EnterUsersScreen(
    modifier: Modifier,
    viewModel: EnterUsersScreenViewModel,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    var user1 by remember { mutableStateOf("") }
    var user2 by remember { mutableStateOf("") }
    var user3 by remember { mutableStateOf("") }
    var user4 by remember { mutableStateOf("") }
    var userNameError1 by remember { mutableStateOf<String?>(null) }
    var userNameError2 by remember { mutableStateOf<String?>(null) }
    var userNameError3 by remember { mutableStateOf<String?>(null) }
    var userNameError4 by remember { mutableStateOf<String?>(null) }
    val listofTextFields = listOf(
        TextFieldModel(
            text = user1, isError = false,
            onTextChange = { newText -> user1 = newText },
            supportingText = { Text(text = userNameError1 ?:" ", color = Color.Red)  },
        ),
        TextFieldModel(
            text = user2, isError = false,
            onTextChange = { newText -> user2 = newText },
            supportingText = { Text(text = userNameError2 ?:" ", color = Color.Red)  },
        ),
        TextFieldModel(
            text = user3, isError = false,
            onTextChange = { newText -> user3 = newText },
            supportingText =  { Text(text = userNameError3 ?:" ", color = Color.Red)  },
        ),
        TextFieldModel(
            text = user4, isError = false,
            onTextChange = { newText -> user4 = newText },
            supportingText =  {Text(text = userNameError4 ?:" " , color = Color.Red , maxLines = 2, ) }
        ),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.multiple_players_screen),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .offset(x = GetWidthConf() * .025f)
        ) {
            Spacer(modifier = Modifier.height(GetHeightConf() * .3f))
            Text(
                text = "enter your names",
                color = red_orange,
                fontWeight = FontWeight(weight = 1000)
            )
            Spacer(modifier = Modifier.height(GetHeightConf() * .12f))
            TextFieldsRow(
                modifier = modifier, fields = listofTextFields
            )
            ButtonsRow(modifier, action1 = {
                viewModel.handleIntent(EnterScreenIntent.StartIntent(
                    user1, user2, user3, user4
                ) { errorFeild, msg ->
                    when (errorFeild) {
                        1 -> userNameError1 = msg
                        2 -> userNameError2 = msg
                        3 -> userNameError3 = msg
                        4 -> userNameError4 = msg
                    }

                }
                )


            }, action2 = {})

Spacer(modifier = Modifier.fillMaxHeight())
           
            }
        }
    LaunchedEffect(key1 = state) {
        if (state.players.isNotEmpty() && state.errors == null) {
            val players = state.players
            navController.currentBackStackEntry?.savedStateHandle?.set(
                ArgumentsKeys.PLAYERS_KEY,
                players
            )
            navController.navigate(NavigationItem.GamePlayScreen.route)
        } else {
            val errors = state.errors
            userNameError1 = errors?.get(0)
            userNameError2 = errors?.get(1)
            userNameError3 = errors?.get(2)
            userNameError4 = errors?.get(3)
        }
    }


}



