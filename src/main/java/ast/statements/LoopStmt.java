package ast.statements;

import java.util.HashSet;
import java.util.Set;
import ast.expressions.Expr;
import cg.CodeGen;
import org.bytedeco.javacpp.LLVM.LLVMBasicBlockRef;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import static org.bytedeco.javacpp.LLVM.LLVMBuildBr;
import static org.bytedeco.javacpp.LLVM.LLVMBuildCondBr;
import static org.bytedeco.javacpp.LLVM.LLVMPositionBuilderAtEnd;

public class LoopStmt extends Stmt {
    private final Expr condition;
    private final Stmt body;

    public LoopStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void codeGen(CodeGen codegen) {
        codegen.popBlockStack();
        LLVMBasicBlockRef condBB = codegen.newBlock();
        LLVMBasicBlockRef bodyBB = codegen.newBlock();
        LLVMBasicBlockRef afterBB = codegen.newBlock();
        LLVMBuildBr(codegen.getBuilder(), condBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), condBB);
        codegen.pushBlockStack(condBB);
        LLVMValueRef cond_value = codegen.getBoolean(this.condition.codeGen(codegen));
        codegen.popBlockStack();
        LLVMBuildCondBr(codegen.getBuilder(), cond_value, bodyBB, afterBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), bodyBB);
        codegen.pushBlockStack(bodyBB);
        this.body.codeGen(codegen);
        codegen.popBlockStack();
        LLVMBuildBr(codegen.getBuilder(), condBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), afterBB);
        codegen.pushBlockStack(afterBB);
    }

    @Override
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<>(this.condition.getIdentifiers());
        result.addAll(this.body.getIdentifiers());
        return result;
    }

    @Override
    public String toString() {
        return "loop " + condition.toString() +
                System.lineSeparator() + body.toString() +
                System.lineSeparator() + "endLoop";
    }
}
