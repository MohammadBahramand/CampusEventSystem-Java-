package app;

import persistence.FileManager;
import persistence.InitResult;

public class Application {
    private FileManager fileManager;
    private boolean keepRuning = true;

    public Application() {
        fileManager = new FileManager();
    }

    public void init()
    {
        InitResult result = fileManager.init();
        if (!result.isSuccess()) {
            // TODO: report the failure and stop startup in the application layer.
        }
        // TODO: read and convert text into business data.
    }

    public void run()
    {
        while(keepRuning)
        {

        }
    //TODO: run console UI
    //TODO: save data
    }
}
