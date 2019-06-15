package com.borges.minesweeper.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.borges.minesweeper.R
import com.borges.minesweeper.data.GridDifficulty
import kotlinx.android.synthetic.main.dialog_difficulty_selector.*

class DifficultySelectorDialog : DialogFragment() {

    interface ChangeDifficultyListener {
        fun onDifficultyChanged(difficulty: GridDifficulty)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_difficulty_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (arguments?.getString("difficulty")) {
            GridDifficulty.EASY.label -> radioGroupDifficulty.check(radioEasy.id)
            GridDifficulty.MEDIUM.label -> radioGroupDifficulty.check(radioMedium.id)
            GridDifficulty.HARD.label -> radioGroupDifficulty.check(radioHard.id)
        }

        radioGroupDifficulty.setOnCheckedChangeListener { group, checkedId ->
            val newDifficulty = when (checkedId) {
                radioEasy.id -> GridDifficulty.EASY
                radioMedium.id -> GridDifficulty.MEDIUM
                radioHard.id -> GridDifficulty.HARD
                else -> GridDifficulty.MEDIUM
            }
            val listener = activity as ChangeDifficultyListener
            listener.onDifficultyChanged(newDifficulty)
            dismiss()
        }
    }
}