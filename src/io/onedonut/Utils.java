package io.onedonut;

/**
 * Created by pamelactan on 2/6/17.
 */
public class Utils {

    public static String printArray(Object[] A) {
        String res = "[";
        for (Object o : A) {
            res = res + o.toString() + ", ";
        }

        res = res.substring(0, res.lastIndexOf(','));

        res = res + "]";

        return res;
    }

    public static String printIterable(Iterable A) {
        String res = "[";
        for (Object o : A) {
            res = res + o.toString() + ", ";
        }

        res = res.substring(0, res.lastIndexOf(','));

        res = res + "]";

        return res;
    }

    public static void print(String s) {
        System.out.println(s);
    }
}
