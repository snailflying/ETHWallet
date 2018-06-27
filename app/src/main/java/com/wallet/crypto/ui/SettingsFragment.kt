package com.wallet.crypto.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.wallet.crypto.R
import com.wallet.crypto.MercuryConstants
import com.wallet.crypto.R.string.facebook
import com.wallet.crypto.interact.FindDefaultWalletInteract
import com.wallet.crypto.repository.EthereumNetworkRepository
import com.wallet.crypto.router.ManageWalletsRouter
import dagger.android.AndroidInjection
import javax.inject.Inject
import android.content.pm.ApplicationInfo
import com.wallet.crypto.ext.toast


class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    internal lateinit var ethereumNetworkRepository: EthereumNetworkRepository
    @Inject
    internal lateinit var findDefaultWalletInteract: FindDefaultWalletInteract
    @Inject
    internal lateinit var manageWalletsRouter: ManageWalletsRouter

    val version: String
        get() {
            var version = "N/A"
            try {
                val pInfo = activity.packageManager.getPackageInfo(activity.packageName, 0)
                version = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return version
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_settings)
        val wallets = findPreference("pref_wallet")
        val changePassword = findPreference("pref_change_password")

        wallets.setOnPreferenceClickListener { preference ->
            manageWalletsRouter!!.open(activity, false)
            false
        }

        changePassword.setOnPreferenceClickListener { preference ->
            startActivity(Intent(activity, ChangePasswordActivity::class.java))
            false
        }

        findDefaultWalletInteract!!
                .find()
                .subscribe { wallet ->
                    PreferenceManager
                            .getDefaultSharedPreferences(activity)
                            .edit()
                            .putString("pref_wallet", wallet.address)
                            .apply()
                    wallets.summary = wallet.address
                }

        val listPreference = findPreference("pref_rpcServer") as ListPreference
        // THIS IS REQUIRED IF YOU DON'T HAVE 'entries' and 'entryValues' in your XML
        setRpcServerPreferenceData(listPreference)
        listPreference.setOnPreferenceClickListener { preference ->
            setRpcServerPreferenceData(listPreference)
            false
        }
        val versionString = version
        val version = findPreference("pref_version")
        version.summary = versionString
        val preferences = PreferenceManager
                .getDefaultSharedPreferences(activity)
        preferences
                .registerOnSharedPreferenceChangeListener(this@SettingsFragment)
        val rate = findPreference("pref_rate")
        rate.setOnPreferenceClickListener { preference ->
            rateThisApp()
            false
        }

        val twitter = findPreference("pref_qq")
        twitter.setOnPreferenceClickListener { preference ->
            joinQQGroup()
            false
        }

       /* val facebook = findPreference("pref_facebook")
        facebook.setOnPreferenceClickListener { preference ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/trustwalletapp"))
            startActivity(intent)
            false
        }*/
         val telegram = findPreference("pref_telegram_red_packet")
        telegram.setOnPreferenceClickListener { preference ->
            val appName = "org.telegram.messenger"
            if (checkApplicationExist(appName)){
                val myIntent = Intent(Intent.ACTION_SEND)
                myIntent.type = "text/plain"
                myIntent.`package` = appName
                myIntent.putExtra(Intent.EXTRA_TEXT, "红包链接...")
                startActivity(Intent.createChooser(myIntent, "Share with"))
            }else{
                toast("请先安装Telegram").show()
            }

            false
        }

        val donate = findPreference("pref_donate")
        donate.setOnPreferenceClickListener { preference ->
            val intent = Intent(activity, SendActivity::class.java)
            intent.putExtra(MercuryConstants.EXTRA_ADDRESS, MercuryConstants.DONATION_ADDRESS)
            startActivity(intent)
            true
        }

        val email = findPreference("pref_email")
        email.setOnPreferenceClickListener { preference ->

            val mailto = Intent(Intent.ACTION_SENDTO)
            mailto.type = "message/rfc822" // use from live device
            mailto.data = Uri.parse("mailto:bravoon@126.com")
                    .buildUpon()
                    .appendQueryParameter("subject", "Android support question")
                    .appendQueryParameter("body", "Hi,")
                    .build()
            startActivity(Intent.createChooser(mailto, "Select email application."))
            true
        }
    }

    /****************
     *
     * 发起添加群流程。群号：世界(195958716) 的 key 为： 3P2GVcsBTTSVd8Vpow2KxfYmZaSn0Anf
     * 调用 joinQQGroup(3P2GVcsBTTSVd8Vpow2KxfYmZaSn0Anf) 即可发起手Q客户端申请加群 世界(195958716)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     */
    fun joinQQGroup(key: String = MercuryConstants.QQ_GROUP): Boolean {
        val intent = Intent()
        intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent)
            return true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            return false
        }

    }


    private fun rateThisApp() {
        val uri = Uri.parse("market://details?id=" + activity.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.packageName)))
        }

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences,
                                           key: String) {
        if (key == "pref_rpcServer") {
            val rpcServerPref = findPreference(key)
            // Set summary
            val selectedRpcServer = sharedPreferences.getString(key, "")
            rpcServerPref.summary = selectedRpcServer
            val networks = ethereumNetworkRepository!!.availableNetworkList
            for (networkInfo in networks) {
                if (networkInfo.name == selectedRpcServer) {
                    ethereumNetworkRepository!!.setDefaultNetworkInfo(networkInfo)
                    return
                }
            }
        }
    }

    private fun setRpcServerPreferenceData(lp: ListPreference) {
        val networks = ethereumNetworkRepository!!.availableNetworkList
        val entries = arrayOfNulls<CharSequence>(networks.size)
        for (ii in networks.indices) {
            entries[ii] = networks[ii].name
        }

        val entryValues = arrayOfNulls<CharSequence>(networks.size)
        for (ii in networks.indices) {
            entryValues[ii] = networks[ii].name
        }

        val currentValue = ethereumNetworkRepository!!.defaultNetwork.name

        lp.entries = entries
        lp.setDefaultValue(currentValue)
        lp.value = currentValue
        lp.summary = currentValue
        lp.entryValues = entryValues
    }

    fun checkApplicationExist(packageName: String?): Boolean {
        if (packageName == null || "" == packageName) {
            return false
        }
        return try {
            val info = activity.packageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }
}

