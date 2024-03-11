package com.example.Triple_clone.service.support;

import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Queue;

@Service
public class YanoljaScrapingManager {
    private final Integer INITIAL_DATA_FOR_INTEGER = 0;
    Queue<String> datas;
    int dataSize = 0;
    String name = null;
    double score = (double) INITIAL_DATA_FOR_INTEGER;
    String category = null;
    int lentTime = INITIAL_DATA_FOR_INTEGER;
    long lentPrice = INITIAL_DATA_FOR_INTEGER;
    long discountRate = INITIAL_DATA_FOR_INTEGER;
    long originPrice = INITIAL_DATA_FOR_INTEGER;
    long totalPrice = INITIAL_DATA_FOR_INTEGER;
    boolean lentStatus = false;
    String lentData = null;
    String lentStatusData = null;
    String enterTime = null;
    public AccommodationDto parseData(Queue<String> result, String local) {
        extractBasicData(result);

        if (dataSize < 6) {
            extractEnterTimeAndDiscountData(Objects.requireNonNull(datas.poll()));
            extractTotalPrice(Objects.requireNonNull(datas.poll()));
        } else {
            lentData = datas.poll();
            lentStatusData = datas.poll();
            extractEnterTimeAndDiscountData(Objects.requireNonNull(datas.poll()));
            extractTotalPrice(Objects.requireNonNull(datas.poll()));
            extractLentData();
        }
        return new AccommodationDto(local, name, score, category, lentTime, lentPrice, lentStatus, enterTime, discountRate, originPrice, totalPrice);
    }

    private void extractEnterTimeAndDiscountData(String data) {
        if (data.length() > 10 && data.contains("%")) {
            enterTime = data;
            String discountData = enterTime.substring(8);
            enterTime = enterTime.substring(2, 7);
            String[] discountDatas = discountData.split("%");

            discountRate = Long.parseLong(discountDatas[0]);
            String[] discountPrices = discountDatas[1].split(",");
            originPrice = Long.parseLong(discountPrices[0] + discountPrices[1]);

        } else if (data.length() > 10 && !data.contains("%")) {
            String origin = data;
            String[] discountPrices = origin.split(" ")[1].split(",");
            discountPrices[1] = discountPrices[1].replace("원", "");
            originPrice = Long.parseLong(discountPrices[0] + discountPrices[1]);

        } else if (data.length() > 2 && data.length() <= 10 && !data.contains("문의")){
            enterTime = data;
            enterTime = enterTime.substring(2, 7);
        }
    }

    private void extractTotalPrice(String data) {
        if (!datas.peek().equals("예약마감")) {
            totalPrice = Long.parseLong(data.replace(",", ""));
        }
    }

    private void extractBasicData(Queue<String> result) {
        this.datas = result;
        dataSize = datas.size();
        name = datas.poll();
        extractScoreAndCategory(Objects.requireNonNull(datas.poll()));
    }

    private void extractScoreAndCategory(String data) {
        if (data.contains(".")) {
            score = Double.parseDouble(data);
            category = datas.poll();
        } else {
            category = data;
        }
    }

    private void extractLentData() {
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
    }
}
