package top.androidjian.androidlearnclient.ui.activity

import Constant
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import toast
import top.androidjian.androidlearnclient.R
import top.androidjian.androidlearnclient.base.BaseActivity
import top.androidjian.androidlearnclient.base.Preference
import top.androidjian.androidlearnclient.ui.fragment.CommonUseFragment
import top.androidjian.androidlearnclient.ui.fragment.HomeFragment
import top.androidjian.androidlearnclient.ui.fragment.TypeFragment
import android.widget.Toast
import top.androidjian.androidlearnclient.util.ClearDataUtil

/**
 * 主界面
 */
class MainActivity : BaseActivity() {
    private var lastTime: Long = 0
    private var currentIndex = 0
    private var homeFragment: HomeFragment? = null
    private var typeFragment: TypeFragment? = null
    private var commonUseFragment: CommonUseFragment? = null
    private val fragmentManager by lazy {
        supportFragmentManager
    }
    /**
     * 检查用户是否登录
     */
    private val isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
    /**
     * 本地用户名
     */
    private val username: String by Preference(Constant.USERNAME_KEY, "")

    private lateinit var navigationViewUsername: TextView

    private lateinit var navigationViewLogout: MenuItem

    override fun setLayoutId(): Int = R.layout.activity_main

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }
        bottomNavigation.run {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_home
        }
        drawerLayout.run {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }
        navigationView.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
        }
        navigationViewUsername =
                navigationView.getHeaderView(0).findViewById<TextView>(R.id.navigationViewUsername)
        navigationViewLogout = navigationView.menu.findItem(R.id.navigationViewLogout)

        navigationViewUsername.run {
            if (!isLogin) {
                text = getString(R.string.not_login)
            } else {
                text = username
            }
        }
        navigationViewLogout.run {
            title = if (!isLogin) {
                getString(R.string.goto_login)
            } else {
                getString(R.string.logout)
            }

            icon=if (!isLogin){
                resources.getDrawable(R.drawable.login_in)
            }else{
                resources.getDrawable(R.drawable.login_out)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSearch -> {
                Intent(this, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constant.MAIN_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    navigationViewUsername.text = data?.getStringExtra(Constant.CONTENT_TITLE_KEY)
                    navigationViewLogout.title = getString(R.string.logout)
                    navigationViewLogout.icon=resources.getDrawable(R.drawable.login_out)
                }
                homeFragment?.refreshData()
            }
            Constant.MAIN_LIKE_REQUEST_CODE -> homeFragment?.refreshData()
        }
    }

    /**
     * cancel request
     */
    override fun cancelRequest() {
    }

    /**
     * 防止重叠
     */
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is HomeFragment -> homeFragment ?: let { homeFragment = fragment }
            is TypeFragment -> typeFragment ?: let { typeFragment = fragment }
            is CommonUseFragment -> commonUseFragment ?: let { commonUseFragment = fragment }
        }
    }

    /**
     * 退出
     */
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressed()
            finish()
        } else {
            toast(getString(R.string.double_click_exit))
            lastTime = currentTime
        }
    }

    /**
     * 显示对应Fragment
     */
    private fun setFragment(index: Int) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        fragmentManager.beginTransaction().apply {
            homeFragment ?: let {
                HomeFragment().let {
                    homeFragment = it
                    add(R.id.content, it)
                }
            }
            typeFragment ?: let {
                TypeFragment().let {
                    typeFragment = it
                    add(R.id.content, it)
                }
            }
            commonUseFragment ?: let {
                CommonUseFragment().let {
                    commonUseFragment = it
                    add(R.id.content, it)
                }
            }
            hideFragment(this)
            when (index) {
                R.id.navigation_home -> {
                    toolbar.title = getString(R.string.app_name)
                    homeFragment?.let {
                        this.show(it)
                    }
                }
                R.id.navigation_type -> {
                    toolbar.title = getString(R.string.title_dashboard)
                    typeFragment?.let {
                        this.show(it)
                    }
                }
                R.id.navigation_hot -> {
                    toolbar.title = getString(R.string.hot_title)
                    commonUseFragment?.let {
                        this.show(it)
                    }
                }
            }
        }.commit()
    }

    /**
     * 隐藏所有fragment
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        homeFragment?.let {
            transaction.hide(it)
        }
        typeFragment?.let {
            transaction.hide(it)
        }
        commonUseFragment?.let {
            transaction.hide(it)
        }
    }

    override fun onResume() {
        super.onResume()
        // other activity login
        if (isLogin && navigationViewUsername.text.toString() != username) {
            navigationViewUsername.text = username
            navigationViewLogout.title = getString(R.string.logout)
            navigationViewLogout.icon=resources.getDrawable(R.drawable.login_out)
            homeFragment?.refreshData()
        }
    }

    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            setFragment(item.itemId)
            return@OnNavigationItemSelectedListener when (item.itemId) {
                R.id.navigation_home -> {
                    if (currentIndex == R.id.navigation_home) {
                        homeFragment?.smoothScrollToPosition()
                    }
                    currentIndex = R.id.navigation_home
                    true
                }
                R.id.navigation_type -> {
                    if (currentIndex == R.id.navigation_type) {
                        typeFragment?.smoothScrollToPosition()
                    }
                    currentIndex = R.id.navigation_type
                    true
                }

                R.id.navigation_hot -> {
                    if (currentIndex == R.id.navigation_hot) {
                        commonUseFragment?.smoothScrollToPosition()
                    }
                    currentIndex = R.id.navigation_hot
                    true
                }

                else -> {
                    false
                }
            }
        }

    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_like -> {
                    if (!isLogin) {
                        Intent(this, LoginActivity::class.java).run {
                            startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                        }
                        toast(getString(R.string.login_please_login))
                        return@OnNavigationItemSelectedListener true
                    }
                    Intent(this, SearchActivity::class.java).run {
                        putExtra(Constant.SEARCH_KEY, false)
                        startActivityForResult(this, Constant.MAIN_LIKE_REQUEST_CODE)
                    }
                }
                R.id.nav_clear -> {
                    showDataClearDialog()
                }
                R.id.nav_about -> {
                    Intent(this, AboutActivity::class.java).run {
                        startActivity(this)
                    }
                }
                R.id.navigationViewLogout -> {
                    if (!isLogin) {
                        Intent(this@MainActivity, LoginActivity::class.java).run {
                            startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                        }
                    } else {
                        Preference.clear()
                        navigationViewUsername.text = getString(R.string.not_login)
                        title = getString(R.string.goto_login)
                        navigationViewLogout.icon=resources.getDrawable(R.drawable.login_in)
                        homeFragment?.refreshData()
                    }
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    private fun showDataClearDialog(){
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.data_clear_title))
                .setMessage(getString(R.string.data_current)+ClearDataUtil.getTotalCacheSize(applicationContext))
                .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .setPositiveButton(getString(R.string.confirm), DialogInterface.OnClickListener { dialog, which ->
                    ClearDataUtil.clearAllCache(applicationContext)
                    Toast.makeText(this@MainActivity, getString(R.string.data_cleard), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }).create()
        dialog.show()
    }
}
