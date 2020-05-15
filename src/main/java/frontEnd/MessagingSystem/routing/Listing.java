/* Licensed under GPL-3.0 */
package frontEnd.MessagingSystem.routing;

import frontEnd.Interface.formatArgument.TypeSpecificArg;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import frontEnd.MessagingSystem.routing.outputStructures.block.Structure;
import frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer;
import frontEnd.MessagingSystem.routing.outputStructures.stream.Legacy;
import frontEnd.argsIdentifier;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 * The enum containing all of the different messaging types available for the user.
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.00
 */
public enum Listing {
  //region Values
  Legacy("Legacy", "L", ".txt", true, null),
  ScarfXML("ScarfXML", "SX", null, true, JacksonSerializer.JacksonType.XML),
  Default("Default", "D", null, true, JacksonSerializer.JacksonType.JSON),
  YAMLGeneric("Default", "Y", null, true, JacksonSerializer.JacksonType.YAML),
  XMLGeneric("Default", "X", null, true, JacksonSerializer.JacksonType.XML);
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Listing.class);
  //endregion
  //region Attributes
  private final String blockPath = "frontEnd.MessagingSystem.routing.outputStructures.block.";
  private final String inputPath = "frontEnd.MessagingSystem.routing.inputStructures";
  private final String streamPath = "frontEnd.MessagingSystem.routing.outputStructures.stream.";
  private final String typeSpecificArgPath = "frontEnd.Interface.formatArgument.";
  private String type;
  private String flag;
  private String outputFileExt;
  private Boolean streamEnabled;
  private JacksonSerializer.JacksonType jacksonType;
  //endregion

  //region Constructor

  /**
   * The inherint constructor of all the enum value types listed here
   *
   * @param Type - the string value of the type of
   * @param Flag - the flag used to identify the specific messaging type
   */
  Listing(
      String Type,
      String Flag,
      String outputFileExt,
      Boolean streamed,
      JacksonSerializer.JacksonType jacksonType) {
    this.type = Type;
    this.flag = Flag;
    this.outputFileExt = outputFileExt;
    this.streamEnabled = streamed;
    this.jacksonType = jacksonType;
  }
  //endregion

  //region Overridden Methods

  /**
   * getInputFullHelp.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getInputFullHelp() {
    StringBuilder out = new StringBuilder();

    for (Listing listingType : Listing.values()) {
      /*
      try {
          out.append(listingType.retrieveSpecificArgHandler().helpInfo()).append("\n");
      } catch (ExceptionHandler e) {}
      */
    }

    return out.toString();
  }
  //endregion

  //region Getter

  /**
   * The dynamic loader for the Listing Type based on the flag
   *
   * @param flag {@link java.lang.String} - The input type looking for the flag type
   * @return {@link frontEnd.MessagingSystem.routing.Listing} - The messaging Type retrieved by
   *     flag, if not found the default will be used
   */
  public static Listing retrieveListingType(String flag) {
    if (flag != null) for (Listing type : Listing.values()) if (type.flag.equals(flag)) return type;

    return Listing.Default;
  }

  /**
   * getShortHelp.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getShortHelp() {
    StringBuilder helpString = new StringBuilder("{");

    for (Listing in : Listing.values())
      helpString.append(in.getType()).append(": ").append(in.getFlag()).append(", ");

    helpString.append("}");

    return helpString.toString();
  }

  /**
   * Getter for flag
   *
   * <p>getFlag()
   *
   * @return a {@link java.lang.String} object.
   */
  public String getFlag() {
    return flag;
  }

  /**
   * retrieveArgs.
   *
   * @return a {@link java.util.List} object.
   */
  public List<argsIdentifier> retrieveArgs() {
    return argsIdentifier.lookup(this);
  }
  //endregion

  //region Helpers Based on the enum type

  //region Output Helpers

  /**
   * Getter for type
   *
   * <p>getType()
   *
   * @return {@link java.lang.String} - The type.
   */
  public String getType() {
    return type;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{ \"type\": \"" + this.type + "\", \"flag\": \"" + this.flag + "\"}";
  }

  //endregion

  //region InputHelpers

  /**
   * getTypeOfMessagingOutput.
   *
   * @param stream a boolean.
   * @param info a {@link frontEnd.MessagingSystem.routing.EnvironmentInformation} object.
   * @return a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public OutputStructure getTypeOfMessagingOutput(boolean stream, EnvironmentInformation info)
      throws ExceptionHandler {

    if (stream) {
      if (!this.streamEnabled) {
        log.info("Streaming is not supported for the format: " + this.getType());
        log.info("Defaulting back to block based output.");
        return getTypeOfMessagingOutput(false, info);
      } else {
        try {
          return (frontEnd.MessagingSystem.routing.outputStructures.stream.Structure)
              Class.forName(this.streamPath + this.type)
                  .getConstructor(EnvironmentInformation.class)
                  .newInstance(info);
        } catch (Exception e) {
          return new Legacy(info);
        }
      }
    } else //non-streamed
    {
      try {
        return (Structure)
            Class.forName(this.blockPath + this.type)
                .getConstructor(EnvironmentInformation.class)
                .newInstance(info);
      } catch (Exception e) {
        return new frontEnd.MessagingSystem.routing.outputStructures.block.Legacy(info);
      }
    }
  }

  /**
   * unmarshall.
   *
   * @param stream a boolean.
   * @param filePath a {@link java.lang.String} object.
   * @return a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public OutputStructure unmarshall(boolean stream, String filePath) throws ExceptionHandler {
    if (stream) {
      if (!this.streamEnabled) {
        log.info("Streaming is not supported for the format: " + this.getType());
        log.info("Defaulting to block based output.");
        return unmarshall(false, filePath);
      } else {
        try {
          return (frontEnd.MessagingSystem.routing.outputStructures.stream.Structure)
              Class.forName(this.streamPath + this.type)
                  .getConstructor(String.class)
                  .newInstance(filePath);
        } catch (Exception e) {
          log.fatal(
              "Issue dynamically calling the stream TypeSpecificArg with the filepath: "
                  + filePath);
          throw new ExceptionHandler(
              ExceptionId.ARG_VALID,
              "Issue dynamically calling the TypeSpecificArg with the filepath: " + filePath);
        }
      }
    } else //non-streamed
    {
      try {
        return (Structure)
            Class.forName(this.blockPath + this.type)
                .getConstructor(String.class)
                .newInstance(filePath);
      } catch (Exception e) {
        log.fatal(
            "Issue dynamically calling the blocked TypeSpecificArg with the filepath: " + filePath);
        throw new ExceptionHandler(
            ExceptionId.ARG_VALID,
            "Issue dynamically calling the TypeSpecificArg with the filepath: " + filePath);
      }
    }
  }

  /**
   * retrieveSpecificArgHandler.
   *
   * @return a {@link frontEnd.Interface.formatArgument.TypeSpecificArg} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public TypeSpecificArg retrieveSpecificArgHandler() throws ExceptionHandler {
    try {
      return (TypeSpecificArg)
          Class.forName(this.typeSpecificArgPath + this.type).getConstructor().newInstance();
    } catch (Exception e) {
      log.warn(
          "Issue dynamically calling the specific argument validator: "
              + this.typeSpecificArgPath
              + this.type);
      throw new ExceptionHandler(
          ExceptionId.ENV_VAR,
          "Issue dynamically calling the specific argument validator: "
              + this.typeSpecificArgPath
              + this.type);
    }
  }

  /**
   * Getter for the field <code>outputFileExt</code>.
   *
   * @return a {@link java.lang.String} object.
   */
  public String getOutputFileExt() {

    if (outputFileExt == null) return this.jacksonType.getExtension();
    else return outputFileExt;
  }

  /**
   * Getter for the field <code>jacksonType</code>.
   *
   * @return a {@link
   *     frontEnd.MessagingSystem.routing.outputStructures.common.JacksonSerializer.JacksonType}
   *     object.
   */
  public JacksonSerializer.JacksonType getJacksonType() {
    return jacksonType;
  }
  //endregion

  //endregion
}
