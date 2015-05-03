package com.zimberland.lib.rating.enums;

/**
 * Different type of rating process
 *
 * STANDARD : Standard mode
 * SESSION  : Start rating process after n hours or days after app launch
 */
public enum  RatingType {

    STANDARD (0),
    SESSION  (1);

    /**
     * Convert the value to a rating type.
     */
    public static RatingType fromId(int id) {
        for (RatingType eventType : values()) {
            if (eventType.type == id) {
                return eventType;
            }
        }

        return STANDARD;
    }

    private RatingType(int type) {
        this.type = type;
    }

    /**
     * Return the value corresponding to the
     * Rating type.
     */
    public int getRatingType() {
        return type;
    }

    private int type;
}
