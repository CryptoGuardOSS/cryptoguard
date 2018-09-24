package main.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MvnPomFileParser implements BuildFileParser {

    Map<String, String> moduleVsPath = new HashMap<>();

    public MvnPomFileParser(String fileName) throws Exception {

        File xmlFile = new File(fileName);
        DocumentBuilderFactory docbuildFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docbuildFactory.newDocumentBuilder();
        Document document = docBuilder.parse(xmlFile);

        NodeList nodeList = document.getElementsByTagName("module");

        String[] splits = fileName.split("/");
        String projectName = splits[splits.length - 2];
        String projectRoot = fileName.substring(0, fileName.lastIndexOf('/'));

        if (nodeList.getLength() == 0) {
            moduleVsPath.put(projectName, projectRoot);
        } else {

            for (int i = 0; i < nodeList.getLength(); i++) {
                String moduleName = nodeList.item(i).getTextContent();
                moduleVsPath.put(moduleName, projectRoot + "/" + moduleName);
            }
        }
    }

    @Override
    public Map<String, List<String>> getDependencyList() throws Exception {

        Map<String, List<String>> moduleVsDependencies = new HashMap<>();

        for (String module : moduleVsPath.keySet()) {

            File xmlFile = new File(moduleVsPath.get(module) + "/pom.xml");
            DocumentBuilderFactory docbuildFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docbuildFactory.newDocumentBuilder();
            Document document = docBuilder.parse(xmlFile);

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/project/dependencies/dependency/artifactId")
                    .evaluate(document, XPathConstants.NODESET);

            List<String> dependencies = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                String dependency = nodeList.item(i).getTextContent();

                if (moduleVsPath.keySet().contains(dependency)) {
                    dependencies.add(dependency);
                }
            }

            moduleVsDependencies.put(module, dependencies);
        }

        Map<String, List<String>> moduleVsDependencyPaths = new HashMap<>();

        for (String module : moduleVsDependencies.keySet()) {
            List<String> dependencyPaths = new ArrayList<>();
            calcAlldependenciesForModule(module, moduleVsDependencies, dependencyPaths);
            dependencyPaths.add(moduleVsPath.get(module) + "/src/main/java");
            moduleVsDependencyPaths.put(module, dependencyPaths);
        }

        return moduleVsDependencyPaths;

    }


    private void calcAlldependenciesForModule(String module, Map<String, List<String>> mVsds, List<String> dependencyPaths) {
        for (String dependency : mVsds.get(module)) {
            dependencyPaths.add(moduleVsPath.get(dependency) + "/src/main/java");
            calcAlldependenciesForModule(dependency, mVsds, dependencyPaths);
        }
    }

    public static void main(String[] args) throws Exception {
        MvnPomFileParser pomFileParser = new MvnPomFileParser("/home/krishnokoli/projects/mvn-sample/pom.xml");
        System.out.println(pomFileParser.getDependencyList());
    }
}
