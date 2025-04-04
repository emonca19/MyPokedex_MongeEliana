package itson.appsmoviles.mypokedex_mongeeliana

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class PokemonAdapter(context: Context, private val pokemonList: List<Pokemon>) : ArrayAdapter<Pokemon>(context, 0, pokemonList) {

}
