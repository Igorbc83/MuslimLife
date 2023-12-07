package com.muslimlife.app.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.muslimlife.app.R;

public class KoranUtil {

    static int[] range = {1,15, 16,28, 29,41, 42,54, 55,67, 68,81, 82,94, 95,107, 108,120, 121,133, 134,146,
            147,159, 160,173, 174,186, 187,201, 202,214, 215,227, 228,240, 241,252, 253,265, 266,278,
            279,291, 292,303, 304,315, 316,328, 329,342, 343,354, 355,367, 368,379, 380,392, 393,406,
            407,419, 420,432, 433,445, 446,457, 458,470, 471,483, 484,496, 497,508, 509,521, 522,534,
            535,547, 548,560, 561,572, 573,584, 585,590, 591,604};

    static int[] fonts = {
            R.font.hafs_01, R.font.hafs_02, R.font.hafs_03, R.font.hafs_04, R.font.hafs_05,
            R.font.hafs_06, R.font.hafs_07, R.font.hafs_08, R.font.hafs_09, R.font.hafs_10,
            R.font.hafs_11, R.font.hafs_12, R.font.hafs_13, R.font.hafs_14, R.font.hafs_15,
            R.font.hafs_16, R.font.hafs_17, R.font.hafs_18, R.font.hafs_19, R.font.hafs_20,
            R.font.hafs_21, R.font.hafs_22, R.font.hafs_23, R.font.hafs_24, R.font.hafs_25,
            R.font.hafs_26, R.font.hafs_27, R.font.hafs_28, R.font.hafs_29, R.font.hafs_30,
            R.font.hafs_31, R.font.hafs_32, R.font.hafs_33, R.font.hafs_34, R.font.hafs_35,
            R.font.hafs_36, R.font.hafs_37, R.font.hafs_38, R.font.hafs_39, R.font.hafs_4o,
            R.font.hafs_41, R.font.hafs_42, R.font.hafs_43, R.font.hafs_44, R.font.hafs_45,
            R.font.hafs_46, R.font.hafs_47
    };

    static String singleTopSurahPage =
            "/50/77/128/151/177/187/208/249/262/282/" +
                    "305/322/332/342/350/367/377/411/415/418/428/446/453/" +
                    "477/483/496/499/507/511/518/526/542/549/553/556/558/560/562/572/574/582/585/";

    static String singleMiddleSurahPage =
            "/106-06/221-07/235-09/255-03/267-07/293-10/312-05/359-11/385-08/396-08/404-10/434-08/440-04/458-04/" +
                    "467-03/489-05/502-07/515-07/520-12/523-08/528-10/531-05/534-07/537-11/545-07/551-07/554-07/" +
                    "564-06/566-10/568-09/570-05/575-08/577-06/578-10/580-07/583-08/586-02/589-03/590-02/592-05/593-03/594-06/";

    static String multiSurahPage =
            "/587/591/595/596/597/598/599/600/601/602/603/604/";

    static String singleMiddleSurahAuthCounter =
            "106/176\n" +
            "221/107-109\n" +
            "235/118-123\n" +
            "255/43\n" +
            "267/91-99\n" +
            "293/105-111\n" +
            "312/96-98\n" +
            "359/62-64\n" +
            "385/89-93\n" +
            "396/85-88\n" +
            "404/64-69\n" +
            "434/49-54\n" +
            "440/45\n" +
            "458/84-88\n" +
            "467/75\n" +
            "489/52-53\n" +
            "502/33-37\n" +
            "515/29\n" +
            "520/36-45\n" +
            "523/52-60\n" +
            "528/45-62\n" +
            "531/50-55\n" +
            "534/70-78\n" +
            "537/77-96\n" +
            "545/22\n" +
            "551/12-13\n" +
            "554/9-11\n" +
            "564/27-30\n" +
            "566/43-52\n" +
            "568/36-52\n" +
            "570/41-44\n" +
            "575/20\n" +
            "577/48-56\n" +
            "578/20-40\n" +
            "580/26-31\n" +
            "583/31-40\n" +
            "586/41-42\n" +
            "589/34-36\n" +
            "590/25\n" +
            "592/11-19\n" +
            "593/23-26\n" +
            "594/23-30\n";

    public static int getPageFont(int page){
        for(short i = 0; i < range.length; i+= 2) {
            if(isBetween(page, range[i], range[i + 1]))
                return  fonts[i / 2];
        }

        return -1;
    }

    public static boolean isBetween(int x, int lower, int upper) {
        Log.d("com.muslimlife.app.utils.isBetween", "isBetween: " + lower + " - " + upper);
        return lower <= x && x <= upper;
    }

    public static boolean isTopSingleSurah(String page){
        return singleTopSurahPage.contains("/" + page + "/");
    }

    public static boolean isMiddleSurah(String page){
        return singleMiddleSurahPage.contains("/" + page + "-");
    }

    public static int getMiddleSurahLine(String page){
        int n = singleMiddleSurahPage.indexOf("/" + page + "-");

        if(n >= 0){
            String l = singleMiddleSurahPage.substring(n + 5, n + 7);
            return Integer.parseInt(l);
        }else
            return -1;
    }

    public static boolean isMultiSurahLines(String page){
        return multiSurahPage.contains("/" + page + "/");
    }

    public static int[] getMultiSurahLines(String page){
        switch (page){
            case "587":
                return new int[]{1, 12};
            case "591":
                return new int[]{1, 10};
            case "595":
                return new int[]{2, 11};
            case "596":
                return new int[]{6, 13};
            case "597":
                return new int[]{3, 9};
            case "598":
                return new int[]{4, 9};
            case "599":
                return new int[]{6, 12};
            case "600":
                return new int[]{4, 11};
            case "601":
                return new int[]{1, 5, 11};
            case "602":
                return new int[]{1, 6, 12};
            case "603":
                return new int[]{1, 6, 11};
            case "604":
                return new int[]{1, 5, 10};
        }

        return null;
    }

    public static String getPageLine(String page, int line){
        String s = page.split("\n")[line - 1];
        return s;
    }

    public static String getAyahNumberInMiddleSurah(String page, String ayahSym, int selected){
        int iStart = singleMiddleSurahAuthCounter.indexOf(page + "/");
        int iEnd = singleMiddleSurahAuthCounter.indexOf("\n", iStart);

        int ayahCount = ayahSym.length();

        String ayahs = singleMiddleSurahAuthCounter.substring(iStart + 4, iEnd);

        String[] ayahsList = ayahs.split("-");

        int startRange = Integer.parseInt(ayahsList[0]);
        int endRange = Integer.parseInt(ayahsList[0]);

        if(ayahsList.length > 1)
            endRange = Integer.parseInt(ayahsList[1]);

        List<Integer> range = getSelectedInRange(startRange, endRange, ayahCount);

        for(int i = 0; i < range.size(); i++){
            if(range.get(i) == 1 && i <= selected){
                return range.get(selected) + "-" + 1;
            }

            if(i == selected)
                return range.get(selected) + "-" + 0;
        }

        return range.get(selected) + "-" + 0;
    }

    public static String getAyahNumberInMultiSurah(String page, int selected){
        List<Integer> range;

        switch (page){
            case "587":
                return getSelectedDataInMultiSurah(getSelectedInRange(1, 19, 23), selected);
            case "591":
                return getSelectedDataInMultiSurah(getSelectedInRange(1, 17, 27), selected);
            case "595":
                range = getSelectedInRange(1, 15, 24);
                range.add(0, 20);
                range.add(0, 19);
                return getSelectedDataInMultiSurah(range, selected);
            case "596":
                range = getSelectedInRange(1, 11, 13);
                for(int i = 21; i >= 10; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "597":
                range = getSelectedInRange(1, 8, 20);
                for(int i = 8; i >= 3; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "598":
                range = getSelectedInRange(1, 5, 10);
                for(int i = 19; i >= 13; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "599":
                range = getSelectedInRange(1, 8, 13);
                for(int i = 8; i >= 6; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "600":
                range = getSelectedInRange(1, 11, 19);
                for(int i = 11; i >= 6; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "601":
                range = getSelectedInRange(1, 9, 14);
                for(int i = 3; i >= 1; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "602":
                range = getSelectedInRange(1, 7, 10);
                for(int i = 4; i >= 1; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "603":
                range = getSelectedInRange(1, 3, 8);
                for(int i = 6; i >= 1; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
            case "604":
                range = getSelectedInRange(1, 5, 11);
                for(int i = 4; i >= 1; i--)
                    range.add(0, i);

                return getSelectedDataInMultiSurah(range, selected);
        }

        return "";
    }

    private static String getSelectedDataInMultiSurah(List<Integer> range, int selected){
        int surahCount = 0;
        for(int i = 0; i < range.size(); i++){
            if(range.get(i) == 1 && i > 0)
                surahCount++;

            if(i == selected)
                 return range.get(selected) + "-" + surahCount;
        }

        return "";
    }

    private static List<Integer> getSelectedInRange(int startRange, int endRange, int ayahCount){
        List<Integer> range = new ArrayList<>();

        for(int i = startRange; i <= endRange; i ++)
            range.add(i);

        int last = ayahCount - range.size();

        for(int i = 1; i <= last; i++)
            range.add(i);

        return range;
    }
}
