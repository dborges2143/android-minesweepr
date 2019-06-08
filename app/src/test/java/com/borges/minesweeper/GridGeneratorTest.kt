package com.borges.minesweeper

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class GridGeneratorTest {

    @Test
    fun `it creates a grid filled with bombs and neighbor counts`() {
        val width = 6
        val height = 12
        val bombCount = 10
        val safeCoordinate = Pair(3, 4)

        val grid = Grid(width, height, bombCount)
        grid.generateGrid(safeCoordinate = safeCoordinate)

        expectThat(grid.width).isEqualTo(width)
        expectThat(grid.height).isEqualTo(height)
        expectThat(grid.bombCount).isEqualTo(bombCount)
        expectThat(grid.cell(safeCoordinate).hasNoNeighboringBombs).isTrue()
    }

}