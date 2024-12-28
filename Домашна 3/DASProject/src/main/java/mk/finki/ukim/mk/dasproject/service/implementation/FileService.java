package mk.finki.ukim.mk.dasproject.service.implementation;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class FileService {

    private final String directoryPath = "/home/atanas/Desktop/dianshw3/DASProject/src/main/java/mk/finki/ukim/mk/dasproject/python/database/";

    public List<String> getFileNames() {
        List<String> fileNames = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv")); // Filter for CSV files
            if (files != null) {
                for (File file : files) {
                    fileNames.add(file.getName().replace(".csv", "")); // Remove .csv extension for display
                }
            }
        }
        Collections.sort(fileNames);
        return fileNames;
    }
}
