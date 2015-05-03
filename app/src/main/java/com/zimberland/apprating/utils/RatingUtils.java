package com.zimberland.apprating.utils;


import com.zimberland.lib.rating.enums.RatingType;

import java.util.ArrayList;
import java.util.List;

/**
 * Rating Utils
 */
public class RatingUtils {

    /**
     * Get the list of rating types
     * @return the list of rating type available
     */
    public static List<String> getRatingTypes(String prompt){
        List<String> types = new ArrayList<String>();

        types.add(prompt);
        types.add(RatingType.STANDARD.toString());
        types.add(RatingType.SESSION.toString());

        return types;
    }
}
