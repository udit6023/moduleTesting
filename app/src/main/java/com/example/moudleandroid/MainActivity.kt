package com.example.moudleandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moudleandroid.databinding.ActivityMainBinding
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val CHANNEL = "com.example.navigation_sdk/config"
    private var flutterEngine: FlutterEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // --- BUTTON OPENS FLUTTER ---
        binding.openFlutterBtn.setOnClickListener {
            startNavigation()
        }
    }

    fun startNavigation(){
        if (flutterEngine == null) {
            flutterEngine = FlutterEngine(this)

            flutterEngine!!.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            MethodChannel(
                flutterEngine!!.dartExecutor.binaryMessenger,
                CHANNEL
            ).setMethodCallHandler { call, result ->
                when (call.method) {
                    "getApiKey" -> result.success(BuildConfig.API_KEY)
                    "getVenueName" -> result.success(BuildConfig.VENUE_NAME)
                    "getConfig" -> {
                        result.success(
                            mapOf(
                                "apiKey" to BuildConfig.API_KEY,
                                "venueName" to BuildConfig.VENUE_NAME
                            )
                        )
                    }
                    else -> result.notImplemented()
                }
            }

            FlutterEngineCache
                .getInstance()
                .put("my_flutter_engine", flutterEngine!!)
        }
        startActivity(
            FlutterActivity
                .withCachedEngine("my_flutter_engine")
                .destroyEngineWithActivity(false)
                .build(this)
        )
    }
}
