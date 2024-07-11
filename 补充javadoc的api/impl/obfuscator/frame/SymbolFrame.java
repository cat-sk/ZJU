package zju.cst.aces.api.impl.obfuscator.frame;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 符号帧
 * @author dfa
 * @date 2024/07/11
 */
@Data
public class SymbolFrame {
    private String className;
    private String superName;
    private List<String> interfaces;
    private Set<Symbol> fieldDef = new HashSet<>();
    private Set<Symbol> fieldUse = new HashSet<>();
    private Set<Symbol> varDef = new HashSet<>();
    private Set<Symbol> varUse = new HashSet<>();
    private Set<Symbol> methodDef = new HashSet<>();
    private Set<Symbol> methodUse = new HashSet<>();

    /**
     * 添加字段定义
     * @param symbol
     */
    public void addFieldDef(Symbol symbol) {
        fieldDef.add(symbol);
    }

    /**
     * 添加字段使用
     * @param symbol
     */
    public void addFieldUse(Symbol symbol) {
        fieldUse.add(symbol);
    }

    /**
     * 获取字段定义
     * @param symbol
     */
    public void addVarDef(Symbol symbol) {
        varDef.add(symbol);
    }

    /**
     * 获取字段使用
     * @param symbol
     */
    public void addVarUse(Symbol symbol) {
        varUse.add(symbol);
    }

    /**
     * 添加方法定义
     * @param symbol
     */
    public void addMethodDef(Symbol symbol) {
        methodDef.add(symbol);
    }

    /**
     * 获取方法使用
     * @param symbol
     */
    public void addMethodUse(Symbol symbol) {
        methodUse.add(symbol);
    }

    /**
     * 将两个对象进行合并
     * @param frame
     */
    public void merge(SymbolFrame frame) {
        if (frame == null) {
            return;
        }
        if (frame.fieldDef != null) {
            fieldDef.addAll(frame.fieldDef);
        }
        if (frame.fieldUse != null) {
            fieldUse.addAll(frame.fieldUse);
        }
        if (frame.varDef != null) {
            varDef.addAll(frame.varDef);
        }
        if (frame.varUse != null) {
            varUse.addAll(frame.varUse);
        }
        if (frame.methodDef != null) {
            methodDef.addAll(frame.methodDef);
        }
        if (frame.methodUse != null) {
            methodUse.addAll(frame.methodUse);
        }
    }

    /**
     * 根据groupId过滤符号
     * @param groupIds
     */
    public void filterSymbolsByGroupId(List<String> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return;
        }
        List<String> targets = groupIds.stream().map(id -> id.replace(".", "/")).collect(Collectors.toList());
        className = isClassInGroup(className, groupIds) ? className.substring(className.lastIndexOf("/") + 1) : null;
        superName = isClassInGroup(superName, groupIds) ? superName.substring(superName.lastIndexOf("/") + 1) : null;
        for (int i=0; i< interfaces.size(); i++) {
            String interfaceName = interfaces.get(i);
            if (isClassInGroup(interfaceName, groupIds)) {
                interfaces.set(i, interfaceName.substring(interfaceName.lastIndexOf("/") + 1));
            } else {
                interfaces.remove(i);
                i--;
            }
        }
        fieldDef.removeIf(symbol -> !symbol.isInGroup(targets));
        fieldUse.removeIf(symbol -> !symbol.isInGroup(targets));
        varDef.removeIf(symbol -> !symbol.isInGroup(targets));
        varUse.removeIf(symbol -> !symbol.isInGroup(targets));
        methodDef.removeIf(symbol -> !symbol.isInGroup(targets));
        methodUse.removeIf(symbol -> !symbol.isInGroup(targets));
    }

    /**
     * 根据groupId拆分符号
     * @param groupIds
     * @return {@link Set }<{@link String }>
     */
    public Set<String> toObNames(List<String> groupIds) {
        Set<String> obNames = new HashSet<>();
        if (className != null) {
            obNames.add(className);
        }
        if (superName != null) {
            obNames.add(superName);
        }
        if (interfaces != null) {
            obNames.addAll(interfaces);
        }
        obNames.addAll(fieldDef.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(fieldDef.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(fieldDef.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(fieldUse.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(fieldUse.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(fieldUse.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(varDef.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(varDef.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(varDef.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(varUse.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(varUse.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(varUse.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(methodDef.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(methodDef.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(methodDef.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));

        obNames.addAll(methodUse.stream().map(Symbol::getName).collect(Collectors.toSet()));
        obNames.addAll(methodUse.stream().map(s -> splitTypeName(s.getOwner(), groupIds)).collect(Collectors.toSet()));
        obNames.addAll(methodUse.stream().map(s -> splitTypeName(s.getType(), groupIds)).collect(Collectors.toSet()));
        obNames.remove("");
        return obNames;
    }

    /**
     * 根据groupIds获取符号表中的所有符号名
     * @param type
     * @param groupIds
     * @return {@link String }
     */
    public String splitTypeName(String type, List<String> groupIds) {
        if (type == null || type.isEmpty() || !isClassInGroup(type, groupIds)) {
            return "";
        }
        String[] parts = type.split("/");
        String ret = parts[parts.length - 1];
        if (ret.contains("$")) {
            ret = ret.substring(ret.lastIndexOf("$") + 1);
        }
        if (ret.contains(";")) {
            ret = ret.substring(0, ret.indexOf(";"));
        }
        return ret;
    }

    /**
     * 判断类名是否在groupIds中
     * @param fullClassName
     * @param groupIds
     * @return boolean
     */
    public static boolean isClassInGroup(String fullClassName, List<String> groupIds) {
        for (String gid : groupIds) {
            if (fullClassName.contains(gid.replace(".", "/"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否在groupIds中
     * @param str
     * @param groupIds
     * @return boolean
     */
    public static boolean isInGroup(String str, List<String> groupIds) {
        for (String gid : groupIds) {
            if (str.contains(gid)) {
                return true;
            }
        }
        return false;
    }

}
