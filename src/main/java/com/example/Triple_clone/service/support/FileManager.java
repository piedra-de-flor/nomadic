package com.example.Triple_clone.service.support;

import com.example.Triple_clone.domain.vo.Image;
import com.example.Triple_clone.dto.accommodation.AccommodationDto;
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
                        AccommodationDto accommodationDto = parseData(datas, filePath);
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

    private AccommodationDto parseData(Queue<String> datas, String local) {
        int dataSize = datas.size();
        String name = null;
        double score = 0.0;
        String category = null;
        int lentTime = 0;
        long lentPrice = 0;
        long discountRate = 0;
        long originPrice = 0;
        long totalPrice = 0;
        boolean lentStatus = false;
        String enterTime = null;

        name = datas.poll();
        System.out.println(name);
        String temp = datas.poll();

        if (temp.contains(".")) {
            score = Double.parseDouble(temp);
            category = datas.poll();
        } else {
            category = temp;
        }

        if (dataSize < 6) {
            if (datas.peek().length() > 10 && datas.peek().contains("%")) {
                enterTime = datas.poll();
                String discountData = enterTime.substring(8);
                enterTime = enterTime.substring(2, 7);
                String[] discountDatas = discountData.split("%");

                discountRate = Long.parseLong(discountDatas[0]);
                String[] discountPrices = discountDatas[1].split(",");
                originPrice = Long.parseLong(discountPrices[0] + discountPrices[1]);
            } else if (datas.peek().length() > 10 && !datas.peek().contains("%")) {
                String origin = datas.poll();
                String[] discountPrices = origin.split(" ")[1].split(",");
                discountPrices[1] = discountPrices[1].replace("원", "");
                originPrice = Long.parseLong(discountPrices[0] + discountPrices[1]);
            } else if (datas.peek().length() > 2 && datas.peek().length() <= 10 && !datas.peek().contains("문의")){
                enterTime = datas.poll();
                enterTime = enterTime.substring(2, 7);
            } else {
                datas.poll();
            }
            if (!datas.peek().equals("예약마감")) {
                totalPrice = Long.parseLong(datas.poll().replace(",", ""));
            }

            return new AccommodationDto(local, name, score, category, lentTime, lentPrice, lentStatus, enterTime, discountRate, originPrice, totalPrice);
        } else {
            String lentData = datas.poll();
            String lentStatusData = datas.poll();

            if (datas.peek().length() > 10 && datas.peek().contains("%")) {
                enterTime = datas.poll();
                String discountData = enterTime.substring(8);
                enterTime = enterTime.substring(2, 7);
                String[] discountDatas = discountData.split("%");

                discountRate = Long.parseLong(discountDatas[0]);
                String[] discountPrices = discountDatas[1].split(",");
                originPrice = Long.parseLong(discountPrices[0] + discountPrices[1]);

            } else if (datas.peek().length() > 10 && !datas.peek().contains("%")) {
                String origin = datas.poll();
                String[] discountPrices = origin.split(" ")[1].split(",");
                discountPrices[1] = discountPrices[1].replace("원", "");
                originPrice = Long.parseLong(discountPrices[0] + discountPrices[1]);

            } else if (datas.peek().length() > 2 && datas.peek().length() <= 10 && !datas.peek().contains("문의")){
                enterTime = datas.poll();
                enterTime = enterTime.substring(2, 7);
            } else {
                datas.poll();
            }

            if (!datas.peek().equals("예약마감")) {
                totalPrice = Long.parseLong(datas.poll().replace(",", ""));
            }

            if (lentData.length() > 6) {
                String lentPriceOfString = lentData.split(" ")[1].replace(",", "").replace("원", "");
                if (!lentPriceOfString.equals("문의")) {
                    lentPrice = Long.parseLong(lentPriceOfString);
                }
            } else {
                lentTime = Integer.parseInt(lentData.replace("대실", "").replace("시간", ""));
                lentPrice = Long.parseLong(lentStatusData.replace(",", ""));
                lentStatus = true;
            }
            return new AccommodationDto(local, name, score, category, lentTime, lentPrice, lentStatus, enterTime, discountRate, originPrice, totalPrice);
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
