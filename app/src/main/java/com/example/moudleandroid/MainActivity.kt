package com.example.moudleandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.moudleandroid.databinding.ActivityMainBinding
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val CHANNEL = "com.example.navigation_sdk/config"
    private var flutterEngine: FlutterEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- NORMAL ANDROID UI CODE ---
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // --- CREATE FLUTTER ENGINE ONLY ONCE ---
        if (flutterEngine == null) {
            flutterEngine = FlutterEngine(this)

            flutterEngine!!.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            // --- SETUP METHOD CHANNEL ---
            MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL)
                .setMethodCallHandler { call, result ->
                    when (call.method) {
                        "getApiKey" -> {
                            result.success(BuildConfig.API_KEY)
                        }

                        "getVenueName" -> {
                            result.success(BuildConfig.VENUE_NAME)
                        }

                        "getConfig" -> {
                            val configMap = mapOf(
                                "apiKey" to BuildConfig.API_KEY,
                                "venueName" to BuildConfig.VENUE_NAME,
//                                "landMarkId" to "66c03fedee50ac873de4e859"
                            )
                            result.success(configMap)
                        }

                        else -> result.notImplemented()
                    }
                }

            // Cache engine
            FlutterEngineCache.getInstance().put("my_flutter_engine", flutterEngine!!)
        }
        // --- FAB OPENS FLUTTER SCREEN ---
        binding.fab.setOnClickListener {
            startActivity(
                FlutterActivity.withCachedEngine("my_flutter_engine")
                    .destroyEngineWithActivity(false)
                    .build(this)
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
