package com.example.android.calc

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.example.android.calc.Operations.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_input.*
import java.lang.Math.floor

class MainActivity : AppCompatActivity(), HomeFragment.OnItemSelectedListener,
            InputFragment.OnResButtonClickListener {

    private val viewModel: InputViewModel by viewModels()
    private var inMainFragment = true
    private var showOperationsView = true
    private lateinit var appBarTitle: String
    private val TAG = "MainActivity"

    companion object {
        var homeFragment = HomeFragment()
        var inputFragment = InputFragment()
        var operation: Operations? = null
        var isPortrait: Boolean = false
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "on create")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            Log.d(TAG, "helelo")
            supportFragmentManager.commit {
                replace(R.id.main_fragment, homeFragment)
            }
            appBarTitle = getString(R.string.app_name)
            supportActionBar?.title = appBarTitle
        }

        if (findViewById<View?>(R.id.input_fragment) != null) {
            supportFragmentManager.popBackStack()
            val inpFrag = supportFragmentManager.findFragmentById(R.id.input_fragment)
            if (inpFrag == null || !viewModel.resultFlag) {
                supportFragmentManager.commit {
                    replace(R.id.input_fragment, inputFragment)
                }
            }
        }
    }

    override fun onStart() {
        Log.d(TAG, "onstart")
        isPortrait =
            resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState")
        inMainFragment = savedInstanceState.getBoolean("main_fragment_check")
        showOperationsView = savedInstanceState.getBoolean("op_view")
        appBarTitle = savedInstanceState.getString("action_bar_title", getString(R.string.app_name))

        supportActionBar?.title = appBarTitle
        Log.d(TAG, "restore $appBarTitle")

        if (!inMainFragment && isPortrait) {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, homeFragment)
                addToBackStack(null)
                replace(R.id.main_fragment, inputFragment)
            }
            showBackButton()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
        outState.putBoolean("main_fragment_check", inMainFragment)
        outState.putBoolean("op_view", showOperationsView)
        outState.putString("action_bar_title", appBarTitle)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed")
        if (supportFragmentManager.backStackEntryCount != 0) {
            Log.d(TAG, "hoho1")
            appBarTitle = getString(R.string.app_name)
            supportActionBar?.title = appBarTitle
            input1.text.clear()
            input2.text.clear()
            operation = null
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            if (!isPortrait) {
                inputFragment.setViewAndChildrenEnabled(findViewById(R.id.input_fragment), false)
            }
            supportFragmentManager.popBackStackImmediate()
            inMainFragment = true
        } else {
            supportActionBar?.title = getString(R.string.app_name)
            if (viewModel.resultFlag) {
                val view = findViewById<Button>(R.id.addButton)
                Log.d(TAG, view.isVisible.toString())
                Log.d(TAG, "hoho2")
                showOptionViews()
                Log.d(TAG, view.isVisible.toString())
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                viewModel.resultFlag = false
            } else {
                Log.d(TAG, "hoho3")
                finish()
            }
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun finish() {
        Log.d(TAG, "finish")
        supportActionBar?.title = getString(R.string.app_name)
        super.finish()
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

    @SuppressLint("RestrictedApi")
    override fun onResultButtonSelected() {
        inMainFragment = true
        appBarTitle = getString(R.string.app_name)
        supportActionBar?.title = appBarTitle
        performOperationForResult()
        operation = null
        if (isPortrait) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportFragmentManager.popBackStackImmediate()
        } else {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, HomeFragment())
            }
        }
    }

    override fun onOperationItemSelected() {

        appBarTitle = operation.toString()
        Log.d(TAG, appBarTitle)
        supportActionBar?.title = operation.toString()//appBarTitle
        inMainFragment = false
        if (isPortrait) {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, inputFragment)
                addToBackStack(null)
            }
            showBackButton()
        } else {
            supportFragmentManager.commit {
                replace(R.id.main_fragment, HomeFragment())
                replace(R.id.input_fragment, inputFragment)
            }
        }
    }

    private fun showBackButton() {
            showOperationsView = isPortrait
            Log.d(TAG, isPortrait.toString())
            supportActionBar?.setDisplayHomeAsUpEnabled(showOperationsView)
    }

    private fun performOperationForResult() {

        val inp1 = viewModel.data1
        val inp2 = viewModel.data2
        val res = when (operation) {
            ADD -> inp1 + inp2
            SUBTRACT -> inp1 - inp2
            MULTIPLY -> inp1 * inp2
            DIVIDE -> inp1 / inp2
            null -> TODO()
        }
        viewModel.result = "Result is ${findWhole(res)}\n" +
                "for inputs ${findWhole(inp1)} and ${findWhole(inp2)}\n" +
                "for operation - ${operation}"
        resultTextView.text = viewModel.result
    }

    private fun findWhole(n: Double) = if (floor(n) == n) n.toInt() else n

    private fun showOptionViews() {
        addButton.visibility = View.VISIBLE
        subButton.visibility = View.VISIBLE
        multiplyButton.visibility = View.VISIBLE
        divideButton.visibility = View.VISIBLE

        result_layout.visibility = View.GONE
    }
}