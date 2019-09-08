package util.manifest;

import java.io.InputStream;

/**
 * Common interface for handlers working on Android manifest files
 *
 * @author Steven Arzt
 * @version 03.07.01
 */
public interface IManifestHandler {

    /**
     * Called when the contents of the Android manifest file shall be processed
     *
     * @param stream The stream through which the manifest file can be accesses
     */
    void handleManifest(InputStream stream);

}

