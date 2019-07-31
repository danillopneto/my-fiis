package br.go.dpn.meusfiis

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import br.go.dpn.meusfiis.activity.SettingsActivity
import br.go.dpn.meusfiis.model.MyPreferences
import net.orange_box.storebox.StoreBox
import java.io.BufferedReader
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setMap()
    }

    override fun onResume() {
        super.onResume()
        setMap()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(getActionMenu(), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish(); return true; }
            R.id.mMenuSettings -> openSettings()
        }

        return super.onOptionsItemSelected(item)
    }

    open fun getActionMenu(): Int {
        return R.menu.menu_action_config
    }

    fun setMap() {
        webView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                injectJS()

                var fiis = ""
                var fundos = getMyPreferences().getFiis()
                if (fundos != null) {
                    fiis = fundos.joinToString("', '")
                }

                webView.loadUrl(
                    "javascript:(function() { window.startMyFiis(" +
                            "['" + fiis + "']);" +
                            "})()")
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                if (request?.url.toString() .contains("application") && request?.url.toString().contains(".css")) {
                    return loadFromAssets("main.css", "text/css", "")
                }

                return null
            }
        }

        webView.loadUrl("https://www.fundsexplorer.com.br/mapa-fiis")
    }

    private fun loadFromAssets(assetPath: String, mimeType: String, encoding: String): WebResourceResponse? {
        val assetManager = this.assets

        try {
            Log.d("WEB-APP", "Loading from assets: $assetPath")

            val input = assetManager.open(assetPath)

            return WebResourceResponse(mimeType, encoding, input)
        } catch (e: IOException) {
            Log.e("WEB-APP", "Error loading " + assetPath + " from assets: " +
                    e.localizedMessage, e)
        }

        return null
    }

    private fun injectJS() {
        try {
            val inputStream = assets.open("style.js")
            inputStream.bufferedReader().use(BufferedReader::readText)
        } catch (e: Exception) {
            null
        }?.let { webView.loadUrl("javascript:($it)()") }
    }

    private fun getMyPreferences(): MyPreferences {
        return StoreBox.create(applicationContext, MyPreferences::class.java)
    }

    private fun openSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
