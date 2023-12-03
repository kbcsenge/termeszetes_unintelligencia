///Termeszetes unintelligencia,kovacs-bodo.csenge@stud.u-szeged.hu
import game.racetrack.Direction;
import game.racetrack.RaceTrackGame;
import game.racetrack.RaceTrackPlayer;
import game.racetrack.utils.Cell;
import game.racetrack.utils.Coin;
import game.racetrack.utils.PathCell;
import game.racetrack.utils.PlayerState;

import java.util.*;

import static game.racetrack.RaceTrackGame.*;

public class Agent extends RaceTrackPlayer {
    private int stepCounter = 0;
    private int lineItemCounter = 0;
    private List<List<PathCell>> lines;

    public Agent(PlayerState state, Random random, int[][] track, Coin[] coins, int color) {
        super(state, random, track, coins, color);

        List<PathCell> route;

        if (coins.length == 0) {
            route = BFS(state.i, state.j, track);
        } else {

            Coin best = null;
            for (Coin c : coins) {
                if (best == null) {
                    best = c;
                } else {
                    if (c.value > best.value) {
                        best = c;
                    }
                }
            }
            route = coinBFS(state.i, state.j, track, 1, best);
        }
        this.lines = breakToLines(route);
        this.lines.get(0).remove(0);
    }

    public Direction getDirection(long remainingTime) {
        Cell currentCell = new Cell(this.state.i, this.state.j);
        int lineItemLength = this.lines.get(this.lineItemCounter).size();
        Direction direction;

        if (lineItemLength % 2 == 0) {
            if (lineItemLength == 2) {
                if (stepCounter == 0) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter == lineItemLength - 1) {
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else {
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else if (lineItemLength == 4) {
                if (stepCounter == 0) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter > 0 && stepCounter < lineItemLength) {
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else {
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else {
                if (stepCounter == 0) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter == 1) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++stepCounter;
                } else if (stepCounter > 1 && stepCounter < lineItemLength - 2) {
                    direction = new Direction(0, 0);
                    stepCounter += 2;
                } else if (stepCounter == lineItemLength - 2) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).i, this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).j);
                    int newI = direction(currentCell, nextCell).i;
                    int newJ = direction(currentCell, nextCell).j;
                    direction = new Direction(newI * -1, newJ * -1);
                    ++this.stepCounter;
                } else {
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            }
        } else {
            if (lineItemLength == 1) {
                if (stepCounter == 0) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else {
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else if (lineItemLength == 3) {
                if (stepCounter == 0) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter < lineItemLength) {
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else {
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else {
                if (stepCounter == 0) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter == 1) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, (this.lines.get(this.lineItemCounter).get(this.stepCounter).j));
                    direction = direction(currentCell, nextCell);
                    ++stepCounter;
                } else if (stepCounter > 1 && stepCounter < lineItemLength - 3) {
                    direction = new Direction(0, 0);
                    stepCounter += 2;
                } else if (stepCounter == lineItemLength - 3) {
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).i, this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).j);
                    int newI = direction(currentCell, nextCell).i;
                    int newJ = direction(currentCell, nextCell).j;
                    direction = new Direction(newI * -1, newJ * -1);
                    ++this.stepCounter;
                } else if (stepCounter == lineItemLength - 2) {
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else {
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            }
        }

        return direction;
    }

    public List<PathCell> coinBFS(int i, int j, int[][] track, int deepness, Coin best) {
        LinkedList<PathCell> path = new LinkedList();
        LinkedList<PathCell> open = new LinkedList();
        LinkedList<PathCell> close = new LinkedList();
        PathCell current = new PathCell(i, j, (PathCell) null);
        open.add(current);

        while (!open.isEmpty()) {
            current = open.pollFirst();

            if (deepness > 0 && current.i == best.i && current.j == best.j) {
                break;
            }

            close.add(current);

            for (int idx = 0; idx < RaceTrackGame.DIRECTIONS.length; ++idx) {
                i = current.i + RaceTrackGame.DIRECTIONS[idx].i;
                j = current.j + RaceTrackGame.DIRECTIONS[idx].j;
                PathCell neighbor = new PathCell(i, j, current);
                if (RaceTrackGame.isNotWall(i, j, track) && !close.contains(neighbor) && !open.contains(neighbor)) {
                    open.add(neighbor);
                }
            }
        }

        while (current != null) {
            path.addFirst(current);
            current = current.parent;
        }

        if (deepness > 0) {
            int newX = (path.get(path.size() - 1)).i;
            int newJ = (path.get(path.size() - 1)).j;
            path.remove(path.size() - 1);
            path.addAll(BFS(newX, newJ, track));
        }

        return path;
    }

    public static List<List<PathCell>> breakToLines(List<PathCell> route) {
        List<List<PathCell>> lines = new ArrayList<>();
        List<PathCell> actualLine = new ArrayList<>();
        List<PathCell> seenCells = new ArrayList<>();

        for (int i = 0; i < route.size(); i++) {
            PathCell current = route.get(i);

            if (seenCells.contains(current)) {
                lines.add(new ArrayList<>(actualLine));
                actualLine.clear();
                seenCells.clear();
            }

            actualLine.add(current);
            seenCells.add(current);

            if (i > 0) {
                PathCell prev = route.get(i - 1);
                int deltaX1 = current.i - prev.i;
                int deltaY1 = current.j - prev.j;

                if (i < route.size() - 1) {
                    PathCell next = route.get(i + 1);
                    int deltaX2 = next.i - current.i;
                    int deltaY2 = next.j - current.j;

                    if ((deltaX1 * deltaY2 != deltaX2 * deltaY1) && (deltaX2 * deltaY1 != deltaX1 * deltaY2)) {
                        lines.add(new ArrayList<>(actualLine));
                        actualLine = new ArrayList<>();

                    }
                }
            }
        }

            if (!actualLine.isEmpty()) {
                lines.add(new ArrayList<>(actualLine));
            }

            return lines;
        }
}
