package com.sendiri.repo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.DateFormat;

import java.math.BigDecimal;
import java.util.Date;

@Document(indexName = "wallet_histories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletHistoryDoc {

    @Id
    private String walletHistoriesId;

    @Field(type = FieldType.Object)
    private WalletTransactionDetail walletTransaction;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class WalletTransactionDetail {

        @Field(type = FieldType.Text)
        private String trxId;
        
        @Field(type = FieldType.Object)
        private UserInfo fromUser;

        @Field(type = FieldType.Object)
        private UserInfo toUser;

        @Field(type = FieldType.Double)
        private BigDecimal balance;

        @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
        private Date transactionDate;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UserInfo {
        @Field(type = FieldType.Text)
        private String userId;

        @Field(type = FieldType.Text, analyzer = "standard")
        private String phoneNo;
    }
}