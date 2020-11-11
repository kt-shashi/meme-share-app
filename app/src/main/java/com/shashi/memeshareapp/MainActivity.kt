package com.shashi.memeshareapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.shashi.memeshareapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var currentUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolBar: Toolbar = binding.toolbarMainActivity
        setSupportActionBar(toolBar)

        initViews()
        loadMeme()
    }

    private fun initViews() {
        binding.imageButtonShare.setOnClickListener { shareClicked() }
        binding.buttonNext.setOnClickListener { nextClicked() }
    }

    private fun shareClicked() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey, checkout this meme I found on Reddit\n $currentUrl"
        )
        val chooser = Intent.createChooser(intent, "Share this MEME")
        startActivity(chooser)
    }

    private fun nextClicked() {
        loadMeme()
    }

    private fun loadMeme() {

        binding.progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentUrl = response.getString("url")
                Glide.with(this).load(currentUrl).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                }).into(binding.imageViewMeme)
            },
            { }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

}