package com.example.villageadministrationcomplaint;

public class ArrayData {

   String StoreTitle;
   String StoreType;
   String StoreStatus;
   String StoreNo;

    public ArrayData(String storeTitle, String storeType, String storeStatus, String storeNo) {
        StoreTitle = storeTitle;
        StoreType = storeType;
        StoreStatus = storeStatus;
        StoreNo = storeNo;
    }

    public String getStoreTitle() {//getStoreTitle
        return StoreTitle;
    }

    public String getStoreType() {
        return StoreType;
    }

    public String getStoreStatus() {
        return StoreStatus;
    }

    public String getStoreNo() {
        return StoreNo;
    }
}
