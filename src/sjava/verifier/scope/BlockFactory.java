package sjava.verifier.scope;

import sjava.verifier.parser.ConditionBlockStmt;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.variable.IllegalValue;
import sjava.verifier.variable.UnassignedFinal;

public class BlockFactory {
    public static Block createBlock(Scope parent, ConditionBlockStmt blockStmt) throws IllegalBlockType, IllegalExpression, IllegalValue, UnexpectedToken, UnassignedFinal {
        String blockType = blockStmt.getConditionType();
        Block block;
        switch(blockType){
            case "if":
                block = new IfBlock(parent, blockStmt.getConditions());
                break;
            case "while":
                block = new WhileBlock(parent, blockStmt.getConditions());
                break;
            default:
                throw new IllegalBlockType(blockType);
        }
        return block;
    }
}
