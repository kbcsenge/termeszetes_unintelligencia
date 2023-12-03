///Termeszetes unintelligencia,kovacs-bodo.csenge@stud.u-szeged.hu
import game.racetrack.Direction;
import game.racetrack.RaceTrackGame;
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
        this.lines = this.breakToLines(this.route);
        System.out.println(lines);
    }

    public Direction getDirection(long remainingTime) {
        Cell currentCell = new Cell(this.state.i, this.state.j);
        this.lineItemLength = this.lines.get(this.lineItemCounter).size();
        System.out.println("line lenght"+lineItemLength);
        Direction direction;

        //Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(lineItemLength-1).i, this.lines.get(this.lineItemCounter).get(lineItemLength-1).j);
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
            }else if(stepCounter==1 && stepCounter < lineItemLength){
                direction = new Direction(0, 0);
                stepCounter++;
            }else if(stepCounter==2 && stepCounter < lineItemLength){
                direction = new Direction(this.state.vi *-1,this.state.vj *-1 );
                stepCounter++;
            }else{
                direction = new Direction(0, 0);
                this.stepCounter=0;
                lineItemCounter++;
            }
        }
        else{
            if(stepCounter==0){
                Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                direction = direction(currentCell, nextCell);
                System.out.println("gyorsít");
                ++this.stepCounter;
            }
            else if(stepCounter==1){
                Cell nextCell = new Cell(((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).i, ((PathCell)((List)this.lines.get(this.lineItemCounter)).get(this.stepCounter)).j);
                direction = direction(currentCell, nextCell);
                System.out.println("megint");
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
