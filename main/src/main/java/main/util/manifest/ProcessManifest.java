/*******************************************************************************
 * Copyright (c) 2012 Secure Software Engineering Group at EC SPRIDE.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors: Christian Fritz, Steven Arzt, Siegfried Rasthofer, Eric
 * Bodden, and others.
 ******************************************************************************/
package main.util.manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;

import test.AXMLPrinter;
import android.content.res.AXmlResourceParser;

public class ProcessManifest {

    private final Set<String> entryPointsClasses = new HashSet<String>();
    private String applicationName = "";

    private int versionCode = -1;
    private String versionName = "";

    private String packageName = "";
    private int minSdkVersion = -1;
    private int targetSdkVersion = -1;

    private final Set<String> permissions = new HashSet<String>();

    /**
     * Opens the given apk file and provides the given handler with a stream for
     * accessing the contained android manifest file
     * @param apk The apk file to process
     * @param handler The handler for processing the apk file
     *
     * @author Steven Arzt
     */
    private void handleAndroidManifestFile(String apk, IManifestHandler handler) {
        File apkF = new File(apk);
        if (!apkF.exists())
            throw new RuntimeException("file '" + apk + "' does not exist!");

        boolean found = false;
        try {
            ZipFile archive = null;
            try {
                archive = new ZipFile(apkF);
                Enumeration<?> entries = archive.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    String entryName = entry.getName();
                    // We are dealing with the Android manifest
                    if (entryName.equals("AndroidManifest.xml")) {
                        found = true;
                        handler.handleManifest(archive.getInputStream(entry));
                        break;
                    }
                }
            }
            finally {
                if (archive != null)
                    archive.close();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "Error when looking for manifest in apk: " + e);
        }
        if (!found)
            throw new RuntimeException("No manifest file found in apk");
    }

    public void loadManifestFile(String apk) {
        handleAndroidManifestFile(apk, new IManifestHandler() {

            @Override
            public void handleManifest(InputStream stream) {
                loadClassesFromBinaryManifest(stream);
            }

        });
    }

    protected void loadClassesFromBinaryManifest(InputStream manifestIS) {
        try {
            AXmlResourceParser parser = new AXmlResourceParser();
            parser.open(manifestIS);

            int type = -1;
            boolean applicationEnabled = true;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if (tagName.equals("manifest")) {
                            this.packageName = getAttributeValue(parser, "package");
                            String versionCode = getAttributeValue(parser, "versionCode");
                            if (versionCode != null && versionCode.length() > 0)
                                this.versionCode = Integer.valueOf(versionCode);
                            this.versionName = getAttributeValue(parser, "versionName");
                        }
                        else if (tagName.equals("activity")
                                || tagName.equals("receiver")
                                || tagName.equals("service")
                                || tagName.equals("provider")) {
                            // We ignore disabled activities
                            if (!applicationEnabled)
                                continue;
                            String attrValue = getAttributeValue(parser, "enabled");
                            if (attrValue != null && attrValue.equals("false"))
                                continue;

                            // Get the class name
                            attrValue = getAttributeValue(parser, "name");
                            entryPointsClasses.add(expandClassName(attrValue));
                        }
                        else if (tagName.equals("uses-permission")) {
                            String permissionName = getAttributeValue(parser, "name");
                            // We probably don't want to do this in some cases, so leave it
                            // to the user
                            // permissionName = permissionName.substring(permissionName.lastIndexOf(".") + 1);
                            this.permissions.add(permissionName);
                        }
                        else if (tagName.equals("uses-sdk")) {
                            String minVersion = getAttributeValue(parser, "minSdkVersion");
                            if (minVersion != null && minVersion.length() > 0)
                                this.minSdkVersion = Integer.valueOf(minVersion);
                            String targetVersion = getAttributeValue(parser, "targetSdkVersion");
                            if (targetVersion != null && targetVersion.length() > 0)
                                this.targetSdkVersion = Integer.valueOf(targetVersion);
                        }
                        else if (tagName.equals("application")) {
                            // Check whether the application is disabled
                            String attrValue = getAttributeValue(parser, "enabled");
                            applicationEnabled = (attrValue == null || !attrValue.equals("false"));

                            // Get the application name which is also the fully-qualified
                            // name of the custom application object
                            this.applicationName = getAttributeValue(parser, "name");
                            if (this.applicationName != null && !this.applicationName.isEmpty())
                                this.entryPointsClasses.add(expandClassName(this.applicationName));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a full class name from a short class name by appending the
     * globally-defined package when necessary
     * @param className The class name to expand
     * @return The expanded class name for the given short name
     */
    private String expandClassName(String className) {
        if (className.startsWith("."))
            return this.packageName + className;
        else if (className.substring(0, 1).equals(className.substring(0, 1).toUpperCase()))
            return this.packageName + "." + className;
        else
            return className;
    }

    private String getAttributeValue(AXmlResourceParser parser, String attributeName) {
        for (int i = 0; i < parser.getAttributeCount(); i++)
            if (parser.getAttributeName(i).equals(attributeName))
                return AXMLPrinter.getAttributeValue(parser, i);
        return "";
    }

    protected void loadClassesFromTextManifest(InputStream manifestIS) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(manifestIS);

            Element rootElement = doc.getDocumentElement();
            this.packageName = rootElement.getAttribute("package");
            String versionCode = rootElement.getAttribute("android:versionCode");
            if (versionCode != null && versionCode.length() > 0)
                this.versionCode = Integer.valueOf(versionCode);
            this.versionName = rootElement.getAttribute("android:versionName");

            NodeList appsElement = rootElement.getElementsByTagName("application");
            if (appsElement.getLength() > 1)
                throw new RuntimeException("More than one application tag in manifest");
            for (int appIdx = 0; appIdx < appsElement.getLength(); appIdx++) {
                Element appElement = (Element) appsElement.item(appIdx);

                this.applicationName = appElement.getAttribute("android:name");
                if (this.applicationName != null && !this.applicationName.isEmpty())
                    this.entryPointsClasses.add(expandClassName(this.applicationName));

                NodeList activities = appElement.getElementsByTagName("activity");
                NodeList receivers = appElement.getElementsByTagName("receiver");
                NodeList services  = appElement.getElementsByTagName("service");

                for (int i = 0; i < activities.getLength(); i++) {
                    Element activity = (Element) activities.item(i);
                    loadManifestEntry(activity, "android.app.Activity", this.packageName);
                }
                for (int i = 0; i < receivers.getLength(); i++) {
                    Element receiver = (Element) receivers.item(i);
                    loadManifestEntry(receiver, "android.content.BroadcastReceiver", this.packageName);
                }
                for (int i = 0; i < services.getLength(); i++) {
                    Element service = (Element) services.item(i);
                    loadManifestEntry(service, "android.app.Service", this.packageName);
                }

                NodeList permissions = appElement.getElementsByTagName("uses-permission");
                for (int i = 0; i < permissions.getLength(); i++) {
                    Element permission = (Element) permissions.item(i);
                    this.permissions.add(permission.getAttribute("android:name"));
                }

                NodeList usesSdkList = appElement.getElementsByTagName("uses-sdk");
                for (int i = 0; i < usesSdkList.getLength(); i++) {
                    Element usesSdk = (Element) usesSdkList.item(i);
                    String minVersion = usesSdk.getAttribute("android:minSdkVersion");
                    if (minVersion != null && minVersion.length() > 0)
                        this.minSdkVersion = Integer.valueOf(minVersion);
                    String targetVersion = usesSdk.getAttribute("android:targetSdkVersion");
                    if (targetVersion != null && targetVersion.length() > 0)
                        this.targetSdkVersion = Integer.valueOf(targetVersion);
                }
            }
        }
        catch (IOException ex) {
            System.err.println("Could not parse manifest: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            System.err.println("Could not parse manifest: " + ex.getMessage());
            ex.printStackTrace();
        } catch (SAXException ex) {
            System.err.println("Could not parse manifest: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadManifestEntry(Element activity, String baseClass, String packageName) {
        if (activity.getAttribute("android:enabled").equals("false"))
            return;

        String className = activity.getAttribute("android:name");
        entryPointsClasses.add(expandClassName(className));
    }

    public void setApplicationName(String name) {
        this.applicationName = name;
    }

    public void setPackageName(String name) {
        this.packageName = name;
    }

    public Set<String> getEntryPointClasses() {
        return this.entryPointsClasses;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getMinSdkVersion() {
        return this.minSdkVersion;
    }

    public int targetSdkVersion() {
        return this.targetSdkVersion;
    }

}

