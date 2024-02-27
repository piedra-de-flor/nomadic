package com.example.Triple_clone.service.support;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileManager {
    private static final String BASE_PATH = "C:\\Users\\USER\\Desktop\\공부\\";

    public Map<String, Long> readHotelsFromFile(String filePath) {
        Map<String, Long> hotelMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BASE_PATH + filePath + ".txt"))) {
            String hotelName = null;
            long price = 0;
            String line;

            while ((line = br.readLine()) != null) {
                if (hotelName == null) {
                    hotelName = line;
                } else {
                    price = Long.parseLong(line);
                    hotelMap.put(hotelName, price);
                    hotelName = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hotelMap;
    }
}
