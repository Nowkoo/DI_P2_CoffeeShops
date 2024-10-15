package com.example.coffeeshops

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshops.ui.theme.titulo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopProfile(string: String?, modifier: Modifier) {
    var shop: Shop = getShops().filter { shop -> shop.nombre == string }.first()
    val rvState = rememberLazyStaggeredGridState()
    val currentScope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf {
            rvState.firstVisibleItemIndex > 0
        }
    }

    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val filteredReviews = getReviews().filter { it.contains(text, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = shop.nombre,
            fontFamily = titulo,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        SearchBar(
            query = text,
            onQueryChange = {text = it},
            onSearch = { expanded = false },
//            active = expanded
            active = false,
            onActiveChange = {expanded = it},
            placeholder = { Text("Search reviews...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        ) {}

        ReviewsStaggeredView(rvState, Modifier.weight(1f), filteredReviews)

        if (showButton) {
            Button(onClick = {
                currentScope.launch {
                    rvState.animateScrollToItem(0)
                }
            },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Add new comment")
            }
        }
    }
}

@Composable
fun ReviewsStaggeredView(
    rvState: LazyStaggeredGridState,
    modifier: Modifier,
    reviews: List<String>
) {
    val context = LocalContext.current

    LazyVerticalStaggeredGrid(
        state = rvState,
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        content = {
            items(reviews.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .shadow(10.dp)
                ) {
                    Text(
                        text = reviews[index],
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    )
}


fun getReviews(): List<String> {
    return listOf(
        "¡Un lugar encantador! El ambiente es muy acogedor y perfecto para relajarse.",
        "El café está delicioso, especialmente el capuchino. Las tartas caseras también son un punto fuerte, especialmente la de zanahoria.",
        "El personal es muy amable y siempre están dispuestos a recomendarte algo nuevo. Lo único que podría mejorar es la conexión Wi-Fi, que a veces es un poco lenta.",
        "Este lugar es una joya escondida en el centro de la ciudad.",
        "El café es increíble, y se nota que utilizan granos de muy buena calidad. Me encantó el flat white, estaba perfecto.",
        "El servicio es impecable, te hacen sentir como en casa desde el momento en que entras.",
        "El lugar estaba sucio, con mesas llenas de restos de comida. El personal no parecía muy interesado en atendernos, tuvimos que esperar mucho para que nos trajeran la cuenta. No creo que vuelva.",
        "El lugar es pequeño pero muy acogedor, ideal para una cita o una tarde tranquila con un buen libro.",
        "Es un lugar lindo y con buena ubicación.",
        "Pedí un latte y estaba frío cuando me lo trajeron. Además, el ambiente es bastante ruidoso y no es un lugar cómodo para pasar tiempo. El personal fue bastante descortés cuando hice una consulta sobre el menú. Definitivamente no lo recomiendo.",
        "El café está bien, aunque no es el mejor que he probado en la ciudad. Lo que más me gustó fue el diseño del local, tiene un estilo moderno y cómodo.",
        "El servicio puede ser un poco lento, sobre todo cuando está lleno. La música a veces está demasiado alta, lo que no lo hace el mejor sitio para trabajar o estudiar.",
        "Una cafetería muy cálida y acogedora, perfecta para una mañana tranquila.",
        "La limpieza del lugar es un problema; las mesas estaban sucias y el baño estaba en mal estado. No es lo que esperaba por el precio que pagué.",
        "El café es excelente, con varias opciones para todos los gustos. Recomiendo mucho el café con leche de almendras.",
        "El menú de brunch es delicioso, especialmente los croissants y los bowls de frutas.",
        "El personal es muy atento y siempre te reciben con una sonrisa. Un lugar perfecto para desconectar.",
        "Tenía muchas expectativas por las buenas opiniones, pero la experiencia fue bastante decepcionante. El café estaba demasiado amargo y parecía recalentado.",
        "No entiendo las buenas críticas que tiene este sitio. El café es caro para lo que ofrecen, y el sabor es mediocre.",
        "El lugar tiene potencial, pero la calidad del café deja mucho que desear. Pedí un capuchino y la leche estaba quemada, lo que arruinó completamente el sabor."
    )
}