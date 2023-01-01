package com.demo;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Post;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class FileUpload {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

    @POST("/upload")
    public String sendMultipart(
            @RestForm @PartType(MediaType.APPLICATION_OCTET_STREAM) File file,
            @RestForm @PartType(MediaType.TEXT_PLAIN) String text) {

        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];

        try
        {
            //Read bytes with InputStream
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

            for (int i = 0; i < bFile.length; i++)
            {
                System.out.print((char) bFile[i]);
                //writeFile(bFile[i], file.getName());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @POST("/sliceUpload")
    public String sendMultipart(@RestForm @PartType(MediaType.APPLICATION_OCTET_STREAM) File file) {

    }

}