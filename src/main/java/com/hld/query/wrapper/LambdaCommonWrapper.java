package com.hld.query.wrapper;


import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 自定义mybatis-plus lambda wrapper
 *
 * @author huald
 * @date 2019/7/19
 */
public class LambdaCommonWrapper<T> extends AbstractLambdaWrapper<T, LambdaCommonWrapper<T>> implements Query<LambdaCommonWrapper<T>, T, SFunction<T, ?>> {
    private SharedString sqlSelect;

    @SuppressWarnings("unchecked")
    public LambdaCommonWrapper() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public LambdaCommonWrapper(T entity) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
    }

    @SuppressWarnings("unchecked")
    LambdaCommonWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql, SharedString sqlComment) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.entityClass = entityClass;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SafeVarargs
    public final LambdaCommonWrapper<T> select(SFunction... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(this.columnsToString(false, columns));
        }

        return this.typedThis;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LambdaCommonWrapper<T> select(Predicate<TableFieldInfo> predicate) {
        return this.select(this.entityClass, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public LambdaCommonWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(this.getCheckEntityClass()).chooseSelect(predicate));
        return this.typedThis;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected LambdaCommonWrapper<T> instance() {
        return new LambdaCommonWrapper(this.entity, this.entityClass, null, this.paramNameSeq, this.paramNameValuePairs, new MergeSegments(), SharedString.emptyString(), SharedString.emptyString());
    }
}
