package itson.appsmoviles.mypokedex_mongeeliana

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

object PokemonRepository {

    private val database = Firebase.database.reference
    private val pokemonsRef = database.child("pokemons")

    fun savePokemon(pokemon: Pokemon) {
        pokemonsRef.push().setValue(pokemon)
    }



    fun getAllPokemons(onSuccess: (List<Map<String, String>>) -> Unit, onFailure: (Exception) -> Unit) {
        pokemonsRef.get()
            .addOnSuccessListener { snapshot ->
                val pokemonList = mutableListOf<Map<String, String>>()
                snapshot.children.forEach { child ->
                    val pokemon = child.value as? Map<String, String>
                    pokemon?.let { pokemonList.add(it) }
                }
                onSuccess(pokemonList)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

}
