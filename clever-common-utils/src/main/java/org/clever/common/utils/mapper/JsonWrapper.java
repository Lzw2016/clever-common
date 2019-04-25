package org.clever.common.utils.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.clever.common.utils.ConversionUtils;
import org.clever.common.utils.exception.ExceptionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Json-Map的相互转换工具<br/>
 * 1.通过Jackson实现<br/>
 * <p/>
 * 作者：LiZW <br/>
 * 创建时间：2016-4-28 0:55 <br/>
 */
public class JsonWrapper {

    private static final ObjectMapper mapper = JacksonMapper.nonEmptyMapper().getMapper();

    private final Map innerMap;

    /**
     * 通过Map构造
     */
    public JsonWrapper(Map innerMap) {
        this.innerMap = innerMap;
    }

    /**
     * 通过Map构造
     */
    public JsonWrapper(InputStream stream) throws IOException {
        this(mapper.readValue(stream, LinkedHashMap.class));
    }

    /**
     * 通过Map构造
     */
    public JsonWrapper() {
        this(new LinkedHashMap());
    }

    /**
     * 通过Json构造
     */
    public JsonWrapper(Object obj) throws JsonProcessingException {
        this(mapper.writeValueAsString(obj));
    }

    /**
     * 通过Json构造
     */
    public JsonWrapper(String jsonString) {
        this(JsonToMap(jsonString));
    }

    /**
     * Json转换成Map
     */
    private static Map JsonToMap(String jsonString) {
        try {
            return mapper.readValue(jsonString, LinkedHashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将对象序列化成json
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    /**
     * 将对象序列化成json(格式化)
     */
    public static String toJsonPretty(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    /**
     * 根据Key路径 获取对应值
     *
     * @param map     Map对象
     * @param keyPath Key路径，如: “hits.hits[0]._source.@timestamp”
     */
    public static Object getKeyPathValue(Map map, String keyPath) {
        final String[] paths = keyPath.split("\\.");
        if (paths.length <= 0) {
            throw new RuntimeException("key属性路径错误: [" + keyPath + "]");
        }
        Map jsonMap = map;
        for (int i = 0; i < paths.length; i++) {
            if (jsonMap == null) {
                return null;
            }
            String path = paths[i];
            // 当前路径值是否是数组 取出属性 path 和 method
            Integer index = null;
            if (path.matches(".+\\[\\d+]")) {
                path = path.substring(0, path.length() - 1);
                String key = path.substring(0, path.lastIndexOf('['));
                String indexStr = path.substring(path.lastIndexOf('[') + 1);
                path = key;
                index = NumberUtils.toInt(indexStr);
            }
            // 最后一级
            boolean isLastPath = false;
            if ((i + 1) == paths.length) {
                isLastPath = true;
            }
            if (StringUtils.isBlank(path)) {
                if (isLastPath) {
                    return null;
                } else {
                    continue;
                }
            }
            if (!jsonMap.containsKey(path)) {
                return null;
            }
            // 当前路径值是数组
            if (index != null) {
                List list = (List) jsonMap.get(path);
                Object value = list.get(index);
                if (isLastPath) {
                    return value;
                } else {
                    jsonMap = (Map) value;
                }
            } else {
                Object value = jsonMap.get(path);
                if (isLastPath) {
                    return value;
                } else {
                    jsonMap = (Map) value;
                }
            }
        }
        return null;
    }

    /**
     * 根据Json属性路径 获取对应值
     *
     * @param jsonWrapper JsonWrapper对象
     * @param jsonPath    Json属性路径，如: “hits.hits[0]._source.@timestamp”
     */
    public static Object getJsonPathValue(JsonWrapper jsonWrapper, String jsonPath) {
        return getKeyPathValue(jsonWrapper.getInnerMap(), jsonPath);
    }

    @SuppressWarnings("unchecked")
    public Map<String, ?> getInnerMap() {
        return (Map<String, ?>) innerMap;
    }

    /**
     * 判断是否包含某个属性，属性名按层级传参
     *
     * @param args 属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public boolean contains(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map cnode = this.innerMap;
        for (int i = 0; i < lst.size() - 1; i++) {
            String v = lst.get(i);
            if (!cnode.containsKey(v) || !(cnode.get(v) instanceof Map)) {
                return false;
            }
            cnode = (Map) cnode.get(v);
        }
        return (cnode.containsKey(lst.get(lst.size() - 1)));
    }

    /**
     * 读取属性值，属性名按层级传参
     *
     * @param args 属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public Object get(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map cnode = this.innerMap;
        for (int i = 0; i < lst.size() - 1; i++) {
            String v = lst.get(i);
            if (!cnode.containsKey(v)) {
                return null;
            }
            cnode = (Map) cnode.get(v);
        }
        if (cnode == null || !cnode.containsKey(lst.get(lst.size() - 1))) {
            return null;
        }
        return cnode.get(lst.get(lst.size() - 1));
    }

    /**
     * 读取属性值，属性名按层级传参
     *
     * @param args 属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public String asStr(String... args) {
        return ConversionUtils.toString(get(args));
    }

    /**
     * 读取属性值，属性名按层级传参
     *
     * @param args 属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public BigDecimal asBigDec(String... args) {
        return ConversionUtils.converter(get(args));
    }

    /**
     * 把整个JsonWrapper转换数据类型
     */
    public <T> T asObject(Class<T> clazz) {
        try {
            String jsonStr = mapper.writeValueAsString(getInnerMap());
            return mapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取属性值(属性名按层级传参)
     *
     * @param clazz 读取的数据类型
     * @param args  属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public <T> T asObject(Class<T> clazz, String... args) {
        try {
            String jsonStr = mapper.writeValueAsString(get(args));
            return mapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取属性值(属性名按层级传参)
     *
     * @param valueTypeRef 读取的数据类型
     * @param args         属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public <T> T asObject(TypeReference valueTypeRef, String... args) {
        try {
            String jsonStr = mapper.writeValueAsString(get(args));
            return mapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取属性值，属性名按层级传参
     *
     * @param args 属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public int asInt(String... args) {
        return ConversionUtils.converter(get(args));
    }

    /**
     * 读取属性值，属性名按层级传参
     *
     * @param args 属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public long asLong(String... args) {
        return ConversionUtils.converter(get(args));
    }

    /**
     * 读取属性值，属性名按层级传参
     *
     * @param args 属性名，按层级依次传参。如：userInfo, contacts, qq, account
     */
    public boolean asBoolean(String... args) {
        return ConversionUtils.converter(get(args));
    }

    /**
     * 删除属性数据
     * @param key 属性key
     */
    public void remove(String key) {
        innerMap.remove(key);
    }

    /**
     * 返回所有的属性key
     */
    @SuppressWarnings("unchecked")
    public Collection<String> keys() {
        return innerMap.keySet();
    }

    /***
     * 数据属性数量
     */
    public int size() {
        return innerMap.size();
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    public JsonWrapper asDic(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map jb;
        if (lst.size() >= 2) {
            jb = buildPath(lst.subList(0, lst.size() - 1));
        } else {
            jb = innerMap;
        }
        Object v = lst.get(lst.size() - 1);
        Map lr;
        if (!jb.containsKey(v)) {
            lr = new LinkedHashMap<String, Object>();
            jb.put(v, lr);
        } else {
            lr = (Map) jb.get(v);
        }
        return new JsonWrapper(lr);
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    public JsonArrayWrapper asList(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map jb;
        if (lst.size() >= 2) {
            jb = buildPath(lst.subList(0, lst.size() - 1));
        } else {
            jb = innerMap;
        }
        Object v = lst.get(lst.size() - 1);
        List lr;
        if (!jb.containsKey(v)) {
            lr = new ArrayList();
            jb.put(v, lr);
        } else {
            lr = (List) jb.get(v);
        }
        return new JsonArrayWrapper(lr);
    }

    @SuppressWarnings("unchecked")
    private Map buildPath(List<?> lst) {
        Map cnode = this.innerMap;
        for (Object obj : lst) {
            assert ((obj instanceof String) && obj.toString().length() > 0);
            String v = (String) obj;
            if (!cnode.containsKey(v)) {
                cnode.put(v, new LinkedHashMap<String, Object>());
            }
            cnode = (Map) cnode.get(v);
        }
        return cnode;
    }

    @SuppressWarnings("unchecked")
    public JsonWrapper set(Object... args) {
        assert (args.length >= 2);
        List<Object> lst = Arrays.asList(args);
        Map jb = buildPath(lst.subList(0, lst.size() - 2));
        Object v = lst.get(lst.size() - 1);
        jb.put(lst.get(lst.size() - 2).toString(), v);
        return this;
    }

    @Override
    public String toString() {
        return toJsonPretty(innerMap);
    }

    /**
     * 根据Json属性路径 获取对应值
     *
     * @param jsonPath Json属性路径，如: “hits.hits[0]._source.@timestamp”
     */
    public Object getJsonPathValue(String jsonPath) {
        return getKeyPathValue(this.innerMap, jsonPath);
    }
}
