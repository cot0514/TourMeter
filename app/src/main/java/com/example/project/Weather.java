package com.example.project;

import java.util.List;

public class Weather {
    public ResponseData response;

    public static class ResponseData {
        public Header header;
        public Body body;
    }

    public static class Header {
        public String resultCode;
        public String resultMsg;
    }

    public static class Body {
        public String dataType;
        public Items items;
        public int pageNo;
        public int numOfRows;
        public int totalCount;
    }

    public static class Items {
        public List<Item> item;
    }

    public static class Item {
        public String tm; // 예보 시각
        public String totalCityName; // 전체 도시 이름
        public String doName; // 도이름
        public String cityName; // 시군구 이름
        public String cityAreaId; // 시군구 아이디
        public String kmaTci; // 관광기후지수
        public String TCI_GRADE; // 관광기후지수등급
    }

}
