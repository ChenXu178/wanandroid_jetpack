package com.yechaoa.wanandroid_jetpack.ui.main

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.yechaoa.wanandroid_jetpack.R
import com.yechaoa.wanandroid_jetpack.base.BaseActivity
import com.yechaoa.wanandroid_jetpack.common.MyConfig
import com.yechaoa.wanandroid_jetpack.databinding.ActivityMainBinding
import com.yechaoa.wanandroid_jetpack.databinding.AppBarMainBinding
import com.yechaoa.wanandroid_jetpack.databinding.ContentMainBinding
import com.yechaoa.wanandroid_jetpack.ui.about.AboutActivity
import com.yechaoa.wanandroid_jetpack.ui.adapter.CommonViewPagerAdapter
import com.yechaoa.wanandroid_jetpack.ui.collect.CollectActivity
import com.yechaoa.wanandroid_jetpack.ui.login.LoginActivity
import com.yechaoa.wanandroid_jetpack.ui.main.home.HomeFragment
import com.yechaoa.wanandroid_jetpack.ui.main.navi.NaviFragment
import com.yechaoa.wanandroid_jetpack.ui.main.pro.ProjectFragment
import com.yechaoa.wanandroid_jetpack.ui.main.tree.TreeFragment
import com.yechaoa.yutilskt.ActivityUtil
import com.yechaoa.yutilskt.SpUtil
import com.yechaoa.yutilskt.ToastUtil

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var mAppBarMainBinding: AppBarMainBinding
    private lateinit var mContentMainBinding: ContentMainBinding

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun init() {
        super.init()
        //include 写法
        mAppBarMainBinding = mBinding.appBarMain
        mContentMainBinding = mBinding.appBarMain.contentMain

        mAppBarMainBinding.toolbar.title = resources.getString(R.string.app_name)

        mAppBarMainBinding.fab.setOnClickListener {
            Snackbar.make(it, "这是一个提示", Snackbar.LENGTH_SHORT).setAction("按钮") {
                ToastUtil.show("点击了按钮")
            }.show()
        }

        initActionBarDrawer()

        initFragments()
    }

    /**
     * Drawer关联Toolbar
     */
    private fun initActionBarDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            mBinding.drawerLayout,
            mAppBarMainBinding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        mBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    /**
     * 初始化Fragment
     */
    private fun initFragments() {
        val viewPagerAdapter = CommonViewPagerAdapter(supportFragmentManager).apply {
            addFragment(HomeFragment())
            addFragment(TreeFragment())
            addFragment(NaviFragment())
            addFragment(ProjectFragment())
        }
        mAppBarMainBinding.contentMain.viewPager.offscreenPageLimit = 1
        mAppBarMainBinding.contentMain.viewPager.adapter = viewPagerAdapter
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        /**
         * 侧边栏点击事件
         */
        mBinding.navView.setNavigationItemSelectedListener {
            // Handle navigation view item clicks here.
            when (it.itemId) {
                R.id.nav_collect -> {
                    startActivity(Intent(this, CollectActivity::class.java))
                }
                R.id.nav_share -> {
                    shareProject()
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.nav_logout -> {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("提示")
                    builder.setMessage("确定退出？")
                    builder.setPositiveButton("确定") { _, _ ->
                        SpUtil.setBoolean(MyConfig.IS_LOGIN, false)
                        SpUtil.removeByKey(MyConfig.COOKIE)
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                    builder.setNegativeButton("取消", null)
                    builder.create().show()
                }
            }

            //关闭侧边栏
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)

            true
        }

        /**
         * view_pager 滑动监听
         */
        mAppBarMainBinding.contentMain.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mAppBarMainBinding.contentMain.bottomNavigation.menu.getItem(position).isChecked = true
                //设置checked为true，但是不能触发ItemSelected事件，所以滑动时也要设置一下标题
                when (position) {
                    0 -> mAppBarMainBinding.toolbar.title = resources.getString(R.string.app_name)
                    1 -> mAppBarMainBinding.toolbar.title = resources.getString(R.string.title_tree)
                    2 -> mAppBarMainBinding.toolbar.title = resources.getString(R.string.title_navi)
                    else -> mAppBarMainBinding.toolbar.title = resources.getString(R.string.title_project)
                }
            }
        })

        /**
         * bottom_navigation 点击事件
         */
        mAppBarMainBinding.contentMain.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    mAppBarMainBinding.contentMain.viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_tree -> {
                    mAppBarMainBinding.contentMain.viewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_navi -> {
                    mAppBarMainBinding.contentMain.viewPager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_project -> {
                    mAppBarMainBinding.contentMain.viewPager.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    /**
     * 调用系统的分享功能
     */
    private fun shareProject() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.wanandroid))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.github))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(Intent.createChooser(intent, getString(R.string.wanandroid)))
    }

    /**
     * 添加toolbar菜单
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * toolbar菜单事件
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
//                startActivity(Intent(this, SearchActivity::class.java))
            }
            R.id.action_settings -> {
                ToastUtil.show("设置")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var mExitTime: Long = 0 // 保存用户按返回键的时间

    /**
     * 拦截返回事件，自处理
     */
    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.show("再按一次退出" + resources.getString(R.string.app_name))
                mExitTime = System.currentTimeMillis()
            } else {
                ActivityUtil.closeAllActivity()
            }
        }
    }
}