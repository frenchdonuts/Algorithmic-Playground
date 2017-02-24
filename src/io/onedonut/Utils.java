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
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        for (Object o : A) {
            builder.append(o.toString() + ", ");
        }

        builder.deleteCharAt(builder.lastIndexOf(","));

        builder.append("]");

        return builder.toString();
    }

    public static void print(String s) {
        System.out.println(s);
    }
}
