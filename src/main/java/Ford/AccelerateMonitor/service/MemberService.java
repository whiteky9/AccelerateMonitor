package Ford.AccelerateMonitor.service;

import Ford.AccelerateMonitor.model.Member;
import Ford.AccelerateMonitor.dataAccess.MemberInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberService {

    private final MemberInterface memberInterface;

    @Autowired
    public MemberService(@Qualifier("memberDataAccess") MemberInterface memberInterface){ this.memberInterface = memberInterface; }

    public void addMember(Member member){ memberInterface.insertMember(member); }

    public List<Member> getAllMembers(){
        return memberInterface.getAllMembers();
    }

    public Member getMember(String id) { return memberInterface.getMember(id); }

    public Member getByEmail(String email) { return memberInterface.getByEmail(email); }

    public void deleteMember(String id){ memberInterface.deleteMember(id); }

    public void updateMember(String id, Member member){ memberInterface.updateMember(id, member); }

    public void addTeam(String id, String team){ memberInterface.addTeam(id, team); }

    public void removeTeam(String id, String team){ memberInterface.removeTeam(id, team); }

}