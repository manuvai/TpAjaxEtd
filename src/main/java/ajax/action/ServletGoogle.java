package ajax.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.bd.Bd;

@WebServlet("/ServletGoogle")
public class ServletGoogle extends CommonServlet {

	private static final long serialVersionUID = -1141444653853903199L;

	protected void responseGet(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException {
		String saisie = request.getParameter("saisie");
		
	    List<String> propositions = new ArrayList<>();
		try {
			propositions = Bd.recupererPropositions(saisie);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		propositions = propositions.stream()
				.map(prop -> "<proposition><![CDATA[" + prop + "]]></proposition>")
				.collect(Collectors.toList());
	    
	    try (PrintWriter out = response.getWriter()) {
		  /*----- Ecriture de la page xml -----*/
		  out.println("<?xml version=\"1.0\"?>");
		  out.println("<propositions>");
		  out.println(String.join("", propositions));
		  out.println("</propositions>");
	    }
		
	}

	protected void responsePost(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException {
		
	}

}
