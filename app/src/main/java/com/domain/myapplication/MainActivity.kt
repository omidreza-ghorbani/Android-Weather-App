package com.domain.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.domain.myapplication.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startWithLink()
    }

    fun startWithLink() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/" +
                    "weather?lat=35.6892523&lon=51.3896004&appid=e556c81db97008495af182067b583d5a&units=metric")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val rawContent = response.body!!.string()
                    loadData(rawContent)
                }
            }
        })
    }

    fun loadData(rawContent:String) {
        val jsonObject = JSONObject(rawContent)

        val cityName = jsonObject.getString("name")

        val weatherArray = jsonObject.getJSONArray("weather")
        val weatherObject = weatherArray.getJSONObject(0)
        val description = weatherObject.getString("description")

        val iconId = weatherObject.getString("icon")
        val imageUrl = "https://openweathermap.org/img/wn/${iconId}@2x.png"

        val mainObject = jsonObject.getJSONObject("main")
        val temperature = mainObject.getDouble("temp").toInt()

        val humidity = mainObject.getDouble("humidity").toInt()

        runOnUiThread {
            binding.imageViewMiladTower.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.textViewCity.text = cityName
            binding.textViewWeaterStatus.text = description
            binding.textViewTemperature.text = "Temperature : $temperature°C"
            binding.textView2Humidity.text = "Humidity : $humidity%"
            Glide.with(this@MainActivity).load(imageUrl).into(binding.imageView8)
            binding.prograssBarInStart.visibility = View.INVISIBLE
            binding.imageView8.visibility = View.VISIBLE
        }
    }

    fun reloadDate(view: View) {
        binding.imageViewMiladTower.visibility = View.INVISIBLE
        binding.imageView8.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.prograssBarInStart.visibility = View.VISIBLE
        binding.textViewCity.text = "--"
        binding.textViewWeaterStatus.text = "--"
        binding.textViewTemperature.text = "Temperature : --°C"
        binding.textView2Humidity.text = "Humidity : --%"
        startWithLink()
    }
}
