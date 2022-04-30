package com.odinsporrt.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appsflyer.AppsFlyerLib
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.odinsporrt.R
import com.odinsporrt.data.MyApiFactory
import com.odinsporrt.databinding.ActivityMainBinding
import com.odinsporrt.presentation.MyApps
import com.odinsporrt.presentation.fragment.MenuFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var isDef = false
    private var mainLink: String? = null


    private var cloacaLink: String? = null
    private var googleId: String? = null
    var appsFlyerUserId: String? = null
    private val bundle = "com.odinsporrt"
    private val facebookToken by lazy { resources.getString(R.string.facebook_token) }
    private val appsDevKey by lazy { resources.getString(R.string.apps_flyer_dev_key) }
    private val facebookAppId by lazy { resources.getString(R.string.facebook_app_id) }
    private var subAll = listOf<String?>(null, null, null, null)

    private var campaign: String? = null
    private var deepLink: String? = null


    private var mediaSource: String? = null
    private var afStatus: String? = null
    private var afChannel: String? = null
    private var isFirstLaunch: String? = null


    private val user by lazy { getSharedPreferences("hasVisited", Context.MODE_PRIVATE)
    }
    private val visited by lazy { user.getBoolean("hasVisited", true) }


    private val link by lazy { getSharedPreferences("link", Context.MODE_PRIVATE)
    }
    private val haveLink by lazy { link.getString("link", "") }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.drawable.loading).into(binding.imLoad)
        startWork()
    }


    /**
     * Запускаю инициализацию и открываю новое окно
     */
    private fun startWork() {

        if (visited) {
            lifecycleScope.launch(Dispatchers.IO) {
                getDataServer()
                getGoogleID()
                startInitialFb()
                lifecycleScope.launch(Dispatchers.Main) {
                    getAppsFlyerParams()
                }
            }
            user.edit().putBoolean("hasVisited", false).apply()
        } else {
            if (haveLink.isNullOrEmpty()) {
                supportFragmentManager.beginTransaction().add(R.id.fragContainer, MenuFragment.newInstance())
                .commit()
            } else {
                Intent(this, WebActivity::class.java).apply {
                    putExtra("link", haveLink)
                    startActivity(this)
                }
            }
        }
    }


    private fun startInitialFb() {
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(
            this
        ) {
            deepLink = it?.targetUri.toString()
            deepLink?.let { deepString ->
                Log.d("test2", "$deepString")
                val arrayDeepLink = deepString.split("//")
                subAll = arrayDeepLink[1].split("_")
            }
        }
    }

    /**
     * Получаю id google
     */
    private fun getGoogleID() {
        val googleId = AdvertisingIdClient.getAdvertisingIdInfo(this)
        this.googleId = googleId.id.toString()
    }


    /**
     * Получаю параметры с AppsFlyer
     */
    private fun getAppsFlyerParams() {
        appsFlyerUserId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        MyApps.liveDataAppsFlyer.observe(this) {
            for (inform in it) {
                when (inform.key) {
                    "af_status" -> {
                        afStatus = inform.value.toString()
                    }
                    "campaign" -> {
                        campaign = inform.value.toString()
                        campaign?.let { it1 -> subAll = it1.split("_") }
                    }
                    "media_source" -> {
                        mediaSource = inform.value.toString()
                    }
                    "is_first_launch" -> {
                        isFirstLaunch = inform.value.toString()
                    }
                    "af_channel" -> {
                        afChannel = inform.value.toString()
                    }
                }
            }
            nextScreen()
        }
    }

    /**
     * Собираю ссылку
     */
    private fun collectingLink() {
        mainLink = cloacaLink + "media_source=$mediaSource" + "&google_adid=$googleId" +
                "&af_userid=$appsFlyerUserId" + "&bundle=$bundle" + "&fb_at=$facebookToken" +
                "&dev_key=$appsDevKey" + "&app_id=$facebookAppId" + "&media_source=$mediaSource" +
                "&af_status=$afStatus" + "&af_channel=$afChannel" + "&campaign=$campaign" +
                "&is_first_launch=$isFirstLaunch" + "&sub1=${subAll[0]}" + "&sub2=${subAll[1]}" +
                "&sub3=${subAll[2]}" + "&sub4=${subAll[3]}"
        Log.d("test2", "$mainLink")
    }

    /**
     * Получаю данные с сервера
     */
    private fun getDataServer() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                MyApiFactory.create().getDataServer().users.forEach {
                    Log.d("test3", "$it")
                    isDef = it.isdef.toBoolean()
                    cloacaLink = it.linka.toString()
                }
            } catch (e: Exception) {
                Log.d("errorGetData", "$e")
            }
        }
    }

    /**
     * Открываю или игру или вебвью
     */
    private fun nextScreen() {
        collectingLink() //формирую ссылку
        if (subAll[1] == "test2") {
            if (afStatus == null || !isDef && afStatus == "Organic" && subAll[1] == null) {
                supportFragmentManager.beginTransaction().add(R.id.fragContainer, MenuFragment.newInstance())
                .commit()
            }
            if (isDef && afStatus == "Organic" || subAll[1] != null) {
                Intent(this, WebActivity::class.java).apply {
                    link.edit().putString("link", "$mainLink").apply()
                    putExtra("link", mainLink)
                    startActivity(this)
                }
            }
        } else {
            if (afStatus == null || !isDef && afStatus == "Organic") {
                supportFragmentManager.beginTransaction().add(R.id.fragContainer, MenuFragment.newInstance())
                .commit()
            }
            if (isDef && afStatus == "Organic" || subAll[1] != null) {
                Intent(this, WebActivity::class.java).apply {
                    link.edit().putString("link", "$mainLink").apply()
                    putExtra("link", mainLink)
                    startActivity(this)
                }
            }
        }
    }
}