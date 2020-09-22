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
    public MemberService(@Qualifier("trialDataAccess") MemberInterface memberInterface){ this.memberInterface = memberInterface; }

    public int addMember(Member member){ return memberInterface.insertMember(member); }

    public List<Member> getAllMembers(){
        return memberInterface.getAllMembers();
    }

    public Member getMember(int id) { return memberInterface.getMember(id); }

    public void deleteMember(int id){ memberInterface.deleteMember(id); }

}