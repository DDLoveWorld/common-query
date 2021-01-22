package com.hld.query.util;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hld.query.enums.SensitiveStrategy;
import com.hld.query.enums.SwitchType;
import com.hld.query.exception.CommonException;
import com.hld.query.exception.ErrorCode;
import com.hld.query.params.AbstractInfo;
import com.hld.query.params.FormatSwitchInfo;
import com.hld.query.params.SensitiveInfo;
import com.hld.query.params.SwitchData;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;

/**
 * Map 工具类
 *
 * @author huald
 * @date 2019/7/18
 */
@Slf4j
public class MapUtils {

    public static final String KEY_CLOB = "oracle.sql.CLOB";

    /**
     * 转换map中key为下划线为驼峰式
     * ABC_DE => absDe , abc_de=>abcDe
     *
     * @param params 待转换数据
     * @return 结果
     */
    public static Map<String, Object> keysToCamel(Map<String, Object> params) throws IOException, SQLException {
        if (params == null) {
            throw new CommonException(ErrorCode.NOT_NULL, "参数");
        }
        HashMap<String, Object> map = new HashMap<String, Object>(16);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            String key = ReflexUtil.underlineToHump(mapKey);
            if (mapValue != null) {
                //log.info("mapValue instanceof CLOB:[{}]", mapValue.toString().contains(KEY_CLOB));
                if (mapValue.toString().contains(KEY_CLOB)) {
                    String s = clobtostring((Clob) mapValue);
                    map.put(key, s);
                } else {
                    map.put(key, mapValue);
                }
            } else {
                map.put(key, "");
            }
        }
        return map;
    }

    /**
     * 转换map中key为下划线为驼峰式
     * ABC_DE => absDe , abc_de=>abcDe
     *
     * @param params 待转换
     * @return 结果
     */
    public static List<Map<String, Object>> keysToCamelByList(List<Map<String, Object>> params) {
        List<Map<String, Object>> list = new ArrayList<>(12);
        if (params != null && params.size() > 0) {
            for (Map<String, Object> m : params) {
                try {
                    if (m == null) {
                        continue;
                    }
                    list.add(keysToCamel(m));
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * Clob类型 转String
     *
     * @param clob 大文本
     * @return 结果
     * @throws SQLException SQL
     * @throws IOException  io
     */
    private static String clobtostring(Clob clob) throws SQLException, IOException {
        String ret = "";
        Reader read = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(read);
        String s = br.readLine();
        StringBuilder sb = new StringBuilder();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        ret = sb.toString();
        br.close();
        read.close();
        return ret;
    }


    /**
     * 转换map中key为下划线为驼峰式
     * 并更改需要转换格式的数据
     * ABC_DE => absDe , abc_de=>abcDe
     * 推荐使用dataHandling方法
     *
     * @param params 待转换
     * @return 结果
     */
    @Deprecated
    public static List<Map<String, Object>> keysToCamelByList(List<Map<String, Object>> params, List<FormatSwitchInfo> infos) {
        List<Map<String, Object>> list = keysToCamelByList(params);
        if (infos != null && infos.size() > 0 && list.size() > 0) {
            //有需要格式转换的数据
            list.parallelStream().forEach(r -> {
                infos.parallelStream().forEach(a -> {
                    Object o = r.get(a.getFieldName());
                    if (o == null) {
                        return;
                    }
                    SwitchData switchData = switchFormat(a, o.toString());
                    r.put(switchData.getKey(), switchData.getValue());
                });

            });
        }
        return list;
    }

    /**
     * 转换map中key为下划线为驼峰式
     * <p>
     * ABC_DE => absDe , abc_de=>abcDe
     *
     * @param params 待处理数据
     * @param infos  待处理字段数据
     * @return 结果
     */
    public static List<Map<String, Object>> dataHandling(List<Map<String, Object>> params, List<AbstractInfo> infos) {
        List<Map<String, Object>> list = keysToCamelByList(params);
        if (infos != null && infos.size() > 0 && list.size() > 0) {
            //有需要格式转换的数据
            list.parallelStream().forEach(r -> {
                infos.parallelStream().forEach(a -> {
                    Object o = r.get(a.getFieldName());
                    switch (a.getAnnotationType()) {
                        case SENSITIVE:
                            SensitiveInfo sensitiveInfo = (SensitiveInfo) a;
                            SensitiveStrategy sensitiveStrategy = sensitiveInfo.getSensitiveStrategy();
                            try {
                                if (StringUtils.isNotBlank(o.toString())) {
                                    String result = sensitiveStrategy.getDesensitizer().apply(o.toString());
                                    r.put(a.getFieldName(), result);
                                }
                            } catch (Exception e) {

                            }
                            break;
                        case SWITCH_FORMAT:
                            SwitchData switchData = switchFormat((FormatSwitchInfo) a, o == null ? null : o.toString());
                            r.put(switchData.getKey(), switchData.getValue());
                            break;
                        default:
                            break;
                    }

                });

            });
        }
        return list;
    }

    /**
     * 转换返回数据格式
     *
     * @param info 需要转换数据格式的字段信息
     * @param data 需要转换的数据
     * @return
     */
    public static SwitchData switchFormat(FormatSwitchInfo info, String data) {
        SwitchType type = info.getType();
        Boolean handleEmptyAndNull = info.getHandleEmptyAndNull();
        String key = null;
        Object value = null;
        switch (type) {
            case STR_TO_ARRAY:
                key = info.getFieldName() + "Arr";
                value = switchStrToArray(info.getSeparation(), data, handleEmptyAndNull);
                break;
            case JSON_TO_OBJ:
                key = info.getFieldName() + "Obj";
                value = switchJsonToObj(data, info.getCls());
                break;
            case JSON_TO_LIST:
                key = info.getFieldName() + "List";
                value = switchJsonToList(data, info.getCls(), handleEmptyAndNull);
                break;
            case JSON_TO_ARRAY:
                key = info.getFieldName() + "Arr";
                value = switchJsonToArray(data, handleEmptyAndNull);
                break;
            default:
                break;
        }

        return new SwitchData().setKey(key)
                .setValue(value);
    }

    /**
     * json 转 list
     *
     * @param data               待转换json数据
     * @param cls                转换成类型
     * @param handleEmptyAndNull
     * @return
     */
    private static List switchJsonToList(String data, Class<?> cls, Boolean handleEmptyAndNull) {
        if (StringUtils.isNotBlank(data)) {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, cls);
            try {
                List list = mapper.readValue(data, javaType);
                return list;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (handleEmptyAndNull) {
            return Collections.EMPTY_LIST;
        }
        return null;
    }

    /**
     * Json 转 对象
     *
     * @param data 待转换json数据
     * @param cls  转换成类型
     * @return
     */
    private static Object switchJsonToObj(String data, Class<?> cls) {
        if (StringUtils.isNotBlank(data)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(data, cls);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 字符串转数组 通过分隔符
     *
     * @param separation         分隔符
     * @param data               需转换数据
     * @param handleEmptyAndNull
     * @return
     */
    private static Object switchStrToArray(String separation, String data, Boolean handleEmptyAndNull) {

        if (StringUtils.isNotBlank(data)) {
            return data.split(separation);
        }
        if (handleEmptyAndNull) {
            return Collections.EMPTY_LIST;
        }
        return null;
    }

    /**
     * 字符串数组  转 数组对象
     *
     * @param data               需转换数据
     * @param handleEmptyAndNull
     * @return
     */
    private static Object switchJsonToArray(String data, Boolean handleEmptyAndNull) {
        if (StringUtils.isNotBlank(data)) {
            try {
                return JSONArray.parse(data);
            } catch (Exception e) {
                if (handleEmptyAndNull) {
                    return Collections.EMPTY_LIST;
                }
                return null;
            }
        }
        if (handleEmptyAndNull) {
            return Collections.EMPTY_LIST;
        }
        return null;
    }

}
