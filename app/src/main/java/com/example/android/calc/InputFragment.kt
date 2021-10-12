package com.example.android.calc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_input.*

class InputFragment: Fragment() {

    companion object {
        const val inputFragmentKey = "INPUT_FKEY"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("headerText", opTypeTextView.text.toString())
        outState.putString("btnText", opButton.text.toString())
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
        if (savedInstanceState != null) {
            opTypeTextView.text = savedInstanceState.getString("headerText")
            opButton.text = savedInstanceState.getString("btnText")
        }

        opButton.setOnClickListener {
            val bundle = Bundle()
            val inputOne = input1.text.toString()
            val inputTwo = input2.text.toString()

            if (inputOne == "" || inputTwo == "") {
                input1.text.clear()
                input2.text.clear()
                Toast.makeText(activity, "enter both values!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            try {
                bundle.putDouble("input1", inputOne.toDouble())
                bundle.putDouble("input2", inputTwo.toDouble())
            } catch (exception: Exception) {
                input1.text.clear()
                input2.text.clear()
                Toast.makeText(activity, "enter valid values !", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            bundle.putSerializable(HomeFragment.requestCode, Operations.valueOf(opButton.text.toString()))
            parentFragmentManager.setFragmentResult(inputFragmentKey, bundle)
            parentFragmentManager.popBackStackImmediate()
        }

        parentFragmentManager.setFragmentResultListener(HomeFragment.homeFragmentKey, viewLifecycleOwner,
            { _, result ->
                val data = result.getString(HomeFragment.requestCode)
                opTypeTextView.text = data
                opButton.text = data
            })
        super.onViewCreated(view, savedInstanceState)
    }

}