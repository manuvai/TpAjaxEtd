package ajax.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.bd.Bd;

@WebServlet("/ServletDelete")
public class ServletDelete extends CommonServlet {

	private static final long serialVersionUID = 3083492649861141830L;

	@Override
	protected void responseGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
	}

	@Override
	protected void responsePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String mot = request.getParameter("mot");
		
		if (Objects.isNull(mot)) {
			return;
		}
		
		int responseDelete;
		
		int responseStatus = 500;
		
		try {
			responseDelete = Bd.deleteMot(mot);
			responseStatus = 200;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseDelete = 0;
			responseStatus = 500;
		}
		String textResponse = responseDelete == 0
				? "Quelque chose a interrompu l'opération"
				: "Tout est OK";
		
		Node responseNode = new Node("response");
		
		Node statusNode = new Node("status", Integer.toString(responseStatus));
		Node messageNode = new Node("message", textResponse);
		responseNode.addNode(statusNode);
		responseNode.addNode(messageNode);


	    try (PrintWriter out = response.getWriter()) {
	      out.println("<?xml version=\"1.0\"?>");
	      out.println(responseNode.toString());
	    }
		
	}

}
