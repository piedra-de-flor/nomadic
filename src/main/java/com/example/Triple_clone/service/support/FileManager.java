package com.example.Triple_clone.service.support;

import com.example.Triple_clone.domain.vo.Image;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public Image uploadImage(MultipartFile image) {
        String originName = image.getOriginalFilename();
        String changedName = changedImageName(originName);
        String storedImagePath = createDirPath(changedName);
        System.out.println("storedImagePath = " + storedImagePath);

        try {
            image.transferTo(new File(storedImagePath));
        } catch (IOException e){
            throw new IllegalArgumentException("이미지 업로드 실패");
        }
        return new Image(originName, storedImagePath);
    }

    public void deleteExistingImage(String imageName) {
        String imagePath = "c:\\images\\" + imageName;
        File existingImage = new File(imagePath);
        if (existingImage.exists()) {
            existingImage.delete();
        }
    }

    private String changedImageName(String originName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        return originName + current_date;
    }

    private String createDirPath(String changedName) {
        return "c:\\images\\"+changedName;
    }

    public Resource loadImageAsResource(String imageName) {
        try {
            Path imagePath = Paths.get(createDirPath(imageName)).resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException();
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new IllegalArgumentException("이미지를 찾을 수 없습니다.", ex);
        }
    }
}
