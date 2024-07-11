package zju.cst.aces.api.impl.obfuscator.frame;

import lombok.Data;

import java.util.List;

/**
 * 符号类
 * @author dfa
 * @date 2024/07/11
 */
@Data
public class Symbol {
    private String name;
    private String owner;
    private String type;
    private Integer lineNum;

    /**
     * @param name
     * @param owner
     * @param type
     * @param line
     */
    public Symbol(String name, String owner, String type, Integer line) {
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.lineNum = line;
    }

    /**
     * 判断符号是否在组中
     * @param groupIds
     * @return boolean
     */
    public boolean isInGroup(List<String> groupIds) {
        for (String gid : groupIds) {
            if (owner.contains(gid) || type.contains(gid)) {
                return true;
            }
        }
        return false;
    }
}
