package com.dubu.turnover.utils;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dubu.turnover.component.aspect.AuthComponent;
public class ReflectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
    /**
     * 鍒╃敤鍙嶅皠鑾峰彇鎸囧畾瀵硅薄鐨勬寚瀹氬睘鎬�
     * @param obj 鐩爣瀵硅薄
     * @param fieldName 鐩爣灞炴��
     * @return 鐩爣灞炴�х殑鍊�
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("", e);
            }
        }
        return result;
    }

    /**
     * 鍒╃敤鍙嶅皠鑾峰彇鎸囧畾瀵硅薄閲岄潰鐨勬寚瀹氬睘鎬�
     * @param obj 鐩爣瀵硅薄
     * @param fieldName 鐩爣灞炴��
     * @return 鐩爣瀛楁
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                //杩欓噷涓嶇敤鍋氬鐞嗭紝瀛愮被娌℃湁璇ュ瓧娈靛彲鑳藉搴旂殑鐖剁被鏈夛紝閮芥病鏈夊氨杩斿洖null銆�
            }
        }
        return field;
    }

    /**
     * 鍒╃敤鍙嶅皠璁剧疆鎸囧畾瀵硅薄鐨勬寚瀹氬睘鎬т负鎸囧畾鐨勫��
     * @param obj 鐩爣瀵硅薄
     * @param fieldName 鐩爣灞炴��
     * @param fieldValue 鐩爣鍊�
     */
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("", e);
            }
        }
    }
}
