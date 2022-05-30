package xland.mcfgmod.enchlevellangpatch.wrapper.forge.wrapper;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @deprecated DON'T TOUCH THIS CLASS!
 */
// The value here should match an entry in the META-INF/mods.toml file
@Mod("enchlevel-langpatch-wrapper-forge")
public class EnchlevelLangpatchWrapperForge {
    private static final Logger LOGGER = LogManager.getLogger();

    public EnchlevelLangpatchWrapperForge() {
        FMLJavaModLoadingContext.get().getModEventBus()
                .register(this);
    }

    @SubscribeEvent
    public void activateLangPatch(FMLCommonSetupEvent event) {
        Class<?> clzLangPatchImpl;
        try {
            clzLangPatchImpl = Class.forName("xland.mcmod.enchlevellangpatch.impl.LangPatchImpl");
        } catch (ClassNotFoundException e) {
            LOGGER.fatal("Unable to find LangPatchImpl class");
            throw new IllegalStateException(e);
        }
        try {
            MethodHandles.lookup().findStatic(clzLangPatchImpl, "init",
                    MethodType.methodType(void.class)).invokeExact();
        } catch (Throwable e) {
            LOGGER.fatal("Unable to invoke LangPatchImpl.init()");
            throw new IllegalStateException(e);
        }
    }
}
