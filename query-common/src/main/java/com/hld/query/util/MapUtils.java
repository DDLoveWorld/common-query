package com.hld.query.util;

import com.hld.query.exception.CommonException;
import com.hld.query.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param params
     * @return
     */
    public static Map<String, Object> keysToCamel(Map<String, Object> params) throws IOException, SQLException {
        if (params == null) {
            throw new CommonException(ErrorCode.NOT_NULL, "参数");
        }
        Map<String, Object> map = new HashMap(16);
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
     * @param params
     * @return
     */
    public static List<Map<String, Object>> keysToCamelByList(List<Map> params) {
        List<Map<String, Object>> list = new ArrayList<>(12);
        if (params != null && params.size() > 0) {
            for (Map<String, Object> m : params) {
                try {
                    if (m == null) {
                        continue;
                    }
                    list.add(keysToCamel(m));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * Clob类型 转String
     *
     * @param clob
     * @return
     * @throws SQLException
     * @throws IOException
     */
    private static String clobtostring(Clob clob) throws SQLException, IOException {
        String ret = "";
        Reader read = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(read);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        ret = sb.toString();
        br.close();
        read.close();
        return ret;
    }

}
