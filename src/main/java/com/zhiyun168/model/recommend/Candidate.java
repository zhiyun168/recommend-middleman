package com.zhiyun168.model.recommend;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ouduobiao on 15/10/30.
 */
public class Candidate{
    List<String> items;
    Map<String,String> itemReason;

    public Candidate() {
        this.items = Collections.EMPTY_LIST;
        this.itemReason = Collections.EMPTY_MAP;
    }

    public Candidate(List<String> items,  Map<String,String> itemReason) {
        this.items = items;
        this.itemReason = itemReason;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public  Map<String,String> getItemReason() {
        return itemReason;
    }

    public void setItemReason( Map<String,String> itemReason) {
        this.itemReason = itemReason;
    }
}
