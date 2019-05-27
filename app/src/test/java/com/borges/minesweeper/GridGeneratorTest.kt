package com.borges.minesweeper

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class GridGeneratorTest {

    @Test
    fun `it creates a grid filled with bombs and neighbor counts`() {
        val width = 6
        val height = 12
        val bombCount = 10

        val grid = Grid(width, height, bombCount)

        expectThat(grid.gridWidth).isEqualTo(width)
        expectThat(grid.gridHeight).isEqualTo(height)
        expectThat(grid.bombCount).isEqualTo(bombCount)
    }

}