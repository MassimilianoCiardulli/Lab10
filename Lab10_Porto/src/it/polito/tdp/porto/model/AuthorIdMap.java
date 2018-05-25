package it.polito.tdp.porto.model;

import java.util.HashMap;
import java.util.Map;

public class AuthorIdMap {
	private Map<Integer, Author> map ;
	
	public AuthorIdMap() {
		map = new HashMap<>();
	}
	public Author get(int id) {
		return map.get(id);
	}
	
	public Author get(Author author) {
		Author old = map.get(author.getId());
		if(old == null) {
			map.put(author.getId(), author);
			return author ;
		} else {
			return old ;
		}
	}
	
	public Author get(String name) {
		for(Author a:map.values()) {
			String nome = a.getLastname() + " " + a.getFirstname();
			if(name.equals(nome))
				return a;
		}
		return null ;
	}
	
	public void put(int id, Author object) {
		map.put(id, object);
	}
}
