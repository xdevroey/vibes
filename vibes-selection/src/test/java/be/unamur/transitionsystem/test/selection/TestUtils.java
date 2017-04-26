package be.unamur.transitionsystem.test.selection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

class TestUtils {

    public static Set<Character> toSet(String str) {
        Set<Character> set = Sets.newHashSet();
        for (char c : str.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    public static List<Character> toList(String str) {
        List<Character> lst = Lists.newArrayList();
        for (char c : str.toCharArray()) {
            lst.add(c);
        }
        return lst;
    }

}
