package frontEnd.Interface;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import lombok.Getter;
import util.Utils;

import java.util.Arrays;

/**
 * <p>version handling class.</p>
 *
 * @author franceme
 * Created on 10/27/19.
 * @version V03.07.06
 * @since V03.07.06
 *
 * <p>The central point for handling the Java Version ().</p>
 */
public enum Version {
    //region Values
    ONE(1, 45),
    TWO(2, 46),
    THREE(3, 47),
    FOUR(4, 48),
    FIVE(5, 49),
    SIX(6, 50),
    SEVEN(7, 51),
    EIGHT(8, 52),
    NINE(9, 53),
    TEN(10, 54),
    ELEVEN(11, 55),
    TWELVE(12, 56),
    THIRTEEN(13, 57),
    FOURTEEN(14, 58),
    FIFTEEN(15, 59),
    SIXTEEN(16, 60),
    SEVENTEEN(17, 61);
    //endregion

    //region Attributes
    @Getter
    private int versionNumber;
    @Getter
    private int majorVersion;
    //endregion

    //region Constructor
    Version(int versionNumber, int majorVersion) {
        this.versionNumber = versionNumber;
        this.majorVersion = majorVersion;
    }
    //endregion

    //region Methods
    public static Version retrieveByMajor(int majorVersion) throws ExceptionHandler {
        return Arrays.stream(Version.values())
                .filter(v -> v.getMajorVersion() == majorVersion)
                .findFirst()
                .orElseThrow(
                        () ->
                                new ExceptionHandler("Major Version: " + majorVersion + " not valid.", ExceptionId.FILE_AFK));
    }

    public boolean supportedFile() {
        return this.getVersionNumber() <= Utils.supportedVersion.getVersionNumber();
    }

    public static boolean supportedByMajor(int majorVersion) throws ExceptionHandler {
        return retrieveByMajor(majorVersion).getVersionNumber() <= Utils.supportedVersion.getVersionNumber();
    }
    //endregion
}
