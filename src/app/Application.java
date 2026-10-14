package app;

import persistence.FileManager;
import persistence.LoadResult;

public class Application {
    private FileManager fileManager;


    public Application() {
        //TODO: Creat class
    }

    public void init()
    {
    //TODO: load data
        LoadResult result = fileManager.load();
        if (!result.isSuccess()) {
            //do something
        }
        
    }

    public void run()
    {
    //TODO: run console UI
    //TODO: save data
    }
}