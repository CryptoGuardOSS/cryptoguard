package util;

import frontEnd.Interface.outputRouting.ExceptionHandler;
import frontEnd.Interface.outputRouting.ExceptionId;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>MvnPomFileParser class.</p>
 *
 * @author RigorityJTeam
 * @version $Id: $Id
 * @since V01.00.00
 */
public class MvnPomFileParser implements BuildFileParser {

    Map<String, String> moduleVsPath = new HashMap<>();
    @Getter
    @Setter
    String projectName;
    @Getter
    @Setter
    String projectVersion;

    public Boolean isGradle() {
        return false;
    }

    /**
     * <p>Constructor for MvnPomFileParser.</p>
     *
     * @param fileName a {@link java.lang.String} object.
     * @throws frontEnd.Interface.outputRouting.ExceptionHandler if any.
     */
    public MvnPomFileParser(String fileName) throws ExceptionHandler {

        try {

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


            String groupId = document.getElementsByTagName("groupId").item(0).getNodeValue();
            String artifactId = document.getElementsByTagName("artifactId").item(0).getNodeValue();
            projectName = StringUtils.trimToNull(groupId) + ":" + StringUtils.trimToNull(artifactId);


            projectVersion = StringUtils.trimToNull(document.getElementsByTagName("version").item(0).getNodeValue());


        } catch (ParserConfigurationException e) {
            throw new ExceptionHandler("Error creating file parser", ExceptionId.FILE_CON);
        } catch (org.xml.sax.SAXException | java.io.IOException e) {
            throw new ExceptionHandler("Error parsing " + fileName, ExceptionId.FILE_O);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getDependencyList() throws ExceptionHandler {

        String currentModule = "";
        try {

            Map<String, List<String>> moduleVsDependencies = new HashMap<>();

            for (String module : moduleVsPath.keySet()) {
                currentModule = module;

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

        } catch (ParserConfigurationException e) {
            throw new ExceptionHandler("Error creating file parser", ExceptionId.FILE_CON);
        } catch (javax.xml.xpath.XPathExpressionException e) {
            throw new ExceptionHandler("Error parsing artifacts from" + currentModule + "/pom.xml", ExceptionId.FILE_READ);
        } catch (org.xml.sax.SAXException | java.io.IOException e) {
            throw new ExceptionHandler("Error parsing " + currentModule + "/pom.xml", ExceptionId.FILE_READ);
        }

    }


    private void calcAlldependenciesForModule(String module, Map<String, List<String>> mVsds, List<String> dependencyPaths) {
        for (String dependency : mVsds.get(module)) {
            dependencyPaths.add(moduleVsPath.get(dependency) + "/src/main/java");
            calcAlldependenciesForModule(dependency, mVsds, dependencyPaths);
        }
    }
}
