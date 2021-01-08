package com.silvanoalbuquerque.mynotesapp.db.entities

import androidx.annotation.ColorRes
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIFTH_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIRST_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FOURTH_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_SECOND_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_THIRD_INDEX

enum class NoteColor(val pickerIndex: Int, @ColorRes val colorResource: Int) {
    FIRST_COLOR_NOTE(COLOR_PICKER_FIRST_INDEX, R.color.colorNote1),

    SECOND_COLOR_NOTE(COLOR_PICKER_SECOND_INDEX, R.color.colorNote2),

    THIRD_COLOR_NOTE(COLOR_PICKER_THIRD_INDEX, R.color.colorNote3),

    FOURTH_COLOR_NOTE(COLOR_PICKER_FOURTH_INDEX, R.color.colorNote4),

    FIFTH_COLOR_NOTE(COLOR_PICKER_FIFTH_INDEX, R.color.colorNote5),
}