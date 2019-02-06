package main.util;

import main.analyzer.backward.*;
import main.slicer.backward.heuristic.HeuristicBasedAnalysisResult;
import main.slicer.backward.heuristic.HeuristicBasedInstructions;
import main.analyzer.backward.UnitContainer;
import main.frontEnd.Interface.ExceptionHandler;
import main.frontEnd.MessagingSystem.routing.Listing;
import main.rule.engine.EngineType;
import main.util.manifest.ProcessManifest;
import org.apache.commons.lang3.StringUtils;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import soot.Scene;
import soot.SootClass;
import soot.Unit;
import soot.ValueBox;
import soot.options.Options;
import soot.*;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.util.Chain;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static soot.SootClass.BODIES;

/**
 * <p>Utils class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class Utils {
    /**
     * {@link main.util.Utils#getClassNamesFromJarArchive}
     * {@link main.util.Utils#retrieveFullyQualifiedName}
     * - Enhance this to look for package declarations not at the top of the file
     * License - TLDR
     * package org.main.hello;
     * {@link main.util.Utils#getClassNamesFromJarArchive}
     * {@link main.util.Utils#getClassNamesFromJarArchive}
     */

    private static String fileSep = System.getProperty("file.separator");
    private static Pattern sootClassPattern = Pattern.compile("[<](.+)[:]");
    private static Pattern sootClassPatternTwo = Pattern.compile("([a-zA-Z0-9]+[.][a-zA-Z0-9]+)\\$[0-9]+");
    private static Pattern sootFoundPattern = Pattern.compile("\\[(.+)\\]");
    private static Pattern sootLineNumPattern = Pattern.compile("\\(\\)\\>\\[(\\d+)\\]");
    private static Pattern sootMthdPattern = Pattern.compile("<((?:[a-zA-Z0-9]+))>");
    private static Pattern sootMthdPatternTwo = Pattern.compile("((?:[a-zA-Z0-9_]+))\\(");
    private static Pattern sootFoundMatchPattern = Pattern.compile("\"{1}(.+)\"{1}");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");

    /**
     * <p>getClassNamesFromJarArchive.</p>
     *
     * @param jarPath a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws java.io.IOException if any.
     */
    public static List<String> getClassNamesFromJarArchive(String jarPath) throws IOException {
        List<String> classNames = new ArrayList<>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                String className = entry.getName().replace('/', '.');
                classNames.add(className.substring(0, className.length() - ".class".length()));
            }
        }
        return classNames;
    }

    /**
     * <p>getBasePackageNameFromApk.</p>
     *
     * @param apkPath a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public static String getBasePackageNameFromApk(String apkPath) {

        String basePackage = null;

        ProcessManifest processManifest = new ProcessManifest();

        try {
            processManifest.loadManifestFile(apkPath);
            basePackage = processManifest.getPackageName();
        } catch (Exception e) {
            System.out.println("Couldn't load manifest file.");
        }

        return basePackage;
    }

    /**
     * <p>getBasePackageNameFromJar.</p>
     *
     * @param jarPath a {@link java.lang.String} object.
     * @param isMain  a boolean.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public static String getBasePackageNameFromJar(String jarPath, boolean isMain) throws IOException {

        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));

        List<String> basePackages = new ArrayList<>();

        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                String className = entry.getName().replace('/', '.');
                className = className.substring(0, className.length() - ".class".length());

                String[] splits = className.split("\\.");
                StringBuilder basePackage = new StringBuilder();

                if (splits.length > 3) { // assumption package structure is org.apache.xyz.main
                    basePackage.append(splits[0])
                            .append(".")
                            .append(splits[1])
                            .append(".")
                            .append(splits[2]);
                } else if (splits.length == 3) {
                    basePackage.append(splits[0])
                            .append(".")
                            .append(splits[1]);
                } else {
                    basePackage.append(splits[0]);
                }

                String basePackageStr = basePackage.toString();

                if (!basePackages.toString().contains(basePackageStr)) {
                    basePackages.add(basePackageStr);
                }
            }
        }

        if (basePackages.size() == 1) {
            return basePackages.get(0);
        } else if (basePackages.size() > 1) {

            if (isMain) {
                System.out.println("***Multiple Base packages of " + jarPath + " : " + basePackages.toString());
            }

            for (String basePackage : basePackages) {
                if (basePackage.split("\\.").length > 2 && jarPath.contains(basePackage.split("\\.")[2])) {
                    return basePackage;
                }
            }
        }

        return null;

    }

    /**
     * <p>getClassNamesFromApkArchive.</p>
     *
     * @param apkfile a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     * @throws java.io.IOException if any.
     */
    public static List<String> getClassNamesFromApkArchive(String apkfile) throws IOException {
        List<String> classNames = new ArrayList<>();

        File zipFile = new File(apkfile);

        DexFile dexFile = DexFileFactory.loadDexEntry(zipFile, "classes.dex", true, Opcodes.forApi(23));

        for (ClassDef classDef : dexFile.getClasses()) {
            String className = classDef.getType().replace('/', '.');
            if (!className.contains("android.")) {
                classNames.add(className.substring(1, className.length() - 1));
            }
        }

        return classNames;
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

            if (path.endsWith(".jar")) {
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
                        if (file.getName().endsWith(".jar")) {
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
     * <p>getJarsInDirectory.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public static List<String> getJarsInDirectory(String path) {

        List<String> jarFiles = new ArrayList<>();
        File dir = new File(path);

        if (dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files == null) {
                return jarFiles;
            }

            for (File file : files) {
                if (file.getName().endsWith(".jar")) {
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

                List<SootClass> childList = classHierarchyMap.get(superClass.getName());

                if (childList == null) {
                    childList = new ArrayList<>();
                    classHierarchyMap.put(superClass.getName(), childList);
                }

                if (childList.isEmpty()) {
                    childList.add(superClass);
                }
                childList.add(sClass);
            }

            for (SootClass parent : parents) {
                List<SootClass> childList = classHierarchyMap.get(parent.getName());

                if (childList == null) {
                    childList = new ArrayList<>();
                    classHierarchyMap.put(parent.getName(), childList);
                }

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
     * @throws java.io.IOException if any.
     */
    public static Map<String, String> getXmlFiles(String projectJarPath, List<String> excludes) throws IOException {
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

    private static List<String> getXmlFileNamesFromJarArchive(String jarPath, List<String> excludes) throws IOException {
        List<String> classNames = new ArrayList<>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            for (String exclude : excludes) {
                if (!entry.isDirectory() && entry.getName().endsWith(".xml") && !entry.getName().endsWith(exclude)) {
                    String className = entry.getName();
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    private static InputStream readFileFromZip(String jarPath, String file) throws IOException {
        ZipFile zipFile = new ZipFile(jarPath);
        ZipEntry entry = zipFile.getEntry(file);
        return zipFile.getInputStream(entry);
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

        List<File> resultList = new ArrayList<File>();

        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
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
                if (name.endsWith(".java")) {
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
     * @param sourceJavaFile a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     */
    public static List<String> retrieveFullyQualifiedName(List<String> sourceJavaFile) {
        List<String> fullPath = new ArrayList<>();
        for (String in : sourceJavaFile)
            fullPath.add(Utils.retrieveFullyQualifiedName(in));

        return fullPath;
    }

    /**
     * <p>retrieveFullyQualifiedName.</p>
     *
     * @param in a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String retrieveFullyQualifiedName(String in) {

        String sourcePackage = trimFilePath(in);
        if (in.endsWith(".java")) {
            sourcePackage = sourcePackage.replace(".java", "");
            try (BufferedReader br = new BufferedReader(new FileReader(in))) {
                String firstLine = br.readLine();

                if (firstLine.startsWith("package ") && firstLine.endsWith(";")) {
                    sourcePackage = firstLine.substring("package ".length(), firstLine.length() - 1) + "." + sourcePackage;
                } else //File has no package declaration, retrieving the last folder path
                {
                    String[] paths = Utils.retrieveFullFilePath(in).split(fileSep);

                    sourcePackage = paths[paths.length - 2] + "." + sourcePackage;
                }

            } catch (IOException e) {
                System.out.println("Issue Reading File: " + in);
            }
        } else if (in.endsWith(".class")) {
            sourcePackage = sourcePackage.replace(".class", "");

            String[] paths = Utils.retrieveFullFilePath(in).split(fileSep);

            sourcePackage = paths[paths.length - 2] + "." + sourcePackage;
        }
        return sourcePackage;
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

            }

            if (!filePaths.contains(relativeFilePath))
                filePaths.add(relativeFilePath);
        }
        return filePaths;
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
     * <p>osPathJoin.</p>
     *
     * @param elements a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String osPathJoin(String... elements) {
        return Utils.join(Utils.fileSep, elements);
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
            tempString.append(in);
            if (!in.equals(elements.get(elements.size() - 1)))
                tempString.append(delimiter);
        }

        return tempString.toString();
    }

    /**
     * <p>getJAVA_HOME.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getJAVA_HOME() {
        String JAVA_HOME = System.getenv("JAVA_HOME");
        if (StringUtils.isEmpty(JAVA_HOME)) {
            System.out.println("Please Set JAVA_HOME");
            System.exit(1);
        }
        return JAVA_HOME;
    }

    /**
     * <p>getJAVA7_HOME.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getJAVA7_HOME() {
        String JAVA7_HOME = System.getenv("JAVA7_HOME");
        if (StringUtils.isEmpty(JAVA7_HOME)) {
            System.out.println("Please Set JAVA7_HOME");
            System.exit(1);
        }
        return JAVA7_HOME;
    }

    /**
     * <p>getANDROID.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getANDROID() {
        String ANDROID_HOME = System.getenv("ANDROID_HOME");
        if (StringUtils.isEmpty(ANDROID_HOME)) {
            System.out.println("Please Set ANDROID_HOME");
            System.exit(1);
        }
        return ANDROID_HOME;
    }

    /**
     * <p>getBaseSOOT.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getBaseSOOT() {
        String rt = Utils.join(Utils.fileSep, "jre", "lib", "rt.jar:");
        String jce = Utils.join(Utils.fileSep, "jre", "lib", "jce.jar");

        return Utils.getJAVA_HOME() + Utils.fileSep + Utils.join(Utils.getJAVA_HOME() + Utils.fileSep, rt, jce);
    }

    /**
     * <p>getBaseSOOT7.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getBaseSOOT7() {
        String rt = Utils.join(Utils.fileSep, "jre", "lib", "rt.jar:");
        String jce = Utils.join(Utils.fileSep, "jre", "lib", "jce.jar");

        return Utils.getJAVA7_HOME() + Utils.fileSep + Utils.join(Utils.getJAVA7_HOME() + Utils.fileSep, rt, jce);
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
        if (!System.getProperty("os.name").contains("Linux")) {
            return System.getProperty("os.name") + "_" + System.getProperty("os.version");
        } else {
            try {
                String baseName = System.getProperty("os.name") + "_" + System.getProperty("os.version");
                Scanner file = new Scanner(new File("/etc/os-release"));
                String line;
                while (file.hasNextLine()) {
                    line = file.nextLine();
                    if (line.startsWith("PRETTY_NAME=")) {
                        baseName = line.replace("PRETTY_NAME=", "").replaceAll("\"", "").replaceAll(" ", "_");
                        break;
                    }
                }
                file.close();
                return baseName;
            } catch (FileNotFoundException e) {
                return System.getProperty("os.name") + "_" + System.getProperty("os.version");
            }
        }
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
     * <p>retrieveDirs.</p>
     *
     * @param arguments a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public static List<String> retrieveDirs(List<String> arguments) throws ExceptionHandler {
        List<String> dirs = new ArrayList<>();
        for (String dir : arguments) {
            File dirChecking = new File(dir);
            if (!dirChecking.exists() || !dirChecking.isDirectory())
                throw new ExceptionHandler(dirChecking.getName() + " is not a valid directory.");

            try {
                dirs.add(dirChecking.getCanonicalPath());
            } catch (Exception e) {
                throw new ExceptionHandler("Error retrieving the path of the directory.");
            }
        }
        return dirs;
    }

    /**
     * <p>verifyFileOut.</p>
     *
     * @param file a {@link java.lang.String} object.
     * @param type a {@link main.frontEnd.MessagingSystem.routing.Listing} object.
     * @return a {@link java.lang.String} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public static String verifyFileOut(String file, Listing type) throws ExceptionHandler {
        if (!file.endsWith(type.getOutputFileExt()))
            throw new ExceptionHandler("File " + file + " doesn't have the right file type ");

        File tempFile = new File(file);

        //TODO - Add flag to verify overwrite a file?
        /*if (tempFile.exists() || tempFile.isFile())
            throw new ExceptionHandler(tempFile.getName() + " is already a valid file.");
*/
        try {
            return tempFile.getCanonicalPath();
        } catch (Exception e) {
            throw new ExceptionHandler("Error retrieving the path of the file " + tempFile.getName() + ".");
        }
    }

    /**
     * <p>retrieveFilePath.</p>
     *
     * @param file a {@link java.lang.String} object.
     * @param type a {@link main.rule.engine.EngineType} object.
     * @return a {@link java.lang.String} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public static String retrieveFilePath(String file, EngineType type) throws ExceptionHandler {
        if (!file.endsWith(type.getInputExtension()))
            throw new ExceptionHandler("File " + file + " doesn't have the right file type ");

        File tempFile = new File(file);
        if (!tempFile.exists() || !tempFile.isFile())
            throw new ExceptionHandler(tempFile.getName() + " is not a valid file.");

        try {
            return tempFile.getCanonicalPath();
        } catch (Exception e) {
            throw new ExceptionHandler("Error retrieving the path of the file " + tempFile.getName() + ".");
        }
    }

    /**
     * <p>retrieveFilesByType.</p>
     *
     * @param arguments a {@link java.util.List} object.
     * @param type      a {@link main.rule.engine.EngineType} object.
     * @return a {@link java.util.List} object.
     * @throws main.frontEnd.Interface.ExceptionHandler if any.
     */
    public static List<String> retrieveFilesByType(List<String> arguments, EngineType type) throws ExceptionHandler {
        if (type == EngineType.DIR)
            if (arguments.size() != 1)
                throw new ExceptionHandler("Please enter one argument for this use case.");
            else
                return retrieveDirs(arguments);

        List<String> filePaths = new ArrayList<>();

        for (String in : arguments)
            filePaths.add(retrieveFilePath(in, type));

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

    public static UnitContainer createAssignInvokeUnitContainer(Unit currInstruction) {

        AssignInvokeUnitContainer unitContainer = new AssignInvokeUnitContainer();

        SootMethod method = ((JAssignStmt) currInstruction).getInvokeExpr().getMethod();
        if (method != null && method.isConcrete()) {

            Scene.v().forceResolve(method.getDeclaringClass().getName(), BODIES);

            HeuristicBasedInstructions returnInfluencingInstructions = new HeuristicBasedInstructions(method,
                    "return");

            List<UnitContainer> intraAnalysis = returnInfluencingInstructions.getAnalysisResult().getAnalysis();

//            System.out.println(intraAnalysis);

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
            unitContainer.setProperties(usedFields);
        }

        return unitContainer;
    }

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

    public static boolean isArgumentOfInvoke(Analysis analysis, int index,
                                             List<UnitContainer> outSet,
                                             Set<String> usedFields, InvokeUnitContainer analysisResult) {
        UnitContainer baseUnit = analysis.getAnalysisResult().get(index);

        if (baseUnit.getUnit() instanceof JInvokeStmt) {

            InvokeExpr invokeExpr = ((JInvokeStmt) baseUnit.getUnit()).getInvokeExpr();

            List<Value> args = invokeExpr.getArgs();

            for (int x = 0; x < args.size(); x++) {
                if (args.get(x) instanceof Constant) {

                    InvokeUnitContainer container = getDefinedFieldsFromInvoke(invokeExpr.getMethod(), usedFields);

                    analysisResult.getAnalysisResult().addAll(container.getAnalysisResult());
                    analysisResult.setDefinedFields(container.getDefinedFields());
                    analysisResult.setArgs(container.getArgs());
                    analysisResult.setUnit(baseUnit.getUnit());
                    return true;

                }
            }
        }


        outSet.add(analysis.getAnalysisResult().get(index));

        for (int i = index; i >= 0; i--) {

            UnitContainer curUnit = analysis.getAnalysisResult().get(i);

            List<UnitContainer> inset = new ArrayList<>();
            inset.addAll(outSet);

            for (UnitContainer insetIns : inset) {
                if (insetIns instanceof PropertyFakeUnitContainer) {
                    String property = ((PropertyFakeUnitContainer) insetIns).getOriginalProperty();

                    if (curUnit.getUnit() instanceof JInvokeStmt) {
                        if (curUnit.getUnit().toString().contains(property + ".<")) {
                            if (!outSet.toString().contains(curUnit.toString())) {
                                outSet.add(curUnit);
                            }
                        } else {

                            InvokeExpr invokeExpr = ((JInvokeStmt) curUnit.getUnit()).getInvokeExpr();

                            List<Value> args = invokeExpr.getArgs();

                            for (int x = 0; x < args.size(); x++) {
                                if (args.get(x).toString().contains(property)) {

                                    InvokeUnitContainer container = getDefinedFieldsFromInvoke(invokeExpr.getMethod(), usedFields);

                                    if (container.getArgs().contains(x)) {
                                        analysisResult.getAnalysisResult().addAll(container.getAnalysisResult());
                                        analysisResult.setDefinedFields(container.getDefinedFields());
                                        analysisResult.setArgs(container.getArgs());

                                    }

                                    analysisResult.setUnit(curUnit.getUnit());
                                    return true;
                                }
                            }
                        }

                    } else {
                        for (ValueBox useBox : curUnit.getUnit().getUseBoxes()) {
                            if (useBox.getValue().toString().contains(property)) {
                                if (!outSet.toString().contains(curUnit.toString())) {
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
                                if (!outSet.toString().contains(curUnit.toString())) {
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

                                for (int x = 0; x < args.size(); x++) {
                                    if (args.get(x).equivTo(defBox.getValue()) ||
                                            isArrayUseBox(curUnit, insetIns, defBox, args.get(x))) {

                                        InvokeUnitContainer container = getDefinedFieldsFromInvoke(invokeExpr.getMethod(), usedFields);

                                        if (container.getArgs().contains(x)) {
                                            analysisResult.getAnalysisResult().addAll(container.getAnalysisResult());
                                            analysisResult.setDefinedFields(container.getDefinedFields());
                                            analysisResult.setArgs(container.getArgs());
                                        }
                                        analysisResult.setUnit(curUnit.getUnit());
                                        return true;
                                    }
                                }
                            } else if (curUnit.getUnit().toString().contains(defBox + ".<")) {
                                if (!outSet.toString().contains(curUnit.toString())) {
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
                                    if (!outSet.toString().contains(curUnit.toString())) {
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
                                if (!outSet.toString().contains(curUnit.toString())) {
                                    outSet.add(curUnit);
                                }
                            } else {

                                InvokeExpr invokeExpr = ((JInvokeStmt) curUnit.getUnit()).getInvokeExpr();

                                List<Value> args = invokeExpr.getArgs();

                                for (int x = 0; x < args.size(); x++) {
                                    if (args.get(x).equivTo(defBox.getValue()) ||
                                            isArrayUseBox(curUnit, insetIns, defBox, args.get(x))) {

                                        InvokeUnitContainer container = getDefinedFieldsFromInvoke(invokeExpr.getMethod(), usedFields);
                                        if (container.getArgs().contains(x)) {
                                            analysisResult.getAnalysisResult().addAll(container.getAnalysisResult());
                                            analysisResult.setDefinedFields(container.getDefinedFields());
                                            analysisResult.setArgs(container.getArgs());
                                        }

                                        analysisResult.setUnit(curUnit.getUnit());
                                        return true;
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
                                    if (!outSet.toString().contains(curUnit.toString())) {
                                        outSet.add(curUnit);
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }

        return false;
    }

    private static boolean isArrayUseBox(UnitContainer curUnit, UnitContainer insetIns, ValueBox defBox, Value useBox) {
        return (defBox.getValue().toString().contains(useBox.toString())
                && curUnit.getMethod().equals(insetIns.getMethod())
                && useBox.getType() instanceof ArrayType);
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
}
