package com.example.android.calc

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val viewModel: InputViewModel by activityViewModels()
    private lateinit var listener: OnItemSelectedListener
    private val TAG = "MainActivityHomeFrag"

    interface OnItemSelectedListener {
        fun onOperationItemSelected()
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onattach")
        super.onAttach(context)
        if (context is OnItemSelectedListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "oncreate")
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        Log.d(TAG, "onstart")
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        if (viewModel.resultFlag) {
            showResultViews()
        }

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
            requireActivity().onBackPressed()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {

        Log.d(TAG, "onviewstate restored")
        if (savedInstanceState != null) {
            Log.d(TAG, "onviewstate restored2")
            if (viewModel.resultFlag) {
                Log.d(TAG, "onviewstate restored3")
                resultTextView.text = savedInstanceState.getString("result")
                showResultViews()
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onviewstate restored -opp")
        if (viewModel.resultFlag) {
            Log.d(TAG, "onviewstate restored -opp2")
            outState.putString("result", resultTextView.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    private fun handleClick(operation: Operations) {
        Log.d(TAG, operation.toString() + addButton.isVisible.toString())
        MainActivity.operation = operation
        MainActivity.inputFragment = InputFragment()
        listener.onOperationItemSelected()
    }

    private fun showResultViews() {
        addButton.visibility = View.GONE
        subButton.visibility = View.GONE
        multiplyButton.visibility = View.GONE
        divideButton.visibility = View.GONE

        result_layout.visibility = View.VISIBLE
        resultTextView.text = viewModel.result
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
    }
}