package com.dubu.turnover.utils;

import java.util.ArrayList;
import java.util.List;

import com.dubu.turnover.domain.TreeVo;

public class TreeUtil {
	public static <T extends TreeVo>  List<T> tree(List<T> list) {
        List<T> returnList = new ArrayList<>();
        for (T next : list) {
            if (next.getParentId() == 0) {
                recursionFn(list, next);
                returnList.add(next);
            }
        }
        return returnList;
    }

	private static  <T extends TreeVo> void recursionFn(List<T> list, T t) {
        List<T> childList = getChildList(list, t);
        t.setChildrens(childList);
        for (T tChild : childList) {
            if (hasChild(list, tChild)) {
                for (T n : childList) {
                    recursionFn(list, n);
                }
            }
        }
    }

	private static  <T extends TreeVo> List<T> getChildList(List<T> list, T t) {
		List<T> tList = new ArrayList<>();
        for (T next : list) {
            if (next.getParentId().longValue() == t.getId().longValue()) {
                tList.add(next);
            }
        }
		return tList;
	}

	private static  <T extends TreeVo> boolean hasChild(List<T> list, T t) {
		return getChildList(list, t).size() > 0;
	}
}
