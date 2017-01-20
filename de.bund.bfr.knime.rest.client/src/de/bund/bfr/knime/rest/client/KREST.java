package de.bund.bfr.knime.rest.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class KREST {

	private static final String restResource = "https://knime.bfrlab.de/com.knime.enterprise.server/rest/v4/";
	//private static final String restResource = "http://vm-knime:8095/vm-knime/rest/v4/";

	public static void main(String[] args)
			throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
		// doFileHead();
		// doUpDown();
		doOwn();
	}

	private static void doOwn() throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
		Map<String, Object> inputs = new HashMap<>();
		File f = new File("C:/Users/weiser/Desktop/Test.xlsx");
		inputs.put("file-upload-211:210", f);
		Map<String, Boolean> outputs = new HashMap<>(); // doStream bedeutet bei
														// true: file download,
														// bei false:
														// sichtbarkeit im
														// browser
		outputs.put("XLS-918:917", false);
		new KREST().doWorkflow("ALEX/Proben-Einsendung_Web2b", inputs, outputs, false);
	}

	private static void doFileHead()
			throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
		Map<String, Object> inputs = new HashMap<>();
		File f = new File("C:/Users/weiser/Desktop/Beispiel.txt");
		inputs.put("file-upload-1", f);
		inputs.put("line-count-3", "{\"integer\":1}");
		Map<String, Boolean> outputs = new HashMap<>(); // doStream bedeutet bei
														// true: file download,
														// bei false:
														// sichtbarkeit im
														// browser
		outputs.put("file-download-7", false);
		new KREST().doWorkflow("ALEX/File-HEAD-Example", inputs, outputs, false);
	}

	private static void doUpDown() throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
		Map<String, Object> inputs = new HashMap<>();
		File f = new File("C:/Users/weiser/Desktop/Test.xlsx");
		inputs.put("UploadedFile-937:5", f);
		Map<String, Boolean> outputs = new HashMap<>();
		outputs.put("XLS-894", true);
		new KREST().doWorkflow("ALEX/Upload_Download_aaw", inputs, outputs, false);
	}

	public Map<String, String> doWorkflow(String wfPath, Map<String, Object> inputs, Map<String, Boolean> outputs, boolean getJSON)
			throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
		String username = "";
		String password = "";
		InputStream in = KREST.class.getClassLoader().getResourceAsStream("/de/bund/bfr/knime/rest/client/userdata.xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(in);
		username = document.getElementsByTagName("user").item(0).getTextContent();
		password = document.getElementsByTagName("password").item(0).getTextContent();

		Map<String, String> result = null;
		Client client = ClientBuilder.newClient();
		client.register(HttpAuthenticationFeature.basic(username, password));
		client.register(MultiPartFeature.class);

		if (getJSON) result = getJobPoolResult(client, restResource, wfPath, inputs, outputs);
		else {
			boolean showSyntaxOnly = inputs.size() == 0 && outputs.size() == 0;
			String jobid = getJobID(client, restResource, "repository/" + wfPath + ":jobs", showSyntaxOnly);
			if (!showSyntaxOnly) {
				boolean success = executeJob(client, restResource, jobid, inputs);
				if (success) {
					result = getResult(client, restResource, jobid, outputs);
					System.err.println(result);
				}
				System.out.println("discardJob: " + discardJob(client, restResource, jobid));
			}
		}
		
		return result;
	}

	private Map<String, String> getJobPoolResult(Client client, String restResource, String wfPath, Map<String, Object> inputs, Map<String, Boolean> outputs)
			throws IOException, ParseException {
		Map<String, String> result = new HashMap<>();
		
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		MultiPart multipartEntity = formDataMultiPart;
		for (String param : inputs.keySet()) {
			Object o = inputs.get(param);
			if (o instanceof File) {
				File f = (File) o;
				FileDataBodyPart filePart = new FileDataBodyPart("file", f);
				filePart.setContentDisposition(FormDataContentDisposition.name(param).fileName(f.getName()).build()); // "file-upload-1"
				multipartEntity = formDataMultiPart.bodyPart(filePart);
			} else {
				multipartEntity = formDataMultiPart.field(param, inputs.get(param), MediaType.APPLICATION_JSON_TYPE); // "line-count-3"
																														// "{\"integer\":2}"
			}
		}

		Builder builder = client.target(restResource).path("repository").path(wfPath + ":job-pool")
				.request().accept(MediaType.APPLICATION_JSON);
		Response res = builder.post(Entity.entity(multipartEntity, MediaType.MULTIPART_FORM_DATA));

		String json = res.readEntity(String.class);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(json);
		JSONObject jsonObject = (JSONObject) obj;
		JSONObject ov = (JSONObject) jsonObject.get("outputValues");
		if (ov != null) {
			for (String param : outputs.keySet()) {
				JSONArray pv = (JSONArray) ov.get(param);
				if (pv != null) result.put(param, pv.toJSONString());
			}
		}
		
		res.close();
		formDataMultiPart.close();
		multipartEntity.close();

		return result;
	}

	private String discardJob(Client client, String restResource, String jobid) {
		Builder builder = client.target(restResource).path("jobs").path(jobid).request()
		// .accept(MediaType.APPLICATION_JSON)
		;
		Response res = builder.delete();
		String result = res.getStatus() + "\t" + res.readEntity(String.class);

		res.close();

		return result;
	}

	private Map<String, String> getResult(Client client, String restResource, String jobid, Map<String, Boolean> outputs)
			throws IOException {
		Map<String, String> result = new HashMap<>();
		for (String param : outputs.keySet()) {
			boolean doStream = outputs.get(param);
			Builder builder = client.target(restResource).path("jobs").path(jobid).path("output-resources").path(param) // "file-download-7"
					.request().accept(doStream ? MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_JSON);
			Response res = builder.get();

			// result += "'" + param + "':\n";
			if (doStream) {
				InputStream stream = res.readEntity(InputStream.class);
				result.put(param, "...stream mit " + stream.available() + " bytes");
				// is2File(stream, "/Users/arminweiser/Downloads/bsp_out.xls");
			} else {
				result.put(param, res.readEntity(String.class));
			}

			res.close();
		}

		return result;
	}

	private boolean executeJob(Client client, String restResource, String jobid, Map<String, Object> inputs)
			throws IOException {
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		MultiPart multipartEntity = formDataMultiPart;
		for (String param : inputs.keySet()) {
			Object o = inputs.get(param);
			if (o instanceof File) {
				File f = (File) o;
				FileDataBodyPart filePart = new FileDataBodyPart("file", f);
				filePart.setContentDisposition(FormDataContentDisposition.name(param).fileName(f.getName()).build()); // "file-upload-1"
				multipartEntity = formDataMultiPart.bodyPart(filePart);
			} else {
				multipartEntity = formDataMultiPart.field(param, inputs.get(param), MediaType.APPLICATION_JSON_TYPE); // "line-count-3"
																														// "{\"integer\":2}"
			}
		}
		/*
		 * MultiPart multipartEntity = formDataMultiPart .field("line-count-3",
		 * "{\"integer\":2}", MediaType.APPLICATION_JSON_TYPE)
		 * .bodyPart(filePart);
		 */
		Builder builder = client.target(restResource).path("jobs").path(jobid).request()
				.accept(MediaType.APPLICATION_JSON);
		Response res = builder.post(Entity.entity(multipartEntity, MediaType.MULTIPART_FORM_DATA));

		boolean result = res.getStatus() == 200;
		// System.err.println(res.readEntity(String.class));

		res.close();
		formDataMultiPart.close();
		multipartEntity.close();

		return result;
	}

	private String getJobID(Client client, String restResource, String path, boolean showSyntax) {
		String jobid = null;
		Builder builder = client.target(restResource)
				// .path("repository").path("testing").path("Alex_testing").path("AFcurrentTests").path("File-HEAD-Example:jobs")
				// // API Module Path
				.path(path).request().accept(MediaType.APPLICATION_JSON);
		Response res = builder.post(null);

		// System.err.println(res.getStatus());
		if (res.getStatus() == 201) { // succesfully created
			if (showSyntax)
				System.err.println(res.readEntity(String.class));
			res.close();

			String location = res.getHeaders().get("Location").get(0).toString();
			jobid = location.substring(location.indexOf("/jobs/") + 6);
		}
		res.close();

		return jobid;
	}

	private void is2File(InputStream is, String filename) {
		OutputStream outputStream = null;
		try {
			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(new File(filename));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
}