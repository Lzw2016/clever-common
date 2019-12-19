package org.clever.common.utils.mapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/12/09 09:09 <br/>
 */
@Slf4j
public class BeanMapperTest {

    final int size = 10000 * 10;

    List<ModelA> list = new ArrayList<>(size);

    @Before
    public void init() {
        for (int i = 0; i < size; i++) {
            ModelA modelA = new ModelA();
            modelA.setF1(1 + i);
            modelA.setF2(1.23D + i);
            modelA.setF3(new Date());
            modelA.setF4("abcd" + i);
            modelA.setF5(i % 3 == 0);
            list.add(modelA);
        }
    }

    /**
     * get set 性能测试
     */
    @Test
    public void t0() {
        // TEST --------> 性能测试
        long start = System.currentTimeMillis();
        ModelB modelB = null;
        for (ModelA modelA : list) {
            modelB = new ModelB();
            modelB.setF1(modelA.getF1());
            modelB.setF2(modelA.getF2());
            modelB.setF3(modelA.getF3());
            modelB.setF4(modelA.getF4());
            modelB.setF5(modelA.getF5());
        }
        long end = System.currentTimeMillis();
        log.info("### 性能测试 --> [{}ms] [{} 个/ms] {}", (end - start), size * 1.0 / (end - start), modelB);
    }

    /**
     * dozer 性能测试
     */
    @Test
    public void t1() {
        // 预热
        ModelB modelB = null;
        int i = 0;
        for (ModelA modelA : list) {
            i++;
            modelB = BeanMapper.mapper(modelA, ModelB.class);
            if (i > 1000) {
                break;
            }
        }
        // 测试
        long start = System.currentTimeMillis();
        for (ModelA modelA : list) {
            modelB = BeanMapper.mapper(modelA, ModelB.class);
        }
        long end = System.currentTimeMillis();
        log.info("### 性能测试 --> [{}ms] [{} 个/ms] {}", (end - start), size * 1.0 / (end - start), modelB);
    }

    /**
     * BeanCopier 性能测试 (封装后性能)
     */
    @Test
    public void t2() {
        // 预热
        ModelB modelB = null;
        int i = 0;
        for (ModelA modelA : list) {
            i++;
            modelB = CgLibBeanMapper.mapper(modelA, ModelB.class);
            if (i > 1000) {
                break;
            }
        }
        // 测试
        long start = System.currentTimeMillis();
        for (ModelA modelA : list) {
            modelB = CgLibBeanMapper.mapper(modelA, ModelB.class);
        }
        long end = System.currentTimeMillis();
        log.info("### 性能测试 --> [{}ms] [{} 个/ms] {}", (end - start), size * 1.0 / (end - start), modelB);
    }

    /**
     * BeanCopier 性能测试 (BeanCopier 原始性能)
     */
    @Test
    public void t3() {
        BeanCopier copier = BeanCopier.create(ModelA.class, ModelB.class, false);
        // 预热
        ModelB modelB = null;
        int i = 0;
        for (ModelA modelA : list) {
            i++;
            modelB = new ModelB();
            copier.copy(modelA, modelB, null);
            if (i > 1000) {
                break;
            }
        }
        // 测试
        long start = System.currentTimeMillis();
        for (ModelA modelA : list) {
            modelB = new ModelB();
            copier.copy(modelA, modelB, null);
        }
        long end = System.currentTimeMillis();
        log.info("### 性能测试 --> [{}ms] [{} 个/ms] {}", (end - start), size * 1.0 / (end - start), modelB);
    }

    @Test
    public void t00() {
        ModelA modelA = new ModelA();
        modelA.setF1(1);
        modelA.setF2(1.23D);
        modelA.setF3(new Date());
        modelA.setF4("abcd");
        modelA.setF5(true);
        modelA.setF6("2018-09-12 13:45:26");
        modelA.setF7("QQQ");
        ModelB modelB = CgLibBeanMapper.mapper(modelA, ModelB.class);
        log.info("### 自定义转换 --> {}", modelB);
    }

//    @Test
//    public void t01() {
//        ModelA modelA = new ModelA();
//        modelA.setF1(1);
//        modelA.setF2(1.23D);
//        modelA.setF3(new Date());
//        modelA.setF4("abcd");
//        modelA.setF5(true);
//        modelA.setF6("2018-09-12 13:45:26");
//        modelA.setF7("QQQ");
//        modelA.setF9(123456L);
//        ModelB modelB = CgLibBeanMapper.mapper(modelA, ModelB.class,  source -> {
//            if (source == null) {
//                return null;
//            }
//            return String.valueOf(source);
//        });
//        log.info("### 自定义转换 --> {}", modelB);
//    }

    @Data
    public static class ModelA {
        private Integer f1;
        private Double f2;
        private Date f3;
        private String f4;
        private Boolean f5;
        // 字段类型不同
        private String f6;
        // 字段没有对于关系
        private String f7;
        // 字段类型不同
        private Long f9;
    }

    @Data
    public static class ModelB {
        private Integer f1;
        private Double f2;
        private Date f3;
        private String f4;
        private Boolean f5;
        // 字段类型不同
        private Date f6;
        // 字段没有对于关系
        private String f8;
        // 字段类型不同
        private String f9;
    }
}

