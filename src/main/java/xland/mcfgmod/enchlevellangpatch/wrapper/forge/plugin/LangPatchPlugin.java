package xland.mcfgmod.enchlevellangpatch.wrapper.forge.plugin;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class LangPatchPlugin implements ITransformationService {
    private static final Logger LOGGER = LogManager.getLogger("enchlevel-langpatch-wrapper-forge");

    private String clazzName, methodName, storageFieldName;

    /**
     * The name of this mod service. It will be used throughout the system. It should be lower case,
     * the first character should be alphanumeric and it should only consist of standard alphanumeric
     * characters
     *
     * @return the name of the mod service
     */
    @Nonnull
    @Override
    public String name() {
        return "enchlevel-langpatch-wrapper-forge";
    }

    /**
     * Initialize your service.
     *
     * @param environment environment - query state from here to determine viability
     */
    @Override
    public void initialize(IEnvironment environment) {
        LOGGER.info("Hello World!");
    }

    /**
     * Scan for mods (but don't classload them), identify metadata that might drive
     * game functionality.
     *
     * @param environment environment
     */
    @Override
    public void beginScanning(IEnvironment environment) {}

    /**
     * Load your service. Called immediately on loading with a list of other services found.
     * Use to identify and immediately indicate incompatibilities with other services, and environment
     * configuration. This is to try and immediately abort a guaranteed bad environment.
     *
     * @param env           environment - query state from here
     * @param otherServices other services loaded with the system
     */
    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices)
            throws IncompatibleEnvironmentException {
        // TODO download mappings
        // TODO check if FML is not loaded
        // TODO load & remap enchlevel-langpatch itself.
    }

    /**
     * The {@link ITransformer} is the fundamental operator of the system.
     *
     * @return A list of transformers for your ITransformationService. This is called after {@link #onLoad(IEnvironment, Set)}
     * and {@link #initialize(IEnvironment)}, so you can return an appropriate Transformer set for the environment
     * you find yourself in.
     */
    @Nonnull
    @Override
    @SuppressWarnings("rawtypes")
    public List<ITransformer> transformers() {
        return Collections.singletonList(new ASMClientLanguageResource(clazzName, methodName, storageFieldName));
    }
}
