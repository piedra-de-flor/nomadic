/*
package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.vo.DataForYanoljaScraping;
import com.example.Triple_clone.domain.vo.StringDataForYanoljaScraping;
import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import com.example.Triple_clone.service.support.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AccommodationDataParsingService {
    private final FileManager fileManager;
    private Queue<String> data = new LinkedList<>();
    private int dataSize = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    private String name = null;
    private double score = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    private String category = null;
    private int lentTime = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    private long lentPrice = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    private long lodgmentDiscountRate = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    private long lodgmentOriginPrice = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    private long lodgmentPrice = DataForYanoljaScraping.INITIAL_DATA_FOR_INTEGER.getValue();
    private boolean lentStatus = false;
    private String lentData = null;
    private String lentStatusData = null;
    private String enterTime = null;

    public List<AccommodationDto> parseDatas(String local) {
        Queue<String> datas = fileManager.readFile(local).fileData();
        List<AccommodationDto> response = new ArrayList<>();

        datas.remove();

        while (!datas.isEmpty()) {
            if (datas.peek().equals(StringDataForYanoljaScraping.BLANK_LINE.name())) {
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
        return new AccommodationDto(local, name, score, category, lentDiscountRate, lentTime, lentOriginPrice, lentPrice, lentStatus, enterTime, lodgmentDiscountRate, lodgmentOriginPrice, lodgmentPrice, lodgmentStatus);
    }

    private void extractEnterTimeAndDiscountData(String data) {
        if (data.length() > DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DISCOUNT.getValue() &&
                data.contains(StringDataForYanoljaScraping.DISCOUNT_DATA_IDENTIFIER.name())) {
            enterTime = data;
            String discountData = enterTime.substring(DataForYanoljaScraping.START_INDEX_TO_EXTRACT_DISCOUNT_DATA.getValue());
            enterTime = enterTime.substring(DataForYanoljaScraping.START_INDEX_TO_EXTRACT_ENTER_TIME.getValue(), DataForYanoljaScraping.END_INDEX_TO_EXTRACT_ENTER_TIME.getValue());
            String[] discountDatas = discountData.split(StringDataForYanoljaScraping.DISCOUNT_DATA_IDENTIFIER.name());

            discountRate = Long.parseLong(discountDatas[DataForYanoljaScraping.INDEX_DATA_ZERO.getValue()]);

            String[] discountPrices = discountDatas[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]
                    .split(StringDataForYanoljaScraping.PRICE_COMMA.name());

            originPrice = Long.parseLong(discountPrices[DataForYanoljaScraping.INDEX_DATA_ZERO.getValue()] + discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]);

        } else if (data.length() > DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DISCOUNT.getValue() &&
                !data.contains(StringDataForYanoljaScraping.DISCOUNT_DATA_IDENTIFIER.name())) {
            String origin = data;
            String[] discountPrices = origin
                    .split(StringDataForYanoljaScraping.WHITE_SPACE.name())[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]
                    .split(StringDataForYanoljaScraping.PRICE_COMMA.name());

            discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()] = discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]
                    .replace(StringDataForYanoljaScraping.PRICE_UNIT.name(), StringDataForYanoljaScraping.REMOVE.name());

            originPrice = Long.parseLong(discountPrices[DataForYanoljaScraping.INDEX_DATA_ZERO.getValue()] + discountPrices[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]);

        } else if (data.length() > DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DATA.getValue() &&
                data.length() <= DataForYanoljaScraping.MINIMUM_DATA_LENGTH_HAS_DISCOUNT.getValue() &&
                !data.contains(StringDataForYanoljaScraping.INQUIRY.name())){
            enterTime = data;
            enterTime = enterTime.substring(DataForYanoljaScraping.START_INDEX_TO_EXTRACT_ENTER_TIME.getValue(), DataForYanoljaScraping.END_INDEX_TO_EXTRACT_ENTER_TIME.getValue());
        }
    }

    private void extractTotalPrice(String data) {
        if (!data.equals(StringDataForYanoljaScraping.RESERVATION_SOLD_OUT.name())) {
            totalPrice = Long.parseLong(data.replace(StringDataForYanoljaScraping.PRICE_COMMA.name(), StringDataForYanoljaScraping.REMOVE.name()));
        }
    }

    private void extractBasicData() {
        dataSize = data.size();
        name = data.poll();
        System.out.print(name);
        extractScoreAndCategory(data.poll());
    }

    private void extractScoreAndCategory(String data) {
        if (data.contains(StringDataForYanoljaScraping.SCORE_DECIMAL_POINT.name())) {
            score = Double.parseDouble(data);
            category = this.data.poll();
        } else {
            category = data;
        }
    }

    private void extractLentData() {
        if (lentData.length() > DataForYanoljaScraping.MINIMUM_DATA_SIZE_HAS_LENT_SERVICE.getValue()) {
            String lentPriceOfString = lentData.split(StringDataForYanoljaScraping.WHITE_SPACE.name())[DataForYanoljaScraping.INDEX_TO_EXTRACT_DATA_FROM_SPLIT.getValue()]
                    .replace(StringDataForYanoljaScraping.PRICE_COMMA.name(), StringDataForYanoljaScraping.REMOVE.name())
                    .replace(StringDataForYanoljaScraping.PRICE_UNIT.name(), StringDataForYanoljaScraping.REMOVE.name());
            if (!lentPriceOfString.equals(StringDataForYanoljaScraping.INQUIRY.name())) {
                lentPrice = Long.parseLong(lentPriceOfString);
            }
        } else {
            lentTime = Integer.parseInt(lentData.replace(StringDataForYanoljaScraping.LENT_SERVICE.name(), StringDataForYanoljaScraping.REMOVE.name())
                    .replace(StringDataForYanoljaScraping.TIME.name(), StringDataForYanoljaScraping.REMOVE.name()));
            lentPrice = Long.parseLong(lentStatusData.replace(StringDataForYanoljaScraping.PRICE_COMMA.name(), StringDataForYanoljaScraping.REMOVE.name()));
            lentStatus = true;
        }
    }
}
*/
