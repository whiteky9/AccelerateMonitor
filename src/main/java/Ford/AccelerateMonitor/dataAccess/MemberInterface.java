package Ford.AccelerateMonitor.dataAccess;

import Ford.AccelerateMonitor.model.Member;

import java.util.List;

public interface MemberInterface {

    int insertMember(Member member);
    List<Member> getAllMembers();
    Member getMember(int id);
    void deleteMember(int id);
}
