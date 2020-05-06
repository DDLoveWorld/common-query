package com.hld.query.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * @author huald@ibuaa.com 花立东
 * @version V1.0
 * @Title: ReflexUtil.java
 * @Package com.techcomer.common.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @date 2018年12月26日 下午4:41:25
 */

public class ReflexUtil {

    static Logger logger = LoggerFactory.getLogger(ReflexUtil.class);


    //getMethod
    static public Object invokeMethod(String propertiesName, Object object) {

        try {
            if (object == null) {
                return null;
            }
            if (!propertiesName.contains(".")) {
                String methodName = "get" + getMethodName(propertiesName);
                Method method = object.getClass().getMethod(methodName);
                return method.invoke(object);
            }
            String methodName = "get" + getMethodName(propertiesName.substring(0, propertiesName.indexOf(".")));
            Method method = object.getClass().getMethod(methodName);
            return invokeMethod(propertiesName.substring(propertiesName.indexOf(".") + 1), method.invoke(object));

        } catch (Exception e) {
            logger.error(e.toString(), e);
            return null;
        }
    }


    private static String getMethodName(String fildeName) {

        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }


    /**
     * test get
     * Person person1 = new Person();
     * person1.setAge(11);
     * person1.setName("旺旺");
     * Object ob = getGetMethod(person1, "name");
     * System.out.println(ob);
     * 根据属性，获取get方法
     *
     * @param ob   对象
     * @param name 属性名
     * @return
     * @throws Exception
     */
    public static Object getGetMethod(Object ob, String name) throws Exception {

        Method[] m = ob.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                return m[i].invoke(ob);
            }
        }
        return null;
    }


    /**
     * 根据属性，拿到set方法，并把值set到对象中
     * test set
     * Person person2 = new Person();
     * String field2 = "name";
     * setValue(person2, person2.getClass(), field2, Person.class.getDeclaredField(field2).getType(), "汪汪");
     * System.out.println(person2);
     * 获取某个属性的类型
     * System.out.println(Person.class.getDeclaredField("age").getType());
     *
     * @param obj       对象
     * @param clazz     对象的class
     * @param filedName 需要设置值得属性
     * @param typeClass
     * @param value
     */
    public static void setValue(Object obj, Class<?> clazz, String filedName, Class<?> typeClass, Object value) {

        filedName = removeLine(filedName);
        String methodName = "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
        try {
            Method method = clazz.getDeclaredMethod(methodName, new Class[]{typeClass});
            method.invoke(obj, new Object[]{getClassTypeValue(typeClass, value)});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 通过class类型获取获取对应类型的值
     *
     * @param typeClass class类型
     * @param value     值
     * @return Object
     */
    private static Object getClassTypeValue(Class<?> typeClass, Object value) {

        if (typeClass == int.class || typeClass == Integer.class || value instanceof Integer) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == short.class || typeClass == Short.class || value instanceof Short) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == byte.class || typeClass == Byte.class || value instanceof Byte) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == double.class || typeClass == Double.class || value instanceof Double) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == long.class || typeClass == Long.class || value instanceof Long) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == String.class) {
            if (null == value) {
                return "";
            }
            return value;
        } else if (typeClass == boolean.class || typeClass == Boolean.class || value instanceof Boolean) {
            if (null == value) {
                return true;
            }
            return value;
        } else if (typeClass == BigDecimal.class) {
            if (null == value) {
                return new BigDecimal(0);
            }
            return new BigDecimal(value + "");
        } else {
            return typeClass.cast(value);
        }
    }


    /**
     * @param @param  str
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: removeLine
     * @Description: 将带有下划线的字符串转成驼峰写法，用于属性反射方法
     */
    public static String removeLine(String str) {

        if (null != str && str.contains("_")) {
            int i = str.indexOf("_");
            char ch = str.charAt(i + 1);
            char newCh = (ch + "").substring(0, 1).toUpperCase().toCharArray()[0];
            String newStr = str.replace(str.charAt(i + 1), newCh);
            String newStr2 = newStr.replace("_", "");
            return newStr2;
        }
        return str;
    }


    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String underlineToHump(String para) {

        StringBuilder result = new StringBuilder();
        String[] a = para.split("_");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String para) {

        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toUpperCase();
    }


    public static void main(String[] args) {

        String s = humpToUnderline("asdWsdfFls");
        //		Video video = new Video();
        //		Album album = new Album();
        //		album.setAlbumId( 346l );
        //		video.setAlbum( album );
        //		video.setVideoId( 126l );
        System.out.println(s);
    }
}
