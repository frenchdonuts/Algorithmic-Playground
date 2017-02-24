package io.onedonut.ctci.ch1;

import java.util.Arrays;

import static io.onedonut.Utils.print;

/**
 * Created by pamelactan on 2/24/17.
 */
public class URLify {

    char[] URLify(String s, int n) {
        char[] cs = s.toCharArray();
        int i = s.length() - 1;
        n = n - 1;

        while (n >= 0) {
            print("cs: " + Arrays.toString(cs));
            if (cs[n] == ' ') {
                cs[i] = '0';
                cs[i-1] = '2';
                cs[i-2] = '%';
                i = i - 3;

            } else {
                cs[i] = cs[n];
                i--;
            }

            n--;
        }

        return cs;
    }


    public static void main(String[] args) {
        char[] res = new URLify().URLify("Mr John Smith    ", 13);
        print(Arrays.toString(res));
        assert Arrays.equals(res, new char[] {'M', 'r', '%', '2', '0', 'J', 'o', 'h', 'n', '%', '2', '0', 'S', 'm', 'i', 't', 'h' });
    }
}
