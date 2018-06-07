package ast.expressions;

import cg.CodeGen;
import org.bytedeco.javacpp.LLVM;

import java.util.HashSet;
import java.util.Set;

public class IfExpr extends Expr {
    private final Expr cond;
    private final Expr ifTrue;
    private final Expr ifFalse;

    public IfExpr(Expr cond, Expr ifTrue, Expr ifFalse) {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public LLVM.LLVMValueRef codeGen(CodeGen codegen) {
        return null;
    }

    @Override
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<>();
        result.addAll(cond.getIdentifiers());
        result.addAll(ifTrue.getIdentifiers());
        result.addAll(ifFalse.getIdentifiers());
        return result;
    }

    @Override
    public String toString() {
        return "( " + cond.toString() + " ? " + ifTrue.toString() + " : " + ifFalse.toString() + " )";
    }
}
