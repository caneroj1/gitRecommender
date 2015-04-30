package gitrecommender;

import java.util.Date;
import java.util.HashMap;

import org.javalite.activejdbc.Model;

public class Repository extends Model {
	public HashMap<String, String> getLanguages() {
		return ((HashMap<String, String>) this.get("languages"));
	}
	
	public String getReadmeUrl() {
		return (String) this.get("readme_url");
	}
	
	public int getWatchers() {
		return (int) this.get("watchers");
	}
	
	public Date getPushedAt() {
		return (Date) this.get("pushed_At");
	}
	
	public String getName() {
		return (String) this.get("name");
	}
}
