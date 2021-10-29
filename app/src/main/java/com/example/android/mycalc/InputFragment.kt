package com.example.android.mycalc

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_input.*

class InputFragment: Fragment() {

    private var operation: String? = null
    private lateinit var listener: ResultButtonClickListener
    private var disableThisFragment: Boolean = false

    interface ResultButtonClickListener {
        fun onResultBtnClicked(flag: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ResultButtonClickListener) {
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
        val rootView = inflater.inflate(R.layout.fragment_input, container, false)

        if (savedInstanceState != null) {
            operation = savedInstanceState.getString("operation")
            disableThisFragment = savedInstanceState.getBoolean("disable_this_fragment")
        }

        return rootView
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setViewAndChildrenEnabled(this.requireView(), disableThisFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        edittext_input1.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        edittext_input2.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI

        operation?.let {
            opButton.text = it
            disableThisFragment = true
        } ?: run {
            disableThisFragment = false
        }
        setViewAndChildrenEnabled(view, disableThisFragment)

        opButton.setOnClickListener {
            val data1 = edittext_input1.text.toString()
            val data2 = edittext_input2.text.toString()

            if (data1 == "" || data2 == "") {
                edittext_input1.text.clear()
                edittext_input2.text.clear()
                Toast.makeText(this.context, "Please enter both values!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val doubleData1: Double
            val doubleData2: Double

            try {
                doubleData1 = data1.toDouble()
                doubleData2 = data2.toDouble()
            } catch (exception: Exception) {
                edittext_input1.text.clear()
                edittext_input2.text.clear()
                Toast.makeText(this.context, "Please enter valid values!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val homeFragment = parentFragmentManager.findFragmentByTag("home_fragment")
            (homeFragment as HomeFragment).getInputs(doubleData1, doubleData2)

            edittext_input1.text.clear()
            edittext_input2.text.clear()

            listener.onResultBtnClicked(true)
            operation = null
            setViewAndChildrenEnabled(view, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("operation", operation)
        outState.putBoolean("disable_this_fragment", disableThisFragment)
    }

    fun setOperation(operation: String) {
        this.operation = operation
    }

    fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }
}