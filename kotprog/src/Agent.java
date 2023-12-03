///Termeszetes unintelligencia,kovacs-bodo.csenge@stud.u-szeged.hu
import game.racetrack.Direction;
import game.racetrack.RaceTrackGame;
import game.racetrack.RaceTrackPlayer;
import game.racetrack.utils.Cell;
import game.racetrack.utils.Coin;
import game.racetrack.utils.PathCell;
import game.racetrack.utils.PlayerState;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Agent extends RaceTrackPlayer {
    private int collected = 0;
    private List<PathCell> route;
    private int stepCounter = 0;
    private int lineItemCounter = 0;
    private int lineItemLength = 0;
    private List<List<PathCell>> lines;

    public Agent(PlayerState state, Random random, int[][] track, Coin[] coins, int color) {
        super(state, random, track, coins, color);
        this.route = this.coinBFS(state.i, state.j, track, 1);
        this.lines = this.breakToLines(this.route);
    }

    public Direction getDirection(long remainingTime) {
        Cell currentCell = new Cell(this.state.i, this.state.j);
        this.lineItemLength = ((List)this.lines.get(this.lineItemCounter)).size();
        Direction direction;
        if (this.stepCounter == 0) {
            Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
            direction = RaceTrackGame.direction(currentCell, nextCell);
            ++this.stepCounter;
        } else if (this.stepCounter == this.lineItemLength) {
            direction = new Direction(this.state.vi * -1, this.state.vj * -1);
            ++this.lineItemCounter;
            this.stepCounter = 0;
        } else {
            direction = new Direction(0, 0);
            ++this.stepCounter;
        }

        return direction;
    }

    public List<PathCell> coinBFS(int i, int j, int[][] track, int deepness) {
        LinkedList<PathCell> path = new LinkedList();
        LinkedList<PathCell> open = new LinkedList();
        LinkedList<PathCell> close = new LinkedList();
        PathCell current = new PathCell(i, j, (PathCell)null);
        open.add(current);

        int idx;
        while(!open.isEmpty()) {
            current = (PathCell)open.pollFirst();
            if (deepness > 0 && RaceTrackGame.mask(track[current.i][current.j], 16) || deepness == 0 && RaceTrackGame.mask(track[current.i][current.j], 4)) {
                break;
            }

            close.add(current);

            for(idx = 0; idx < RaceTrackGame.DIRECTIONS.length; ++idx) {
                i = current.i + RaceTrackGame.DIRECTIONS[idx].i;
                j = current.j + RaceTrackGame.DIRECTIONS[idx].j;
                PathCell neighbor = new PathCell(i, j, current);
                if (RaceTrackGame.isNotWall(i, j, track) && !close.contains(neighbor) && !open.contains(neighbor)) {
                    open.add(neighbor);
                }
            }
        }

        while(current != null) {
            path.addFirst(current);
            current = current.parent;
        }

        if (deepness > 0) {
            idx = ((PathCell)path.get(path.size() - 1)).i;
            int newJ = ((PathCell)path.get(path.size() - 1)).j;
            path.remove(path.size() - 1);
            path.addAll(this.coinBFS(idx, newJ, track, deepness - 1));
        }

        return path;
    }

    public List<List<PathCell>> breakToLines(List<PathCell> route) {
        List<List<PathCell>> lines = new ArrayList();
        List<PathCell> actualLine = new ArrayList();

        for(int i = 1; i < route.size(); ++i) {
            PathCell cell = (PathCell)route.get(i);
            if (i != 0 && !isEgyenesen(actualLine, cell)) {
                lines.add(new ArrayList(actualLine));
                actualLine.clear();
                actualLine.add(cell);
            } else {
                actualLine.add(cell);
            }
        }

        lines.add(new ArrayList(actualLine));
        return lines;
    }

    private static boolean isEgyenesen(List<PathCell> szakasz, PathCell ujPont) {
        if (szakasz.size() < 2) {
            return true;
        } else {
            int deltaX1 = ((PathCell)szakasz.get(1)).i - ((PathCell)szakasz.get(0)).i;
            int deltaY1 = ((PathCell)szakasz.get(1)).j - ((PathCell)szakasz.get(0)).j;
            int deltaX2 = ujPont.i - ((PathCell)szakasz.get(0)).i;
            int deltaY2 = ujPont.j - ((PathCell)szakasz.get(0)).j;
            return deltaX1 * deltaY2 == deltaX2 * deltaY1;
        }
    }
}
