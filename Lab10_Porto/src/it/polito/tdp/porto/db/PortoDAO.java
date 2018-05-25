package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author> getAllAuthors(AuthorIdMap authorIdMap){
		final String sql = "SELECT * FROM author ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			List<Author> result = new ArrayList<>();

			while (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				result.add(authorIdMap.get(autore));
			}

			return result;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Author> getAllCoauthors(Author a) {
		final String sql = "SELECT DISTINCT a.id, a.lastname, a.firstname " + 
				"FROM creator c1, creator c2, author a " + 
				"WHERE c1.eprintid=c2.eprintid " + 
				"AND c2.authorid=a.id " + 
				"AND c1.authorid= ? " + 
				"AND a.id <> c1.authorid " + 
				"ORDER BY a.lastname ASC, a.firstname ASC ";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			ResultSet rs = st.executeQuery();
			List<Author> result = new ArrayList<>();

			while (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				result.add(autore);
			}

			return result;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public Paper getPaperFromAuthors(Author a1, Author a2) {
		final String sql = "SELECT * " + 
				"FROM paper p, creator c1, creator c2 " + 
				"WHERE p.eprintid = c1.eprintid " + 
				"AND p.eprintid = c2.eprintid " + 
				"AND c1.authorid = ? " + 
				"AND c2.authorid = ? ";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());
			ResultSet rs = st.executeQuery();
			Paper result = null ;

			if (rs.next()) {

				result = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"), rs.getString("publication"),
						rs.getString("type"), rs.getString("types"));
			
			}

			return result;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
}