package itson.appsmoviles.mypokedex_mongeeliana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonListView: ListView
    private lateinit var pokemonAdapter: PokemonAdapter
    private val pokemonList = mutableListOf<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonListView = findViewById(R.id.pokemonList)
        pokemonAdapter = PokemonAdapter(this, pokemonList)
        pokemonListView.adapter = pokemonAdapter

        val addPokemonButton: Button = findViewById(R.id.addPokemon)
        addPokemonButton.setOnClickListener {
            val intent = Intent(this, RegisterPokemonActivity::class.java)
            startActivity(intent)
        }

        loadPokemons()
    }

    override fun onResume() {
        super.onResume()
        loadPokemons()
    }

    private fun loadPokemons() {
        PokemonRepository.getAllPokemons(
            onSuccess = { list ->
                pokemonList.clear()
                pokemonList.addAll(list)
                pokemonAdapter.notifyDataSetChanged()
            },
            onFailure = { exception ->
                Toast.makeText(this, "Error loading Pokémons: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
