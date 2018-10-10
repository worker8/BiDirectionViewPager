package github.com.worker8.bidirectionviewpager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val stringsRow1 = "Scenery" to mutableListOf("https://i.imgur.com/FHeHGRB.jpg", "https://i.imgur.com/Stt4bp8.jpg", "https://i.imgur.com/Adcqb3a.jpg", "https://i.imgur.com/qk0bjhg.jpg")
    val stringsRow2 = "Corgi" to mutableListOf("https://i.imgur.com/GdLJSWd.jpg", "https://i.imgur.com/naucCPS.jpg", "https://i.imgur.com/bm3B1D2.jpg", "https://i.imgur.com/S27r6rM.jpg", "https://i.imgur.com/rb4N8PX.jpg")
    val stringsRow3 = "Interior Design" to mutableListOf("https://i.imgur.com/dn4RqVR.jpg", "https://i.imgur.com/tSon4Cu.jpg", "https://i.imgur.com/hjfVA8R.jpg", "https://i.imgur.com/6xq5eST.jpg", "https://i.imgur.com/DseSlT0.jpg", "https://i.imgur.com/tfiDgdG.jpg")
    val stringsRow4 = "Animals" to mutableListOf("https://i.imgur.com/5HEIddN.jpg", "https://i.imgur.com/37VjUX6.jpg", "https://i.imgur.com/MtImoQm.jpg", "https://i.imgur.com/Ye74n6j.png")
    val stringsRow5 = "More Animals" to mutableListOf("https://i.imgur.com/LKkNjxa.jpg", "https://i.imgur.com/q1r1jUF.jpg", "https://i.imgur.com/W2tYCkF.jpg", "https://i.imgur.com/LxkwMfN.jpg", "https://i.imgur.com/czWaDtW.jpg")

    val strings = mutableListOf(stringsRow1, stringsRow2, stringsRow3, stringsRow4, stringsRow5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        setContentView(R.layout.activity_main)

        val biAdapter = MainAdapter(strings)
        homeViewPager.biAdapter = biAdapter

    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
