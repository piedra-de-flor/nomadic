package com.example.Triple_clone.common.file;

import com.example.Triple_clone.common.logging.logMessage.FileLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileManager {
    private static final String BASE_PATH = "C:\\Users\\USER\\Desktop\\Study\\";

    public FileDataDto readFile(String filePath) {
        Queue<String> datas = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                datas.add(isBlank(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileDataDto(datas);
    }

    private String isBlank(String line) {
        if (!line.isEmpty()) {
            return line;
        } else {
            return "/n";
        }
    }

    public Image uploadImage(MultipartFile image) {
        String originName = image.getOriginalFilename();
        String changedName = changedImageName(originName);
        String storedImagePath = createDirPath(changedName);
        System.out.println("storedImagePath = " + storedImagePath);

        try {
            image.transferTo(new File(storedImagePath));
        } catch (IOException e){
            log.error(FileLogMessage.IO_ERROR.format(e.getMessage()));
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
            log.error(FileLogMessage.FILE_SEARCH_ERROR.format(e.getMessage()));
            throw new IllegalArgumentException("이미지를 찾을 수 없습니다.", e);
        } catch (IOException e) {
            log.error(FileLogMessage.FILE_HANDLE_ERROR.format(e.getMessage()));
            throw new IllegalArgumentException("이미지 처리하는 과정에서 오류가 발생하였습니다.", e);
        }
    }
}
