package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Member;

import java.util.List;
import java.util.UUID;

// Interface to allow easy implementation of additional data sources.
public interface MemberInterface {
    void insertMember(Member member);
    List<Member> getAllMembers();
    Member getMember(String id);
    void deleteMember(String id);
    void updateMember(String id, Member member);
}