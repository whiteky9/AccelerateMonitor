package Ford.AccelerateMonitor.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

/**
 * Used to parse information from
 */
public class GitHub {
    private String node_id;
    private String sha;
    private JsonNode commit;
    private String url;
    private String html_url;
    private String comments_url;
    private Map<String, Object> author;
    private Map<String, Object> committer;
    private List<Object> parents;

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public JsonNode getCommit() {
        return commit;
    }

    public void setCommit(JsonNode commit) {
        this.commit = commit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    public Map<String, Object> getAuthor() {
        return author;
    }

    public void setAuthor(Map<String, Object> author) {
        this.author = author;
    }

    public Map<String, Object> getCommitter() {
        return committer;
    }

    public void setCommitter(Map<String, Object> committer) {
        this.committer = committer;
    }

    public List<Object> getParents() {
        return parents;
    }

    public void setParents(List<Object> parents) {
        this.parents = parents;
    }
}
