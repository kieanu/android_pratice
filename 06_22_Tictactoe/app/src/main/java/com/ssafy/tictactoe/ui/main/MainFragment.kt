package com.ssafy.tictactoe.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.ssafy.tictactoe.R
import com.ssafy.tictactoe.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var buttons: Array<Array<Button>>
    private var player1Turn = true
    private var roundCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = Array(3) { row ->
            Array(3) { column ->
                initButton(row, column)
            }
        }

        val resetButton: Button = binding.btnReset
        resetButton.setOnClickListener {
            resetGame()
        }
    }

    private fun initButton(row: Int, column: Int): Button {
        val buttonId = resources.getIdentifier("btn$row$column", "id", activity?.packageName)
        val button = binding.root.findViewById<Button>(buttonId)
        button.setOnClickListener {
            onClick(it)
        }
        return button
    }

    // 버튼 클릭 이벤트
    private fun onClick(v: View) {
        val button = v as Button

        if (button.text.toString().isNotEmpty()) { return }

        if (player1Turn) { button.text = "X" }
        else { button.text = "O" }

        roundCount++

        if (checkForWin()) {
            if (player1Turn) {
                showWinner("Player 1 wins!")
            } else {
                showWinner("Player 2 wins!")
            }
        } else if (roundCount == 9) {
            showWinner("It's a draw!")
        } else {
            player1Turn = !player1Turn
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array(3) { row ->
            Array(3) { column ->
                buttons[row][column].text.toString()
            }
        }

        for (i in 0 until 3) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0].isNotEmpty()) {
                return true
            }
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i].isNotEmpty()) {
                return true
            }
        }

        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0].isNotEmpty()) {
            return true
        }
        if (field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2].isNotEmpty()) {
            return true
        }

        return false
    }

    private fun showWinner(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun resetGame() {
        roundCount = 0
        player1Turn = true

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                buttons[i][j].text = ""
            }
        }
    }
}