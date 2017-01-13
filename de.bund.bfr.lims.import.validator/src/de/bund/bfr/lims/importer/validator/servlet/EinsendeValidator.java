package de.bund.bfr.lims.importer.validator.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.bund.bfr.knime.rest.client.KREST;

/**
 * Servlet implementation class EinsendeValidator
 */
public class EinsendeValidator extends HttpServlet {

	private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EinsendeValidator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";

        // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {
        	message = "Request does not contain upload data";
        }
        else {
            // configures upload settings
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(THRESHOLD_SIZE);
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
             
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(MAX_FILE_SIZE);
            upload.setSizeMax(MAX_REQUEST_SIZE);

            
            // constructs the directory path to store upload file
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY + File.separator + System.currentTimeMillis();
           // creates the directory if it does not exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
           
            try {
                // parses the request's content to extract file data
                List<?> formItems = upload.parseRequest(request);
                Iterator<?> iter = formItems.iterator();
                 
                // iterates over form's fields
                while (iter.hasNext()) {
                	DiskFileItem item = (DiskFileItem) iter.next();
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        if (fileName.trim().length() == 0) {
                        	message = "No file to validate!";
                            break;
                        }
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                         
                        String ext = getFileExtension(fileName);
                        if (!ext.equals("xls") && !ext.equals("xlsx")) {
                        	message = "The submitted file '" + fileName + "' has no correct file extension.";
                            break;
                        }

                        // saves the file on disk
                        item.write(storeFile);

                        if (storeFile == null || !storeFile.exists() || !storeFile.isFile()) {
                        	message = "There is an unknown problem with the submitted file '" + fileName + "'<BR>Please write an email to foodrisklabs@bfr.bund.de with your submitted file.";
                            break;
                        }

                    	System.out.println(uploadPath + "\t" + storeFile);
                    	
            	  		if (storeFile != null && storeFile.exists()) {
            	  	    	Map<String, Object> inputs = new HashMap<>();
            	  		    inputs.put("file-upload-211:210", storeFile);
            	  		    Map<String, Boolean> outputs = new HashMap<>(); // doStream bedeutet bei true: file download, bei false: sichtbarkeit im browser
            	  		    outputs.put("XLS-945:941", false);
            	  		    outputs.put("json-output-945:947", false);
            	  		    Map<String, String> r = new KREST().doWorkflow("ALEX/Proben-Einsendung_Web3_aaw", inputs, outputs);
            	  	    	message = r.get("json-output-945:947");      	  			
            	  		}
             
    	                //deregisterDrivers();
    	                deleteUploadDir(uploadDir);
                    }
                }
            } catch (Exception ex) {
            	message = "There was an error: " + ex.getMessage();
             }
        }
        
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        //message = readFile("/Users/arminweiser/Desktop/Desktop.csv", Charset.defaultCharset());//"dtraralla dfgd f fglkdfgl  rkgjfdkjgdlfgj dfgjfdgj dlfkkg fkgj dlkfgjd f!";
        //if (message == null || message.trim().isEmpty()) message = "success!";
        //message = message.trim().replace("\n", "<BR>");
        //System.out.println(message.length());
        out.println(message);
        out.flush();
	}
	private String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	private void deleteUploadDir(File uploadDir){
	    try {
	        for (File f : uploadDir.listFiles()) {
	            f.delete();
	        }
	        //uploadDir.delete();
	    } catch (Exception e) {
	        e.printStackTrace(System.err);
	    }
	}
	private String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
