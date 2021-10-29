package com.example.android.mycalc

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {

    private lateinit var listener: OperationClickListener
    private var operation: Operations? = null
    private var isInResultView: Boolean = false

    interface OperationClickListener {
        fun onOperationClicked(operation: Operations?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OperationClickListener) {
            listener = context
        }
        else {
            throw ClassCastException(context.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        if (savedInstanceState != null) {
            savedInstanceState.getSerializable("operation")?.let {
                operation = it as Operations
            }
            isInResultView = savedInstanceState.getBoolean("is_in_result_view")
        }

        return rootView
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (isInResultView) {
            resultTextView.text = savedInstanceState?.getString("result")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        addButton.setOnClickListener {
            handleClick(Operations.ADD)
        }

        subButton.setOnClickListener {
            handleClick(Operations.SUBTRACT)
        }

        multiplyButton.setOnClickListener {
            handleClick(Operations.MULTIPLY)
        }

        divideButton.setOnClickListener {
            handleClick(Operations.DIVIDE)
        }

        resetButton.setOnClickListener {
            handleClick(Operations.ADD)
            (activity as MainActivity).setShowResultView(false)
            showOptionViews()
        }
    }

    private fun handleClick(operation: Operations?) {

        if (operation != null) {
            this.operation = operation
        }

        val inputFragment = InputFragment()
        inputFragment.setOperation(operation.toString())
        with(parentFragmentManager) {
            commit {
                replace(R.id.inputContainer, inputFragment, "input_fragment")
            }
        }
        listener.onOperationClicked(operation)
    }

    fun getInputs(inp1: Double, inp2: Double) {
        val res = when (operation) {
            Operations.ADD -> inp1 + inp2
            Operations.SUBTRACT -> inp1 - inp2
            Operations.MULTIPLY -> inp1 * inp2
            Operations.DIVIDE -> inp1 / inp2
            else -> TODO()
        }

        val result = "Result is ${findWhole(res)}\n" +
                "for inputs ${findWhole(inp1)} and ${findWhole(inp2)}\n" +
                "for operation - $operation"
        resultTextView.text = result
        showResultViews()
        isInResultView = true
    }

    private fun findWhole(n: Double) = if (Math.floor(n) == n) n.toInt() else n

    private fun showOptionViews() {
        addButton.visibility = View.VISIBLE
        subButton.visibility = View.VISIBLE
        multiplyButton.visibility = View.VISIBLE
        divideButton.visibility = View.VISIBLE

        resultTextView.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    private fun showResultViews() {
        addButton.visibility = View.GONE
        subButton.visibility = View.GONE
        multiplyButton.visibility = View.GONE
        divideButton.visibility = View.GONE

        resultTextView.visibility = View.VISIBLE
        resetButton.visibility = View.VISIBLE
    }

    fun onBackPressedToOperations() {
        showOptionViews()
        isInResultView = false
    }

    fun onBackPressedToResultViews() {
        showResultViews()
        isInResultView = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("operation", operation)
        outState.putBoolean("is_in_result_view", isInResultView)
        if (isInResultView) {
            outState.putString("result", resultTextView.text.toString())
        }
    }
}