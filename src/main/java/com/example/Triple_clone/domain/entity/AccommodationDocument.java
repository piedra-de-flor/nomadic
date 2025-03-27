package com.example.Triple_clone.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(indexName = "accommodation")
public class AccommodationDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String category;

    @Field(type = FieldType.Text)
    private String local;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Double)
    private Double score;

    @Field(type = FieldType.Text)
    private String enterTime;

    @Field(type = FieldType.Long)
    private Long lentDiscountRate;

    @Field(type = FieldType.Long)
    private Long lentOriginPrice;

    @Field(type = FieldType.Long)
    private Long lentPrice;

    @Field(type = FieldType.Boolean)
    private Boolean lentStatus;

    @Field(type = FieldType.Integer)
    private Integer lentTime;

    @Field(type = FieldType.Long)
    private Long lodgmentDiscountRate;

    @Field(type = FieldType.Long)
    private Long lodgmentOriginPrice;

    @Field(type = FieldType.Long)
    private Long lodgmentPrice;

    @Field(type = FieldType.Boolean)
    private Boolean lodgmentStatus;
}
