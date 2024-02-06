package ajax.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajax.bd.Bd;

@WebServlet("/ServletCount")
public class ServletCount extends CommonServlet {

	private static final long serialVersionUID = 3083492649861141830L;

	@Override
	protected void responseGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// TODO Terminer l'implémentation du retour de la servlet
		// TODO Ajouter la méthode COUNT dans Bd.java /!\ Il faudra penser à nommer le COUNT
		
		Node responseNode = new Node("response");
		
		int responseStatus = 200;
		String textResponse = "";
		
		try {
			textResponse = Integer.toString(Bd.countMessages());
		} catch (ClassNotFoundException | SQLException e) {
			textResponse = "Problème lors de la récupération";
			e.printStackTrace();
		}
		
		Node statusNode = new Node("status", Integer.toString(responseStatus));
		Node messageNode = new Node("message", textResponse);
		responseNode.addNode(statusNode);
		responseNode.addNode(messageNode);


	    try (PrintWriter out = response.getWriter()) {
	      out.println("<?xml version=\"1.0\"?>");
	      out.println(responseNode.toString());
	    }
		
	}

	@Override
	protected void responsePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		responseGet(request, response);
		
	}

}
