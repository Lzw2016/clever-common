package org.clever.common.utils.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String asStr(String... args) {
        return ConversionUtils.toString(get(args));
    }

    public BigDecimal asBigDec(String... args) {
        return ConversionUtils.converter(get(args));
    }

    public <T> T asObject(Class<T> clazz) {
        try {
            String jsonStr = mapper.writeValueAsString(getInnerMap());
            return mapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T asObject(Class<T> clazz, String... args) {
        try {
            String jsonStr = mapper.writeValueAsString(get(args));
            return mapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T asObject(TypeReference valueTypeRef, String... args) {
        try {
            String jsonStr = mapper.writeValueAsString(get(args));
            return mapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int asInt(String... args) {
        return ConversionUtils.converter(get(args));
    }

    public long asLong(String... args) {
        return ConversionUtils.converter(get(args));
    }

    public void remove(String key) {
        innerMap.remove(key);
    }

    public boolean asBoolean(String... args) {
        return ConversionUtils.converter(get(args));
    }

    @SuppressWarnings("unchecked")
    public Collection<String> keys() {
        return innerMap.keySet();
    }

    public int size() {
        return innerMap.size();
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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


}
