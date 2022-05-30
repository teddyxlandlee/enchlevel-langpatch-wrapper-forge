package xland.mcfgmod.enchlevellangpatch.wrapper.forge.plugin;

import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

class ModRemapper {
    private final Path rootPath;
    private final InputStream modJar;

    ModRemapper(Path rootPath, InputStream modJar) {
        this.rootPath = rootPath;
        this.modJar = modJar;
    }

    public URL remap() throws IOException {
        throw new NotImplementedException("TODO");
    }
}
