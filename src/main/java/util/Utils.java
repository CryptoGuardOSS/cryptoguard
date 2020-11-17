/* Licensed under GPL-3.0 */
package util;

import static soot.SootClass.BODIES;

import analyzer.backward.*;
import frontEnd.Interface.Version;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import frontEnd.argsIdentifier;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.ZipDexContainer;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.objectweb.asm.ClassReader;
import rule.engine.EngineType;
import slicer.backward.heuristic.HeuristicBasedAnalysisResult;
import slicer.backward.heuristic.HeuristicBasedInstructions;
import slicer.backward.orthogonal.OrthogonalInfluenceInstructions;
import slicer.backward.orthogonal.OrthogonalSlicingResult;
import soot.*;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.options.Options;
import soot.util.Chain;

/**
 * Utils class.
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since 01.00.00
 */
public class Utils {

  //region Static Variables
  /** Constant <code>SLICE_LENGTH</code> */
  public static final ArrayList<Integer> SLICE_LENGTH = new ArrayList<>();
  /** Constant <code>lineSep="System.getProperty(file.separator)"</code> */
  public static final String fileSep = System.getProperty("file.separator");
  /** Constant <code>lineSep="System.getProperty(line.separator)"</code> */
  public static final String lineSep = System.getProperty("line.separator");
  /** Constant <code>projectVersion=":$CVER 04.05.02$"</code> */
  public static final String version = "$CVER 04.05.02$";

  public static final String projectVersion =
      version.replaceAll(",]", "").replace("CVER ", "").replaceAll("\\$", "");
  /** Constant <code>projectName="CryptoGuard"</code> */
  public static final String projectName = "CryptoGuard";
  /** Constant <code>userPath="System.getProperty(user.home)"</code> */
  public static final String userPath = System.getProperty("user.home");

  private static final List<String> ASSIGN_DONT_VISIT = new ArrayList<>();
  private static final List<String> INVOKE_DONT_VISIT = new ArrayList<>();
  private static final Pattern sootClassPattern = Pattern.compile("[<](.+)[:]");
  private static final Pattern sootClassPatternTwo =
      Pattern.compile("([a-zA-Z0-9]+[.][a-zA-Z0-9]+)\\$[0-9]+");
  private static final Pattern sootFoundPattern = Pattern.compile("\\[(.+)\\]");
  private static final Pattern sootLineNumPattern = Pattern.compile("\\(\\)\\>\\[(\\d+)\\]");
  private static final Pattern sootMthdPattern = Pattern.compile("<((?:[a-zA-Z0-9]+))>");
  private static final Pattern sootMthdPatternTwo = Pattern.compile("((?:[a-zA-Z0-9_]+))\\(");
  private static final Pattern sootFoundMatchPattern = Pattern.compile("\"{1}(.+)\"{1}");
  private static final Pattern startComment = Pattern.compile("^\\s?\\/{1}\\*{1}");
  private static final Pattern comment = Pattern.compile("^\\s?\\*{1}");
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Utils.class);
  /** Constant <code>NUM_ORTHOGONAL=0</code> */
  public static int NUM_ORTHOGONAL = 0;
  /** Constant <code>NUM_CONSTS_TO_CHECK=0</code> */
  public static int NUM_CONSTS_TO_CHECK = 0;
  /** Constant <code>NUM_SLICES=0</code> */
  public static int NUM_SLICES = 0;
  /** Constant <code>NUM_HEURISTIC=0</code> */
  public static int NUM_HEURISTIC = 0;
  /** Constant <code>DEPTH_COUNT</code> */
  public static int[] DEPTH_COUNT;
  /** Constant <code>DEPTH=0</code> */
  public static int DEPTH = 0;

  static {
    ASSIGN_DONT_VISIT.add("<java.util.Map: java.lang.Object get(java.lang.Object)>");
    INVOKE_DONT_VISIT.add(
        "<java.util.Map: java.lang.Object put(java.lang.Object,java.lang.Object)>");
    INVOKE_DONT_VISIT.add("java.lang.String: void <init>");
  }

  /**
   * initDepth.
   *
   * @param depth a int.
   */
  public static void initDepth(int depth) {
    DEPTH = depth;
    DEPTH_COUNT = new int[depth];
  }
  //endregion

  //region HotMethods
  //region Wrappers

  public static ArrayList<String> retrievingThroughXArgs(
      EngineType eType, Boolean deps, Boolean needstoExist) throws ExceptionHandler {
    ArrayList<String> out = new ArrayList<>();

    ArrayList<String> types =
        deps
            ? new ArrayList<String>() {
              {
                add(".java");
                add(".class");
                add(".jar");
                add("dir");
              }
            }
            : new ArrayList<String>() {
              {
                add(eType.getInputExtension());
              }
            };

    try (BufferedReader inputStream =
        new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

      String curLine;
      while ((curLine = inputStream.readLine()) != null)
        out.addAll(retrieveFilePathTypesSingle(curLine, types, true, false, needstoExist));

    } catch (IOException | ExceptionHandler e) {
      throw new ExceptionHandler("Issue running through xArgs", ExceptionId.FILE_I);
    }
    return out;
  }

  public static ArrayList<String> retrieveFilePathTypesSingle(
      String rawFileString,
      ArrayList<String> types,
      Boolean expandPath,
      Boolean overwrite,
      Boolean needstoExist)
      throws ExceptionHandler {
    return retrieveFilePaths(
        new ArrayList<String>() {
          {
            add(rawFileString);
          }
        },
        types,
        expandPath,
        overwrite,
        needstoExist);
  }

  /**
   * retrieveFilePathTypes.
   *
   * @param rawFileString a {@link java.util.ArrayList} object.
   * @param expandPath a {@link java.lang.Boolean} object.
   * @param overwrite a {@link java.lang.Boolean} object.
   * @return a {@link java.util.ArrayList} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static ArrayList<String> retrieveFilePathTypes(
      ArrayList<String> rawFileString, Boolean expandPath, Boolean overwrite, Boolean needstoExist)
      throws ExceptionHandler {
    return retrieveFilePaths(
        rawFileString,
        new ArrayList<String>() {
          {
            add(".java");
            add(".class");
            add(".jar");
            add("dir");
          }
        },
        expandPath,
        overwrite,
        needstoExist);
  }

  /**
   * retrieveFilePathTypes.
   *
   * @param rawFileString a {@link java.lang.String} object.
   * @param type a {@link rule.engine.EngineType} object.
   * @param expandPath a {@link java.lang.Boolean} object.
   * @param overwrite a {@link java.lang.Boolean} object.
   * @return a {@link java.util.ArrayList} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static ArrayList<String> retrieveFilePathTypes(
      String rawFileString,
      EngineType type,
      Boolean expandPath,
      Boolean overwrite,
      Boolean needstoExist)
      throws ExceptionHandler {
    return retrieveFilePaths(
        new ArrayList<String>() {
          {
            add(rawFileString);
          }
        },
        type == null
            ? new ArrayList<>()
            : new ArrayList<String>() {
              {
                add(type.getInputExtension());
              }
            },
        expandPath,
        overwrite,
        needstoExist);
  }

  /**
   * retrieveFilePathTypes.
   *
   * @param rawFileString a {@link java.util.ArrayList} object.
   * @param type a {@link rule.engine.EngineType} object.
   * @param expandPath a {@link java.lang.Boolean} object.
   * @param overwrite a {@link java.lang.Boolean} object.
   * @return a {@link java.util.ArrayList} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static ArrayList<String> retrieveFilePathTypes(
      ArrayList<String> rawFileString,
      EngineType type,
      Boolean expandPath,
      Boolean overwrite,
      Boolean needstoExist)
      throws ExceptionHandler {
    return retrieveFilePaths(
        rawFileString,
        type == null
            ? new ArrayList<>()
            : new ArrayList<String>() {
              {
                add(type.getInputExtension());
              }
            },
        expandPath,
        overwrite,
        needstoExist);
  }
  //endregion

  /**
   * inputFiles.
   *
   * @param file a {@link java.lang.String} object.
   * @param needsToExist a {@link java.lang.Boolean} object.
   * @return a {@link java.util.ArrayList} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  static ArrayList<String> inputFiles(
      String file,
      ArrayList<String> type,
      Boolean expandPath,
      Boolean overwrite,
      Boolean needsToExist)
      throws ExceptionHandler {
    ArrayList<String> filePaths = new ArrayList<>();
    String curLine = null;

    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      while ((curLine = StringUtils.trimToNull(reader.readLine())) != null) {
        for (String rawType : type)
          if (curLine.endsWith(rawType))
            if ((curLine = retrieveFilePath(curLine, rawType, expandPath, overwrite, needsToExist))
                != null) filePaths.add(curLine);

        if (type == null || type.size() == 0)
          if ((curLine = retrieveFilePath(curLine, null, expandPath, overwrite, needsToExist))
              != null) filePaths.add(curLine);
      }

      return filePaths;
    } catch (FileNotFoundException e) {
      log.fatal("File " + file + " not found");
      throw new ExceptionHandler("File " + file + " not found", ExceptionId.FILE_I);
    } catch (IOException e) {
      log.fatal("Error reading the file  " + file);
      throw new ExceptionHandler("Error reading the file  " + file, ExceptionId.FILE_I);
    }
  }

  /**
   * retrieveFilePaths.
   *
   * @param rawFileStrings a {@link java.util.ArrayList} object.
   * @param type a {@link java.util.ArrayList} object.
   * @param expandPath a {@link java.lang.Boolean} object.
   * @param overwrite a {@link java.lang.Boolean} object.
   * @param needsToExist a {@link java.lang.Boolean} object.
   * @return a {@link java.util.ArrayList} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static ArrayList<String> retrieveFilePaths(
      ArrayList<String> rawFileStrings,
      ArrayList<String> type,
      Boolean expandPath,
      Boolean overwrite,
      Boolean needsToExist)
      throws ExceptionHandler {
    ArrayList<String> output = new ArrayList<>();

    if (type != null
        && type.size() == 1
        && type.get(0).equals("dir")
        && rawFileStrings.size() > 1) {
      log.fatal("Please enter one source argument for this use case.");
      throw new ExceptionHandler(
          "Please enter one source argument for this use case.", ExceptionId.GEN_VALID);
    } else if (rawFileStrings.size() == 1 && rawFileStrings.get(0).endsWith(".in"))
      output = inputFiles(rawFileStrings.get(0), type, expandPath, overwrite, needsToExist);
    else
      for (String rawString : rawFileStrings)
        //Splitting the file just in case it is a java class path
        for (String fileString : rawString.split(":")) {
          String filePath = null;

          //Adding a null type to ensure it still loops through all of the strings
          if (type.size() == 0) type.add(null);

          for (String rawType : type) {
            //If the only string passed is a java class path and starts with a colon
            //Remove it
            if (rawString.startsWith(":")) rawString = rawString.replaceFirst(":", "");

            if (null == rawType || rawType.equals("dir"))
              filePath = retrieveFilePath(fileString, null, expandPath, false, needsToExist);
            else if (fileString.endsWith(rawType))
              filePath = retrieveFilePath(fileString, rawType, expandPath, false, needsToExist);

            if (StringUtils.isNotEmpty(filePath)) {
              output.add(filePath);
              break;
            }
          }
        }
    return output;
  }

  /**
   * retrieveFilePath.
   *
   * @param file a {@link java.lang.String} object.
   * @param type a {@link rule.engine.EngineType} object.
   * @param expandPath a {@link java.lang.Boolean} object.
   * @param overwrite a {@link java.lang.Boolean} object.
   * @param needsToExist a {@link java.lang.Boolean} object.
   * @return a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static String retrieveFilePath(
      String file, String type, Boolean expandPath, Boolean overwrite, Boolean needsToExist)
      throws ExceptionHandler {
    log.debug("Retrieving and verifying the file: " + file);
    //Base line string check
    if (StringUtils.isEmpty(file)) return null;

    //Handling the file extension
    if (null != type)
      if (!type.equals("dir") && !file.toLowerCase().toLowerCase().endsWith(type)) {
        log.debug(
            "File "
                + file
                + " doesn't have the right file type for "
                + type
                + ", often over-zealous checking.");
        return null;
      }

    File tempFile = new File(file);

    Boolean exists = tempFile.exists() || overwrite;

    if (!exists) {
      if (needsToExist) {
        log.fatal(tempFile.getName() + " does not exist.");
        throw new ExceptionHandler(tempFile.getName() + " does not exist.", ExceptionId.ARG_VALID);
      } else return null;
    }

    Boolean isDir = tempFile.isDirectory() || overwrite;
    Boolean isFile = tempFile.isFile() || overwrite;

    if (type != null)
      switch (type) {
        case ".class":
          //region Verifying the file was compiled with a Java Version Lower than 8
          try (DataInputStream stream = new DataInputStream(new FileInputStream(file))) {
            //Verifying if the class file has the Magic Java Number
            if (stream.readInt() != 0xcafebabe) {
              log.fatal("The class file " + file + " is not a valid java.class file.");
              throw new ExceptionHandler(
                  "The class file " + file + " is not a valid java.class file.",
                  ExceptionId.ARG_VALID);
            } else {

              //Moving the stream past the minor version
              stream.readUnsignedShort();

              //Checking the Major Version of the JDK that compiled the file against the supported version
              Version fileVersion = Version.retrieveByMajor(stream.readUnsignedShort());
              if (!fileVersion.supportedFile()) {
                log.fatal(
                    "The class file (compiled by a JDK Version "
                        + fileVersion.getVersionNumber()
                        + ") is not supported.");
                throw new ExceptionHandler(
                    "The class file (compiled by a JDK Version "
                        + fileVersion.getVersionNumber()
                        + ") is not supported.",
                    ExceptionId.ARG_VALID);
              }
            }
          } catch (IOException e) {
            log.fatal("Error reading the file " + file + ".");
            throw new ExceptionHandler(
                "Error reading the file " + file + ".", ExceptionId.FILE_READ);
          }
          //endregion
        case ".java":
        case ".jar":
        case ".apk":
          if (!isFile) {
            log.fatal(tempFile.getName() + " is not a valid file.");
            throw new ExceptionHandler(
                tempFile.getName() + " is not a valid file.", ExceptionId.ARG_VALID);
          }
          break;
        case "dir":
          if (!isDir) {
            log.fatal(tempFile.getName() + " is not a valid directory.");
            throw new ExceptionHandler(
                tempFile.getName() + " is not a valid directory.", ExceptionId.ARG_VALID);
          }
          break;
        default:
          if (!isFile && !isDir) {
            log.fatal(tempFile.getName() + " is not a valid file or directory.");
            throw new ExceptionHandler(
                tempFile.getName() + " is not a valid file or directory.", ExceptionId.ARG_VALID);
          }
          break;
      }
    else if (!isFile && !isDir) {
      log.fatal(tempFile.getName() + " is not a valid file or directory.");
      throw new ExceptionHandler(
          tempFile.getName() + " is not a valid file or directory.", ExceptionId.ARG_VALID);
    }

    try {
      if (expandPath) return tempFile.getCanonicalPath();
      else return file;
    } catch (Exception e) {
      log.fatal("Error retrieving the path of the file " + tempFile.getName() + ".");
      throw new ExceptionHandler(
          "Error retrieving the path of the file " + tempFile.getName() + ".",
          ExceptionId.FILE_AFK);
    }
  }

  /**
   * getDefaultFileOut.
   *
   * @param packageName a {@link java.lang.String} object.
   * @param fileExtension a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String getDefaultFileOut(String packageName, String fileExtension) {

    StringBuilder output = new StringBuilder("_" + projectName + "-");

    if (StringUtils.isNotEmpty(projectVersion))
      output.append(StringUtils.trimToNull(projectVersion)).append("_");

    if (StringUtils.isNotEmpty(packageName))
      output.append(StringUtils.trimToNull(packageName)).append("_");

    output.append(java.util.UUID.randomUUID().toString()).append("_").append(fileExtension);

    return Utils.osPathJoin(System.getProperty("user.dir"), output.toString());
  }
  //endregion

  //region ArgMethods

  /**
   * makeArg.
   *
   * @param id a {@link frontEnd.argsIdentifier} object.
   * @param value a {@link java.lang.Object} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(argsIdentifier id, Object value) {
    return makeArg(id.getId(), value.toString());
  }

  /**
   * makeArg.
   *
   * @param id a {@link frontEnd.argsIdentifier} object.
   * @param value a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(argsIdentifier id, String value) {
    return makeArg(id.getId(), value);
  }

  /**
   * makeArg.
   *
   * @param id a {@link frontEnd.argsIdentifier} object.
   * @param value a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(argsIdentifier id, List<String> value) {
    return makeArg(id.getId(), value);
  }

  /**
   * makeArg.
   *
   * @param id a {@link frontEnd.argsIdentifier} object.
   * @param value a {@link rule.engine.EngineType} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(argsIdentifier id, EngineType value) {
    return makeArg(id.getId(), value.getFlag());
  }

  /**
   * makeArg.
   *
   * @param id a {@link frontEnd.argsIdentifier} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(argsIdentifier id) {
    return " " + id.getArg() + " ";
  }

  /**
   * makeArg.
   *
   * @param id a {@link java.lang.String} object.
   * @param value a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(String id, String value) {
    if (StringUtils.isEmpty(value)) return "";
    return " -" + id + " " + value + " ";
  }

  /**
   * makeArg.
   *
   * @param id a {@link java.lang.String} object.
   * @param value a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(String id, List<String> value) {
    if (value.size() >= 1) return makeArg(id, value.get(0)) + String.join(" ", value);
    return "";
  }

  /**
   * makeArg.
   *
   * @param id a {@link frontEnd.argsIdentifier} object.
   * @param value a {@link frontEnd.MessagingSystem.routing.Listing} object.
   * @return a {@link java.lang.String} object.
   */
  public static String makeArg(argsIdentifier id, Listing value) {
    return makeArg(id, value.getFlag());
  }
  //endregion

  //region NotHotMethods
  public static String prep(Object obj) {
    if (obj == null)
      return "\"None\"";

    if (obj instanceof EngineType) {
      return "\"" + obj.toString() + "\"";
    }
    else if (obj instanceof String) {
      return "\"" + obj + "\"";
    }
    else if (obj instanceof List) {
      StringBuilder toReturn = new StringBuilder("[");

      for (Object sub_obj : (List) obj)
        toReturn.append(prep(sub_obj)).append(",");

      if ( ((List<?>) obj).size() > 0)
        toReturn.setLength(toReturn.length() - 1);

      toReturn.append("]");
      return toReturn.toString();
    }
    else
      return obj.toString();
  }

  public static void setDebuggingLevel(int level) {
    switch (level) {
      case 5:
        Configurator.setRootLevel(Level.ALL);
        break;
      case 4:
        Configurator.setRootLevel(Level.TRACE);
        break;
      case 3:
        Configurator.setRootLevel(Level.DEBUG);
        break;
      case 2:
        Configurator.setRootLevel(Level.INFO);
        break;
      case 0:
        Configurator.setRootLevel(Level.FATAL);
        break;
      //case 1
      default:
        Configurator.setRootLevel(Level.WARN);
        break;
    }
  }

  /**
   * getClassNamesFromJarArchive.
   *
   * @param jarPath a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> getClassNamesFromJarArchive(String jarPath) throws ExceptionHandler {
    List<String> classNames = new ArrayList<>();
    try {
      ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
      for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
          String className = entry.getName().replace('/', '.');
          classNames.add(className.substring(0, className.length() - ".class".length()));
        }
      }
      return classNames;
    } catch (IOException e) {
      log.fatal("Error with file " + jarPath);
      throw new ExceptionHandler("Error with file " + jarPath, ExceptionId.FILE_I);
    }
  }

  /**
   * stripEmpty.
   *
   * @param args an array of {@link java.lang.String} objects.
   * @return a {@link java.util.ArrayList} object.
   */
  public static ArrayList<String> stripEmpty(String[] args) {

    return Arrays.stream(args)
        .filter(StringUtils::isNotEmpty)
        .collect(Collectors.toCollection(ArrayList::new));

    //region Old Method
    /*
    ArrayList<String> strippedArgs = new ArrayList<>();
    for (String arg : args)
        if (StringUtils.isNotEmpty(arg))
            strippedArgs.add(arg);
    return strippedArgs;
     */
    //endregion
  }

  /**
   * retrieveFullyQualifiedNameFileSep.
   *
   * @param sourceJavaFile a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> retrieveFullyQualifiedNameFileSep(List<String> sourceJavaFile)
      throws ExceptionHandler {
    List<String> fullPath = new ArrayList<>();
    for (String in : sourceJavaFile)
      fullPath.add(Utils.retrieveFullyQualifiedName(in).replace(".", fileSep));

    return fullPath;
  }

  /**
   * handleErrorMessage.
   *
   * @param e a {@link frontEnd.Interface.outputRouting.ExceptionHandler} object.
   */
  public static void handleErrorMessage(ExceptionHandler e) {
    if (e.getErrorCode().getId().equals(0)) {
      System.out.print(e.getLongDescriptionString());
    } else {
      System.err.print(e.toString());
    }
  }

  /**
   * joinSpecialSootClassPath.
   *
   * @param fileIn a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static String joinSpecialSootClassPath(List<String> fileIn) throws ExceptionHandler {
    return join(":", retrieveClosePath(fileIn, true));
  }

  /**
   * retrieveClosePath.
   *
   * @param fileIn a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> retrieveClosePath(List<String> fileIn, Boolean needstoExist)
      throws ExceptionHandler {
    ArrayList<String> output = new ArrayList<>();

    for (String path : fileIn) {
      String temp = null;

      if ((temp =
              Utils.retrieveFilePath(
                  path, EngineType.JAVAFILES.getInputExtension(), true, false, needstoExist))
          == null)
        temp =
            Utils.retrieveFilePath(
                path, EngineType.CLASSFILES.getInputExtension(), true, false, needstoExist);

      if (StringUtils.isNotBlank(temp)) {
        temp =
            replaceLast(temp, retrieveFullyQualifiedName(path).replace(".", fileSep))
                .replace(".java", "")
                .replace(".class", "");
        if (!output.contains(temp)) output.add(temp);
      }
    }
    return output;
  }

  /**
   * retrieveFullyQualifiedName.
   *
   * @param in a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static String retrieveFullyQualifiedName(String in) throws ExceptionHandler {

    String sourcePackage = trimFilePath(in);
    if (in.toLowerCase().endsWith(".java")) {
      sourcePackage = sourcePackage.replace(".java", "");
      try (BufferedReader br = new BufferedReader(new FileReader(in))) {

        String firstLine = br.readLine();
        Matcher match = null;
        while (StringUtils.isBlank(firstLine)
            || (match = startComment.matcher(firstLine)).find()
            || (match = comment.matcher(firstLine)).find()) firstLine = br.readLine();

        if (firstLine.startsWith("package ") && firstLine.toLowerCase().endsWith(";")) {
          sourcePackage =
              firstLine.substring("package ".length(), firstLine.length() - 1)
                  + "."
                  + sourcePackage;
        }

      } catch (IOException e) {
        log.fatal("Error parsing file: " + in);
        throw new ExceptionHandler("Error parsing file: " + in, ExceptionId.FILE_READ);
      }
    } else if (in.toLowerCase().endsWith(".class")) {
      try {
        ClassReader reader = new ClassReader(new FileInputStream(in));
        sourcePackage = reader.getClassName().replace(fileSep, ".");
      } catch (FileNotFoundException e) {
        log.fatal("File was not found " + in);
        throw new ExceptionHandler("File " + in + " not available.", ExceptionId.FILE_READ);
      } catch (IOException e) {
        log.fatal("Error parsing file: " + in);
        throw new ExceptionHandler("Error parsing file: " + in, ExceptionId.FILE_READ);
      }
    }
    return sourcePackage;
  }

  /**
   * containsAny.
   *
   * @param input a {@link java.lang.String} object.
   * @param stringsToCheck an array of {@link java.lang.String} objects.
   * @return a {@link java.lang.Boolean} object.
   */
  public static Boolean containsAny(String input, String[] stringsToCheck) {
    return containsAny(input, Arrays.asList(stringsToCheck));
  }

  /**
   * containsAny.
   *
   * @param input a {@link java.lang.String} object.
   * @param stringsToCheck a {@link java.util.List} object.
   * @return a {@link java.lang.Boolean} object.
   */
  public static Boolean containsAny(String input, List<String> stringsToCheck) {
    return stringsToCheck.stream().anyMatch(input::contains);
  }

  /**
   * listf.EntryPointTest_CLASS
   *
   * @param path a {@link java.lang.String} object.
   * @param fileCheck a {@link java.util.function.Predicate} object.
   * @param functor a {@link java.util.function.Function} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> retrieveFilesPredicate(
      String path,
      Predicate<String> fileCheck,
      Function<File, String> functor,
      Boolean needsToExist)
      throws ExceptionHandler {

    List<String> output = new ArrayList<>();
    for (File file :
        Objects.requireNonNull(
            new File(retrieveFilePath(path, null, true, false, needsToExist)).listFiles())) {
      if (file.isFile() && fileCheck.test(file.getName())) {
        if (functor == null) output.add(file.getAbsolutePath());
        else output.add(functor.apply(file));
      } else if (file.isDirectory())
        output.addAll(
            retrieveFilesPredicate(file.getAbsolutePath(), fileCheck, functor, needsToExist));
    }

    return output;
  }

  /**
   * retrieveJavaFileImports.
   *
   * @param paths a {@link java.util.List} object.
   * @return a {@link java.util.Set} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static Set<String> retrieveJavaFileImports(List<String> paths) throws ExceptionHandler {
    Set<String> results = new HashSet<>();
    for (String path : paths) results.addAll(retrieveJavaFileImports(path));
    return results;
  }

  /**
   * retrieveJavaFileImports.
   *
   * @param path a {@link java.lang.String} object.
   * @return a {@link java.util.Set} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static Set<String> retrieveJavaFileImports(String path) throws ExceptionHandler {
    Set<String> results = new HashSet<>();
    String javaFile =
        retrieveFilePath(path, EngineType.JAVAFILES.getInputExtension(), false, false, true);

    try (BufferedReader reader = new BufferedReader(new FileReader(javaFile))) {
      String curLine;

      leave:
      while ((curLine = reader.readLine()) != null && !curLine.isEmpty()) {
        if (!curLine.startsWith("package")
            && !curLine.startsWith("import")
            && StringUtils.isNotEmpty(curLine)) break leave;
        else if (curLine.startsWith("import"))
          results.add(curLine.replace("import ", "").replace(";", ""));
      }
    } catch (FileNotFoundException e) {
      log.fatal("File: " + javaFile + " is not found.");
      throw new ExceptionHandler(ExceptionId.FILE_READ, "File: " + javaFile + " is not found.");
    } catch (IOException e) {
      log.fatal("Issue reading the file: " + javaFile);
      throw new ExceptionHandler(ExceptionId.FILE_READ, "Issue reading the file: " + javaFile);
    }

    return results;
  }

  /**
   * setSunBootPath.
   *
   * @param basePath a {@link java.lang.String} object.
   * @param rt a {@link java.lang.String} object.
   */
  public static void setSunBootPath(String basePath, String rt) {
    System.setProperty("sun.boot.class.path", rt);
    System.setProperty("java.ext.dirs", osSurround(basePath, "lib"));
  }

  /**
   * getClassNamesFromApkArchive.
   *
   * @param apkfile a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> getClassNamesFromApkArchive(String apkfile) throws ExceptionHandler {
    List<String> classNames = new ArrayList<>();

    File zipFile = new File(apkfile);

    try {
      ZipDexContainer zipContainer =
          (ZipDexContainer) DexFileFactory.loadDexContainer(zipFile, Opcodes.forApi(23));

      for (String dexEntryName : zipContainer.getDexEntryNames()) {
        DexFile dexFile =
            DexFileFactory.loadDexEntry(zipFile, dexEntryName, true, Opcodes.forApi(23))
                .getDexFile();

        for (ClassDef classDef : dexFile.getClasses()) {
          String className = classDef.getType().replace('/', '.');
          if (!className.toLowerCase().startsWith("landroid.")) {
            classNames.add(className.substring(1, className.length() - 1));
          }
        }
      }
      return classNames;
    } catch (IOException e) {
      log.fatal("Error with dex file classes.dex");
      throw new ExceptionHandler("Error with dex file classes.dex", ExceptionId.FILE_I);
    }
  }

  /**
   * buildSootClassPath.
   *
   * @param paths a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String buildSootClassPath(String... paths) {
    return buildSootClassPath(Arrays.asList(paths));
  }

  /**
   * buildSootClassPath.
   *
   * @param paths a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   */
  public static String buildSootClassPath(List<String> paths) {

    StringBuilder classPath = new StringBuilder();

    for (String path : paths) {

      if (path.toLowerCase().endsWith(".jar")) {
        classPath.append(path);
        classPath.append(":");
      } else {
        File dir = new File(path);

        if (dir.isDirectory()) {
          File[] files = dir.listFiles();

          if (files == null) {
            continue;
          }

          for (File file : files) {
            if (file.getName().toLowerCase().endsWith(".jar")) {
              classPath.append(file.getAbsolutePath());
              classPath.append(":");
            }
          }
        }
      }
    }

    return classPath.toString();
  }

  /**
   * getJarsInDirectories.
   *
   * @param dirs a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   */
  public static List<String> getJarsInDirectories(List<String> dirs) {
    ArrayList<String> list = new ArrayList<>();
    for (String dir : dirs) list.addAll(getJarsInDirectory(dir));
    return list;
  }

  /**
   * getJarsInDirectory.
   *
   * @param path a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   */
  public static List<String> getJarsInDirectory(String path) {

    if (null == path || path.trim().equals("")) return new ArrayList<>();

    List<String> jarFiles = new ArrayList<>();
    File dir = new File(path);

    if (dir.isDirectory()) {
      File[] files = dir.listFiles();

      if (files == null) {
        return jarFiles;
      }

      for (File file : files) {
        if (file.getName().toLowerCase().endsWith(".jar")) {
          jarFiles.add(file.getAbsolutePath());
        }
      }
    } else if (dir.getName().toLowerCase().endsWith(".jar")) {
      //TODO - Verify and see if this is needed
      //jarFiles.add(dir.getAbsolutePath());
    }

    return jarFiles;
  }

  /**
   * getClassHierarchyAnalysis.
   *
   * @param classNames a {@link java.util.List} object.
   * @return a {@link java.util.Map} object.
   */
  public static Map<String, List<SootClass>> getClassHierarchyAnalysis(List<String> classNames) {

    Map<String, List<SootClass>> classHierarchyMap = new HashMap<>();

    for (String className : classNames) {

      SootClass sClass = Scene.v().getSootClass(className);
      Chain<SootClass> parents = sClass.getInterfaces();

      if (sClass.hasSuperclass()) {
        SootClass superClass = sClass.getSuperclass();

        List<SootClass> childList =
            classHierarchyMap.computeIfAbsent(superClass.getName(), k -> new ArrayList<>());

        if (childList.isEmpty()) {
          childList.add(superClass);
        }
        childList.add(sClass);
      }

      for (SootClass parent : parents) {
        List<SootClass> childList =
            classHierarchyMap.computeIfAbsent(parent.getName(), k -> new ArrayList<>());

        if (childList.isEmpty()) {
          childList.add(parent);
        }
        childList.add(sClass);
      }
    }

    return classHierarchyMap;
  }

  /**
   * getXmlFiles.
   *
   * @param projectJarPath a {@link java.lang.String} object.
   * @param excludes a {@link java.util.List} object.
   * @return a {@link java.util.Map} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static Map<String, String> getXmlFiles(String projectJarPath, List<String> excludes)
      throws ExceptionHandler {
    Map<String, String> fileStrs = new HashMap<>();

    if (new File(projectJarPath).isDirectory()) {
      return fileStrs;
    }

    List<String> fileNames = getXmlFileNamesFromJarArchive(projectJarPath, excludes);

    for (String fileName : fileNames) {
      InputStream stream = readFileFromZip(projectJarPath, fileName);
      fileStrs.put(fileName, convertStreamToString(stream));
    }

    return fileStrs;
  }

  private static List<String> getXmlFileNamesFromJarArchive(String jarPath, List<String> excludes)
      throws ExceptionHandler {
    List<String> classNames = new ArrayList<>();
    try {
      ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));

      for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
        for (String exclude : excludes) {
          if (!entry.isDirectory()
              && entry.getName().endsWith(".xml")
              && !entry.getName().endsWith(exclude)) {
            String className = entry.getName();
            classNames.add(className);
          }
        }
      }
    } catch (FileNotFoundException e) {
      log.fatal("File " + jarPath + " is not found.");
      throw new ExceptionHandler("File " + jarPath + " is not found.", ExceptionId.FILE_AFK);
    } catch (IOException e) {
      log.fatal("Error Reading " + jarPath + ".");
      throw new ExceptionHandler("Error Reading " + jarPath + ".", ExceptionId.FILE_I);
    }
    return classNames;
  }

  private static InputStream readFileFromZip(String jarPath, String file) throws ExceptionHandler {
    try {
      ZipFile zipFile = new ZipFile(jarPath);
      ZipEntry entry = zipFile.getEntry(file);
      return zipFile.getInputStream(entry);
    } catch (IOException e) {
      log.fatal("Error Reading " + jarPath + ".");
      throw new ExceptionHandler("Error Reading " + jarPath + ".", ExceptionId.FILE_I);
    }
  }

  private static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  /**
   * findInfluencingParamters.
   *
   * @param analysisResult a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   */
  public static List<Integer> findInfluencingParamters(List<UnitContainer> analysisResult) {
    List<Integer> influencingParam = new ArrayList<>();

    for (int index = analysisResult.size() - 1; index >= 0; index--) {
      UnitContainer unit = analysisResult.get(index);

      for (ValueBox useBox : unit.getUnit().getUseBoxes()) {
        String useboxStr = useBox.getValue().toString();
        if (useboxStr.contains("@parameter")) {
          Integer param =
              Integer.valueOf(useboxStr.substring("@parameter".length(), useboxStr.indexOf(':')));
          influencingParam.add(param);
        }
      }
    }

    return influencingParam;
  }

  /**
   * isSpecialInvokeOn.
   *
   * @param currInstruction a {@link soot.Unit} object.
   * @param usebox a {@link java.lang.String} object.
   * @return a boolean.
   */
  public static boolean isSpecialInvokeOn(Unit currInstruction, String usebox) {
    return currInstruction.toString().contains("specialinvoke")
        && currInstruction.toString().contains(usebox + ".<");
  }

  /**
   * listf.
   *
   * @param directoryName a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   */
  public static List<File> listf(String directoryName) {
    File directory = new File(directoryName);

    File[] fList = directory.listFiles();
    List<File> resultList = new ArrayList<>(Arrays.asList(fList));
    for (File file : fList) {
      if (file.isFile()) {
      } else if (file.isDirectory()) {
        resultList.addAll(listf(file.getAbsolutePath()));
      }
    }

    return resultList;
  }

  /**
   * getClassNamesFromSnippet.
   *
   * @param sourcePaths a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   */
  public static List<String> getClassNamesFromSnippet(List<String> sourcePaths) {

    List<String> classNames = new ArrayList<>();

    for (String sourcePath : sourcePaths) {

      List<File> files = listf(sourcePath);

      if (files == null) {
        return classNames;
      }

      for (File file : files) {
        String name = file.getAbsolutePath();
        if (name.toLowerCase().endsWith(".java")) {
          String className = name.substring(sourcePath.length() + 1, name.length() - 5);
          classNames.add(className.replaceAll("/", "."));
        }
      }
    }

    return classNames;
  }

  /**
   * retrieveFullyQualifiedName.
   *
   * @param sourceJavaFile a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> retrieveFullyQualifiedName(String... sourceJavaFile)
      throws ExceptionHandler {
    return retrieveFullyQualifiedName(Arrays.asList(sourceJavaFile));
  }

  /**
   * retrieveFullyQualifiedName.
   *
   * @param sourceJavaFile a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> retrieveFullyQualifiedName(List<String> sourceJavaFile)
      throws ExceptionHandler {
    List<String> fullPath = new ArrayList<>();
    for (String in : sourceJavaFile) fullPath.add(Utils.retrieveFullyQualifiedName(in));

    return fullPath;
  }

  /**
   * replaceLast.
   *
   * @param text a {@link java.lang.String} object.
   * @param regexish a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String replaceLast(String text, String regexish) {
    return replaceLast(text, regexish, "");
  }

  /**
   * replaceLast.
   *
   * @param text a {@link java.lang.String} object.
   * @param regexish a {@link java.lang.String} object.
   * @param replacement a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String replaceLast(String text, String regexish, String replacement) {
    int lastIdx = text.lastIndexOf(regexish);
    if (lastIdx != -1)
      return text.substring(0, lastIdx) + replacement + text.substring(lastIdx + regexish.length());
    else return text;
  }

  /**
   * retrievePackageFromJavaFiles.
   *
   * @param sourceFiles a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrievePackageFromJavaFiles(List<String> sourceFiles) {
    String commonPath = null;

    for (String in : sourceFiles) {
      for (String file : in.split(".")) {

        String tempPath = in.replace(retrievePackageFromJavaFiles(file), "");

        if (commonPath == null) commonPath = tempPath;
        else if (!commonPath.equals(tempPath)) {
          String removable = commonPath.replace(in, "");
          commonPath = commonPath.replace(removable, "");
        }
      }
    }

    return commonPath;
  }

  /**
   * retrievePackageFromJavaFiles.
   *
   * @param file a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrievePackageFromJavaFiles(String file) {
    try {
      return retrieveFullyQualifiedName(file).replaceAll("\\.", fileSep) + ".java";
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * retrieveBaseDirectory.
   *
   * @param file a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static String retrieveBaseDirectory(List<String> file) throws ExceptionHandler {
    String baseDir = "";
    String[] baseSplit = new String[0];
    for (String path : file) {
      String fullPath = retrieveFileParentPath(path);

      //Base Case
      if (baseDir.equals("")) {
        baseDir = fullPath;
      } else if (!fullPath.equals(baseDir)) {
        String[] tempSplit = fullPath.split(fileSep);
        int smallerSize = Math.min(baseSplit.length, tempSplit.length);
        ArrayList<String> common = new ArrayList<>();

        loopCheck:
        for (int itr = 0; itr < smallerSize; itr++) {

          //BaseDir
          if (itr == baseSplit.length || itr == tempSplit.length) break loopCheck;

          if (baseSplit[itr].equals(tempSplit[itr])) common.add(baseSplit[itr]);
          else break loopCheck;
        }

        baseDir = join(fileSep, common);
      }

      baseSplit =
          Arrays.stream(baseDir.split(fileSep))
              .filter(StringUtils::isNotBlank)
              .toArray(String[]::new);
    }

    if (baseDir.equals("")) {
      log.fatal("Different file paths sent in.");
      throw new ExceptionHandler("Different file paths sent in.", ExceptionId.ARG_VALID);
    }

    return baseDir;
  }

  /**
   * retrieveTrimmedSourcePaths.
   *
   * @param files a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static List<String> retrieveTrimmedSourcePaths(List<String> files)
      throws ExceptionHandler {
    List<String> filePaths = new ArrayList<>();
    for (String relativeFile : files) {
      String relativeFilePath = "";

      File file = new File(relativeFile);

      try {
        relativeFilePath = file.getCanonicalPath().replace(file.getName(), "");
      } catch (IOException e) {
        log.fatal("Issue retrieving the file path from file: " + file);
        throw new ExceptionHandler(
            ExceptionId.FILE_READ, "Issue retrieving the file path from file: " + file);
      }

      if (!filePaths.contains(relativeFilePath)) filePaths.add(relativeFilePath);
    }
    return filePaths;
  }

  /**
   * retrieveBaseSourcePath.
   *
   * @param sourcePaths a {@link java.util.List} object.
   * @param dependencyPath a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveBaseSourcePath(List<String> sourcePaths, String dependencyPath) {
    String tempDependencyPath = sourcePaths.get(0);
    for (String in : sourcePaths)
      if (!in.equals(tempDependencyPath)) {
        tempDependencyPath = System.getProperty("user.dir");
        break;
      }
    return Utils.osPathJoin(tempDependencyPath, dependencyPath);
  }

  /**
   * retrieveFileParentPath.
   *
   * @param filePath a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveFileParentPath(String filePath) {
    String fullPaths = retrieveFullFilePath(filePath);
    return fullPaths.substring(0, fullPaths.lastIndexOf(fileSep));
  }

  /**
   * retrieveFullFilePath.
   *
   * @param filename a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveFullFilePath(String filename) {
    File file = new File(filename);
    if (file.exists())
      try {
        return file.getCanonicalPath();
      } catch (IOException e) {
        return filename;
      }
    else return filename;
  }

  /**
   * This method trims the file path and package from the absolute path.
   *
   * <p>EX: src/main/java/com/test/me/main.java {@literal -}{@literal >} main.java
   *
   * @param fullFilePath {@link java.lang.String} - The full file path
   * @return {@link java.lang.String} - The file name with the extension attached
   */
  public static String trimFilePath(String fullFilePath) {
    String[] folderSplit = fullFilePath.split(Pattern.quote(fileSep));
    return folderSplit[folderSplit.length - 1];
  }

  /**
   * osSurround.
   *
   * @param elements a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String osSurround(String... elements) {
    return surround(Utils.fileSep, elements);
  }

  /**
   * osPathJoin.
   *
   * @param elements a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String osPathJoin(String... elements) {
    return join(Utils.fileSep, elements);
  }

  /**
   * surround.
   *
   * @param delimiter a {@link java.lang.String} object.
   * @param elements a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String surround(String delimiter, String... elements) {
    return surround(delimiter, Arrays.asList(elements));
  }

  /**
   * surround.
   *
   * @param delimiter a {@link java.lang.String} object.
   * @param elements a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   */
  public static String surround(String delimiter, List<String> elements) {
    String current = StringUtils.trimToNull(join(delimiter, elements));

    if (!current.startsWith(delimiter)) current = delimiter + current;

    if (!current.endsWith(delimiter)) current += delimiter;

    return current;
  }

  /**
   * join.
   *
   * @param delimiter a {@link java.lang.String} object.
   * @param elements a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String join(String delimiter, String... elements) {
    return join(delimiter, Arrays.asList(elements));
  }

  /**
   * join.
   *
   * @param delimiter a {@link java.lang.String} object.
   * @param elements a {@link java.util.List} object.
   * @return a {@link java.lang.String} object.
   */
  public static String join(String delimiter, List<String> elements) {
    if (elements == null) return null;

    StringBuilder tempString = new StringBuilder();
    for (String in : elements) {
      if (null != (in = StringUtils.trimToNull(in))) {
        tempString.append(in);
        if (!in.equals(elements.get(elements.size() - 1))) tempString.append(delimiter);
      }
    }

    return tempString.toString();
  }

  /**
   * The overridden Soot class path builder, used for Java JDK 7 or 8 paths.
   *
   * @param javaPath {@link java.lang.String} - The path to either the JDK 7 or 8.
   * @return {@link java.lang.String} - The built up path for the Soot String. ex.
   *     /jdk...:/jdk.../jre/lib/rt.jar:/jdk.../jre/lib/jce.jar
   */
  public static String getBaseSoot(String javaPath) {
    String rt = Utils.osPathJoin(javaPath, "jre", "lib", "rt.jar");
    String jce = Utils.osPathJoin(javaPath, "jre", "lib", "jce.jar");

    setSunBootPath(javaPath, rt);

    return Utils.join(":", javaPath, rt, jce);
  }

  /**
   * loadSootClasses.
   *
   * @param classes a {@link java.util.List} object.
   */
  public static void loadSootClasses(List<String> classes) {
    Options.v().set_keep_line_number(true);
    Options.v().set_allow_phantom_refs(true);

    if (classes != null) for (String clazz : classes) Options.v().classes().add(clazz);

    Scene.v().loadBasicClasses();
  }

  /**
   * retrieveClassNameFromSootString.
   *
   * @param sootString a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveClassNameFromSootString(String sootString) {
    Matcher secondMatches = sootClassPatternTwo.matcher(sootString);
    if (secondMatches.find()) return secondMatches.group(1);

    Matcher matches = sootClassPattern.matcher(sootString);
    if (matches.find()) return matches.group(1);

    return "UNKNOWN";
  }

  /**
   * retrieveFoundPatternFromSootString.
   *
   * @param sootString a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveFoundPatternFromSootString(String sootString) {
    Matcher matches = sootFoundPattern.matcher(sootString);

    if (matches.find()) return matches.group(1).replaceAll("\"", "");
    return "UNKNOWN";
  }

  /**
   * retrieveLineNumFromSootString.
   *
   * @param sootString a {@link java.lang.String} object.
   * @return a {@link java.lang.Integer} object.
   */
  public static Integer retrieveLineNumFromSootString(String sootString) {
    Matcher matches = sootLineNumPattern.matcher(sootString);

    if (matches.find()) return Integer.parseInt(matches.group(1));
    return -1;
  }

  /**
   * retrieveMethodFromSootString.
   *
   * @param sootString a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveMethodFromSootString(String sootString) {
    Matcher matches = sootMthdPattern.matcher(sootString);

    if (matches.find()) return matches.group(1);

    Matcher secondMatches = sootMthdPatternTwo.matcher(sootString);
    if (secondMatches.find()) return secondMatches.group(1);

    return "UNKNOWN";
  }

  /**
   * getPlatform.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getPlatform() {

    return System.getProperty("os.name") + "_" + System.getProperty("os.version");
  }

  /**
   * getJVMInfo.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getJVMInfo() {
    return System.getProperty("java.runtime.version");
  }

  /**
   * retrieveFoundMatchFromSootString.
   *
   * @param sootString a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String retrieveFoundMatchFromSootString(String sootString) {
    Matcher matches = sootFoundMatchPattern.matcher(sootString);

    if (matches.find()) return StringUtils.trimToNull(matches.group(1));

    return "UNKNOWN";
  }

  /**
   * getCurrentTimeStamp.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getCurrentTimeStamp() {
    return dateFormat.format(new Date());
  }

  /**
   * getRelativeFilePath.
   *
   * @param filePath a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static String getRelativeFilePath(String filePath) throws ExceptionHandler {
    try {
      String fullPath = new File(filePath).getCanonicalPath();

      return Utils.osPathJoin("~", fullPath.replaceAll(Utils.userPath + fileSep, ""));
    } catch (IOException e) {
      log.fatal("Error reading file: " + filePath);
      throw new ExceptionHandler("Error reading file: " + filePath, ExceptionId.FILE_I);
    }
  }

  /**
   * createAssignInvokeUnitContainer.
   *
   * @param currInstruction a {@link soot.Unit} object.
   * @param caller a {@link java.lang.String} object.
   * @param depth a int.
   * @return a {@link analyzer.backward.UnitContainer} object.
   */
  public static UnitContainer createAssignInvokeUnitContainer(
      Unit currInstruction, String caller, int depth) {

    for (String dontVisit : ASSIGN_DONT_VISIT) {
      if (currInstruction.toString().contains(dontVisit)) {
        UnitContainer unitContainer = new UnitContainer();
        unitContainer.setUnit(currInstruction);
        unitContainer.setMethod(caller);
        return unitContainer;
      }
    }

    AssignInvokeUnitContainer unitContainer = new AssignInvokeUnitContainer();

    SootMethod method = ((JAssignStmt) currInstruction).getInvokeExpr().getMethod();
    if (method != null && method.isConcrete()) {

      Scene.v().forceResolve(method.getDeclaringClass().getName(), BODIES);

      List<UnitContainer> intraAnalysis = null;

      DEPTH_COUNT[depth - 1]++;

      if (depth == 1) {

        NUM_HEURISTIC++;

        HeuristicBasedInstructions returnInfluencingInstructions =
            new HeuristicBasedInstructions(method, "return");

        intraAnalysis = returnInfluencingInstructions.getAnalysisResult().getAnalysis();
      } else {

        NUM_ORTHOGONAL++;

        OrthogonalInfluenceInstructions other =
            new OrthogonalInfluenceInstructions(method, "return", depth - 1);
        intraAnalysis = other.getOrthogonalSlicingResult().getAnalysisResult();
      }

      // Get args
      List<Integer> args = Utils.findInfluencingParamters(intraAnalysis);

      // Get fields
      Set<String> usedFields = new HashSet<>();
      for (UnitContainer iUnit : intraAnalysis) {
        for (ValueBox usebox : iUnit.getUnit().getUseBoxes()) {
          if (usebox.getValue().toString().startsWith("r0.")
              || usebox.getValue().toString().startsWith("this.")) {
            usedFields.add(usebox.getValue().toString());
          }
        }
      }

      unitContainer.setArgs(args);
      unitContainer.setAnalysisResult(intraAnalysis);
      unitContainer.setMethod(caller);
      unitContainer.setProperties(usedFields);
    }

    return unitContainer;
  }

  /**
   * isArgOfAssignInvoke.
   *
   * @param useBox a {@link soot.ValueBox} object.
   * @param unit a {@link soot.Unit} object.
   * @return a int.
   */
  public static int isArgOfAssignInvoke(ValueBox useBox, Unit unit) {

    if (unit instanceof JAssignStmt && unit.toString().contains("invoke ")) {

      InvokeExpr invokeExpr = ((JAssignStmt) unit).getInvokeExpr();
      List<Value> args = invokeExpr.getArgs();
      for (int index = 0; index < args.size(); index++) {
        if (args.get(index).equivTo(useBox.getValue())) {
          return index;
        }
      }
    }

    return -1;
  }

  /**
   * isArgOfByteArrayCreation.
   *
   * @param useBox a {@link soot.ValueBox} object.
   * @param unit a {@link soot.Unit} object.
   * @return a boolean.
   */
  public static boolean isArgOfByteArrayCreation(ValueBox useBox, Unit unit) {
    if (unit.toString().contains(" newarray ")) {
      for (ValueBox valueBox : unit.getUseBoxes()) {
        if (valueBox.getValue().equivTo(useBox.getValue())) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * isArgumentOfInvoke.
   *
   * @param analysis a {@link analyzer.backward.Analysis} object.
   * @param index a int.
   * @param outSet a {@link java.util.List} object.
   * @return a {@link analyzer.backward.UnitContainer} object.
   */
  public static UnitContainer isArgumentOfInvoke(
      Analysis analysis, int index, List<UnitContainer> outSet) {
    NUM_CONSTS_TO_CHECK++;

    UnitContainer baseUnit = analysis.getAnalysisResult().get(index);

    if (baseUnit.getUnit() instanceof JInvokeStmt) {

      InvokeExpr invokeExpr = ((JInvokeStmt) baseUnit.getUnit()).getInvokeExpr();

      List<Value> args = invokeExpr.getArgs();

      for (Value arg : args) {
        if (arg instanceof Constant) {
          return baseUnit;
        }
      }
    }

    outSet.add(analysis.getAnalysisResult().get(index));

    for (int i = index; i >= 0; i--) {

      UnitContainer curUnit = analysis.getAnalysisResult().get(i);

      List<UnitContainer> inset = new ArrayList<>(outSet);

      for (UnitContainer insetIns : inset) {
        boolean outSetContainsCurUnit = !outSet.toString().contains(curUnit.toString());
        if (insetIns instanceof PropertyFakeUnitContainer) {
          String property = ((PropertyFakeUnitContainer) insetIns).getOriginalProperty();

          if (curUnit.getUnit() instanceof JInvokeStmt) {
            if (curUnit.getUnit().toString().contains(property + ".<")) {
              if (outSetContainsCurUnit) {
                outSet.add(curUnit);
              }
            } else {

              InvokeExpr invokeExpr = ((JInvokeStmt) curUnit.getUnit()).getInvokeExpr();

              List<Value> args = invokeExpr.getArgs();

              for (Value arg : args) {
                if (arg.toString().contains(property)) {
                  return curUnit;
                }
              }
            }

          } else {
            for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
              if (useBox.getValue().toString().contains(property)) {
                if (outSetContainsCurUnit) {
                  outSet.add(curUnit);
                }
              }
            }
          }
        } else if (insetIns instanceof ParamFakeUnitContainer) {

          int param = ((ParamFakeUnitContainer) insetIns).getParam();
          String method = ((ParamFakeUnitContainer) insetIns).getCallee();

          for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
            String useboxStr = useBox.getValue().toString();
            if (useboxStr.contains("@parameter")) {
              Integer parameter =
                  Integer.valueOf(
                      useboxStr.substring("@parameter".length(), useboxStr.indexOf(':')));
              if (parameter.equals(param) && curUnit.getMethod().equals(method)) {
                if (outSetContainsCurUnit) {
                  outSet.add(curUnit);
                }
              }
            }
          }
        } else if (insetIns.getUnit() instanceof JAssignStmt) {
          if (curUnit.getUnit() instanceof JInvokeStmt) {

            for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {

              if (((JInvokeStmt) curUnit.getUnit()).containsInvokeExpr()) {

                InvokeExpr invokeExpr = ((JInvokeStmt) curUnit.getUnit()).getInvokeExpr();
                List<Value> args = invokeExpr.getArgs();

                for (Value arg : args) {
                  if (arg.equivTo(defBox.getValue())
                      || isArrayUseBox(curUnit, insetIns, defBox, arg)) {
                    return curUnit;
                  }
                }
              } else if (curUnit.getUnit().toString().contains(defBox + ".<")) {
                if (outSetContainsCurUnit) {
                  outSet.add(curUnit);
                }
              }
            }

          } else {
            for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {

              if ((defBox.getValue().toString().equals("r0")
                      && insetIns.getUnit().toString().startsWith("r0."))
                  || (defBox.getValue().toString().equals("this")
                      && insetIns.getUnit().toString().startsWith("this."))) {
                continue;
              }

              for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {

                if (defBox.getValue().equivTo(useBox.getValue())
                    || isArrayUseBox(curUnit, insetIns, defBox, useBox.getValue())) {
                  if (outSetContainsCurUnit) {
                    outSet.add(curUnit);
                  }
                }
              }
            }
          }

        } else {

          if (curUnit.getUnit() instanceof JInvokeStmt) {
            for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {
              if (curUnit.getUnit().toString().contains(defBox + ".<")) {
                if (outSetContainsCurUnit) {
                  outSet.add(curUnit);
                }
              } else {

                InvokeExpr invokeExpr = ((JInvokeStmt) curUnit.getUnit()).getInvokeExpr();

                List<Value> args = invokeExpr.getArgs();

                for (Value arg : args) {
                  if (arg.equivTo(defBox.getValue())
                      || isArrayUseBox(curUnit, insetIns, defBox, arg)) {
                    return curUnit;
                  }
                }
              }
            }

          } else {
            for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {

              if ((defBox.getValue().toString().equals("r0")
                      && insetIns.getUnit().toString().startsWith("r0."))
                  || (defBox.getValue().toString().equals("this")
                      && insetIns.getUnit().toString().startsWith("this."))) {
                continue;
              }

              for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {

                if (defBox.getValue().equivTo(useBox.getValue())
                    || isArrayUseBox(curUnit, insetIns, defBox, useBox.getValue())) {
                  if (outSetContainsCurUnit) {
                    outSet.add(curUnit);
                  }
                }
              }
            }
          }
        }
      }
    }

    return null;
  }

  /**
   * createAnalysisOutput.
   *
   * @param xmlFileStr a {@link java.util.Map} object.
   * @param sourcePaths a {@link java.util.List} object.
   * @param predictableSourcMap a {@link java.util.Map} object.
   * @param rule a {@link java.lang.String} object.
   * @param output a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure}
   *     object.
   * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
   */
  public static void createAnalysisOutput(
      Map<String, String> xmlFileStr,
      List<String> sourcePaths,
      Map<UnitContainer, List<String>> predictableSourcMap,
      String rule,
      OutputStructure output)
      throws ExceptionHandler {
    Integer ruleNumber = Integer.parseInt(rule);

    for (UnitContainer unit : predictableSourcMap.keySet())
      if (predictableSourcMap.get(unit).size() <= 0)
        output.addIssue(new AnalysisIssue(unit, ruleNumber, "", sourcePaths));
      else
        for (String sootString : predictableSourcMap.get(unit))
          output.addIssue(
              new AnalysisIssue(
                  unit,
                  ruleNumber,
                  "Found: \"" + sootString.replaceAll("\"", "") + "\"",
                  sourcePaths));
  }

  /**
   * calculateAverage.
   *
   * @return a double.
   */
  public static double calculateAverage() {
    Integer sum = 0;
    if (!Utils.SLICE_LENGTH.isEmpty()) {
      for (Integer mark : Utils.SLICE_LENGTH) {
        sum += mark;
      }
      return sum.doubleValue() / Utils.SLICE_LENGTH.size();
    }
    return sum;
  }

  /**
   * createDepthCountList.
   *
   * @return a {@link java.util.ArrayList} object.
   */
  public static ArrayList<String> createDepthCountList() {
    ArrayList<String> output = new ArrayList<>();

    for (int i = 0; i < Utils.DEPTH_COUNT.length; i++) {
      output.add(String.format("Depth: %d, Count %d", i + 1, Utils.DEPTH_COUNT[i]));
    }

    return output;
  }

  /**
   * isArgOfInvoke.
   *
   * @param useBox a {@link soot.ValueBox} object.
   * @param unit a {@link soot.Unit} object.
   * @return a int.
   */
  public static int isArgOfInvoke(ValueBox useBox, Unit unit) {

    if (unit instanceof JInvokeStmt) {

      InvokeExpr invokeExpr = ((JInvokeStmt) unit).getInvokeExpr();
      List<Value> args = invokeExpr.getArgs();
      for (int index = 0; index < args.size(); index++) {
        if (args.get(index).equivTo(useBox.getValue())) {
          return index;
        }
      }
    }

    return -1;
  }

  private static boolean isArrayUseBox(
      UnitContainer curUnit, UnitContainer insetIns, ValueBox defBox, Value useBox) {
    return (defBox.getValue().toString().contains(useBox.toString())
        && curUnit.getMethod().equals(insetIns.getMethod())
        && useBox.getType() instanceof ArrayType);
  }

  /**
   * createInvokeUnitContainer.
   *
   * @param currInstruction a {@link soot.Unit} object.
   * @param caller a {@link java.lang.String} object.
   * @param usedFields a {@link java.util.List} object.
   * @param depth a int.
   * @return a {@link analyzer.backward.UnitContainer} object.
   */
  public static UnitContainer createInvokeUnitContainer(
      Unit currInstruction, String caller, List<String> usedFields, int depth) {

    for (String dontVisit : INVOKE_DONT_VISIT) {
      if (currInstruction.toString().contains(dontVisit)) {
        UnitContainer unitContainer = new UnitContainer();
        unitContainer.setUnit(currInstruction);
        unitContainer.setMethod(caller);
        return unitContainer;
      }
    }

    InvokeUnitContainer unitContainer = new InvokeUnitContainer();
    SootMethod method = ((JInvokeStmt) currInstruction).getInvokeExpr().getMethod();

    if (method.isConcrete()) {

      Scene.v().forceResolve(method.getDeclaringClass().getName(), BODIES);

      if (depth == 1) {

        for (String field : usedFields) {

          HeuristicBasedInstructions influencingInstructions =
              new HeuristicBasedInstructions(method, field);
          HeuristicBasedAnalysisResult propAnalysis = influencingInstructions.getAnalysisResult();

          if (propAnalysis.getAnalysis() != null) {
            // Get args
            List<Integer> args = Utils.findInfluencingParamters(propAnalysis.getAnalysis());
            unitContainer.setArgs(args);
            unitContainer.setMethod(caller);
            unitContainer.getDefinedFields().add(field);
            unitContainer.setAnalysisResult(propAnalysis.getAnalysis());
          }
        }
      } else {

        for (String field : usedFields) {

          OrthogonalInfluenceInstructions other =
              new OrthogonalInfluenceInstructions(method, field, depth - 1);
          OrthogonalSlicingResult orthoAnalysis = other.getOrthogonalSlicingResult();

          if (orthoAnalysis.getAnalysisResult() != null) {
            // Get args
            List<Integer> args = Utils.findInfluencingParamters(orthoAnalysis.getAnalysisResult());
            unitContainer.setArgs(args);
            unitContainer.setMethod(caller);
            unitContainer.getDefinedFields().add(field);
            unitContainer.setAnalysisResult(orthoAnalysis.getAnalysisResult());
          }
        }
      }
    }

    return unitContainer;
  }
  //endregion
}
