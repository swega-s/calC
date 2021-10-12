package com.example.android.calc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.floor

class HomeFragment : Fragment() {

    companion object {
        const val requestCode = "REQUEST_CODE"
        const val homeFragmentKey = "HOME_FKEY"
    }

    private var flag = false

    override fun onSaveInstanceState(outState: Bundle) {
        if (flag) {
            outState.putString("result", resultTextView.text.toString())
        }
        outState.putBoolean("flag", flag)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (flag) {
                showOptionViews()
                flag = false
            } else {
                activity?.finish()
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            flag = savedInstanceState.getBoolean("flag")
            if (flag) {
                resultTextView.text = savedInstanceState.getString("result")
                showResultViews()
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        parentFragmentManager.setFragmentResultListener(InputFragment.inputFragmentKey,
            viewLifecycleOwner,
            { _, result ->
                val inp1 = result.getDouble("input1")
                val inp2 = result.getDouble("input2")
                val operation = result.getSerializable(requestCode) as Operations
                performCalculation(inp1, inp2, operation)
            })

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        addButton.setOnClickListener {
            handleClick(Operations.ADD.toString())
        }

        subButton.setOnClickListener {
            handleClick(Operations.SUBTRACT.toString())
        }

        multiplyButton.setOnClickListener {
            handleClick(Operations.MULTIPLY.toString())
        }

        divideButton.setOnClickListener {
            handleClick(Operations.DIVIDE.toString())
        }
        resetButton.setOnClickListener {
            flag = false
            showOptionViews()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleClick(operation: String) {

        parentFragmentManager.apply {
            setFragmentResult(
                homeFragmentKey,
                bundleOf(requestCode to operation)
            )
            beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_fragment, InputFragment()).commit()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun performCalculation(inp1: Double, inp2: Double, reqCode: Operations) {

        val res = when (reqCode) {
            Operations.ADD      -> inp1 + inp2
            Operations.SUBTRACT -> inp1 - inp2
            Operations.MULTIPLY -> inp1 * inp2
            Operations.DIVIDE   -> inp1 / inp2
        }

        resultTextView.text = "Result is ${findWhole(res)}\n" +
                "for inputs ${findWhole(inp1)} and ${findWhole(inp2)}\n" +
                "for operation - $reqCode"
        flag = true
        showResultViews()
    }

    private fun findWhole(n: Double) = if (floor(n) == n) n.toInt() else n

    private fun showResultViews() {
        addButton.visibility = View.GONE
        subButton.visibility = View.GONE
        multiplyButton.visibility = View.GONE
        divideButton.visibility = View.GONE

        result_layout.visibility = View.VISIBLE
    }

    private fun showOptionViews() {
        addButton.visibility = View.VISIBLE
        subButton.visibility = View.VISIBLE
        multiplyButton.visibility = View.VISIBLE
        divideButton.visibility = View.VISIBLE

        result_layout.visibility = View.GONE
    }
}