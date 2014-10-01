package com.webs.michael_ray.meetingofminds.logic;

import com.webs.michael_ray.meetingofminds.CategoryFragment;
import com.webs.michael_ray.meetingofminds.R;

/**
 * Created by asb on 01/10/14.
 */
public class CategoryManager {

    public static int catToIndex(String anyCategory) {
        if (CategoryFragment.categories == null){
            return 0;
        }

        int outerIndex = -1;
        int innerIndex = -1;
        String mainCategory = null;

        for (int i = 0; i < CategoryFragment.categories.size() && mainCategory == null; i++){
            for (int j = 0; j < CategoryFragment.categories.get(j).size() && mainCategory == null; j++){
                if (CategoryFragment.categories.get(i).get(j).equals(anyCategory)){
                    mainCategory = CategoryFragment.categories.get(i).get(j);
                    outerIndex = i;
                    innerIndex = j;
                }
            }
        }
        return outerIndex;
    }

    public static int resource(String anyCategory){
        int outerIndex = catToIndex(anyCategory);

        switch (outerIndex){
            case 0: return R.drawable.community_icon;
            case 1: return R.drawable.energy_icon;
            case 2: return R.drawable.facilities_icon;
            case 3: return R.drawable.food_icon;
            case 4: return R.drawable.transportation_icon;
            default: return R.drawable.facilities_icon;
        }
    }

    public static int color(String anyCategory){
        int outerIndex = catToIndex(anyCategory);

        switch (outerIndex){
            case 0: return R.color.soup;
            case 1: return R.color.ev_charge;
            case 2: return R.color.home;
            case 3: return R.color.food;
            case 4: return R.color.bike;
            default: return R.color.home;
        }
    }
}
