///Termeszetes unintelligencia,kovacs-bodo.csenge@stud.u-szeged.hu
import java.util.*;
import java.util.List;

import game.racetrack.Direction;
import game.racetrack.RaceTrackPlayer;
import game.racetrack.utils.Cell;
import game.racetrack.utils.Coin;
import game.racetrack.utils.PathCell;
import game.racetrack.utils.PlayerState;

import static game.racetrack.RaceTrackGame.*;

public class Agent extends RaceTrackPlayer {
    private List<PathCell> route;
    private int stepCounter = 0;
    private Cell previousState;
    private int badTries = 0;
        boolean isCoin = false;
        int collectedCoins = 0;

    public Agent(PlayerState state, Random random, int[][] track, Coin[] coins, int color) {
        super(state, random, track, coins, color);
        route = ImprovedBFS(state.i,state.j,track, coins);
    }

    @Override
    public Direction getDirection(long remainingTime) {
        Direction direction;

        if(isCoin && collectedCoins < coins.length){
            route = ImprovedBFS(state.i,state.j,track, coins);
            stepCounter = 0;
        }else if(isCoin && collectedCoins == coins.length){
            route = BFS(state.i,state.j,track);
            stepCounter = 0;
        }

        Cell currentCell = new Cell(state.i,state.j);
        Cell nextCell = new Cell(route.get(stepCounter).i,route.get(stepCounter).j);

                if(mask(track[nextCell.i][nextCell.j], COIN)){
                    isCoin = true;
                    collectedCoins++;
                }

        if(previousState != null && (previousState.i == state.i && previousState.j == state.j)){
            badTries++;

            if(badTries > 30){
                route = BFS(state.i,state.j,track);
                stepCounter = 1;
                nextCell = new Cell(route.get(stepCounter).i,route.get(stepCounter).j);
            }

            direction = direction(currentCell,nextCell);

            stepCounter += 1;
        }
        else{
            direction = direction(currentCell,nextCell);
            previousState = currentCell;
            stepCounter += 1;
        }

        return direction;
    }

    private static List<PathCell> ImprovedBFS(int i, int j, int[][] track, Coin[] coins) {
        LinkedList<PathCell> path = new LinkedList<>();

        // Rendezzük az érméket az értékük alapján csökkenő sorrendbe
        Arrays.sort(coins, (c1, c2) -> Integer.compare(c2.value, c1.value));

        // Többrétegű BFS minden érmére
        for (Coin currentCoin : coins) {
            LinkedList<PathCell> open = new LinkedList<>();
            LinkedList<PathCell> close = new LinkedList<>();
            PathCell current = new PathCell(i, j, null);
            open.add(current);

            // BFS az aktuális érméhez
            while (!open.isEmpty()) {
                current = open.pollFirst();

                // Ha találtunk egy érmét, akkor megszakítjuk a belső ciklust
                if (current.i == currentCoin.i && current.j == currentCoin.j) {
                    break;
                }

                close.add(current);

                for (int idx = 0; idx < DIRECTIONS.length; idx++) {
                    int nextI = current.i + DIRECTIONS[idx].i;
                    int nextJ = current.j + DIRECTIONS[idx].j;
                    PathCell neighbor = new PathCell(nextI, nextJ, current);

                    // Ellenőrizzük, hogy a szomszéd nem fal és nem szerepel a close vagy open listákon
                    if (isNotWall(nextI, nextJ, track) && !close.contains(neighbor) && !open.contains(neighbor)) {
                        open.add(neighbor);
                    }
                }
            }

            // Adjuk hozzá az érme felé vezető utat a path listához
            while (current != null) {
                path.addFirst(current);
                current = current.parent;
            }
        }

        return path;
    }
}
