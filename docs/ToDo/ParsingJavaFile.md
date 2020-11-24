# Parsing Java File
---
* [JavaParser](https://javaparser.org/)
* [Maven Search](https://search.maven.org/search?q=com.github.javaparser)
* [Searched](https://github.com/javaparser/javaparser/blob/69a2e0ed4e0f260ccd61cbb2594b2c3069e47990/javaparser-core/src/main/java/com/github/javaparser/ast/CompilationUnit.java)
* [StackOverflow Search](https://stackoverflow.com/questions/2206065/java-parse-java-source-code-extract-methods)


```Java
%maven com.github.javaparser:javaparser-core:3.16.1

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.StaticJavaParser;

import java.util.Optional;
```


```Java
public void print(String in) {
	System.out.println(in);
}
```

## Retrieving Java Annotations
### Original
```java
public Optional<AnnotationDeclaration> getAnnotationDeclarationByName(String annotationName) {
        return getTypes().stream().filter(type -> type.getNameAsString().equals(annotationName) && type instanceof AnnotationDeclaration).findFirst().map(t -> (AnnotationDeclaration) t);
    }
```

### Modified
```java
//TODO - Need to determine if there is a list of AnnotationDeclarations

public Optional<AnnotationDeclaration> getAnnotationDeclarationByName() {
        return getTypes().stream().filter(type ->type instanceof AnnotationDeclaration).findFirst().map(t -> (AnnotationDeclaration) t);
    }
```


```Java
String path = System.getProperty("user.dir") + "/" + "test.java";
print(path);
```

    /home/maister/test.java



```Java
CompilationUnit cu = StaticJavaParser.parse(new File(path));
```


```Java
String test = cu.getPackageDeclaration().orElse(null)
	!= null 
	? cu.getPackageDeclaration().get().getName().asString() 
	: "null";

print(test);
```

    null

