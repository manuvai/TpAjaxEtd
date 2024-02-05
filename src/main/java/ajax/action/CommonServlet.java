package ajax.action;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class CommonServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(
		    HttpServletRequest request,
		    HttpServletResponse response) throws ServletException, IOException {
		
		xmlContent(response);
		
		responseGet(request, response);
	}
	
	protected abstract void responseGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	protected void doPost(
		    HttpServletRequest request,
		    HttpServletResponse response) throws ServletException, IOException {
		xmlContent(response);
		responsePost(request, response);
	}
	
	protected abstract void responsePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	protected void xmlContent(HttpServletResponse response) {
		if (Objects.isNull(response)) {
			return;
		}

	    response.setContentType("application/xml;charset=UTF-8");
	    response.addHeader("Access-Control-Allow-Origin", "*");
	    response.setCharacterEncoding("UTF-8");

	}

}
