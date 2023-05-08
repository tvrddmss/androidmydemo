package esa.mylibrary.utils.log;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述：日志写入类
 **/
public class FileWriteHandler extends Handler {

    private String folder;//日志存储路径
    private int maxFileSize;//单个日志最大占用内存
    private final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");

    public FileWriteHandler(Looper looper, String folder, int maxFileSize) {
        super(looper);
        this.folder = folder;
        this.maxFileSize = maxFileSize;
    }

    @SuppressWarnings("checkstyle:emptyblock")
    @Override
    public void handleMessage(Message msg) {
        if (MyLog.isWriteLog) {
            String content = (String) msg.obj;
            FileWriter fileWriter = null;
            File logFile = getLogFile(folder, "logs");
            try {
                fileWriter = new FileWriter(logFile, true);
                writeLog(fileWriter, content);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                handleException(fileWriter);
            }
        }
    }

    private void handleException(FileWriter fileWriter) {
        if (fileWriter == null) return;
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * This is always called on a single background thread.
     * Implementing classes must ONLY write to the fileWriter and nothing more.
     * The abstract class takes care of everything else including close the stream and catching IOException
     *
     * @param fileWriter an instance of FileWriter already initialised to the correct file
     */
    private void writeLog(FileWriter fileWriter, String content) throws IOException {
        fileWriter.append(content);
    }

    private File getLogFile(String folderName, String fileName) {
        folderName = folderName + "/" + getTime();
        fileName = fileName + "-" + getTime();
        File folder = new File(folderName);
        if (!folder.exists()) folder.mkdirs();
        int newFileCount = 0;
        File existingFile = null;

        File newFile = new File(folder, String.format("%s-%s.txt", fileName, newFileCount));
        while (newFile.exists()) {
            existingFile = newFile;
            newFileCount++;
            newFile = new File(folder, String.format("%s-%s.txt", fileName, newFileCount));
        }

        if (existingFile == null) return newFile;
        if (existingFile.length() >= maxFileSize) return newFile;
        return existingFile;
    }

    private String getTime() {
        return sdfTime.format(new Date());
    }
}
