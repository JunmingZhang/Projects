package fall2018.csc207_project.GameCenter;

import java.util.List;

import fall2018.csc207_project.Interfaces.Game;
import fall2018.csc207_project.MineSweeper.MineSweeperGame;
import fall2018.csc207_project.SlidingTileGame.SlidingTileGame;
import fall2018.csc207_project.game2048.Game2048;

/**
 * the factory of all of the games
 */
class GameFactory {

    /**
     * create a game with a name and setting infro
     *
     * @param gameName the name of game
     * @param settings the setting information of game
     * @return null or the new game
     */
    Game createGame(String gameName, List<Object> settings) {
        if (gameName.equals(SlidingTileGame.GAMENAME))
            return new SlidingTileGame(settings);
        else if (gameName.equals(Game2048.GAMENAME)) {
            return new Game2048(settings);
        } else if (gameName.equals(MineSweeperGame.GAMENAME)) {
            return new MineSweeperGame(settings);
        }
        return null;
    }
}
