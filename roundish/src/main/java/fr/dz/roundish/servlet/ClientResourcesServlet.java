package fr.dz.roundish.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Cleanup;

import org.apache.commons.io.IOUtils;

import fr.dz.roundish.util.FileNameMap;

/**
 * Servlet for client resources (js client scripts for example)
 */
@WebServlet(name = "roundish-clientresources", urlPatterns = "/roundish/*")
public class ClientResourcesServlet extends HttpServlet {

    private static final long serialVersionUID = 8424935205916309929L;

    // Constants
    private static final String CLIENT_RESOURCES_FOLDER = "/client";

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
	    throws ServletException, IOException {

	// Gets the static file name
	String fileName = CLIENT_RESOURCES_FOLDER + request.getPathInfo();
	@Cleanup
	InputStream resource = ClientResourcesServlet.class.getResourceAsStream(fileName);

	// Returns the file content
	if (resource != null) {

	    // Sets the Mime type
	    FileNameMap fileNameMap = new FileNameMap();
	    String mimeType = fileNameMap.getContentTypeFor(fileName);

	    // Text
	    if (mimeType.startsWith("text/")) {
		response.setContentType(mimeType + "; charset=UTF-8");
		IOUtils.copy(resource, response.getWriter(), "UTF-8");
	    }
	    // Binary
	    else {
		response.setContentType(mimeType);
		IOUtils.copy(resource, response.getOutputStream());
	    }
	} else {
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
    }
}