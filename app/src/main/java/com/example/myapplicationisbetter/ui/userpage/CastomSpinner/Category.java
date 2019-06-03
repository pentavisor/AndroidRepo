package com.example.myapplicationisbetter.ui.userpage.CastomSpinner;

import com.example.myapplicationisbetter.R;

import java.util.ArrayList;
import java.util.List;

public class Category {

    public long id;
    public String category;

    public Category(long id, String category){
        super();
        this.id = id;
        this.category = category;
    }

    public static List<Category> generateCategoryList(){
        List<Category> categories = new ArrayList<>();
        String[] programming = {"Изменить", "Удалить"};

        for(int i = 0; i < programming.length; i++){
            categories.add(new Category(i, programming[i]));
        }
        return categories;
    }
}
