package com.demo.services;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;

/**
 *
 */
@ApplicationScoped
@Produces(MediaType.TEXT_HTML)
@Path("files.html")
public class FileUpload {

    @Location("files.html")
    Template template;

    @GET
    public TemplateInstance get() {
        return template.instance();
    }

    /*@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }*/

    public class FormDto {
        @RestForm
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public File file;

        @FormParam("otherField")
        @PartType(MediaType.TEXT_PLAIN)
        public String textProperty;
    }

    //@RestForm File file, @RestForm String text

    /**
     *
     * @param file
     * @param text
     * @return
     */
    @POST
    //@Path("/binary")
    public String sendMultipart(@RestForm @PartType(MediaType.APPLICATION_OCTET_STREAM) File file, @RestForm @PartType(MediaType.TEXT_PLAIN) String text) {
        try {
            System.out.println(file.getPath());
            System.out.println(file.getCanonicalPath());
            System.out.println(file.toPath());
            System.out.println(file.exists());
            System.out.println(file.createNewFile());
            System.out.println(file.getName());
            System.out.println(file.length());
            System.out.println(file.createNewFile());
            System.out.println("file.toURI()");
            System.out.println(file.toURI());
            System.out.println(file.hashCode());
            System.out.println("----------");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

        /*try(FileReader fileStream = new FileReader( file );
            BufferedReader bufferedReader = new BufferedReader( fileStream ) ) {

            String line = null;

            while( (line = bufferedReader.readLine()) != null ) {
                //do something with line
                System.out.println(line);
            }

        } catch ( FileNotFoundException ex ) {
            //exception Handling
        } catch ( IOException ex ) {
            //exception Handling
        }*/

        //System.out.println();
        System.out.println(text);
        System.out.println(file.toString());
        String path = file.getAbsolutePath();
        System.out.println(path);
        return path;
    }

    /**
     *
     * @param content
     * @param filename
     * @throws IOException
     */
    private void writeFile(byte[] content, String filename) throws IOException {

        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
    }

    /*@POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {

        String fileName = "";

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {

            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileName = getFileName(header);

                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                byte[] bytes = IOUtils.toByteArray(inputStream);

                // constructs upload file path
                fileName = UPLOADED_FILE_PATH + fileName;

                writeFile(bytes, fileName);

                System.out.println("Done");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();

    }*/

}