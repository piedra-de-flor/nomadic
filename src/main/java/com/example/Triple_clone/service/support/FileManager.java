package com.example.Triple_clone.service.support;

import com.example.Triple_clone.domain.vo.Image;
import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FileManager {
    private static final String BASE_PATH = "C:\\Users\\USER\\Desktop\\공부\\";
    private final YanoljaScrapingManager yanoljaScrapingManager;

    public List<AccommodationDto> readHotelsFromFile(String filePath) {
        List<AccommodationDto> hotelList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BASE_PATH + filePath + ".txt"))) {
            Queue<String> datas = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    datas.add(line);
                } else {
                    if (!datas.isEmpty()) {
                        AccommodationDto accommodationDto = yanoljaScrapingManager.parseData(datas, filePath);
                        hotelList.add(accommodationDto);
                        datas.clear();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hotelList;
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

    public void deleteExistingImage(String imagePath) {
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
        return BASE_PATH + changedName;
    }

    public byte[] loadImageAsResource(String imageName) {
        try {
            Path imagePath = Paths.get(imageName).resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                return imageBytes;
            } else {
                throw new FileNotFoundException();
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new IllegalArgumentException("이미지를 찾을 수 없습니다.", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 처리하는 과정에서 오류가 발생하였습니다.", e);
        }
    }
}
