package itson.appsmoviles.mypokedex_mongeeliana

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback


class RegisterPokemonActivity : AppCompatActivity() {

    private lateinit var etPokemonName: EditText
    private lateinit var etPokemonNumber: EditText
    private lateinit var btnSelectImage: Button
    private lateinit var btnSavePokemon: Button
    private lateinit var ivPokemonImage: ImageView

    private val REQUEST_IMAGE_GET = 1
    private val CLOUD_NAME = "dgml9aesa"
    private val UPLOAD_PRESET = "pokemon-preset"
    private var imageUri: Uri? = null
    var uploadedImageUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initCloudinary()
        etPokemonName = findViewById(R.id.pokemonName)
        etPokemonNumber = findViewById(R.id.pokemonNumber)
        btnSelectImage = findViewById(R.id.selectImage)
        btnSavePokemon = findViewById(R.id.savePokemon)
        ivPokemonImage = findViewById(R.id.tumbnail)



        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        btnSavePokemon.setOnClickListener {
            savePokemon()
        }


    }

    private fun savePokemon() {
        if (!validateFields()) return

        if (imageUri != null) {
            MediaManager.get().upload(imageUri).unsigned(UPLOAD_PRESET)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        Log.d("Cloudinary", "Upload started")
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                        Log.d("Cloudinary", "Upload successful: $resultData")
                        uploadedImageUrl = resultData["secure_url"] as String
                        persistPokemon()  // 👈 Aquí ya puedes guardar el Pokémon
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Log.e("Cloudinary", "Upload error: $error")
                        Toast.makeText(this@RegisterPokemonActivity, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        Log.e("Cloudinary", "Upload rescheduled: $error")
                    }
                }).dispatch()
        }
    }
    private fun persistPokemon() {
        val pokemon = Pokemon(
            name = etPokemonName.text.toString(),
            number = etPokemonNumber.text.toString().toInt(),
            imageUrl = uploadedImageUrl
        )

        PokemonRepository.savePokemon(pokemon)
        Toast.makeText(this, "Pokémon guardado exitosamente", Toast.LENGTH_SHORT).show()
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            val fullPhotoUri: Uri? = data?.data

            if (fullPhotoUri != null) {
                imageUri = fullPhotoUri  // 👈 IMPORTANTE: guardar la URI aquí
                changeImage(fullPhotoUri)
            }
        }
    }

    private fun changeImage(uri: Uri) {
        val thumbnail: ImageView = findViewById(R.id.tumbnail)
        try {
            thumbnail.setImageURI(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCloudinary() {

            val config: MutableMap<String,String> = HashMap<String, String>()
            config["cloud_name"] = CLOUD_NAME
            MediaManager.init(this, config)
            Log.d("Cloudinary", "Cloudinary initialized")

    }

    private fun validateFields(): Boolean {
        if (etPokemonName.text.isEmpty() || etPokemonName.text.isBlank()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etPokemonNumber.text.isEmpty() || etPokemonNumber.text.isBlank()) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}