# Testing Reading Java Jar File


```Java
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.jar.JarEntry;
import java.util.zip.ZipFile;
import java.io.InputStream;
import java.nio.file.*;
import java.util.jar.JarInputStream;
import java.nio.file.Paths;

%maven org.apache.bcel:bcel:6.4.0
%maven commons-io:commons-io:2.4

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.io.IOUtils;

%maven org.ow2.asm:asm:8.0.1
%maven org.ow2.asm:asm-tree:8.0.1

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
```


```Java
public String getJarFile(String file) {
	return System.getProperty("user.dir") + "/" + file;
}
public String getJar() {
	return getJarFile("cryptoguard.jar");
}
```


```Java
public void print(Object obj) {
	System.out.println(obj);
}
```


```Java
String jarFile = getJar();
print(jarFile);

Manifest m = new JarFile(jarFile).getManifest();

var main = m.getMainAttributes();

for (var key:main.keySet())
	print(key);

var entry = main.getValue("Main-Class");

print(entry);


//JavaClass sample = Repository.lookupClass(entry);
```

    /home/maister/cryptoguard.jar
    Manifest-Version
    Main-Class
    frontEnd.Interface.EntryPoint



```Java
public static InputStream raw(String zipFilePath, String relativeFilePath) {
    try {
        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration<? extends ZipEntry> e = zipFile.entries();

        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
			//print(entry.getName());
            // if the entry is not directory and matches relative file then extract it
            if (!entry.isDirectory() && entry.getName().equals(relativeFilePath)) {
                //BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				return zipFile.getInputStream(entry);
                // Read the file
                    // With Apache Commons I/O
                 //String fileContentsStr = IOUtils.toString(bis, "UTF-8");

                    // With Guava
                //String fileContentsStr = new String(ByteStreams.toByteArray(bis),Charsets.UTF_8);
                // close the input stream.
                //bis.close();
                //return fileContentsStr;
            } else {
                continue;
            }
        }
    } catch (IOException e) {
        print("IOError :" + e);
        e.printStackTrace();
    }
    return null;
}
```


```Java
public ClassNode readJarPath(Path path, String classname) throws IOException {
	//Application application = new Application();
	print("Looking for class: "+classname);
	try (JarInputStream in = new JarInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
		JarEntry entry;
		while ((entry = in.getNextJarEntry()) != null) {
			String name = entry.getName();
			if (!name.endsWith(".class")) continue;
			else if (!name.contains(classname)) continue;

			name = name.replaceAll(".class$", "");

			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(in);
			reader.accept(node, ClassReader.SKIP_DEBUG);
			return node;

			//application.classes.put(name, node);
		}
	}
	return null;
	//return application;
}
public ClassNode readJarNode(String path, String classname) throws IOException {
	return readJarPath(new File(path).toPath(),classname.replace(".","/")+".class");
}
```


```Java
//readZipFile(jarFile, entry)
var contents = raw(jarFile, entry.replace(".","/")+".class");

//JavaClass sample = Repository.lookupClass(entry);
print(contents == null);
print(contents.getClass().getName());

ClassReader read = new ClassReader(contents);
```

    false
    java.util.zip.ZipFile$ZipFileInflaterInputStream



```Java
print(read.getClassName());
for (String in:read.getInterfaces())
	print(in);
print(read.getAccess());

ClassNode node = new ClassNode();
read.accept(node, ClassReader.SKIP_DEBUG);
```


    ---------------------------------------------------------------------------

    java.lang.NullPointerException: null

    	at .(#103:1)



```Java
print(jarFile);
ClassNode klass = readJarNode(jarFile, entry);
```

    /home/maister/cryptoguard.jar
    Looking for class: frontEnd/Interface/EntryPoint.class



```Java
print(klass.name);
for (var mtd:klass.methods)
	if (mtd.name.equals("main"))
		for (int ktr=0;ktr<mtd.instructions.size();ktr++)
			print(mtd.instructions.get(ktr));
```

    frontEnd/Interface/EntryPoint
    org.objectweb.asm.tree.VarInsnNode@36db219c
    org.objectweb.asm.tree.MethodInsnNode@381cc937
    org.objectweb.asm.tree.VarInsnNode@3c234aec
    org.objectweb.asm.tree.FieldInsnNode@7921c30c
    org.objectweb.asm.tree.LdcInsnNode@1699da75
    org.objectweb.asm.tree.MethodInsnNode@4812fce0
    org.objectweb.asm.tree.VarInsnNode@6e40bb24
    org.objectweb.asm.tree.FieldInsnNode@190e3c43
    org.objectweb.asm.tree.MethodInsnNode@419c1765
    org.objectweb.asm.tree.MethodInsnNode@1e36164a
    org.objectweb.asm.tree.JumpInsnNode@75441733
    org.objectweb.asm.tree.InsnNode@1d40c1fc
    org.objectweb.asm.tree.JumpInsnNode@3cc6da0e
    org.objectweb.asm.tree.LabelNode@12ec0f9a
    org.objectweb.asm.tree.FrameNode@47c47f7b
    org.objectweb.asm.tree.InsnNode@5c90095e
    org.objectweb.asm.tree.LabelNode@560cae68
    org.objectweb.asm.tree.FrameNode@24eeebec
    org.objectweb.asm.tree.VarInsnNode@403577ad
    org.objectweb.asm.tree.LabelNode@1cb34c0c
    org.objectweb.asm.tree.VarInsnNode@2fffce1c
    org.objectweb.asm.tree.MethodInsnNode@7c1de184
    org.objectweb.asm.tree.VarInsnNode@17fe69b5
    org.objectweb.asm.tree.FieldInsnNode@112b846a
    org.objectweb.asm.tree.VarInsnNode@40f5377d
    org.objectweb.asm.tree.MethodInsnNode@53f7ff26
    org.objectweb.asm.tree.MethodInsnNode@3720972c
    org.objectweb.asm.tree.VarInsnNode@7ae55d76
    org.objectweb.asm.tree.JumpInsnNode@13c2c388
    org.objectweb.asm.tree.FieldInsnNode@7fa00019
    org.objectweb.asm.tree.MethodInsnNode@4c8a6136
    org.objectweb.asm.tree.MethodInsnNode@1cd49b3c
    org.objectweb.asm.tree.MethodInsnNode@190fd5ad
    org.objectweb.asm.tree.LabelNode@66d0ae5d
    org.objectweb.asm.tree.FrameNode@79cce060
    org.objectweb.asm.tree.JumpInsnNode@41f9991e
    org.objectweb.asm.tree.LabelNode@26bc8e31
    org.objectweb.asm.tree.FrameNode@457d7efd
    org.objectweb.asm.tree.VarInsnNode@5bac49d2
    org.objectweb.asm.tree.VarInsnNode@56100ad0
    org.objectweb.asm.tree.MethodInsnNode@149fcb25
    org.objectweb.asm.tree.VarInsnNode@3a3bd5dd
    org.objectweb.asm.tree.JumpInsnNode@360e991
    org.objectweb.asm.tree.VarInsnNode@55f035a7
    org.objectweb.asm.tree.MethodInsnNode@3fdc1adf
    org.objectweb.asm.tree.MethodInsnNode@5bada5c4
    org.objectweb.asm.tree.MethodInsnNode@5f05e341
    org.objectweb.asm.tree.MethodInsnNode@b8ca80b
    org.objectweb.asm.tree.LabelNode@54d94147
    org.objectweb.asm.tree.FrameNode@626c4d9d
    org.objectweb.asm.tree.InsnNode@1283b57a

