package com.example.android.calc

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_input.*
import android.view.ViewGroup


class InputFragment : Fragment() {

    private val viewModel: InputViewModel by activityViewModels()
    private lateinit var listener: OnResButtonClickListener
    private val TAG = "MainActivityInputFrag"

    interface OnResButtonClickListener {
        fun onResultButtonSelected()
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onattach")
        if (context is OnResButtonClickListener) {
            listener = context
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        MainActivity.isPortrait =
            resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
        MainActivity.operation?.apply {
            setViewAndChildrenEnabled(view, true)
            opButton.text = this.toString()
        } ?: run {
            setViewAndChildrenEnabled(view, false)
        }

        input1.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        input2.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI

        opButton.setOnClickListener {

            if (MainActivity.operation == null) {
                input1.text.clear()
                input2.text.clear()
                return@setOnClickListener
            }

            val inputOne = input1.text.toString()
            val inputTwo = input2.text.toString()

            if (inputOne == "" || inputTwo == "") {
                input1.text.clear()
                input2.text.clear()
                Toast.makeText(activity, "enter both values!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            try {
                viewModel.data1 = inputOne.toDouble()
                viewModel.data2 = inputTwo.toDouble()

                viewModel.resultFlag = true
                input1.text.clear()
                input2.text.clear()
                if (MainActivity.isPortrait)
                    parentFragmentManager.popBackStackImmediate()
            } catch (exception: Exception) {
                input1.text.clear()
                input2.text.clear()
                Toast.makeText(activity, "enter valid values !", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            listener.onResultButtonSelected()
            if (!MainActivity.isPortrait) {
                MainActivity.inputFragment.setViewAndChildrenEnabled(view, false)
            }
        }
    }

    internal fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
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