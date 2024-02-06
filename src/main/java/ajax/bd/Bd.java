package ajax.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe en charge de la base de données.
 */
public class Bd {

  /*---------*/
  /* Données */
  /*---------*/

  /*----- Connexion -----*/
  private static Connection cx = null;

  /*----- Données de connexion -----*/
  private static final String URL = "jdbc:mysql://localhost:3307/ensg_berro";
  private static final String LOGIN = "berro";
  private static final String PASSWORD = "berro";

  /*----------*/
  /* Méthodes */
  /*----------*/

  /**
   * Crée la connexion avec la base de données.
   */
  private static void connexion() throws ClassNotFoundException, SQLException {
    if (Objects.nonNull(cx)) {
      // On n'a pas besoin de repasser car la connexion est déjà mise en place
      return;
    }
    /*----- Chargement du pilote pour la BD -----*/
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException ex) {
      throw new ClassNotFoundException(
        "Exception Bd.connexion() - Pilote MySql introuvable - " +
        ex.getMessage()
      );
    }

    /*----- Ouverture de la connexion -----*/
    try {
      Bd.cx = DriverManager.getConnection(URL, LOGIN, PASSWORD);
    } catch (SQLException ex) {
      throw new SQLException(
        "Exception Bd.connexion() - Problème de connexion à la base de données - " +
        ex.getMessage()
      );
    }
  }

  public static boolean isTexteAlreadyInTable(String saisie) throws ClassNotFoundException, SQLException {
	  boolean isTexteAlreadyInTable = Objects.nonNull(saisie) 
			  && !"".equals(saisie);
	  
	  if (isTexteAlreadyInTable) {
	      connexion();
	      String sql = "SELECT Texte " 
	    		  + "FROM Mot " 
	    		  + "WHERE Texte = ?";

	      PreparedStatement st = cx.prepareStatement(sql);
	      st.setString(1, saisie);

	      ResultSet resultSet = st.executeQuery();
	      
	      isTexteAlreadyInTable = isTexteAlreadyInTable && resultSet.next();
		  
	  }
	  
	  
	  return isTexteAlreadyInTable;
  }
  
  public static int countMessages() throws ClassNotFoundException, SQLException {
	  int responseCount = 0;
	  
	  connexion();
	  
	  String sql = "SELECT COUNT(*) AS nb_mots "
	  		+ "FROM Mot";
	  
	  PreparedStatement statement = cx.prepareStatement(sql);
	  ResultSet resultSet = statement.executeQuery();
	  
	  if (resultSet.next()) {
		  responseCount = resultSet.getInt(1);
	  }
	  
	  return responseCount;
  }
  
  public static int ajouterMessage(String saisie) throws ClassNotFoundException, SQLException {
	  int resultId = 0;
	  
	  if (Objects.nonNull(saisie)) {
		  connexion();
		  String sql = "INSERT INTO Mot (Texte) "
		  		+ "VALUES (?)";
		  PreparedStatement statement = cx.prepareStatement(sql);
		  statement.setString(1, saisie);

		  resultId = statement.executeUpdate();
		  
	  }
	  
	  return resultId;
  }

  public static int deleteMot(String mot) throws ClassNotFoundException, SQLException {
	  int result = 0;
	  
	  if (Objects.nonNull(mot)) {
		  connexion();
		  String sql = "DELETE FROM Mot "
		  		+ "WHERE Texte = ?";
		  PreparedStatement statement = cx.prepareStatement(sql);
		  statement.setString(1, mot);

		  result = statement.executeUpdate();
		  
	  }
	  
	  return result;
  }
  
  
  public static List<String> recupererPropositions(String saisie)
    throws ClassNotFoundException, SQLException {
    List<String> propositions = new ArrayList<>();

    if (Objects.nonNull(saisie) && saisie.length() > 0) {
      connexion();
      String sql = "SELECT Texte " 
    		  + "FROM Mot " 
    		  + "WHERE Texte LIKE ?";

      PreparedStatement st = Bd.cx.prepareStatement(sql);
      st.setString(1, saisie + "%");

      ResultSet resultSet = st.executeQuery();
      while (resultSet.next()) {
        propositions.add(resultSet.getString(1));
      }
    }

    return propositions;
  }

  /**
   * Retourne la liste de citations de 'nom_auteur'.
   */
  public static ArrayList<String> lireCitations(String nom_auteur)
    throws ClassNotFoundException, SQLException {
    /*----- Création de la connexion à la base de données -----*/
    if (Bd.cx == null) Bd.connexion();

    /*----- Interrogation de la base -----*/
    ArrayList<String> liste = new ArrayList<>();

    /*----- Requête SQL -----*/
    String sql =
      "SELECT LibCitation FROM Auteur, Citation WHERE Auteur.IdAuteur=Citation.AutCitation AND Auteur.NomAuteur=?";

    /*----- Ouverture de l'espace de requête -----*/
    try (PreparedStatement st = Bd.cx.prepareStatement(sql)) {
      /*----- Exécution de la requête -----*/
      st.setString(1, nom_auteur);
      try (ResultSet rs = st.executeQuery()) {
        /*----- Lecture du contenu du ResultSet -----*/
        while (rs.next()) liste.add(rs.getString(1));
      }
    } catch (SQLException ex) {
      throw new SQLException(
        "Exception Bd.lireCitations() : Problème SQL - " + ex.getMessage()
      );
    }

    return liste;
  }

  /*----------------------------*/
  /* Programme principal (test) */
  /*----------------------------*/

  public static void main(String[] s) {
    try {
      List<String> l = Bd.recupererPropositions("A");
      for (String msg : l) 
    	  System.out.println(msg);
    } catch (ClassNotFoundException | SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }
}/*----- Fin de la classe Bd -----*/
