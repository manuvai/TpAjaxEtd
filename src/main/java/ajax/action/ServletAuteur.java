package ajax.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cette servlet retourne un flux XML.
 */
@WebServlet(value = "/ServletAuteur")
public class ServletAuteur extends HttpServlet {

  private static final long serialVersionUID = -1603520530466987632L;

  @Override
  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    /*----- Type de la réponse -----*/
    response.setContentType("application/xml;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    try (PrintWriter out = response.getWriter()) {
      out.println("<?xml version=\"1.0\"?>");

      out.println("<donnees>");
      out.println("<nuplet id=\"s1\">");
      out.println("<nom>Guitry</nom>");
      out.println("<pays>France</pays>");
      out.println("</nuplet>");
      out.println("<nuplet id=\"s2\">");
      out.println("<nom>Wilde</nom>");
      out.println("<pays>Irlande</pays>");
      out.println("</nuplet>");
      out.println("<nuplet id=\"s3\">");
      out.println("<nom>Coluche</nom>");
      out.println("<pays>France</pays>");
      out.println("</nuplet>");
      out.println("<nuplet id=\"s4\">");
      out.println("<nom>Von Goethe</nom>");
      out.println("<pays>Allemagne</pays>");
      out.println("</nuplet>");
      out.println("<nuplet id=\"s5\">");
      out.println("<nom>Churchill</nom>");
      out.println("<pays>Angleterre</pays>");
      out.println("</nuplet>");
      out.println("</donnees>");
    }
  }

  @Override
  protected void doPost(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    doGet(request, response);
  }
}/*----- Fin de la servlet ServletAuteur -----*/
