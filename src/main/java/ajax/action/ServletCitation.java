package ajax.action;

import ajax.bd.Bd;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interroge la base de données et retourne la liste des citations
 * sous format XML.
 */
@WebServlet(value = "/ServletCitation")
public class ServletCitation extends HttpServlet {

  private static final long serialVersionUID = 5558284359009356382L;

  @Override
  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    /*----- Type de la réponse -----*/
    response.setContentType("application/xml;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    try (PrintWriter out = response.getWriter()) {
      /*----- Ecriture de la page XML -----*/
      out.println("<?xml version=\"1.0\"?>");
      out.println("<liste_citation>");

      /*----- Récupération des paramètres -----*/
      String nom = request.getParameter("nomauteur");

      try {
        /*----- Lecture de liste de mots dans la BD -----*/
        ArrayList<String> lCitations = Bd.lireCitations(nom);

        for (String citation : lCitations) out.println(
          "<citation>" + citation + "</citation>"
        );
      } catch (ClassNotFoundException | SQLException ex) {
        out.println("<citation>Erreur - " + ex.getMessage() + "</citation>");
      }

      out.println("</liste_citation>");
    }
  }

  @Override
  protected void doPost(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    doGet(request, response);
  }
}/*----- Fin de la servlet ServletCitation -----*/
