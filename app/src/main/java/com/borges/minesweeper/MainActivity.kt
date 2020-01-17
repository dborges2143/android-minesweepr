package com.borges.minesweeper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.borges.minesweeper.data.Cell
import com.borges.minesweeper.data.Grid
import com.borges.minesweeper.data.GridDifficulty
import com.borges.minesweeper.dialogs.DifficultySelectorDialog
import com.borges.minesweeper.dialogs.InfoDialog
import com.borges.minesweeper.fragments.EasyGridFragment
import com.borges.minesweeper.fragments.HardGridFragment
import com.borges.minesweeper.fragments.MediumGridFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DifficultySelectorDialog.ChangeDifficultyListener {
    private lateinit var mInterstitialAd: InterstitialAd
    private val manager = supportFragmentManager
    private val vibrator: Vibrator
        get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private var selectedDifficulty = GridDifficulty.MEDIUM
    private var bombsLeft = selectedDifficulty.bombCount

    private lateinit var grid: Grid
    private var totalTime = 0
    private val handler = Handler()
    private val runnable = Runnable {
        totalTime += 1
        textTimer.text = totalTime.toString()
        tickTimer()
    }
    private val adLoaderRunnable = Runnable {
        loadAd()
    }

    private val mineColor by lazy { resources.getColor(R.color.colorMineCell, null) }
    private val concealedColor by lazy { resources.getColor(R.color.colorConcealedCell, null) }
    private val successColor by lazy { resources.getColor(R.color.colorPrimary, null) }
    private val mineIcon by lazy { getDrawable(R.drawable.ic_flag_black_24dp) }

    private fun buttonId(x: Int, y: Int): String = "button_${x}_$y"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(minesweepr_toolbar)

        initializeAds()

        updateDifficulty()
        buttonDifficulty.setOnClickListener { openDifficultyDialog() }
        buttonInfo.setOnClickListener { openInfoDialog() }
    }

    private fun initializeAds() {
        MobileAds.initialize(this)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                mInterstitialAd.loadAd(AdRequest.Builder().build())
                buttonNewGame.isEnabled = true
                buttonDifficulty.isEnabled = true
            }
        }
    }

    private fun openDifficultyDialog() {
        val dialog = DifficultySelectorDialog()
        val bundle = Bundle()
        bundle.putString("difficulty", selectedDifficulty.label)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "Select Difficulty")
    }

    private fun openInfoDialog() {
        val dialog = InfoDialog()
        dialog.show(supportFragmentManager, "About Minesweepr")
    }

    override fun onDifficultyChanged(difficulty: GridDifficulty) {
        selectedDifficulty = difficulty
        updateDifficulty()
    }

    private fun updateDifficulty() {
        val fragment = when(selectedDifficulty) {
            GridDifficulty.EASY -> EasyGridFragment()
            GridDifficulty.MEDIUM -> MediumGridFragment()
            GridDifficulty.HARD -> HardGridFragment()
        }
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

        grid = Grid(
            width = selectedDifficulty.width,
            height = selectedDifficulty.height,
            bombCount = selectedDifficulty.bombCount
        )
        updateBombsLeftLabel()
        buttonDifficulty.text = selectedDifficulty.label
    }

    private fun newGame() {
        performActionOnAllButtons {
            it.text = ""
            it.setBackgroundColor(concealedColor)
            it.setTextColor(Color.WHITE)
            it.isEnabled = true
            it.setCompoundDrawables(null, null, null, null)
        }
        grid.reset()
        resetTimer()
        bombsLeft = selectedDifficulty.bombCount
        updateBombsLeftLabel()
    }

    private fun updateBombsLeftLabel() {
        bombsLeft = if (grid.isInitialized)
            selectedDifficulty.bombCount - grid.markedBombs
        else
            selectedDifficulty.bombCount
        textBombsLeft.text = bombsLeft.toString()
    }

    private fun performActionOnAllButtons(action: (Button) -> Unit) {
        repeat(selectedDifficulty.width) { x ->
            repeat(selectedDifficulty.height) { y ->
                val button: Button = findViewById(resources.getIdentifier(buttonId(x, y), "id", packageName))
                action(button)
            }
        }
    }

    private fun gameWon() {
        handler.removeCallbacks(runnable)
        highlightBombs()
        Toast.makeText(this, "YOU WIN!!", Toast.LENGTH_SHORT).show()
        disableGrid()

        handler.postDelayed(adLoaderRunnable, 3000)
    }

    private fun gameLost() {
        handler.removeCallbacks(runnable)
        revealAllCells()
        Toast.makeText(this, "YOU LOSE", Toast.LENGTH_SHORT).show()
        disableGrid()

        handler.postDelayed(adLoaderRunnable, 3000)
    }

    private fun loadAd() {
        if (mInterstitialAd.isLoaded) {
            buttonNewGame.isEnabled = false
            buttonDifficulty.isEnabled = false
            mInterstitialAd.show()
        } else {
            println("The interstitial wasn't loaded yet")
        }
    }

    private fun disableGrid() {
        performActionOnAllButtons { it.isEnabled = false }
    }

    private fun initializeButtonGrid() {
        performActionOnAllButtons {

            it.setOnLongClickListener { view ->
                vibrator.vibrate(VibrationEffect.createOneShot(100, 100))

                if (grid.isInitialized) {
                    val cell = findCell(resources.getResourceName(view.id))

                    if (!cell.isRevealed) {

                        if (cell.isMarkedAsBomb) {
                            cell.unMarkBomb()
                            view.setBackgroundColor(concealedColor)
                            val button = view as Button
                            button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                        } else {
                            cell.markAsBomb()
                            val button = view as Button
                            button.setCompoundDrawablesWithIntrinsicBounds(mineIcon, null, null, null)
                            if (grid.isClean) gameWon()
                        }
                        
                        updateBombsLeftLabel()
                    }

                } else clickCell(it)

                true
            }
        }
    }

    fun clickCell(view: View) {
        if (grid.isInitialized) {
            val cell = findCell(resources.getResourceName(view.id))
            revealCell(cell, view as Button)

            when {
                cell.isBomb -> gameLost()
                cell.hasNoNeighboringBombs -> clickNeighbors(cell)
                grid.isClean -> gameWon()
            }

        } else {
            val safeCoordinate = findButtonCoordinate(resources.getResourceName(view.id))
            startNewGame(view, safeCoordinate)
        }
    }

    private fun startNewGame(view: View, safeCoordinate: Pair<Int, Int>) {
        grid.generateGrid(safeCoordinate)
        initializeButtonGrid()
        buttonNewGame.isEnabled = true
        buttonNewGame.setOnClickListener { newGame() }
        clickCell(view)
        tickTimer()
    }

    private fun tickTimer() {
        handler.postDelayed(runnable, 1000)
    }

    private fun resetTimer() {
        handler.removeCallbacks(runnable)
        totalTime = 0
        textTimer.text = totalTime.toString()
    }

    private fun clickNeighbors(cell: Cell) {
        val neighbors = grid.neighbors(cell.locationX, cell.locationY)
        neighbors.filter { !it.isRevealed }.forEach { neighbor ->
            val neighborButton: Button = findViewById(resources.getIdentifier(buttonId(neighbor.locationX, neighbor.locationY), "id", packageName))
            clickCell(neighborButton)
        }
    }

    private fun highlightBombs() {
        performActionOnAllButtons {
            val cell = findCell(resources.getResourceName(it.id))
            if (cell.isBomb) {
                it.setCompoundDrawablesWithIntrinsicBounds(mineIcon, null, null, null)
                it.text = ""
                it.setBackgroundColor(successColor)
            }
        }
    }

    private fun revealAllCells() {
        performActionOnAllButtons {
            val cell = findCell(resources.getResourceName(it.id))
            revealCell(cell, it)
        }
    }

    private fun findCell(resourceName: String): Cell {
        val coordinate = findButtonCoordinate(resourceName)
        return grid.cell(coordinate)
    }

    private fun findButtonCoordinate(resourceName: String): Pair<Int, Int> {
        val location = resourceName.substringAfter('_')
        val x = location[0].minus('0')
        val y = location.substring(2).toInt()
        return x to y
    }

    private fun revealCell(cell: Cell, button: Button) {
        if (cell.isBomb) {
            button.setCompoundDrawablesWithIntrinsicBounds(mineIcon, null, null, null)
            button.text = ""
        } else {
            button.setCompoundDrawables(null, null, null, null)
            button.text = cell.toString()
            button.setTextColor(resources.getColor(cell.textColor, null))
        }
        button.setBackgroundColor(resources.getColor(cell.backgroundColor, null))
        cell.isRevealed = true
        button.isEnabled = false
    }

}
