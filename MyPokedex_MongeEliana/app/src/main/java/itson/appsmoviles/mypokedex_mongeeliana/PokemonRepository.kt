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

    fun getAllPokemons(
        onSuccess: (List<Pokemon>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        pokemonsRef.get()
            .addOnSuccessListener { snapshot ->
                val pokemonList = mutableListOf<Pokemon>()
                snapshot.children.forEach { child ->
                    val name = child.child("name").getValue(String::class.java) ?: ""
                    val number = child.child("number").getValue(Int::class.java) ?: 0
                    val imageUrl = child.child("imageUrl").getValue(String::class.java) ?: ""

                    val pokemon = Pokemon(name, number, imageUrl)
                    pokemonList.add(pokemon)
                }
                onSuccess(pokemonList)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
