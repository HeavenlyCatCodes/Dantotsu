package ani.dantotsu.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnAttach
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ani.dantotsu.R
import ani.dantotsu.ZoomOutPageTransformer
import ani.dantotsu.bindGlassChrome
import ani.dantotsu.databinding.ActivityNoInternetBinding
import ani.dantotsu.download.manga.OfflineMangaFragment
import ani.dantotsu.initActivity
import ani.dantotsu.navBarHeight
import ani.dantotsu.offline.LocalFragment
import ani.dantotsu.selectedOption
import ani.dantotsu.settings.saving.PrefManager
import ani.dantotsu.settings.saving.PrefName
import ani.dantotsu.snackString
import ani.dantotsu.themes.ThemeManager
import nl.joery.animatedbottombar.AnimatedBottomBar

class NoInternet : AppCompatActivity() {
    private lateinit var binding: ActivityNoInternetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeManager(this).applyTheme()

        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomBar = findViewById<AnimatedBottomBar>(R.id.navbar)
        bottomBar.background = null
        binding.includedNavbar.navbarGlass.bindGlassChrome(binding.viewpager)


        var doubleBackToExitPressedOnce = false
        onBackPressedDispatcher.addCallback(this) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
            }
            doubleBackToExitPressedOnce = true
            snackString(this@NoInternet.getString(R.string.back_to_exit))
            Handler(Looper.getMainLooper()).postDelayed(
                { doubleBackToExitPressedOnce = false },
                2000
            )
        }

        binding.root.doOnAttach {
            initActivity(this)
            val stored = PrefManager.getVal<Int>(PrefName.DefaultStartUpTab)
            selectedOption = if (stored == 2) 1 else 0

            binding.includedNavbar.navbarContainer.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarHeight
            }
        }
        val navbar = binding.includedNavbar.navbar
        ani.dantotsu.bottomBar = navbar
        navbar.visibility = View.VISIBLE
        val mainViewPager = binding.viewpager
        mainViewPager.isUserInputEnabled = false
        mainViewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        mainViewPager.setPageTransformer(ZoomOutPageTransformer())
        navbar.setOnTabSelectListener(object :
            AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                navbar.animate().translationZ(12f).setDuration(200).start()
                selectedOption = newIndex
                mainViewPager.setCurrentItem(newIndex, false)
            }
        })
        navbar.selectTabAt(selectedOption)

        //supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, OfflineFragment()).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpager.adapter = null
        ani.dantotsu.clearBottomBar()
    }

    private class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                1 -> OfflineMangaFragment()
                else -> LocalFragment()
            }
        }
    }
}
