package ajax.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Classe en charge de la base de données.
 */
public class Bd
{
	/*---------*/
	/* Données */
	/*---------*/

	/*----- Connexion -----*/
	private static Connection cx = null;

	/*----- Données de connexion -----*/
	private static final String URL			= "jdbc:mysql://localhost:3307/ensg_berro";
	private static final String LOGIN		= "berro";
	private static final String PASSWORD	= "berro";


	/*----------*/
	/* Méthodes */
	/*----------*/

	/**
	 * Crée la connexion avec la base de données.
	 */
	private static void connexion() throws ClassNotFoundException, SQLException
		{
		/*----- Chargement du pilote pour la BD -----*/
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			}
		catch (ClassNotFoundException ex)
			{
			throw new ClassNotFoundException("Exception Bd.connexion() - Pilote MySql introuvable - " + ex.getMessage());
			}

		/*----- Ouverture de la connexion -----*/
		try {
			Bd.cx = DriverManager.getConnection(URL,LOGIN,PASSWORD);
			}
		catch (SQLException ex)
			{
			throw new SQLException("Exception Bd.connexion() - Problème de connexion à la base de données - " + ex.getMessage());
			}
		}


	/**
	 * Retourne la liste de citations de 'nom_auteur'.
	 */
	public static ArrayList<String> lireCitations (String nom_auteur) throws ClassNotFoundException, SQLException
		{
		/*----- Création de la connexion à la base de données -----*/
		if (Bd.cx == null)
			Bd.connexion();

		/*----- Interrogation de la base -----*/
		ArrayList<String> liste = new ArrayList<>();

		/*----- Requête SQL -----*/
		String sql = "SELECT LibCitation FROM Auteur, Citation WHERE Auteur.IdAuteur=Citation.AutCitation AND Auteur.NomAuteur=?";

		/*----- Ouverture de l'espace de requête -----*/
		try (PreparedStatement st = Bd.cx.prepareStatement(sql))
			{
			/*----- Exécution de la requête -----*/
			st.setString(1, nom_auteur);
			try (ResultSet rs = st.executeQuery())
				{
				/*----- Lecture du contenu du ResultSet -----*/
				while (rs.next())
					liste.add(rs.getString(1));
				}
			}
		catch (SQLException ex)
			{
			throw new SQLException("Exception Bd.lireCitations() : Problème SQL - " + ex.getMessage());
			}

		return liste;
		}


	/*----------------------------*/
	/* Programme principal (test) */
	/*----------------------------*/

	public static void main (String[] s)
		{
		try {
			ArrayList<String> l = Bd.lireCitations("Coluche");
			for (String msg : l) System.out.println(msg);
			}
		catch (ClassNotFoundException | SQLException ex)
			{
			System.out.println(ex.getMessage());
			}
		}

} /*----- Fin de la classe Bd -----*/
