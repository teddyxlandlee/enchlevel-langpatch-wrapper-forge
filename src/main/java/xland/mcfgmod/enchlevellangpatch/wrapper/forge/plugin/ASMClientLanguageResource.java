package xland.mcfgmod.enchlevellangpatch.wrapper.forge.plugin;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public class ASMClientLanguageResource implements ITransformer<MethodNode> {
    private final String clazzName, methodName, storageFieldName;
    public static final String methodDesc = "(Ljava/lang/String;)Ljava/lang/String;";
    public static final String storageFieldDesc = "Ljava/util/Map;";

    public static final String langPatchClassDotted = "xland.mcmod.enchlevellangpatch.impl.LangPatchImpl";
    public static final String langPatchClassSlashed = "xland/mcmod/enchlevellangpatch/impl/LangPatchImpl";
    public static final String asmHookClass = "xland/mcmod/enchlevellangpatch/impl/AsmHook";
    public static final String asmHookMethodName = "langPatchHook";
    public static final String asmHookMethodDesc = "(Ljava/lang/String;Ljava/util/Map;)Ljava/lang/String;";

    public ASMClientLanguageResource(String clazzName, String methodName, String storageFieldName) {
        this.clazzName = clazzName;
        this.methodName = methodName;
        this.storageFieldName = storageFieldName;
    }

    /**
     * Transform the input to the ITransformer's desire. The context from the last vote is
     * provided as well.
     *
     * @param input   The ASM input node, which can be mutated directly
     * @param context The voting context
     * @return An ASM node of the same type as that supplied. It will be used for subsequent
     * rounds of voting.
     */
    @Nonnull
    @Override
    public MethodNode transform(MethodNode input, ITransformerVotingContext context) {
        InsnList instructions = input.instructions;
        InsnList inserted = new InsnList(); {
            // load key, arg 1
            inserted.add(new VarInsnNode(Opcodes.ALOAD, 1));
            // load this.storage
            inserted.add(new VarInsnNode(Opcodes.ALOAD, 0));
            inserted.add(new FieldInsnNode(Opcodes.GETFIELD, clazzName, storageFieldName, storageFieldDesc));
            // invokeStatic AsmHook.langPatchHook (String, Map) -> String
            inserted.add(new MethodInsnNode(Opcodes.INVOKESTATIC, asmHookClass, asmHookMethodName, asmHookMethodDesc));

            LabelNode labelNext = new LabelNode();
            inserted.add(new InsnNode(Opcodes.DUP));
            inserted.add(new JumpInsnNode(Opcodes.IFNULL, labelNext));
            inserted.add(new InsnNode(Opcodes.ARETURN));
            inserted.add(labelNext);
            inserted.add(new InsnNode(Opcodes.POP));
        }
        instructions.insertBefore(instructions.getFirst(), inserted);
        return input;
    }

    /**
     * Return the {@link TransformerVoteResult} for this transformer.
     * The transformer should evaluate whether or not is is a candidate to apply during
     * the round of voting in progress, represented by the context parameter.
     * How the vote works:
     * <ul>
     * <li>If the transformer wishes to be a candidate, it should return {@link TransformerVoteResult#YES}.</li>
     * <li>If the transformer wishes to exit the voting (the transformer has already
     * has its intended change applied, for example), it should return {@link TransformerVoteResult#NO}</li>
     * <li>If the transformer wishes to wait for future rounds of voting it should return
     * {@link TransformerVoteResult#DEFER}. Note that if there is <em>no</em> YES candidate, but DEFER
     * candidates remain, this is a DEFERRAL stalemate and the game will crash.</li>
     * <li>If the transformer wishes to crash the game, it should return {@link TransformerVoteResult#REJECT}.
     * This is extremely frowned upon, and should not be used except in extreme circumstances. If an
     * incompatibility is present, it should detect and handle it in the {@link cpw.mods.modlauncher.api.ITransformationService#onLoad}
     * </li>
     * </ul>
     * After all votes from candidate transformers are collected, the NOs are removed from the
     * current set of voters, one from the set of YES voters is selected and it's {@link ITransformer#transform(Object, ITransformerVotingContext)}
     * method called. It is then removed from the set of transformers and another round is performed.
     *
     * @param context The context of the vote
     * @return A TransformerVoteResult indicating the desire of this transformer
     */
    @Nonnull
    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return context.doesClassExist() ? TransformerVoteResult.YES : TransformerVoteResult.NO;
    }

    /**
     * Return a set of {@link Target} identifying which elements this transformer wishes to try
     * and apply to. The {@link Target#getTargetType()} must match the T variable for the transformer
     * as documented in {@link TargetType}, other combinations will be rejected.
     *
     * @return The set of targets this transformer wishes to apply to
     */
    @Nonnull
    @Override
    public Set<Target> targets() {
        return Collections.singleton(Target.targetMethod(clazzName, methodName, methodDesc));
    }
}
