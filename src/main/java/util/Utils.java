package util;

import analyzer.backward.*;
import frontEnd.Interface.Version;
import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import frontEnd.MessagingSystem.AnalysisIssue;
import frontEnd.MessagingSystem.routing.Listing;
import frontEnd.MessagingSystem.routing.inputStructures.ScarfXMLId;
import frontEnd.MessagingSystem.routing.outputStructures.OutputStructure;
import frontEnd.argsIdentifier;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static soot.SootClass.BODIES;

/**
 * <p>Utils class.</p>
 *
 * @author CryptoguardTeam
 * @version 03.07.01
 * @since 01.00.00
 */
@Log4j2
public class Utils {

    /**
     * Constant <code>SLICE_LENGTH</code>
     */
    public static final ArrayList<Integer> SLICE_LENGTH = new ArrayList<>();
    /**
     * Constant <code>lineSep="System.getProperty(file.separator)"</code>
     */
    public final static String fileSep = System.getProperty("file.separator");
    /**
     * Constant <code>lineSep="System.getProperty(line.separator)"</code>
     */
    public final static String lineSep = System.getProperty("line.separator");
    /**
     * Constant <code>projectVersion="V03.10.02"</code>
     */
    public final static String projectVersion = "V03.10.02";
    /**
     * Constant <code>projectName="CryptoGuard"</code>
     */
    public final static String projectName = "CryptoGuard";
    /**
     * Constant <code>userPath="System.getProperty(user.home)"</code>
     */
    public final static String userPath = System.getProperty("user.home");
    //region Static Variables
    private static final List<String> ASSIGN_DONT_VISIT = new ArrayList<>();
    private static final List<String> INVOKE_DONT_VISIT = new ArrayList<>();
    private final static Pattern sootClassPattern = Pattern.compile("[<](.+)[:]");
    private final static Pattern sootClassPatternTwo = Pattern.compile("([a-zA-Z0-9]+[.][a-zA-Z0-9]+)\\$[0-9]+");
    private final static Pattern sootFoundPattern = Pattern.compile("\\[(.+)\\]");
    private final static Pattern sootLineNumPattern = Pattern.compile("\\(\\)\\>\\[(\\d+)\\]");
    private final static Pattern sootMthdPattern = Pattern.compile("<((?:[a-zA-Z0-9]+))>");
    private final static Pattern sootMthdPatternTwo = Pattern.compile("((?:[a-zA-Z0-9_]+))\\(");
    private final static Pattern sootFoundMatchPattern = Pattern.compile("\"{1}(.+)\"{1}");
    private final static Pattern packagePattern = Pattern.compile("package ([[a-zA-Z]+?.]+);");
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
    /**
     * Constant <code>NUM_ORTHOGONAL=0</code>
     */
    public static int NUM_ORTHOGONAL = 0;
    /**
     * Constant <code>NUM_CONSTS_TO_CHECK=0</code>
     */
    public static int NUM_CONSTS_TO_CHECK = 0;
    /**
     * Constant <code>NUM_SLICES=0</code>
     */
    public static int NUM_SLICES = 0;
    /**
     * Constant <code>NUM_HEURISTIC=0</code>
     */
    public static int NUM_HEURISTIC = 0;
    /**
     * Constant <code>DEPTH_COUNT</code>
     */
    public static int[] DEPTH_COUNT;
    /**
     * Constant <code>DEPTH=0</code>
     */
    public static int DEPTH = 0;
    /**
     * Constant <code>supportedVersion</code>
     */
    public static Version supportedVersion = Version.EIGHT;

    static {
        ASSIGN_DONT_VISIT.add("<java.util.Map: java.lang.Object get(java.lang.Object)>");
        INVOKE_DONT_VISIT.add("<java.util.Map: java.lang.Object put(java.lang.Object,java.lang.Object)>");
        INVOKE_DONT_VISIT.add("java.lang.String: void <init>");
    }

    /**
     * <p>initDepth.</p>
     *
     * @param depth a int.
     */
    public static void initDepth(int depth) {
        DEPTH = depth;
        DEPTH_COUNT = new int[depth];
    }
    //endregion

    /**
     * <p>getClassNamesFromJarArchive.</p>
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
            throw new ExceptionHandler("Error with file " + jarPath, ExceptionId.FILE_I);
        }
    }

    //region HotMethods

    /**
     * <p>retrieveFullyQualifiedNameFileSep.</p>
     *
     * @param sourceJavaFile a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> retrieveFullyQualifiedNameFileSep(List<String> sourceJavaFile) throws ExceptionHandler {
        List<String> fullPath = new ArrayList<>();
        for (String in : sourceJavaFile)
            fullPath.add(Utils.retrieveFullyQualifiedName(in).replace(".", fileSep));

        return fullPath;
    }

    /**
     * <p>handleErrorMessage.</p>
     *
     * @param e a {@link frontEnd.Interface.outputRouting.ExceptionHandler} object.
     */
    public static void handleErrorMessage(ExceptionHandler e) {
        log.debug(e.getErrorCode().getMessage());

        if (e.getErrorCode().getId().equals(0)) {
            log.info(e.getErrorCode().getMessage());
            System.out.print(e.getLongDesciption());
        } else {
            log.fatal(e.getErrorCode().getMessage());
            System.err.print(e.toString());
        }
    }

    /**
     * <p>joinSpecialSootClassPath.</p>
     *
     * @param fileIn a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String joinSpecialSootClassPath(List<String> fileIn) throws ExceptionHandler {
        return join(":", retrieveClosePath(fileIn));
    }

    /**
     * <p>retrieveClosePath.</p>
     *
     * @param fileIn a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> retrieveClosePath(List<String> fileIn) throws ExceptionHandler {
        ArrayList<String> output = new ArrayList<>();

        for (String path : fileIn) {
            String temp = Utils.verifyFileExts(path, new String[]{".java", ".class"}, false);
            if (StringUtils.isNotBlank(temp)) {
                temp = replaceLast(temp, retrieveFullyQualifiedName(path).replace(".", fileSep)).replace(".java", "").replace(".class", "");
                if (!output.contains(temp))
                    output.add(temp);
            }
        }
        return output;

    }

    /**
     * <p>retrieveFullyQualifiedName.</p>
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

                if (firstLine.startsWith("package ") && firstLine.toLowerCase().endsWith(";")) {
                    sourcePackage = firstLine.substring("package ".length(), firstLine.length() - 1) + "." + sourcePackage;
                }

            } catch (IOException e) {
                throw new ExceptionHandler("Error parsing file: " + in, ExceptionId.FILE_READ);
            }
        } else if (in.toLowerCase().endsWith(".class")) {
            sourcePackage = sourcePackage.replace(".class", "");

            String pathBreak = "";
            String fullPath = Utils.retrieveFullFilePath(in).replace(".class", "");

            //Maven-Class
            if (fullPath.contains(pathBreak = osSurround("target", "classes"))) {
            }
            //Gradle-Class
            else if (fullPath.contains(pathBreak = osSurround("java", "main"))) {
            }
            //Gen-Classes
            else if (fullPath.contains(pathBreak = osSurround("output"))) {
            } else {
                //Base Case
                fullPath = sourcePackage;
            }

            int indexOf = fullPath.indexOf(pathBreak);
            sourcePackage = fullPath.substring(indexOf == -1 ? 0 : indexOf).replace(pathBreak, "").replaceAll(fileSep, ".");

        }
        return sourcePackage;
    }

    /**
     * <p>containsAny.</p>
     *
     * @param input          a {@link java.lang.String} object.
     * @param stringsToCheck an array of {@link java.lang.String} objects.
     * @return a {@link java.lang.Boolean} object.
     */
    public static Boolean containsAny(String input, String[] stringsToCheck) {
        return containsAny(input, Arrays.asList(stringsToCheck));
    }

    /**
     * <p>containsAny.</p>
     *
     * @param input          a {@link java.lang.String} object.
     * @param stringsToCheck a {@link java.util.List} object.
     * @return a {@link java.lang.Boolean} object.
     */
    public static Boolean containsAny(String input, List<String> stringsToCheck) {
        return stringsToCheck.stream().anyMatch(input::contains);
    }

    /**
     * <p>listf.</p>EntryPointTest_CLASS
     *
     * @param path      a {@link java.lang.String} object.
     * @param fileCheck a {@link java.util.function.Predicate} object.
     * @param functor   a {@link java.util.function.Function} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> retrieveFilesPredicate(String path, Predicate<String> fileCheck, Function<File, String> functor) throws ExceptionHandler {

        List<String> output = new ArrayList<>();
        for (File file : Objects.requireNonNull(new File(verifyDir(path)).listFiles())) {
            if (file.isFile() && fileCheck.test(file.getName())) {
                if (functor == null)
                    output.add(file.getAbsolutePath());
                else
                    output.add(functor.apply(file));
            } else if (file.isDirectory())
                output.addAll(retrieveFilesPredicate(file.getAbsolutePath(), fileCheck, functor));
        }

        return output;
    }

    /**
     * <p>retrieveJavaFileImports.</p>
     *
     * @param paths a {@link java.lang.String} object.
     * @return a {@link java.util.Set} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static Set<String> retrieveJavaFileImports(String... paths) throws ExceptionHandler {
        return retrieveJavaFileImports(Arrays.asList(paths));
    }

    /**
     * <p>retrieveJavaFileImports.</p>
     *
     * @param paths a {@link java.util.List} object.
     * @return a {@link java.util.Set} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static Set<String> retrieveJavaFileImports(List<String> paths) throws ExceptionHandler {
        Set<String> results = new HashSet<>();
        for (String path : paths)
            results.addAll(retrieveJavaFileImports(path));
        return results;
    }

    /**
     * <p>retrieveJavaFileImports.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @return a {@link java.util.Set} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static Set<String> retrieveJavaFileImports(String path) throws ExceptionHandler {
        Set<String> results = new HashSet<>();
        String javaFile = verifyFileExt(path, ".java", false);

        try (BufferedReader reader = new BufferedReader(new FileReader(javaFile))) {
            String curLine;

            leave:
            while ((curLine = reader.readLine()) != null && !curLine.isEmpty()) {
                if (!curLine.startsWith("package") && !curLine.startsWith("import") && StringUtils.isNotEmpty(curLine))
                    break leave;
                else if (curLine.startsWith("import"))
                    results.add(curLine.replace("import ", "").replace(";", ""));
            }
        } catch (FileNotFoundException e) {
            //TODO - Add exception here
        } catch (IOException e) {
            //TODO - Add Exception here
        }

        return results;
    }

    /**
     * <p>setSunBootPath.</p>
     *
     * @param basePath a {@link java.lang.String} object.
     * @param rt       a {@link java.lang.String} object.
     */
    public static void setSunBootPath(String basePath, String rt) {
        System.setProperty("sun.boot.class.path", rt);
        System.setProperty("java.ext.dirs", osSurround(basePath, "lib"));
    }
    //endregion

    //region ArgMethods

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link frontEnd.argsIdentifier} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id, Object value) {
        return makeArg(id.getId(), value.toString());
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link frontEnd.argsIdentifier} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id, String value) {
        return makeArg(id.getId(), value);
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link frontEnd.argsIdentifier} object.
     * @param value a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id, List<String> value) {
        return makeArg(id.getId(), value);
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link frontEnd.argsIdentifier} object.
     * @param value a {@link rule.engine.EngineType} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id, EngineType value) {
        return makeArg(id.getId(), value.getFlag());
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id a {@link frontEnd.argsIdentifier} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id) {
        return " " + id.getArg() + " ";
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link frontEnd.MessagingSystem.routing.inputStructures.ScarfXMLId} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(ScarfXMLId id, String value) {
        return makeArg(id.getId(), value);
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(String id, String value) {
        if (StringUtils.isEmpty(value))
            return "";
        return " -" + id + " " + value + " ";
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(String id, String... value) {
        return makeArg(id, Arrays.asList(value));
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link java.lang.String} object.
     * @param value a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(String id, List<String> value) {
        if (value.size() >= 1)
            return makeArg(id, value.get(0)) + String.join(" ", value);
        return "";
    }

    /**
     * <p>makeArg.</p>
     *
     * @param id    a {@link frontEnd.argsIdentifier} object.
     * @param value a {@link frontEnd.MessagingSystem.routing.Listing} object.
     * @return a {@link java.lang.String} object.
     */
    public static String makeArg(argsIdentifier id, Listing value) {
        return makeArg(id, value.getFlag());
    }
    //endregion

    //region NotHotMethods

    /**
     * <p>getClassNamesFromApkArchive.</p>
     *
     * @param apkfile a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> getClassNamesFromApkArchive(String apkfile) throws ExceptionHandler {
        List<String> classNames = new ArrayList<>();

        File zipFile = new File(apkfile);

        try {
            DexFile dexFile = DexFileFactory.loadDexEntry(zipFile, "classes.dex", true, Opcodes.forApi(23));

            for (ClassDef classDef : dexFile.getClasses()) {
                String className = classDef.getType().replace('/', '.');
                if (!className.contains("android.")) {
                    classNames.add(className.substring(1, className.length() - 1));
                }
            }
            return classNames;
        } catch (IOException e) {
            throw new ExceptionHandler("Error with dex file classes.dex", ExceptionId.FILE_I);
        }
    }

    /**
     * <p>buildSootClassPath.</p>
     *
     * @param paths a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String buildSootClassPath(String... paths) {
        return buildSootClassPath(Arrays.asList(paths));
    }

    /**
     * <p>buildSootClassPath.</p>
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
     * <p>getJarsInDirectories.</p>
     *
     * @param dirs a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     */
    public static List<String> getJarsInDirectories(List<String> dirs) {
        ArrayList<String> list = new ArrayList<>();
        for (String dir : dirs)
            list.addAll(getJarsInDirectory(dir));
        return list;
    }

    /**
     * <p>getJarsInDirectory.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public static List<String> getJarsInDirectory(String path) {

        if (null == path || path.trim().equals(""))
            return new ArrayList<>();

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
        }

        return jarFiles;
    }

    /**
     * <p>getClassHierarchyAnalysis.</p>
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

                List<SootClass> childList = classHierarchyMap.computeIfAbsent(superClass.getName(), k -> new ArrayList<>());

                if (childList.isEmpty()) {
                    childList.add(superClass);
                }
                childList.add(sClass);
            }

            for (SootClass parent : parents) {
                List<SootClass> childList = classHierarchyMap.computeIfAbsent(parent.getName(), k -> new ArrayList<>());

                if (childList.isEmpty()) {
                    childList.add(parent);
                }
                childList.add(sClass);
            }
        }

        return classHierarchyMap;
    }

    /**
     * <p>getXmlFiles.</p>
     *
     * @param projectJarPath a {@link java.lang.String} object.
     * @param excludes       a {@link java.util.List} object.
     * @return a {@link java.util.Map} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static Map<String, String> getXmlFiles(String projectJarPath, List<String> excludes) throws ExceptionHandler {
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

    private static List<String> getXmlFileNamesFromJarArchive(String jarPath, List<String> excludes) throws ExceptionHandler {
        List<String> classNames = new ArrayList<>();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));

            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                for (String exclude : excludes) {
                    if (!entry.isDirectory() && entry.getName().endsWith(".xml") && !entry.getName().endsWith(exclude)) {
                        String className = entry.getName();
                        classNames.add(className);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new ExceptionHandler("File " + jarPath + " is not found.", ExceptionId.FILE_AFK);
        } catch (IOException e) {
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
            throw new ExceptionHandler("Error Reading " + jarPath + ".", ExceptionId.FILE_I);
        }
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * <p>findInfluencingParamters.</p>
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
                    Integer param = Integer.valueOf(useboxStr.substring("@parameter".length(), useboxStr.indexOf(':')));
                    influencingParam.add(param);
                }
            }
        }

        return influencingParam;
    }

    /**
     * <p>isSpecialInvokeOn.</p>
     *
     * @param currInstruction a {@link soot.Unit} object.
     * @param usebox          a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean isSpecialInvokeOn(Unit currInstruction, String usebox) {
        return currInstruction.toString().contains("specialinvoke")
                && currInstruction.toString().contains(usebox + ".<");
    }

    /**
     * <p>listf.</p>
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
     * <p>getClassNamesFromSnippet.</p>
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
     * <p>retrieveFullyQualifiedName.</p>
     *
     * @param sourceJavaFile a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> retrieveFullyQualifiedName(String... sourceJavaFile) throws ExceptionHandler {
        return retrieveFullyQualifiedName(Arrays.asList(sourceJavaFile));
    }

    /**
     * <p>retrieveFullyQualifiedName.</p>
     *
     * @param sourceJavaFile a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> retrieveFullyQualifiedName(List<String> sourceJavaFile) throws ExceptionHandler {
        List<String> fullPath = new ArrayList<>();
        for (String in : sourceJavaFile)
            fullPath.add(Utils.retrieveFullyQualifiedName(in));

        return fullPath;
    }

    /**
     * <p>replaceLast.</p>
     *
     * @param text     a {@link java.lang.String} object.
     * @param regexish a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String replaceLast(String text, String regexish) {
        return replaceLast(text, regexish, "");
    }

    /**
     * <p>replaceLast.</p>
     *
     * @param text        a {@link java.lang.String} object.
     * @param regexish    a {@link java.lang.String} object.
     * @param replacement a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String replaceLast(String text, String regexish, String replacement) {
        int lastIdx = text.lastIndexOf(regexish);
        if (lastIdx != -1)
            return text.substring(0, lastIdx) + replacement + text.substring(lastIdx + regexish.length());
        else
            return text;
    }

    /**
     * <p>retrievePackageFromJavaFiles.</p>
     *
     * @param sourceFiles a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrievePackageFromJavaFiles(String... sourceFiles) {
        return retrievePackageFromJavaFiles(Arrays.asList(sourceFiles));
    }

    /**
     * <p>retrievePackageFromJavaFiles.</p>
     *
     * @param sourceFiles a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrievePackageFromJavaFiles(List<String> sourceFiles) {
        String commonPath = null;

        for (String in : sourceFiles) {

            String tempPath = in.replace(retrievePackageFromJavaFiles(in), "");

            if (commonPath == null)
                commonPath = tempPath;
            else if (!commonPath.equals(tempPath)) {
                String removable = commonPath.replace(in, "");
                commonPath = commonPath.replace(removable, "");
            }

        }

        return commonPath;
    }

    /**
     * <p>retrievePackageFromJavaFiles.</p>
     *
     * @param file a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrievePackageFromJavaFiles(String file) {
        try {
            File in = new File(file);

            if (file.toLowerCase().endsWith(".java")) {
                for (String line : Files.readAllLines(in.toPath(), StandardCharsets.UTF_8)) {
                    Matcher matches = packagePattern.matcher(line);
                    if (matches.find())
                        return Utils.fileSep + StringUtils.trimToNull(matches.group(1)) + Utils.fileSep + in.getName();
                }
            } else
                return in.getName();
        } catch (IOException e) {
            //TODO - Add Catch Here
        }
        return file;
    }

    /**
     * <p>retrieveBaseDirectory.</p>
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
                    if (itr == baseSplit.length || itr == tempSplit.length)
                        break loopCheck;

                    if (baseSplit[itr].equals(tempSplit[itr]))
                        common.add(baseSplit[itr]);
                    else
                        break loopCheck;
                }

                baseDir = join(fileSep, common);
            }

            baseSplit = Arrays.stream(baseDir.split(fileSep)).filter(StringUtils::isNotBlank).toArray(String[]::new);
        }

        if (baseDir.equals(""))
            throw new ExceptionHandler("Different file paths sent in.", ExceptionId.ARG_VALID);

        return baseDir;
    }

    /**
     * <p>retrieveTrimmedSourcePaths.</p>
     *
     * @param files a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     */
    public static List<String> retrieveTrimmedSourcePaths(List<String> files) {
        List<String> filePaths = new ArrayList<>();
        for (String relativeFile : files) {
            String relativeFilePath = "";

            File file = new File(relativeFile);

            try {
                relativeFilePath = file.getCanonicalPath().replace(file.getName(), "");
            } catch (IOException e) {
                //TODO - Add Catch Here
            }

            if (!filePaths.contains(relativeFilePath))
                filePaths.add(relativeFilePath);
        }
        return filePaths;
    }

    /**
     * <p>retrieveJavaFilesFromDir.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @return a {@link java.util.ArrayList} object.
     */
    public static ArrayList<String> retrieveJavaFilesFromDir(String path) {
        if (StringUtils.isEmpty(path))
            return null;

        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            return walk
                    .map(Path::toString)
                    .filter(f -> f.endsWith(".java"))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            //TODO - add exception here
            return null;
        }
    }

    /**
     * <p>retrieveBaseSourcePath.</p>
     *
     * @param sourcePaths    a {@link java.util.List} object.
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
     * <p>inputFiles.</p>
     *
     * @param file a {@link java.lang.String} object.
     * @return a {@link java.util.ArrayList} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static ArrayList<String> inputFiles(String file) throws ExceptionHandler {
        ArrayList<String> filePaths = new ArrayList<>();
        String curLine = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((curLine = reader.readLine()) != null && !curLine.isEmpty())
                if ((curLine = verifyFile(curLine, false)) != null)
                    filePaths.add(curLine);

            return filePaths;
        } catch (FileNotFoundException e) {
            throw new ExceptionHandler("File " + file + " not found", ExceptionId.FILE_I);
        } catch (IOException e) {
            throw new ExceptionHandler("Error reading the file  " + file, ExceptionId.FILE_I);
        }
    }

    /**
     * <p>retrieveFileParentPath.</p>
     *
     * @param filePath a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveFileParentPath(String filePath) {
        String fullPaths = retrieveFullFilePath(filePath);
        return fullPaths.substring(0, fullPaths.lastIndexOf(fileSep));
    }

    /**
     * <p>retrieveFullFilePath.</p>
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
        else
            return filename;
    }

    /**
     * This method trims the file path and package from the absolute path.
     * <p>EX: src/main/java/com/test/me/main.java {@literal -}{@literal >} main.java</p>
     *
     * @param fullFilePath {@link java.lang.String} - The full file path
     * @return {@link java.lang.String} - The file name with the extension attached
     */
    public static String trimFilePath(String fullFilePath) {
        String[] folderSplit = fullFilePath.split(Pattern.quote(System.getProperty("file.separator")));
        return folderSplit[folderSplit.length - 1];
    }

    /**
     * <p>osSurround.</p>
     *
     * @param elements a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String osSurround(String... elements) {
        return surround(Utils.fileSep, elements);
    }

    /**
     * <p>osPathJoin.</p>
     *
     * @param elements a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String osPathJoin(String... elements) {
        return join(Utils.fileSep, elements);
    }

    /**
     * <p>surround.</p>
     *
     * @param delimiter a {@link java.lang.String} object.
     * @param elements  a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String surround(String delimiter, String... elements) {
        return surround(delimiter, Arrays.asList(elements));
    }

    /**
     * <p>surround.</p>
     *
     * @param delimiter a {@link java.lang.String} object.
     * @param elements  a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    public static String surround(String delimiter, List<String> elements) {
        String current = StringUtils.trimToNull(join(delimiter, elements));

        if (!current.startsWith(delimiter))
            current = delimiter + current;

        if (!current.endsWith(delimiter))
            current += delimiter;

        return current;
    }

    /**
     * <p>join.</p>
     *
     * @param delimiter a {@link java.lang.String} object.
     * @param elements  a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String join(String delimiter, String... elements) {
        return join(delimiter, Arrays.asList(elements));
    }

    /**
     * <p>join.</p>
     *
     * @param delimiter a {@link java.lang.String} object.
     * @param elements  a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    public static String join(String delimiter, List<String> elements) {
        StringBuilder tempString = new StringBuilder();
        for (String in : elements) {
            if (null != (in = StringUtils.trimToNull(in))) {
                tempString.append(in);
                if (!in.equals(elements.get(elements.size() - 1)))
                    tempString.append(delimiter);
            }
        }

        return tempString.toString();
    }

    /**
     * <p>getJAVA_HOME.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String getJAVA_HOME() throws ExceptionHandler {
        String JAVA_HOME = System.getenv("JAVA_HOME");
        if (StringUtils.isEmpty(JAVA_HOME)) {
            throw new ExceptionHandler("Environment Variable: JAVA_HOME is not set.", ExceptionId.ENV_VAR);
        }
        return JAVA_HOME.replaceAll("//", "/");
    }

    /**
     * <p>getJAVA7_HOME.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String getJAVA7_HOME() throws ExceptionHandler {
        String JAVA7_HOME = System.getenv("JAVA7_HOME");
        if (StringUtils.isEmpty(JAVA7_HOME)) {
            throw new ExceptionHandler("Environment Variable: JAVA7_HOME is not set.", ExceptionId.ENV_VAR);
        }
        return JAVA7_HOME.replaceAll("//", "/");
    }

    /**
     * <p>getANDROID.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String getANDROID() throws ExceptionHandler {
        String ANDROID_HOME = System.getenv("ANDROID_HOME");
        if (StringUtils.isEmpty(ANDROID_HOME)) {
            throw new ExceptionHandler("Environment Variable: ANDROID_HOME is not set.", ExceptionId.ENV_VAR);
        }
        return ANDROID_HOME;
    }

    /**
     * //TODO - Need to verify this is nessecary
     * <p>getBaseSOOT.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String getBaseSOOT() throws ExceptionHandler {
        String rt = getRT();
        setSunBootPath(Utils.getJAVA_HOME(), rt);

        return join(":", getJCE(), rt);
    }

    /**
     * <p>getRT.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String getRT() throws ExceptionHandler {
        String rt = Utils.osPathJoin("jre", "lib", "rt.jar");

        return osPathJoin(Utils.getJAVA_HOME(), rt);
    }

    /**
     * <p>getJCE.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String getJCE() throws ExceptionHandler {
        String jce = Utils.osPathJoin("jre", "lib", "jce.jar");

        return osPathJoin(Utils.getJAVA_HOME(), jce);
    }

    /**
     * <p>getBaseSOOT7.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String getBaseSOOT7() throws ExceptionHandler {
        String rt = Utils.osPathJoin(Utils.getJAVA7_HOME(), "jre", "lib", "rt.jar");
        String jce = Utils.osPathJoin(Utils.getJAVA7_HOME(), "jre", "lib", "jce.jar");

        setSunBootPath(Utils.getJAVA7_HOME(), rt);

        return Utils.join(":", Utils.getJAVA7_HOME(), rt, jce);
    }

    /**
     * <p>loadSootClasses.</p>
     *
     * @param classes a {@link java.util.List} object.
     */
    public static void loadSootClasses(List<String> classes) {
        Options.v().set_keep_line_number(true);
        Options.v().set_allow_phantom_refs(true);

        if (classes != null)
            for (String clazz : classes)
                Options.v().classes().add(clazz);

        Scene.v().loadBasicClasses();
    }

    /**
     * <p>retrieveClassNameFromSootString.</p>
     *
     * @param sootString a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveClassNameFromSootString(String sootString) {
        Matcher secondMatches = sootClassPatternTwo.matcher(sootString);
        if (secondMatches.find())
            return secondMatches.group(1);

        Matcher matches = sootClassPattern.matcher(sootString);
        if (matches.find())
            return matches.group(1);

        return "UNKNOWN";
    }

    /**
     * <p>retrieveFoundPatternFromSootString.</p>
     *
     * @param sootString a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveFoundPatternFromSootString(String sootString) {
        Matcher matches = sootFoundPattern.matcher(sootString);

        if (matches.find())
            return matches.group(1).replaceAll("\"", "");
        return "UNKNOWN";
    }

    /**
     * <p>retrieveLineNumFromSootString.</p>
     *
     * @param sootString a {@link java.lang.String} object.
     * @return a {@link java.lang.Integer} object.
     */
    public static Integer retrieveLineNumFromSootString(String sootString) {
        Matcher matches = sootLineNumPattern.matcher(sootString);

        if (matches.find())
            return Integer.parseInt(matches.group(1));
        return -1;
    }

    /**
     * <p>retrieveMethodFromSootString.</p>
     *
     * @param sootString a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveMethodFromSootString(String sootString) {
        Matcher matches = sootMthdPattern.matcher(sootString);

        if (matches.find())
            return matches.group(1);

        Matcher secondMatches = sootMthdPatternTwo.matcher(sootString);
        if (secondMatches.find())
            return secondMatches.group(1);

        return "UNKNOWN";
    }

    /**
     * <p>getPlatform.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getPlatform() {

        return System.getProperty("os.name") + "_" + System.getProperty("os.version");
    }

    /**
     * <p>getJVMInfo.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getJVMInfo() {
        return System.getProperty("java.runtime.version");
    }

    /**
     * <p>retrieveFoundMatchFromSootString.</p>
     *
     * @param sootString a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveFoundMatchFromSootString(String sootString) {
        Matcher matches = sootFoundMatchPattern.matcher(sootString);

        if (matches.find())
            return StringUtils.trimToNull(matches.group(1));

        return "UNKNOWN";
    }

    /**
     * <p>verifyDir.</p>
     *
     * @param dir a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String verifyDir(String dir) throws ExceptionHandler {
        File dirChecking = new File(dir);
        if (!dirChecking.exists() || !dirChecking.isDirectory())
            throw new ExceptionHandler(dirChecking.getName() + " is not a valid directory.", ExceptionId.ARG_VALID);

        try {
            return dirChecking.getCanonicalPath();
        } catch (Exception e) {
            throw new ExceptionHandler("Error retrieving the full path of the " + dirChecking + ".", ExceptionId.FILE_AFK);
        }
    }

    /**
     * <p>retrieveDirs.</p>
     *
     * @param arguments a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> retrieveDirs(List<String> arguments) throws ExceptionHandler {
        List<String> dirs = new ArrayList<>();
        for (String dir : arguments)
            dirs.add(Utils.verifyDir(dir));

        return dirs;
    }

    /**
     * <p>verifyFile.</p>
     *
     * @param file      a {@link java.lang.String} object.
     * @param overWrite a {@link java.lang.Boolean} object.
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String verifyFile(String file, Boolean overWrite) throws ExceptionHandler {
        //Base line string check
        if (null == file || "".equals(file))
            return null;

        File tempFile = new File(file);

        if (overWrite && (tempFile.exists() || tempFile.isFile()))
            throw new ExceptionHandler(tempFile.getName() + " is already a valid file.", ExceptionId.FILE_O);

        //Enhance Validation on the compiled java class file
        if (tempFile.isFile() && tempFile.getName().endsWith(".class")) {
            try (DataInputStream stream = new DataInputStream(new FileInputStream(file))) {
                //Verifying if the class file has the Magic Java Number
                if (stream.readInt() != 0xcafebabe) {
                    throw new ExceptionHandler("The class file " + file + " is not a valid java.class file.", ExceptionId.ARG_VALID);
                } else {

                    //Moving the stream past the minor version
                    stream.readUnsignedShort();

                    //Checking the Major Version of the JDK that compiled the file against the supported version
                    Version fileVersion = Version.retrieveByMajor(stream.readUnsignedShort());
                    if (!fileVersion.supportedFile()) {
                        throw new ExceptionHandler("The class file (compiled by a JDK Version " + fileVersion.getVersionNumber() + ") is not supported.", ExceptionId.ARG_VALID);
                    }
                }
            } catch (IOException e) {
                throw new ExceptionHandler("Error reading the file " + file + ".", ExceptionId.FILE_READ);
            }
        }

        try {
            return tempFile.getCanonicalPath();
        } catch (Exception e) {
            throw new ExceptionHandler("Error retrieving the path of the file " + tempFile.getName() + ".", ExceptionId.FILE_AFK);
        }
    }

    /**
     * <p>verifyFileExt.</p>
     *
     * @param file      a {@link java.lang.String} object.
     * @param fileExt   a {@link java.lang.String} object.
     * @param overWrite a {@link java.lang.Boolean} object.
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String verifyFileExt(String file, String fileExt, Boolean overWrite) throws ExceptionHandler {
        if ("dir".equals(fileExt))
            return Utils.verifyDir(file);
        else {
            if (!file.toLowerCase().endsWith(fileExt))
                throw new ExceptionHandler("File " + file + " doesn't have the right file type ", ExceptionId.ARG_VALID);

            return Utils.verifyFile(file, overWrite);
        }
    }

    /**
     * <p>verifyFileExts.</p>
     *
     * @param file      a {@link java.lang.String} object.
     * @param fileExt   an array of {@link java.lang.String} objects.
     * @param overWrite a {@link java.lang.Boolean} object.
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String verifyFileExts(String file, String[] fileExt, Boolean overWrite) throws ExceptionHandler {

        Boolean matches = false;

        for (String extensions : fileExt)
            if (file.toLowerCase().endsWith(extensions)) {
                matches = true;
                break;
            }

        //Attempting to verify if the class path is a directory
        if (matches)
            return Utils.verifyFile(file, overWrite);
        else
            try {
                return Utils.verifyDir(file);
            } catch (ExceptionHandler e) {
                throw new ExceptionHandler("File " + file + " doesn't have the right file type ", ExceptionId.ARG_VALID);
            }
    }

    /**
     * <p>verifyClassPaths.</p>
     *
     * @param classPaths a {@link java.lang.String} object.
     * @return a {@link java.util.ArrayList} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static ArrayList<String> verifyClassPaths(String... classPaths) throws ExceptionHandler {
        return verifyClassPaths(Arrays.asList(classPaths));
    }

    /**
     * <p>verifyClassPaths.</p>
     *
     * @param classPaths a {@link java.util.List} object.
     * @return a {@link java.util.ArrayList} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static ArrayList<String> verifyClassPaths(List<String> classPaths) throws ExceptionHandler {
        ArrayList<String> output = new ArrayList<>();
        for (String klazz : classPaths)
            output.addAll(verifyClassPaths(klazz));
        return output;
    }

    /**
     * <p>verifyClassPaths.</p>
     *
     * @param classPaths a {@link java.lang.String} object.
     * @return a {@link java.util.ArrayList} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static ArrayList<String> verifyClassPaths(String classPaths) throws ExceptionHandler {
        ArrayList<String> output = new ArrayList<>();
        for (String path : classPaths.split(":"))
            output.add(Utils.verifyFileExts(path, new String[]{".java", ".class", ".jar", "dir"}, false));

        return output;
    }

    /**
     * <p>retrieveFilePath.</p>
     *
     * @param file a {@link java.lang.String} object.
     * @param type a {@link rule.engine.EngineType} object.
     * @return a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static String retrieveFilePath(String file, EngineType type) throws ExceptionHandler {
        if (!file.toLowerCase().toLowerCase().endsWith(type.getInputExtension()))
            throw new ExceptionHandler("File " + file + " doesn't have the right file type ", ExceptionId.ARG_VALID);

        File tempFile = new File(file);
        if (!tempFile.exists() || !tempFile.isFile())
            throw new ExceptionHandler(tempFile.getName() + " is not a valid file.", ExceptionId.ARG_VALID);

        try {
            return tempFile.getCanonicalPath();
        } catch (Exception e) {
            throw new ExceptionHandler("Error retrieving the path of the file " + tempFile.getName() + ".", ExceptionId.FILE_AFK);
        }
    }

    /**
     * <p>retrieveFilesByType.</p>
     *
     * @param arguments a {@link java.util.List} object.
     * @param type      a {@link rule.engine.EngineType} object.
     * @return a {@link java.util.List} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static List<String> retrieveFilesByType(List<String> arguments, EngineType type) throws ExceptionHandler {
        if (type == EngineType.DIR)
            if (arguments.size() != 1)
                throw new ExceptionHandler("Please enter one source argument for this use case.", ExceptionId.GEN_VALID);
            else
                return retrieveDirs(arguments);

        List<String> filePaths = new ArrayList<>();

        for (String in : arguments)
            for (String foil : in.split(":"))
                filePaths.add(retrieveFilePath(foil, type));

        return filePaths;
    }

    /**
     * <p>getCurrentTimeStamp.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getCurrentTimeStamp() {
        return dateFormat.format(new Date());

    }

    /**
     * <p>getRelativeFilePath.</p>
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
            throw new ExceptionHandler("Error reading file: " + filePath, ExceptionId.FILE_I);
        }
    }

    /**
     * <p>createAssignInvokeUnitContainer.</p>
     *
     * @param currInstruction a {@link soot.Unit} object.
     * @param caller          a {@link java.lang.String} object.
     * @param depth           a int.
     * @return a {@link analyzer.backward.UnitContainer} object.
     */
    public static UnitContainer createAssignInvokeUnitContainer(Unit currInstruction, String caller, int depth) {

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

                HeuristicBasedInstructions returnInfluencingInstructions = new HeuristicBasedInstructions(method,
                        "return");

                intraAnalysis = returnInfluencingInstructions.getAnalysisResult().getAnalysis();
            } else {

                NUM_ORTHOGONAL++;

                OrthogonalInfluenceInstructions other = new OrthogonalInfluenceInstructions(method, "return", depth - 1);
                intraAnalysis = other.getOrthogonalSlicingResult().getAnalysisResult();
            }

            // Get args
            List<Integer> args = Utils.findInfluencingParamters(intraAnalysis);

            // Get fields
            Set<String> usedFields = new HashSet<>();
            for (UnitContainer iUnit : intraAnalysis) {
                for (ValueBox usebox : iUnit.getUnit().getUseBoxes()) {
                    if (usebox.getValue().toString().startsWith("r0.") || usebox.getValue().toString().startsWith("this.")) {
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
     * <p>isArgOfAssignInvoke.</p>
     *
     * @param useBox a {@link soot.ValueBox} object.
     * @param unit   a {@link soot.Unit} object.
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
     * <p>isArgOfByteArrayCreation.</p>
     *
     * @param useBox a {@link soot.ValueBox} object.
     * @param unit   a {@link soot.Unit} object.
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
     * <p>isArgumentOfInvoke.</p>
     *
     * @param analysis a {@link analyzer.backward.Analysis} object.
     * @param index    a int.
     * @param outSet   a {@link java.util.List} object.
     * @return a {@link analyzer.backward.UnitContainer} object.
     */
    public static UnitContainer isArgumentOfInvoke(Analysis analysis, int index,
                                                   List<UnitContainer> outSet) {
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
                            Integer parameter = Integer.valueOf(useboxStr.substring("@parameter".length(), useboxStr.indexOf(':')));
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
                                    if (arg.equivTo(defBox.getValue()) ||
                                            isArrayUseBox(curUnit, insetIns, defBox, arg)) {
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

                            if ((defBox.getValue().toString().equals("r0") && insetIns.getUnit().toString().startsWith("r0.")) ||
                                    (defBox.getValue().toString().equals("this") && insetIns.getUnit().toString().startsWith("this."))) {
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
                                    if (arg.equivTo(defBox.getValue()) ||
                                            isArrayUseBox(curUnit, insetIns, defBox, arg)) {
                                        return curUnit;
                                    }
                                }
                            }
                        }

                    } else {
                        for (ValueBox defBox : insetIns.getUnit().getDefBoxes()) {

                            if ((defBox.getValue().toString().equals("r0") && insetIns.getUnit().toString().startsWith("r0.")) ||
                                    (defBox.getValue().toString().equals("this") && insetIns.getUnit().toString().startsWith("this."))) {
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

    private static InvokeUnitContainer getDefinedFieldsFromInvoke(SootMethod method, Set<String> usedFields) {

        Chain<SootField> fields = method.getDeclaringClass().getFields();

        InvokeUnitContainer unitContainer = new InvokeUnitContainer();

        for (String usedField : usedFields) {
            for (SootField field : fields) {
                if (usedField.contains(field.toString())) {
                    unitContainer.getDefinedFields().add(usedField);
                }
            }
        }

        for (String field : unitContainer.getDefinedFields()) {

            HeuristicBasedInstructions influencingInstructions = new HeuristicBasedInstructions(method, field);

            HeuristicBasedAnalysisResult propAnalysis = influencingInstructions.getAnalysisResult();

            if (propAnalysis.getAnalysis() != null) {

                // Get args
                List<Integer> args = Utils.findInfluencingParamters(propAnalysis.getAnalysis());
                unitContainer.setArgs(args);

                unitContainer.setAnalysisResult(propAnalysis.getAnalysis());
            }
        }

        return unitContainer;
    }

    /**
     * <p>createAnalysisOutput.</p>
     *
     * @param xmlFileStr          a {@link java.util.Map} object.
     * @param sourcePaths         a {@link java.util.List} object.
     * @param predictableSourcMap a {@link java.util.Map} object.
     * @param rule                a {@link java.lang.String} object.
     * @param output              a {@link frontEnd.MessagingSystem.routing.outputStructures.OutputStructure} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public static void createAnalysisOutput(Map<String, String> xmlFileStr, List<String> sourcePaths, Map<UnitContainer, List<String>> predictableSourcMap, String rule, OutputStructure output) throws ExceptionHandler {
        Integer ruleNumber = Integer.parseInt(rule);

        for (UnitContainer unit : predictableSourcMap.keySet())
            if (predictableSourcMap.get(unit).size() <= 0)
                output.addIssue(new AnalysisIssue(unit, ruleNumber, "", sourcePaths));
            else
                for (String sootString : predictableSourcMap.get(unit))
                    output.addIssue(new AnalysisIssue(unit, ruleNumber, "Found: \"" + sootString.replaceAll("\"", "") + "\"", sourcePaths));

    }

    /**
     * <p>calculateAverage.</p>
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
     * <p>createDepthCountList.</p>
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
     * <p>isArgOfInvoke.</p>
     *
     * @param useBox a {@link soot.ValueBox} object.
     * @param unit   a {@link soot.Unit} object.
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

    private static boolean isArrayUseBox(UnitContainer curUnit, UnitContainer insetIns, ValueBox defBox, Value useBox) {
        return (defBox.getValue().toString().contains(useBox.toString())
                && curUnit.getMethod().equals(insetIns.getMethod())
                && useBox.getType() instanceof ArrayType);
    }

    /**
     * <p>createInvokeUnitContainer.</p>
     *
     * @param currInstruction a {@link soot.Unit} object.
     * @param caller          a {@link java.lang.String} object.
     * @param usedFields      a {@link java.util.List} object.
     * @param depth           a int.
     * @return a {@link analyzer.backward.UnitContainer} object.
     */
    public static UnitContainer createInvokeUnitContainer(Unit currInstruction, String caller, List<String> usedFields, int depth) {

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

                    HeuristicBasedInstructions influencingInstructions = new HeuristicBasedInstructions(method, field);
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

                    OrthogonalInfluenceInstructions other = new OrthogonalInfluenceInstructions(method, field, depth - 1);
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
