package main.util;

import main.analyzer.backward.*;
import main.slicer.backward.heuristic.HeuristicBasedAnalysisResult;
import main.slicer.backward.heuristic.HeuristicBasedInstructions;
import main.slicer.backward.orthogonal.OrthogonalInfluenceInstructions;
import main.slicer.backward.orthogonal.OrthogonalSlicingResult;
import main.util.manifest.ProcessManifest;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import soot.*;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.util.Chain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static soot.SootClass.BODIES;

public class Utils {


    public static int DEPTH = 0;

    public static int NUM_ORTHOGONAL = 0;
    public static int NUM_CONSTS_TO_CHECK = 0;
    public static int NUM_SLICES = 0;
    public static int NUM_HEURISTIC = 0;
    public static final ArrayList<Integer> SLICE_LENGTH = new ArrayList<>();
    public static int[] DEPTH_COUNT;

    public static void initDepth(int depth) {
        DEPTH = depth;
        DEPTH_COUNT = new int[depth];
    }


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

    public static List<String> getClassNamesFromApkArchive(String apkfile) throws IOException {
        List<String> classNames = new ArrayList<>();

        File zipFile = new File(apkfile);

        DexFile dexFile = DexFileFactory.loadDexEntry(zipFile, "classes.dex", true, Opcodes.forApi(23));

        for (ClassDef classDef : dexFile.getClasses()) {
            String className = classDef.getType().replace('/', '.');
            if (!className.contains("android."))
                classNames.add(className.substring(1, className.length() - 1));
        }

        return classNames;
    }

    public static String buildSootClassPath(String... paths) {
        return buildSootClassPath(Arrays.asList(paths));
    }

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

    public static boolean isSpecialInvokeOn(Unit currInstruction, String usebox) {
        return currInstruction.toString().contains("specialinvoke")
                && currInstruction.toString().contains(usebox + ".<");
    }

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

    private static final List<String> ASSIGN_DONT_VISIT = new ArrayList<>();
    private static final List<String> INVOKE_DONT_VISIT = new ArrayList<>();

    static {
        ASSIGN_DONT_VISIT.add("<java.util.Map: java.lang.Object get(java.lang.Object)>");

        INVOKE_DONT_VISIT.add("<java.util.Map: java.lang.Object put(java.lang.Object,java.lang.Object)>");
        INVOKE_DONT_VISIT.add("java.lang.String: void <init>");
    }

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

    public static UnitContainer isArgumentOfInvoke(Analysis analysis, int index,
                                                   List<UnitContainer> outSet) {

        NUM_CONSTS_TO_CHECK++;

        UnitContainer baseUnit = analysis.getAnalysisResult().get(index);

        if (baseUnit.getUnit() instanceof JInvokeStmt) {

            InvokeExpr invokeExpr = ((JInvokeStmt) baseUnit.getUnit()).getInvokeExpr();

            List<Value> args = invokeExpr.getArgs();

            for (int x = 0; x < args.size(); x++) {
                if (args.get(x) instanceof Constant) {
                    return baseUnit;
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
                                    return curUnit;
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

                                        return curUnit;
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

        return null;
    }

    private static boolean isArrayUseBox(UnitContainer curUnit, UnitContainer insetIns, ValueBox defBox, Value useBox) {
        return (defBox.getValue().toString().contains(useBox.toString())
                && curUnit.getMethod().equals(insetIns.getMethod())
                && useBox.getType() instanceof ArrayType);
    }

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
}
