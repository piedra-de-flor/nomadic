package com.example.Triple_clone.service.support;

import org.python.core.PyException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReader {
    private static final String BASE_PATH = "C:\\Users\\USER\\Desktop\\공부\\";

    public List<String> findAccommodations(String local/*, String sort, long page*/) throws PyException {
        return readFile(BASE_PATH + local);
    }

    private List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
