package itson.appsmoviles.mypokedex_mongeeliana

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.swiperefreshlayout.widget.CircularProgressDrawable


class PokemonAdapter(context: Context, private val pokemons: List<Pokemon>) :
    ArrayAdapter<Pokemon>(context, 0, pokemons) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false)

        val pokemon = pokemons[position]

        val nameText = view.findViewById<TextView>(R.id.pokemonName)
        val numberText = view.findViewById<TextView>(R.id.pokemonNumber)
        val imageView = view.findViewById<ImageView>(R.id.pokemonImage)

        nameText.text = pokemon.name
        numberText.text = "No. ${pokemon.number}"
        val circularProgressDrawable = CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        Glide.with(context)
            .load(pokemon.imageUrl)
            .placeholder(circularProgressDrawable)
            .into(imageView)


        return view
    }
}

