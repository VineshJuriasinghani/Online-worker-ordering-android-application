package com.technical.myapplication.Worker_category_stuff;

import com.technical.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Categories_data {


    public static List<category_modelClass> getCategories()
    {
        List<category_modelClass> category = new ArrayList<category_modelClass>();


        category_modelClass select_category = new category_modelClass();
        select_category.setName("Select Category");
        select_category.setImage(R.drawable.ic_category);
        category.add(select_category);


        category_modelClass Mechanic = new category_modelClass();
        Mechanic.setName("Mechanic");
        Mechanic.setImage(R.drawable.mechanic);
        category.add(Mechanic);

        category_modelClass Plumber = new category_modelClass();
        Plumber.setName("Plumber");
        Plumber.setImage(R.drawable.plumber);
        category.add(Plumber);


        category_modelClass Carpenter = new category_modelClass();
        Carpenter.setName("Carpenter");
        Carpenter.setImage(R.drawable.carpenter);
        category.add(Carpenter);


        category_modelClass Electrician = new category_modelClass();
        Electrician.setName("Electrician");
        Electrician.setImage(R.drawable.electrician);
        category.add(Electrician);

return  category;

    }
}
