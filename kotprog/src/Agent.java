///Bukott diák,Kovacs-Bodo.Csenge@stud.u-szeged.hu
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import game.racetrack.Direction;
import game.racetrack.RaceTrackGame;
import game.racetrack.RaceTrackPlayer;
import game.racetrack.utils.Cell;
import game.racetrack.utils.Coin;
import game.racetrack.utils.PathCell;
import game.racetrack.utils.PlayerState;

import static game.racetrack.RaceTrackGame.*;

public class Agent extends RaceTrackPlayer {
    private List<PathCell> route;
    private int stepCounter = 1;
    private Cell previousState;

    private int badTries = 0;
    public Agent(PlayerState state, Random random, int[][] track, Coin[] coins, int color) {
        super(state, random, track, coins, color);
        route = BFS(state.i,state.j,track);
    }

    @Override
    public Direction getDirection(long remainingTime) {
        Cell currentCell = new Cell(state.i,state.j);
        Cell nextCell = new Cell(route.get(stepCounter).i,route.get(stepCounter).j);

        if(previousState != null && (previousState.i == state.i && previousState.j == state.j)){
            System.out.println("SZAR");

            badTries++;

            if(badTries > 15){
                route = BFS(state.i,state.j,track);
                stepCounter = 1;
            }

            nextCell = new Cell(route.get(stepCounter).i,route.get(stepCounter).j);

            Direction direction = direction(currentCell,nextCell);

            stepCounter += 1;
            return direction;
        }
        else{
            Direction direction = direction(currentCell,nextCell);
            previousState = currentCell;
            stepCounter += 1;
            return direction;
        }
    }

//    private static List<PathCell> ImprovedBFS(int i, int j, int[][] track) {
//        LinkedList<PathCell> path = new LinkedList<PathCell>();
//        LinkedList<PathCell> open = new LinkedList<PathCell>();
//        LinkedList<PathCell> close = new LinkedList<PathCell>();
//        PathCell current = new PathCell(i, j, null);
//        open.add(current);
//        while (!open.isEmpty()) {
//            current = open.pollFirst();
//            if (mask(track[current.i][current.j], FINISH)) {
//                break;
//            }
//            close.add(current);
//            for (int idx = 1; idx < DIRECTIONS.length; idx++) {
//                i = current.i + DIRECTIONS[idx].i;
//                j = current.j + DIRECTIONS[idx].j;
//                PathCell neighbor = new PathCell(i, j, current);
//                if (isNotWall(i, j, track) && !close.contains(neighbor) && !open.contains(neighbor)) {
//                    open.add(neighbor);
//                }
//            }
//        }
//        while (current != null) {
//            path.addFirst(current);
//            current = current.parent;
//        }
//        return path;
//    }
}