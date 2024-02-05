package ajax.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cette servlet récupère un flux et le décode.
 */
@WebServlet(value = "/ServletEncode")
public class ServletEncode extends CommonServlet {

  private static final long serialVersionUID = 2561098435378867777L;

  @Override
  protected void responseGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    /*----- Lecture de la requête en UTF-8 -----*/

    /*----- Type de la réponse -----*/
    response.setContentType("application/xml;charset=UTF-8");
    response.addHeader("Access-Control-Allow-Origin", "http://localhost:5500");
    response.setCharacterEncoding("UTF-8");
    try (PrintWriter out = response.getWriter()) {
      String ch = request.getParameter("texte");

      /*----- Ecriture de la page xml -----*/
      out.println("<?xml version=\"1.0\"?>");
      out.println("<msg><![CDATA[" + ch + "]]></msg>");
    }
  }

  @Override
  protected void responsePost(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    doGet(request, response);
  }
}/*----- Fin de la servlet ServletEncode -----*/
