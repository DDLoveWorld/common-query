package com.hld.query.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hld.query.enums.SwitchType;
import com.hld.query.exception.CommonException;
import com.hld.query.exception.ErrorCode;
import com.hld.query.params.FormatSwitchInfo;
import com.hld.query.params.SwitchData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
     * ABC_DE => absDe , abc_de=>abcDe
     *
     * @param params 待转换
     * @return 结果
     */
    public static List<Map<String, Object>> keysToCamelByList(List<Map<String, Object>> params, List<FormatSwitchInfo> infos) {
        List<Map<String, Object>> list = keysToCamelByList(params);
        if (infos != null && infos.size() > 0 && list.size() > 0) {
            //有需要格式转换的数据
            list.parallelStream().forEach(r -> {
                infos.parallelStream().forEach(a -> {
                    Object o = r.get(a.getFiledName());
                    if (o == null) {
                        return;
                    }
                    SwitchData switchData = switchFormat(a, o.toString());
                    if (switchData != null) {
                        r.put(switchData.getKey(), switchData.getValue());
                    }

                });

            });
        }
        return list;
    }


    public static SwitchData switchFormat(FormatSwitchInfo info, String data) {
        SwitchData switchData = null;
        SwitchType type = info.getType();
        String key = null;
        Object value = null;
        switch (type) {
            case STR_TO_ARRAY:
                key = info.getFiledName() + "Arr";
                value = switchStrToArray(info.getSeparation(), data);
                break;
            case JSON_TO_OBJ:
                key = info.getFiledName() + "Obj";
                value = switchJsonToObj(data, info.getCls());
                break;
            case JSON_TO_LIST:
                key = info.getFiledName() + "List";
                value = switchJsonToList(data, info.getCls());
                break;
            case JSON_TO_ARRAY:
                break;
            default:
                break;
        }
        if (key != null && value != null) {
            switchData = new SwitchData();
            switchData.setKey(key)
                    .setValue(value);
        }
        return switchData;
    }


    private static List switchJsonToList(String data, Class<?> cls) {
        if (StringUtils.isNotBlank(data)) {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, cls);
            try {
                List list = mapper.readValue(data, javaType);
                return list;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

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
     * @param separation
     * @param data
     * @return
     */
    private static String[] switchStrToArray(String separation, String data) {

        if (StringUtils.isNotBlank(data)) {
            return data.split(separation);
        }
        return null;
    }


}
