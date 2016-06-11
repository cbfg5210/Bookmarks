package com.hawk.bookmarks.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Reference:https://github.com/litesuits/android-common/blob/master/src/com/litesuits/common/assist/Check.java
 * Author:
 * Date:2016/5/24.
 */
public class CheckUtil {
    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmpty(Object[] os) {
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Collection<?> l) {
        return isNull(l) || l.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return isNull(m) || m.isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }
}
