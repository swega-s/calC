package com.example.android.mycalc

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
    , HomeFragment.OperationClickListener
    , InputFragment.ResultButtonClickListener
{

    private var isHomeFragment: Boolean = true
    private var showResultView: Boolean = false
    private lateinit var appBarTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {

            appBarTitle = getString(R.string.app_name)
            supportActionBar?.title = appBarTitle

            supportFragmentManager.commit {
                replace(R.id.homeContainer, HomeFragment(), "home_fragment")
            }

            val inputFragment = InputFragment()
            supportFragmentManager.commit {
                replace(R.id.inputContainer, inputFragment, "input_fragment")
            }
            inputFragment.setViewAndChildrenEnabled(findViewById(R.id.inputContainer), false)

            isHomeFragment = true
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        isHomeFragment = savedInstanceState.getBoolean("is_home_fragment")
        showResultView = savedInstanceState.getBoolean("result_flag")
        appBarTitle = savedInstanceState.getString("appbar_title").toString()
        supportActionBar?.title = appBarTitle

        val homeFragment = supportFragmentManager.findFragmentByTag("home_fragment") as HomeFragment

        val inpFragment = supportFragmentManager.findFragmentByTag("input_fragment") as InputFragment

        if (activity_portrait != null && !isHomeFragment) {
            showBackButton(true)
            inputContainer.visibility = View.VISIBLE
            homeContainer.visibility = View.GONE

        } else if (activity_portrait != null && isHomeFragment) {

            inputContainer.visibility = View.GONE
            homeContainer.visibility = View.VISIBLE

            if (showResultView) {
                homeFragment.onBackPressedToResultViews()
                showBackButton(true)
            } else {
                homeFragment.onBackPressedToOperations()
            }
        } else if (activity_landscape != null) {
            if (showResultView) {
                homeFragment.onBackPressedToResultViews()
                showBackButton(false)
            } else {
                homeFragment.onBackPressedToOperations()
                if (isHomeFragment) {
                    inpFragment.setViewAndChildrenEnabled(
                        findViewById(R.id.inputContainer),
                        false
                    )
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_home_fragment", isHomeFragment)
        outState.putBoolean("result_flag", showResultView)
        outState.putString("appbar_title", appBarTitle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOperationClicked(operation: Operations?) {

        isHomeFragment = false
        appBarTitle = operation.toString()
        supportActionBar?.title = operation.toString() // change to appbar title
        if (activity_portrait != null) {
            inputContainer.visibility = View.VISIBLE
            homeContainer.visibility = View.GONE
            showBackButton(true)
        }

    }

    override fun onResultBtnClicked(flag: Boolean) {
        setShowResultView(true)
        if (activity_portrait != null) {
            inputContainer.visibility = View.GONE
            homeContainer.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        appBarTitle = getString(R.string.app_name)
        supportActionBar?.title = appBarTitle
        isHomeFragment = true

        if (showResultView) {
            showResultView = false
            (supportFragmentManager
                .findFragmentByTag("home_fragment")
                    as HomeFragment).onBackPressedToOperations()
            if (activity_portrait != null) {
                showBackButton(false)
            }
        } else {
            if (activity_portrait != null &&
                inputContainer.visibility == View.VISIBLE
            ) {
                inputContainer.visibility = View.GONE
                homeContainer.visibility = View.VISIBLE
                showBackButton(false)
            } else {
                finish()
            }
        }
    }

    fun setShowResultView(flag: Boolean) {
        if (!flag && activity_portrait != null) {
            inputContainer.visibility = View.GONE
            homeContainer.visibility = View.VISIBLE
            onBackPressed()
        } else if (flag && activity_portrait != null) {
            this.showResultView = flag
            isHomeFragment = true
        } else {
            this.showResultView = flag
            isHomeFragment = true
            if (showResultView) {
                (supportFragmentManager
                    .findFragmentByTag("home_fragment")
                        as HomeFragment).onBackPressedToResultViews()
            } else {
                (supportFragmentManager
                    .findFragmentByTag("home_fragment")
                        as HomeFragment).onBackPressedToOperations()
                appBarTitle = getString(R.string.app_name)
                supportActionBar?.title = appBarTitle
            }
        }
    }

    private fun showBackButton(flag: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(flag)
    }
}