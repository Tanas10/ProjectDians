package mk.finki.ukim.mk.dasproject.service.implementation;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileService {

    private final ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<String> getFileNames() {
        List<String> fileNames = new ArrayList<>();

        // Load the directory as a resource
        Resource resource = resourceLoader.getResource("classpath:python/database/");

        try {
            File directory = resource.getFile(); // Convert the resource to a File object

            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv")); // Filter for CSV files
                if (files != null) {
                    for (File file : files) {
                        fileNames.add(file.getName().replace(".csv", "")); // Remove .csv extension
                    }
                }
            } else {
                System.out.println("Directory does not exist or is not a directory: " + directory.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log any exceptions
        }

        Collections.sort(fileNames);
        return fileNames;
    }
}
