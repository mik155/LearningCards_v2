package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Utils {
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
}
