package com.ob.rfi;


import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler defaultHandler;
    private final Context context;

    public CustomExceptionHandler(Context context) {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        // Write crash details to a file
        writeCrashToFile(throwable);

        // Call the default handler (optional)
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, throwable);
        }
    }

    private void writeCrashToFile(Throwable throwable) {
        String filename = "rfi_crash_log.txt";
        File file = new File(context.getFilesDir(), filename);

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write("Crash occurred: " + new Date().toString() + "\n");
            writer.write("Thread: " + Thread.currentThread().getName() + "\n");
            writer.write("Exception: " + throwable.toString() + "\n");
            for (StackTraceElement element : throwable.getStackTrace()) {
                writer.write("\t at " + element.toString() + "\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}