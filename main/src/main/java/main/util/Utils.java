package main.util;

import main.analyzer.backward.UnitContainer;
import main.util.manifest.ProcessManifest;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import soot.Scene;
import soot.SootClass;
import soot.Unit;
import soot.ValueBox;
import soot.util.Chain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Utils
{

	public static List<String> getClassNamesFromJarArchive(String jarPath) throws IOException
	{
		List<String> classNames = new ArrayList<>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if (!entry.isDirectory() && entry.getName().endsWith(".class"))
			{
				String className = entry.getName().replace('/', '.');
				classNames.add(className.substring(0, className.length() - ".class".length()));
			}
		}
		return classNames;
	}

	public static String getBasePackageNameFromApk(String apkPath) throws IOException
	{
		ProcessManifest processManifest = new ProcessManifest();
		processManifest.loadManifestFile(apkPath);
		return processManifest.getPackageName();
	}

	public static String getBasePackageNameFromJar(String jarPath, boolean isMain) throws IOException
	{

		ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));

		List<String> basePackages = new ArrayList<>();

		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if (!entry.isDirectory() && entry.getName().endsWith(".class"))
			{
				String className = entry.getName().replace('/', '.');
				className = className.substring(0, className.length() - ".class".length());

				String[] splits = className.split("\\.");
				StringBuilder basePackage = new StringBuilder();

				if (splits.length > 3)
				{ // assumption package structure is org.apache.xyz.main
					basePackage.append(splits[0])
							.append(".")
							.append(splits[1])
							.append(".")
							.append(splits[2]);
				}
				else if (splits.length == 3)
				{
					basePackage.append(splits[0])
							.append(".")
							.append(splits[1]);
				}
				else
				{
					basePackage.append(splits[0]);
				}

				String basePackageStr = basePackage.toString();

				if (!basePackages.toString().contains(basePackageStr))
				{
					basePackages.add(basePackageStr);
				}
			}
		}

		if (basePackages.size() == 1)
		{
			return basePackages.get(0);
		}
		else if (basePackages.size() > 1)
		{

//            if (isMain) {
//                System.out.println("***Multiple Base packages of " + jarPath + " : " + basePackages.toString());
//            }

			for (String basePackage : basePackages)
			{
				if (basePackage.split("\\.").length > 2 && jarPath.contains(basePackage.split("\\.")[2]))
				{
					return basePackage;
				}
			}
		}

		return null;

	}

	public static List<String> getClassNamesFromApkArchive(String apkfile) throws IOException
	{
		List<String> classNames = new ArrayList<>();

		File zipFile = new File(apkfile);

		DexFile dexFile = DexFileFactory.loadDexEntry(zipFile, "classes.dex", true, Opcodes.forApi(23));

		for (ClassDef classDef : dexFile.getClasses())
		{
			String className = classDef.getType().replace('/', '.');
			if (!className.contains("android."))
			{
				classNames.add(className.substring(1, className.length() - 1));
			}
		}

		return classNames;
	}

	public static String buildSootClassPath(String... paths)
	{
		return buildSootClassPath(Arrays.asList(paths));
	}

	public static String buildSootClassPath(List<String> paths)
	{

		StringBuilder classPath = new StringBuilder();

		for (String path : paths)
		{

			if (path.endsWith(".jar"))
			{
				classPath.append(path);
				classPath.append(":");
			}
			else
			{
				File dir = new File(path);

				if (dir.isDirectory())
				{
					File[] files = dir.listFiles();

					if (files == null)
					{
						continue;
					}

					for (File file : files)
					{
						if (file.getName().endsWith(".jar"))
						{
							classPath.append(file.getAbsolutePath());
							classPath.append(":");
						}
					}
				}
			}
		}

		return classPath.toString();
	}

	public static List<String> getJarsInDirectory(String path)
	{

		List<String> jarFiles = new ArrayList<>();
		File dir = new File(path);

		if (dir.isDirectory())
		{
			File[] files = dir.listFiles();

			if (files == null)
			{
				return jarFiles;
			}

			for (File file : files)
			{
				if (file.getName().endsWith(".jar"))
				{
					jarFiles.add(file.getAbsolutePath());
				}
			}
		}

		return jarFiles;
	}

	public static Map<String, List<SootClass>> getClassHierarchyAnalysis(List<String> classNames)
	{

		Map<String, List<SootClass>> classHierarchyMap = new HashMap<>();

		for (String className : classNames)
		{

			SootClass sClass = Scene.v().getSootClass(className);
			Chain<SootClass> parents = sClass.getInterfaces();

			if (sClass.hasSuperclass())
			{
				SootClass superClass = sClass.getSuperclass();

				List<SootClass> childList = classHierarchyMap.get(superClass.getName());

				if (childList == null)
				{
					childList = new ArrayList<>();
					classHierarchyMap.put(superClass.getName(), childList);
				}

				if (childList.isEmpty())
				{
					childList.add(superClass);
				}
				childList.add(sClass);
			}

			for (SootClass parent : parents)
			{
				List<SootClass> childList = classHierarchyMap.get(parent.getName());

				if (childList == null)
				{
					childList = new ArrayList<>();
					classHierarchyMap.put(parent.getName(), childList);
				}

				if (childList.isEmpty())
				{
					childList.add(parent);
				}
				childList.add(sClass);
			}
		}

		return classHierarchyMap;
	}

	public static Map<String, String> getXmlFiles(String projectJarPath, List<String> excludes) throws IOException
	{
		Map<String, String> fileStrs = new HashMap<>();

		if (new File(projectJarPath).isDirectory())
		{
			return fileStrs;
		}

		List<String> fileNames = getXmlFileNamesFromJarArchive(projectJarPath, excludes);

		for (String fileName : fileNames)
		{
			InputStream stream = readFileFromZip(projectJarPath, fileName);
			fileStrs.put(fileName, convertStreamToString(stream));
		}

		return fileStrs;
	}

	private static List<String> getXmlFileNamesFromJarArchive(String jarPath, List<String> excludes) throws IOException
	{
		List<String> classNames = new ArrayList<>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			for (String exclude : excludes)
			{
				if (!entry.isDirectory() && entry.getName().endsWith(".xml") && !entry.getName().endsWith(exclude))
				{
					String className = entry.getName();
					classNames.add(className);
				}
			}
		}
		return classNames;
	}

	private static InputStream readFileFromZip(String jarPath, String file) throws IOException
	{
		ZipFile zipFile = new ZipFile(jarPath);
		ZipEntry entry = zipFile.getEntry(file);
		return zipFile.getInputStream(entry);
	}

	private static String convertStreamToString(java.io.InputStream is)
	{
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static List<Integer> findInfluencingParamters(List<UnitContainer> analysisResult)
	{
		List<Integer> influencingParam = new ArrayList<>();

		for (int index = analysisResult.size() - 1; index >= 0; index--)
		{
			UnitContainer unit = analysisResult.get(index);

			for (ValueBox useBox : unit.getUnit().getUseBoxes())
			{
				String useboxStr = useBox.getValue().toString();
				if (useboxStr.contains("@parameter"))
				{
					Integer param = Integer.valueOf(useboxStr.substring("@parameter".length(), useboxStr.indexOf(':')));
					influencingParam.add(param);
				}
			}
		}

		return influencingParam;
	}

	public static boolean isSpecialInvokeOn(Unit currInstruction, String usebox)
	{
		return currInstruction.toString().contains("specialinvoke")
				&& currInstruction.toString().contains(usebox + ".<");
	}

	public static List<File> listf(String directoryName)
	{
		File directory = new File(directoryName);

		List<File> resultList = new ArrayList<File>();

		File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (File file : fList)
		{
			if (file.isFile())
			{
			}
			else if (file.isDirectory())
			{
				resultList.addAll(listf(file.getAbsolutePath()));
			}
		}

		return resultList;
	}

	public static List<String> getClassNamesFromSnippet(List<String> sourcePaths)
	{

		List<String> classNames = new ArrayList<>();

		for (String sourcePath : sourcePaths)
		{

			List<File> files = listf(sourcePath);

			if (files == null)
			{
				return classNames;
			}

			for (File file : files)
			{
				String name = file.getAbsolutePath();
				if (name.endsWith(".java"))
				{
					String className = name.substring(sourcePath.length() + 1, name.length() - 5);
					classNames.add(className.replaceAll("/", "."));
				}
			}
		}

		return classNames;
	}
}
