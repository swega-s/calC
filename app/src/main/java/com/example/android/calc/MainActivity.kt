package com.example.android.calc

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Math.floor

class MainActivity : AppCompatActivity(), HomeFragment.OnItemSelectedListener,
            InputFragment.OnResButtonClickListener {

    private val viewModel: InputViewModel by viewModels()
    private var inMainFragment = true
    private var showOperationsView = true
    private lateinit var appBarTitle: String

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, HomeFragment())
            }
            appBarTitle = getString(R.string.app_name)
        }

        if (findViewById<View?>(R.id.input_fragment) != null) {
            supportFragmentManager.popBackStack()
            val inputFragment = supportFragmentManager.findFragmentById(R.id.input_fragment)
            if (inputFragment == null) {
                supportFragmentManager.commit {
                    replace(R.id.input_fragment, InputFragment())
                }
            }
        }
    }

    override fun onStart() {
        viewModel.isPortrait =
            resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        inMainFragment = savedInstanceState.getBoolean("main_fragment_check")
        showOperationsView = savedInstanceState.getBoolean("op_view")
        appBarTitle = savedInstanceState.getString("action_bar_title", getString(R.string.app_name))

        if (!inMainFragment && viewModel.isPortrait) {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, HomeFragment())
                addToBackStack(null)
                replace(R.id.main_fragment, InputFragment())
            }
            showBackButton()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        supportActionBar?.title = appBarTitle
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("main_fragment_check", inMainFragment)
        outState.putBoolean("op_view", showOperationsView)
        outState.putString("action_bar_title", appBarTitle)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0) {
            appBarTitle = getString(R.string.app_name)
            supportActionBar?.title = appBarTitle
            supportFragmentManager.popBackStackImmediate()
            inMainFragment = true
        }
        else
            super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStackImmediate()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun resultButtonSelected() {
        inMainFragment = true
        performOperationForResult()
        if (viewModel.isPortrait) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, HomeFragment())
            }
        }
    }

    override fun onOperationItemSelected() {
        showBackButton()

        inMainFragment = false
        if (viewModel.isPortrait) {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, InputFragment())
                addToBackStack(null)
            }
        } else {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, HomeFragment())
                replace(R.id.input_fragment, InputFragment())
            }
        }
        appBarTitle = viewModel.operation.toString()
        supportActionBar?.title = appBarTitle
    }

    override fun onResetSelected() {
        appBarTitle = getString(R.string.app_name)
        supportActionBar?.title = appBarTitle
    }

    private fun showBackButton() {
        supportFragmentManager.addOnBackStackChangedListener {
            val stackHeight = supportFragmentManager.backStackEntryCount
            showOperationsView = stackHeight > 0 && viewModel.isPortrait
            supportActionBar?.setDisplayHomeAsUpEnabled(showOperationsView)
        }
    }

    private fun performOperationForResult() {

        val inp1 = viewModel.data1
        val inp2 = viewModel.data2
        val res = when (viewModel.operation) {
            Operations.ADD -> inp1 + inp2
            Operations.SUBTRACT -> inp1 - inp2
            Operations.MULTIPLY -> inp1 * inp2
            Operations.DIVIDE -> inp1 / inp2
        }
        viewModel.result = "Result is ${findWhole(res)}\n" +
                "for inputs ${findWhole(inp1)} and ${findWhole(inp2)}\n" +
                "for operation - ${viewModel.operation}"
        resultTextView.text = viewModel.result
    }

    private fun findWhole(n: Double) = if (floor(n) == n) n.toInt() else n
}