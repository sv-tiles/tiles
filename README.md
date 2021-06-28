# tiles

[![Build Status](https://travis-ci.com/sv-tiles/tiles.svg?branch=main)](https://travis-ci.com/sv-tiles/tiles)
[![Coverage Status](https://coveralls.io/repos/github/sv-tiles/tiles/badge.svg?branch=main)](https://coveralls.io/github/sv-tiles/tiles?branch=main)

Scala project based on Carcassonne

## Quickstart

### Run `sbt run`

### Add players

Add a player with the `Add Player` button. Enter a name and change the color if needed.

### Place tiles

Place tiles by dragging the current tile from the stack (top left) and releasing it on the board.

Rotate the tile with the `Left` and `Right` buttons (on the left side), or use the fourth and fifth mouse buttons (
forward and backward).

### Place people

Double-click to place people on the current tile. If you move the mouse over the tiles, a preview of the area is
displayed. A solid line represents a closed area, a dashed line represents an area that is still open.

### End turn

End your turn by clicking `End turn` button, or use the middle mouse button on the placed tile. If an area was closed,
the points will be distributed in the player overview.

The next player can now start his turn and [place his tile](#place-tiles).
