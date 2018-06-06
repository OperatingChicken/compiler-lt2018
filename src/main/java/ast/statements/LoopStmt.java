package ast.statements;

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

    public void codeGen(CodeGen codegen) {
        LLVMBasicBlockRef currentBB = codegen.popBlockStack();
        LLVMBasicBlockRef condBB = codegen.newBlock();
        LLVMBasicBlockRef bodyBB = codegen.newBlock();
        LLVMBasicBlockRef afterBB = codegen.newBlock();
        LLVMBuildBr(codegen.getBuilder(), condBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), condBB);
        codegen.pushBlockStack(condBB);
        LLVMValueRef cond_value = codegen.getBoolean(this.condition.codeGen(codegen));
        LLVMBasicBlockRef newCondBB = codegen.popBlockStack();
        LLVMBuildCondBr(codegen.getBuilder(), cond_value, bodyBB, afterBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), bodyBB);
        codegen.pushBlockStack(bodyBB);
        this.body.codeGen(codegen);
        LLVMBasicBlockRef newBodyBB = codegen.popBlockStack();
        LLVMBuildBr(codegen.getBuilder(), condBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), afterBB);
        codegen.pushBlockStack(afterBB);
    }

    public Set<String> getIdentifiers() {
        Set<String> result = this.condition.getIdentifiers();
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
