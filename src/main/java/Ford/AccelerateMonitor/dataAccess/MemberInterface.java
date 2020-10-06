package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Member;

import java.util.List;

// Interface to allow easy implementation of additional data sources.
public interface MemberInterface {
    void insertMember(Member member);
    List<Member> getAllMembers();
    Member getMember(int id);
    void deleteMember(int id);
    void updateMember(int id, Member member);
}
