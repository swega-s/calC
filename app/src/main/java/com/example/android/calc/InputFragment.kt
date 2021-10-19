package com.example.android.calc

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_input.*

class InputFragment : Fragment() {

    private val viewModel: InputViewModel by activityViewModels()
    private lateinit var listener: OnResButtonClickListener
    private lateinit var value1 : String//= viewModel.data1.toString()
    private lateinit var value2 : String//= viewModel.data2.toString()

    interface OnResButtonClickListener {
        fun resultButtonSelected()
    }

    override fun onAttach(context: Context) {
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
        Log.d("tag", "created")


//        if (!viewModel.isPortrait) {
//            if (input1 != null && input2 != null) {
//                input1.setText("")
//                input2.setText("")
//            }
//        }

        viewModel.isPortrait = resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
        viewModel.operation?.apply {
            opButton.text = this.toString()
        } ?: opButton.setText(getString(R.string.op_btn_default))
//        if (viewModel.operation != null) {
//            opButton.text = viewModel.operation.toString()
//        } else {
//            opButton.text = "CLICK"
//        }

        input1.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        input2.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI

        opButton.setOnClickListener {

            if (viewModel.operation == null) {
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
//                val t = viewModel.data1.toDouble()
//                val t2 = viewModel.data2.toDouble()
                viewModel.data1 = inputOne.toDouble()
                viewModel.data2 = inputTwo.toDouble()

                viewModel.resultFlag = true
                input1.text.clear()
                input2.text.clear()
                if (viewModel.isPortrait)
                    parentFragmentManager.popBackStackImmediate()
            } catch (exception: Exception) {
                input1.text.clear()
                input2.text.clear()
                Toast.makeText(activity, "enter valid values !", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            listener.resultButtonSelected()
        }
    }

//    override fun onStop() {
//        if (!viewModel.isPortrait) {
//            if (input1 != null && input2 != null) {
//                input1.setText("")
//                input2.setText("")
//            }
//        }
//        super.onStop()
//    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        Log.d("tag", "restore")
//        if (savedInstanceState != null) {
//            input1.setText(savedInstanceState.getString("value1"))
//            input2.setText(savedInstanceState.getString("value2"))
//        }
//        super.onViewStateRestored(savedInstanceState)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        Log.d("tag", "save $value1 hello save $value2")
//        if (input1 != null && input2 != null) {
//            outState.putString("value1", input1.text.toString())
//            outState.putString("value2", input2.text.toString())
//        }
//        super.onSaveInstanceState(outState)
//    }
}