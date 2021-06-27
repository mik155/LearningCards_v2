package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.lang.Math;
public class Utils {
    private static final int MAX_INTEGER = (int) Math.pow(2, 31) - 1;
    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File f : files)
                deleteDirectory(f);
            directory.delete();
        } else
            directory.delete();
    }

    public static boolean ifContainsFile(final Path directoryPath, final String fileName) {
        File directory = new File(directoryPath.toString());
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File f : files)
                if (f.getName().equals(fileName))
                    return true;
        }
        return false;
    }

    public static String getTextFromFile(final Path path)
    {
        if(path != null)
        {
            File file = new File(path.toString());
            if(!file.exists())
                return null;

            try
            {
                Scanner scanner = new Scanner(file);
                String output = "";
                while (scanner.hasNext())
                    output += scanner.nextLine();
                return output;
            }
            catch (IOException e)
            {
                return null;
            }
        }
        return null;
    }

    public static String getFreeFileName(final Path path, final String segment)
    {
        File file = new File(path.toString());
        if(!file.isDirectory())
            return null;
        String fileNameCandidate = null;
        for(int i = 0 ; i < MAX_INTEGER; i++)
        {
            fileNameCandidate = nameGenerator(segment, i);
            if(!ifContainsFile(path, fileNameCandidate))
                break;
        }
        return fileNameCandidate;
    }

    private static String nameGenerator(final String segment, int number)
    {
        return segment + Integer.toString(number);
    }
}
