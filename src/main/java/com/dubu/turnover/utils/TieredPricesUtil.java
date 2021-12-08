package com.dubu.turnover.utils;

/**
 * Created by DELL on 2019/3/21.
 */
public class TieredPricesUtil {
    private static long[] tiers = new long[] { 50, 100, 500, 1000, 2000, 5000, 10000, 20000, 50000, 200000 };
    private static int[] steps = new int[] { 1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000 };

    public static long next(long price) {
        return price + step(price);
    }

    public static boolean legalPrice(long price) {
        return price % step(price) == 0;
    }

    public static long tieredPrice(long price) {
        int step = step(price);
        return price % step == 0 ? price : price - price % step;
    }

    public static int step(long price) {
        int step = tiers.length;
        for (int i = 0; i < tiers.length; i++) {
            if (price < tiers[i]) {
                step = steps[i];
                break;
            }
        }
        return step;
    }
}

