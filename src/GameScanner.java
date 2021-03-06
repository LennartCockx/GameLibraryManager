import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;

class GameScanner {

    //global variables
    private static final ArrayList<Game> Games = new ArrayList<>();
    private static volatile int activethreads;
    private static volatile int numberofthreads = 0;
    private final String directory;
    private final SetupController controller;
    private final MainMenuController parent;
    private String[] directories;
    private float progress;
    private int totalthreads;

    //methods
    public GameScanner(String directory, SetupController controller, MainMenuController parent) //Constructor
    {
        this.parent = parent;
        this.controller = controller;
        this.directory = directory;
        ScanDirectory();
        Gameinfo();
    }

    public static synchronized void DecreaseActiveThreads(int i, Game temp) {
        Games.add(temp);
        activethreads = activethreads - (i + 1);
        numberofthreads--;
    }

    private void ScanDirectory() //Maakt een array met de namen van alle bestanden en mappen in de opgegeven directory
    {
        File file = new File(directory);

        directories = file.list();
    }

    public void task(int i) {
        File f = new File(directory + directories[i]);

        if (f.isDirectory()) {
            String temp = directory + directories[i];
            directories[i] = directories[i].replace(' ', '.');

            if (parent.newgame(directories[i])) {
                Game tempgame = new Game(directories[i], temp);
                Platform.runLater(() -> controller.SetGameName(tempgame.getGameTitle()));
                DecreaseActiveThreads(i, tempgame);
            } else {
                DecreaseActiveThreads(i, null);
            }
        } else {
            DecreaseActiveThreads(i, null);
        }

        progress = (float) (100.00 - (activethreads * 100.00 / totalthreads));
        Platform.runLater(() -> controller.SetProgress(progress));
        Platform.runLater(controller::DisableInterface);
        if (activethreads == 0) {
            Platform.runLater(controller::EnableInterface);
            Platform.runLater(() -> controller.SetGameName("Scan Complete"));
            if (Games != null) {
                Platform.runLater(() -> parent.setData(Games));
            }
        }
    }

    private void Gameinfo() {

        Parallel_executer[] execute = new Parallel_executer[directories.length];

        activethreads = (directories.length * (directories.length + 1)) / 2;
        totalthreads = activethreads;

        Games.clear();

        for (int i = 0; i < execute.length; i++) {
            execute[i] = new Parallel_executer(this, i);
            numberofthreads++;
            while (numberofthreads >= 20) {
                System.out.print("");
            }
        }
    }

}
