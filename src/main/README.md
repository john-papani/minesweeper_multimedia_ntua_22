- ###  **Minesweeper.java** 
``` 
It is the main file from which the game starts. Compiling and running this file builds the basic UI, as defined in the project with the appropriate MainBar. Also here all the functions of MenuItems are implemented, such as creating or loading a game scenario.
```
- ###  **Board.java**
``` 
In this file, it's the whole 'game' since this is where the main table with known squares is constructed. Beyond from build this file initializes and rebuilds on win/loss, but mostly refreshes the content, based on the player's mouse selections, opening the correct boxes each time.
```
- ###  **Tile.java** 
``` 
In this file, there is the functionality of each such box, so that it is easier both to find it in space (x,y axis), but also to capture it based on some already pre-installed photos (./img/)
```

- ###  **GameTimer.java** 
``` 
Finally, a timer is properly defined in this file, where every time a new round starts, it also starts to count down. Obviously the start time is set in the initialization of the game (and in particular the table where the specifications of each round are selected).
```