package Ford.AccelerateMonitor.model;

/**
 * Used to store information from a jenkins build
 */
public class Commit extends Record{

    public Commit(String date, String projectName, String user, String sha){
        super(projectName, date);
        this.user = user;
        this.sha = sha;
    }

    public Commit(){
        super();
        user = null;
        sha = null;
    }

    @Override
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    private String user;
    private String sha;
}
