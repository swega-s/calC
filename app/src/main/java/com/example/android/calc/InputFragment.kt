package com.example.android.calc

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_input.*

class InputFragment : Fragment() {

    private val viewModel: InputViewModel by activityViewModels()
    private lateinit var listener: OnResButtonClickListener

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
        viewModel.isPortrait = resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE

        input1.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        input2.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI

        opButton.text = viewModel.operation.toString()
        opButton.setOnClickListener {
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
}