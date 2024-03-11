package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.vo.DataForYanoljaScraping;
import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import com.example.Triple_clone.service.support.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AccommodationDataParsingService {
    private final FileManager fileManager;
    Queue<String> data = new LinkedList<>();
    int dataSize = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    String name = null;
    double score = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    String category = null;
    int lentTime = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    long lentPrice = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    long discountRate = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    long originPrice = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    long totalPrice = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    boolean lentStatus = false;
    String lentData = null;
    String lentStatusData = null;
    String enterTime = null;

    public List<AccommodationDto> parseDatas(String local) {
        Queue<String> datas = fileManager.readFile(local).fileData();
        List<AccommodationDto> response = new ArrayList<>();

        datas.remove();

        while (!datas.isEmpty()) {
            if (datas.peek().equals("/n")) {
                response.add(parseData(local));
                datas.remove();
                data.clear();
            } else {
                data.add(datas.poll());
            }
        }

        return response;
    }

    private AccommodationDto parseData(String local) {
        extractBasicData();

        if (dataSize < DataForYanoljaScraping.MINIMUM_DATA_SIZE_HAS_LENT_SERVICE.getValue()) {
            extractEnterTimeAndDiscountData(Objects.requireNonNull(data.poll()));
            extractTotalPrice(Objects.requireNonNull(data.poll()));
        } else {
            lentData = data.poll();
            lentStatusData = data.poll();
            extractEnterTimeAndDiscountData(Objects.requireNonNull(data.poll()));
            extractTotalPrice(Objects.requireNonNull(data.poll()));
            extractLentData();
        }
        return new AccommodationDto(local, name, score, category, lentTime, lentPrice, lentStatus, enterTime, discountRate, originPrice, totalPrice);
    }

    private void extractEnterTimeAndDiscountData(String data) {
        if (data.length() > DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DISCOUNT.getValue() &&
                data.contains("%")) {
            enterTime = data;
            String discountData = enterTime.substring(DataForYanoljaScraping.START_INDEX_TO_EXTRACT_DISCOUNT_DATA.getValue());
            enterTime = enterTime.substring(DataForYanoljaScraping.START_INDEX_TO_EXTRACT_ENTER_TIME.getValue(), DataForYanoljaScraping.END_INDEX_TO_EXTRACT_ENTER_TIME.getValue());
            String[] discountDatas = discountData.split("%");

            discountRate = Long.parseLong(discountDatas[DataForYanoljaScraping.INDEX_DATA_ZERO.getValue()]);
            String[] discountPrices = discountDatas[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()].split(",");
            originPrice = Long.parseLong(discountPrices[DataForYanoljaScraping.INDEX_DATA_ZERO.getValue()] + discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]);

        } else if (data.length() > DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DISCOUNT.getValue() &&
                !data.contains("%")) {
            String origin = data;
            String[] discountPrices = origin.split(" ")[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()].split(",");
            discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()] = discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()].replace("원", "");
            originPrice = Long.parseLong(discountPrices[DataForYanoljaScraping.INDEX_DATA_ZERO.getValue()] + discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]);

        } else if (data.length() > DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DATA.getValue() &&
                data.length() <= DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DISCOUNT.getValue() &&
                !data.contains("문의")){
            enterTime = data;
            enterTime = enterTime.substring(DataForYanoljaScraping.START_INDEX_TO_EXTRACT_ENTER_TIME.getValue(), DataForYanoljaScraping.END_INDEX_TO_EXTRACT_ENTER_TIME.getValue());
        }
    }

    private void extractTotalPrice(String data) {
        if (!data.equals("예약마감")) {
            totalPrice = Long.parseLong(data.replace(",", ""));
        }
    }

    private void extractBasicData() {
        dataSize = data.size();
        name = data.poll();
        System.out.print(name);
        extractScoreAndCategory(data.poll());
    }

    private void extractScoreAndCategory(String data) {
        if (data.contains(".")) {
            score = Double.parseDouble(data);
            category = this.data.poll();
        } else {
            category = data;
        }
    }

    private void extractLentData() {
        if (lentData.length() > DataForYanoljaScraping.MINIMUM_DATA_SIZE_HAS_LENT_SERVICE.getValue()) {
            String lentPriceOfString = lentData.split(" ")[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()].replace(",", "").replace("원", "");
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
