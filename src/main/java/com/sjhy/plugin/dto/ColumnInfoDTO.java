package com.sjhy.plugin.dto;

import com.intellij.database.model.DasColumn;
import com.sjhy.plugin.entity.TypeMapper;
import com.sjhy.plugin.tool.CurrGroupUtils;
import com.sjhy.plugin.tool.NameUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 列信息传输对象
 *
 * @author makejava
 * @version 1.0.0
 * @date 2021/08/14 17:29
 */
@Data
@NoArgsConstructor
public class ColumnInfoDTO {
    public ColumnInfoDTO(DasColumn column) {
        this.name = NameUtils.getInstance().getJavaName(column.getName());
        this.comment = column.getComment();
        this.type = getJavaType(column.getDataType().toString());
        this.custom = false;
        this.ext = new HashMap<>();
    }

    private String getJavaType(String dbType) {
        for (TypeMapper typeMapper : CurrGroupUtils.getCurrTypeMapperGroup().getElementList()) {
            switch (typeMapper.getMatchType()) {
                case REGEX:
                    if (Pattern.compile(typeMapper.getColumnType()).matcher(dbType).matches()) {
                        return typeMapper.getJavaType();
                    }
                    break;
                case ORDINARY:
                default:
                    if (dbType.equalsIgnoreCase(typeMapper.getColumnType())) {
                        return typeMapper.getJavaType();
                    }
                    break;
            }
        }
        return "java.lang.Object";
    }

    /**
     * 名称
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 全类型
     */
    private String type;
    /**
     * 标记是否为自定义附加列
     */
    private Boolean custom;
    /**
     * 扩展数据
     */
    private Map<String, Object> ext;
}