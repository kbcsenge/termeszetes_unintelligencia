///Termeszetes unintelligencia,kovacs-bodo.csenge@stud.u-szeged.hu
import game.racetrack.Direction;
import game.racetrack.RaceTrackPlayer;
import game.racetrack.utils.Cell;
import game.racetrack.utils.Coin;
import game.racetrack.utils.PathCell;
import game.racetrack.utils.PlayerState;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static game.racetrack.RaceTrackGame.BFS;
import static game.racetrack.RaceTrackGame.direction;

public class Agent extends RaceTrackPlayer {
    private List<PathCell> route;
    private int stepCounter = 0;
    private int lineItemCounter = 0;
    private int lineItemLength = 0;
    private List<List<PathCell>> lines;
    private boolean ujLepes = false;

    public Agent(PlayerState state, Random random, int[][] track, Coin[] coins, int color) {
        super(state, random, track, coins, color);
        this.route = BFS(state.i, state.j, track);
        this.route.remove(0);
        this.lines = this.breakToLines(this.route);

    }

    public Direction getDirection(long remainingTime) {
        Cell currentCell = new Cell(this.state.i, this.state.j);
        this.lineItemLength = this.lines.get(this.lineItemCounter).size();
        Direction direction;

        if(lineItemLength%2==0){
            if(lineItemLength==2){
                if(stepCounter==0){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                }else if(stepCounter==lineItemLength-1 && stepCounter < lineItemLength){
                    direction = new Direction(0, 0);
                    stepCounter++;
                }else{
                    direction = new Direction(this.state.vi *-1,this.state.vj *-1 );
                    this.stepCounter=0;
                    lineItemCounter++;
                }
            }else if(lineItemLength==4){
                if(stepCounter==0){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                }else if(stepCounter> 0 && stepCounter < lineItemLength){
                    direction = new Direction(0, 0);
                    stepCounter++;
                }else{
                    direction = new Direction(this.state.vi *-1,this.state.vj *-1 );
                    this.stepCounter=0;
                    lineItemCounter++;
                }
            }else{
                if(stepCounter==0){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                }
                else if(stepCounter==1){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++stepCounter;
                }else if(stepCounter>1 && stepCounter<lineItemLength-2){
                    direction = new Direction(0, 0);
                    stepCounter+=2;
                }else if(stepCounter==lineItemLength-2 && stepCounter < lineItemLength){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter+1)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter+1)).j);
                    int newI=direction(currentCell, nextCell).i;
                    int newJ=direction(currentCell, nextCell).j;
                    direction=new Direction(newI * -1, newJ * -1);
                    ++this.stepCounter;
                }else if(stepCounter==lineItemLength-1 && stepCounter < lineItemLength){
                    direction = new Direction(this.state.vi *-1,this.state.vj *-1 );
                    stepCounter++;
                }else{
                    direction = new Direction(0, 0);
                    this.stepCounter=0;
                    lineItemCounter++;
                }
            }
        }else{
            if(lineItemLength==1){
                if(stepCounter==0){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                }else{
                    direction = new Direction(this.state.vi *-1,this.state.vj *-1 );
                    this.stepCounter=0;
                    lineItemCounter++;
                }
            }else if(lineItemLength==3){
                if(stepCounter==0){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                }else if(stepCounter < lineItemLength){
                    direction = new Direction(0, 0);
                    stepCounter++;
                }else {
                    direction = new Direction(this.state.vi *-1,this.state.vj *-1 );
                    this.stepCounter=0;
                    lineItemCounter++;

                }
            }
            else{
                if(stepCounter==0){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                }
                else if(stepCounter==1){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                    direction = direction(currentCell, nextCell);
                    ++stepCounter;
                }else if(stepCounter>1 && stepCounter<lineItemLength-3){
                    direction = new Direction(0, 0);
                    stepCounter+=2;
                }else if(stepCounter==lineItemLength-3 && stepCounter < lineItemLength){
                    Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter+1)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter+1)).j);
                    int newI=direction(currentCell, nextCell).i;
                    int newJ=direction(currentCell, nextCell).j;
                    direction=new Direction(newI * -1, newJ * -1);
                    ++this.stepCounter;
                }else if(stepCounter==lineItemLength-2 && stepCounter < lineItemLength){
                    direction = new Direction(0, 0);
                    stepCounter++;
                }else if(stepCounter==lineItemLength-1 && stepCounter < lineItemLength){
                    direction = new Direction(this.state.vi *-1,this.state.vj *-1 );
                    stepCounter++;
                }else{
                    direction = new Direction(0, 0);
                    this.stepCounter=0;
                    lineItemCounter++;
                }
            }
        }
        return direction;
    }


    public static List<List<PathCell>> breakToLines(List<PathCell> route) {
        List<List<PathCell>> lines = new ArrayList<>();
        List<PathCell> actualLine = new ArrayList<>();

        for (int i = 0; i < route.size(); i++) {
            actualLine.add(route.get(i));

            if (i < route.size() - 1) {
                PathCell current = route.get(i);
                PathCell next = route.get(i + 1);

                int deltaX1 = next.i - current.i;
                int deltaY1 = next.j - current.j;

                if (i > 0) {
                    PathCell prev = route.get(i - 1);
                    int deltaX2 = current.i - prev.i;
                    int deltaY2 = current.j - prev.j;

                    if (deltaX1 * deltaY2 != deltaX2 * deltaY1) {
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
