package com.kworkapp.audiogid;

public class CardUtils {

    public static Integer sectionImage(int sectionNumber, int index) {
        Integer res0 = null;
        if (sectionNumber == 9) {
            res0 = (R.drawable.woman);

        } else if (sectionNumber == 3 || sectionNumber == 1
                || sectionNumber == 5 || sectionNumber == 6
                || sectionNumber == 11 || sectionNumber == 13) {
            res0 = (R.drawable.woman);
        } else {
            //sectionNumber == 4
            //7
            //8
//                10
//                12
            //14
            if (sectionNumber == 14 && index == 7) {
                res0 = (R.drawable.woman);
            } else {
                res0 = (R.drawable.man);
            }
        }
        return res0;
    }
}
