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

/**
 * Ez az osztály egy leszármazott osztálya a RaceTrackPlayer-nek, amely egy játékost reprezentál a pályán.
 * Az osztály felelős a játékos mozgásának implementációjáért.
 * Szélességi kereséssel megkeresi a legrövidebb utat a kezdőponttól a célig, miközben megkeresi a legértékesebb coin-t a pályán.
 */
public class Agent extends RaceTrackPlayer {

    /**
     * Az Agent osztály adattagjai.
     * stepCounter : egy egyenesen belül hanyadik lépésnél tartunk
     * lineItemCounter : az útvonalon belül hanyadik egyenesnél tartunk
     * lines: az útvonalat tárolja el egyenesek listájaként
     */
    private int stepCounter = 0;
    private int lineItemCounter = 0;
    private List<List<PathCell>> lines;

    /**
     * Az Agent osztály konstruktora
     * Meghívja az ősosztály konstruktorát és beállítja a következő adattagokat:
     * @param state : a játékos pozíciója
     * @param random : random generátor
     * @param track : a pályát reprezentáló kétdimenziós tömb
     * @param coins : a pályán található érmék tömbje
     * @param color : a játákos színe a pályán
     * Ha a pályán nem található coin, akkor meghívja a RaceTrackGame osztály a BFS( szélességi keresés) függvényét.
     * Ha található coin, akkor a legértékesebbel meghívja az Agent osztályban felüldefiniált szélességi keresés függvényt, a coinBFS-t.
     * A szélességi keresésből kapott útvonalat beállítja a route változó értékének, majd az Agent osztályban implementált breakToLines függvénnyel egyenesekre bontja.
     * Eltávolítja az lines első listaelemének első elemét, ami a játékos kezdőállapota a játék kezdetén, viszont ennél a cellánál nem kell irányt számolni.
     */

    public Agent(PlayerState state, Random random, int[][] track, Coin[] coins, int color) {
        super(state, random, track, coins, color);

        List<PathCell> route;

        //Ha nincs coin a pályán akkor az alap BFS fügvénnyel számítjuk ki az útvonalat.
        if (coins.length == 0) {
            route = BFS(state.i, state.j, track);
        } else {
        //A coins tömbben megkeresi azt a coint, ami a legértékesebb, azaz a value adattagja a legnagyobb.
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

        //Felbonja az útvonalat egyenesekre.
        this.lines = breakToLines(route);
        this.lines.get(0).remove(0);
    }

    /**
     * A getDirection függvény meghatározza, hogy a játékos milyen irányba menjen a pályán.
     * @param remainingTime : a hátralévő idő a játékban
     * A játékos sebességvektora maximum 2-re nő a játék során.
     * Attól függően hogy az egyenes páros, vagy páratlan darab cellából áll, kiszámoljuk az aktuális irányt.
     * A függvény kezeli a speciális eseteket is.
     * Minden egyenes végén a játékos sebességvektora (0,0).
     * @return : direction : a kiszámított irány.
     */
    public Direction getDirection(long remainingTime) {
        Cell currentCell = new Cell(this.state.i, this.state.j);
        int lineItemLength = this.lines.get(this.lineItemCounter).size(); // az egyenesen hossza
        Direction direction;

        // Páros darab cellából áll az egyenes. Speciális esete: 0, 2 és 4.
        if (lineItemLength % 2 == 0) {
            if(lineItemLength==0){
                direction= new Direction(0,0); //Itt nem gyorsítunk csak átlépünk egy másik egyenesre.
                lineItemCounter++;
            }
            else if (lineItemLength == 2) {
                if (stepCounter == 0) { //gyorsítunk, rálépünk a első cellára
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter == lineItemLength - 1) { //megtartjuk a sebességet
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else { //vége az egyenesnek, lelassítunk és átlépünk egy másik egyenesre
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else if (lineItemLength == 4) {
                if (stepCounter == 0) { //gyorsítunk, rálépünk a első cellára
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter > 0 && stepCounter < lineItemLength) { //megtartjuk a sebességet az egyenes végéig
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else { //vége az egyenesnek, lelassítunk és átlépünk egy másik egyenesre
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else {
                if (stepCounter == 0) { //gyorsítunk, rálépünk a első cellára
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter == 1) { //mégegyszer gyorsítunk
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++stepCounter;
                } else if (stepCounter > 1 && stepCounter < lineItemLength - 2) { //megtartjuk a sebességet az utolsó előtti celláig
                    direction = new Direction(0, 0);
                    stepCounter += 2;
                } else if (stepCounter == lineItemLength - 2) { //elkezdünk lassítani
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).i, this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).j);
                    int newI = direction(currentCell, nextCell).i;
                    int newJ = direction(currentCell, nextCell).j;
                    direction = new Direction(newI * -1, newJ * -1);
                    ++this.stepCounter;
                } else { //vége az egyenesnek, teljesen lelassítjuk az autót és átlépünk a következő egyenesre
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            }
        // Páratlan darab cellából áll az egyenes. Speciális esete: 1 és 3.
        } else {
            if (lineItemLength == 1) {
                if (stepCounter == 0) { //gyorsítunk, rálépünk a első cellára
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else { //vége az egyenesnek, lelassítunk és átlépünk egy másik egyenesre
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else if (lineItemLength == 3) {
                if (stepCounter == 0) { //gyorsítunk, rálépünk a első cellára
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter < lineItemLength) { //megtartjuk a sebességet
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else { //vége az egyenesnek, lelassítunk és átlépünk egy másik egyenesre
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            } else {
                if (stepCounter == 0) { //gyorsítunk, rálépünk a első cellára
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, this.lines.get(this.lineItemCounter).get(this.stepCounter).j);
                    direction = direction(currentCell, nextCell);
                    ++this.stepCounter;
                } else if (stepCounter == 1) { //mégegyszer gyorsítunk
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter).i, (this.lines.get(this.lineItemCounter).get(this.stepCounter).j));
                    direction = direction(currentCell, nextCell);
                    ++stepCounter;
                } else if (stepCounter > 1 && stepCounter < lineItemLength - 3) { //megtartjuk a sebességet közel az utolsó előtti előtti celláig
                    direction = new Direction(0, 0);
                    stepCounter += 2;
                } else if (stepCounter == lineItemLength - 3) { //eggyel lassítjuk az autót
                    Cell nextCell = new Cell(this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).i, this.lines.get(this.lineItemCounter).get(this.stepCounter + 1).j);
                    int newI = direction(currentCell, nextCell).i;
                    int newJ = direction(currentCell, nextCell).j;
                    direction = new Direction(newI * -1, newJ * -1);
                    ++this.stepCounter;
                } else if (stepCounter == lineItemLength - 2) { //megtartjuk a sebességet és lépünk az utolsó cellára
                    direction = new Direction(0, 0);
                    stepCounter++;
                } else { //vége az egyenesnek, teljesen lelassítjuk az autót és átlépünk a következő egyenesre
                    direction = new Direction(this.state.vi * -1, this.state.vj * -1);
                    this.stepCounter = 0;
                    lineItemCounter++;
                }
            }
        }

        return direction;
    }

    /**
     * A coinBFS függvény felüldefiniálja a RaceTrackGame BFS függvényét.
     * Célja meghatározni a legrövidebb utat a kezdőpont, a legértékesebb coin és a cél között.
     * @param i : a kezdőpont i koordinátája
     * @param j : a kezdőpont j koordinátája
     * @param track : a pályát reprezentáló kétdimenziós tömb
     * @param deepness : a rekurzió megállítására szolgáló változó. Az értékét folyamatosan csökkentjük. Amikor eléri a nullát, akkor kilépünk a rekurzióból.
     * @param best : legértékesebb coin
     * @return : visszaadja egy PathCell-ekből álló listát,ami a legrövidebb út a kezdőpont, a legértékesebb coin és a cél közöttt.
     */
    public List<PathCell> coinBFS(int i, int j, int[][] track, int deepness, Coin best) {
        LinkedList<PathCell> path = new LinkedList();
        LinkedList<PathCell> open = new LinkedList();
        LinkedList<PathCell> close = new LinkedList();
        PathCell current = new PathCell(i, j, null);
        open.add(current);

        while (!open.isEmpty()) {
            current = open.pollFirst();

            //Amennyiben megtaláltuk a keresett coin-t, lezárjuk az útvonalat.
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

        //Ha már megtalátuk a coint akkor meghívjuk az alap BFS függvényt a coin pozíciójától, ami visszaadja az útvonalat a célig.
        //Ezt az útvonalat hozzáfűzzük a coin-ig vezető útvonalhoz.
        if (deepness > 0) {
            int newX = (path.get(path.size() - 1)).i;
            int newJ = (path.get(path.size() - 1)).j;
            path.remove(path.size() - 1);
            path.addAll(BFS(newX, newJ, track));
        }

        return path;
    }

    /**
     * A breakToLines függvény feladata felbontani egyenesekre a szélességi keresés által kapott útvonalat.
     * @param route : az útvonal
     * @return : egy PathCell listákból álló lista, amely tartalmazza az útvonal koordinátáit az alapján csoportosítva, hoy mely koordináták vannak egy irányban.
     */
    public static List<List<PathCell>> breakToLines(List<PathCell> route) {
        List<List<PathCell>> lines = new ArrayList<>();
        List<PathCell> actual = new ArrayList<>(); //aktuális egyenes amibe helyezzük a cellákat
        List<PathCell> seen = new ArrayList<>(); //már bejárt cella egy adott egyenesen

        //végigmegyünk az útvonalon
        for (int i = 0; i < route.size(); i++) {
            PathCell current = route.get(i);

            //Ha a cella már szerepel az aktuális egyenesen akkor nem tesszük bele az aktuális egyenes listájába,hanem egy újba.
            //Ezzel kezeljük az oda-vissza irányban lévő cellákat.
            if (seen.contains(current)) {
                lines.add(new ArrayList<>(actual));
                actual.clear();
                seen.clear();
            }
            //Ha nincs benne még akkor bekerül a listába.
            actual.add(current);
            seen.add(current);

            //Minden egyes pontot ellenőrzünk az útvonalon, hogy a megelőző és a rákövetkező pontok közötti szakaszra esik-e.
            if (i > 0) {
                PathCell previous = route.get(i - 1);
                //Kiszámoljuk az előző és a jelenlegi pont közötti távolságot.
                int distanceI_previous = current.i - previous.i;
                int distanceJ_previous = current.j - previous.j;

                if (i < route.size() - 1) {
                    //Kiszámoljuk a jelenlegi és a következő pont közötti távolságot.
                    PathCell next = route.get(i + 1);
                    int distanceI_next = next.i - current.i;
                    int distanceJ_next = next.j - current.j;

                    //Megvizsgáljuk az előző és a rákövetkező pontok irányvektorainak keresztszorzatát.
                    //Ha ezek nem egyeznek meg, akkor nincsenek egy irányban , ezért új egyenest kezdünk.
                    if ((distanceI_previous * distanceJ_next)!= (distanceI_next * distanceJ_previous)) {
                        lines.add(new ArrayList<>(actual));
                        actual = new ArrayList<>();
                    }
                }
            }
        }
        //Hozzáfűzzük a lines listához a jelenlegi egyenest.
        lines.add(new ArrayList<>(actual));
        return lines;
    }
}
