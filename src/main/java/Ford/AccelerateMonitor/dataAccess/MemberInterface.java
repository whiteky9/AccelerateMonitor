package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Member;

import java.util.List;

/**
 * Interface to allow easy implementation of additional data sources.
 * in order to use a different database, create a corresponding data access class
 * which inheirits from this interface
 */
public interface MemberInterface {
    void insertMember(Member member);
    List<Member> getAllMembers();
    Member getMember(String id);
    Member getByEmail(String email);
    void deleteMember(String id);
    void updateMember(String id, Member member);
    void addTeam(String id, String team);
    void removeTeam(String id, String team);
}