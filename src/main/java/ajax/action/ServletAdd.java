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

@WebServlet("/ServletAdd")
public class ServletAdd extends CommonServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void responseGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String saisie = request.getParameter("saisie");
		
		String textResponse;
		
		if (Objects.isNull(saisie)) {
			textResponse = "<message statut=\"404\">Veuillez fournir un bon format de saisie</message>";
					
		} else {
			
			boolean isTexteAlreadyInTable = true;
			try {
				isTexteAlreadyInTable = Bd.isTexteAlreadyInTable(saisie);
				textResponse = isTexteAlreadyInTable 
						? "<message statut=\"403\">Ce mot existe déjà en base</message>"
						: "<message statut=\"200\">Ce mot est dispo</message>";
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				textResponse = "<message statut=\"500\">Une erreur s'est produite</message>";
			}
			
		}

	    try (PrintWriter out = response.getWriter()) {
	      out.println("<?xml version=\"1.0\"?>");
	      out.println(textResponse);
	    }
		
	}

	@Override
	protected void responsePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String saisie = request.getParameter("saisie");
		
		boolean isTexteAlreadyInTable = true;
		
		try {
			isTexteAlreadyInTable = Bd.isTexteAlreadyInTable(saisie);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		if (isTexteAlreadyInTable) {
			return;
		}
		
		int insertResponse = 0;
		
		try {
			insertResponse = Bd.ajouterMessage(saisie);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		Node responseNode = new Node("response");
		
		int responseStatus = insertResponse == 0
				? 400
				: 200;
		
		String textResponse = insertResponse == 0
				? "Un problème est survenu lors de l'enregistrement"
				: "Il a été ajouté, c'est gud";
		
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
