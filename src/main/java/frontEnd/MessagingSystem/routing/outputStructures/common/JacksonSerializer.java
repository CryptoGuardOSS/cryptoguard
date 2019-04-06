package frontEnd.MessagingSystem.routing.outputStructures.common;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import frontEnd.Interface.ExceptionHandler;
import frontEnd.Interface.ExceptionId;

import java.io.File;
import java.io.IOException;

/**
 * @author RigorityJTeam
 * Created on 4/6/19.
 * @since {VersionHere}
 *
 * <p>{Description Here}</p>
 */
public class JacksonSerializer {

    public static String serializeXML(Object obj, Boolean prettyPrint) throws ExceptionHandler {
        XmlMapper serializer = new XmlMapper();
        serializer.setSerializationInclusion(Include.NON_EMPTY);
        if (prettyPrint)
            serializer.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return serializer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new ExceptionHandler("Error marshalling output", ExceptionId.MAR_VAR);
        }

    }

    public static void serializeXML(Object obj, Boolean prettyPrint, File file) throws ExceptionHandler {
        XmlMapper serializer = new XmlMapper();
        serializer.setSerializationInclusion(Include.NON_EMPTY);
        if (prettyPrint)
            serializer.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            serializer.writeValue(file, obj);
        } catch (IOException e) {
            throw new ExceptionHandler("Error writing output", ExceptionId.FILE_O);
        }

    }

    public static String serializeJson(Object obj, Boolean prettyPrint) throws ExceptionHandler {
        ObjectMapper serializer = new ObjectMapper();
        serializer.setSerializationInclusion(Include.NON_NULL);
        if (prettyPrint)
            serializer.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return serializer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new ExceptionHandler("Error marshalling output", ExceptionId.MAR_VAR);
        }

    }

    public static void serializeJson(Object obj, Boolean prettyPrint, File file) throws ExceptionHandler {
        ObjectMapper serializer = new ObjectMapper();
        serializer.setSerializationInclusion(Include.NON_NULL);
        if (prettyPrint)
            serializer.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            serializer.writeValue(file, obj);
        } catch (IOException e) {
            throw new ExceptionHandler("Error writing output", ExceptionId.FILE_O);
        }

    }

}
