package com.example.android.calc

import androidx.lifecycle.ViewModel

data class InputViewModel (
    var data1: Double = 0.0,
    var data2: Double = 0.0,
    var result: String = "",
    var operation: Operations? = null,
    var resultFlag: Boolean = false,
    var isPortrait: Boolean = false,
    val homeFragment: HomeFragment = HomeFragment(),
    var inputFragment: InputFragment = InputFragment()
) : ViewModel()