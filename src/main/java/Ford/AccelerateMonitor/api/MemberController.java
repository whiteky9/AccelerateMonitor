package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import Ford.AccelerateMonitor.model.Member;

import java.util.List;

@RequestMapping("members")
@RestController
public class MemberController {


    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public void addMember(@RequestBody Member member) {
        memberService.addMember(member);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }


/*    @PostMapping
    public String createMember(@RequestBody Member member ) throws InterruptedException, ExecutionException {
        return memberService.saveMemberDetails(member);
    }*/

/*
    @PutMapping("/updateMember")
    public String updateMember(@RequestBody Member member  ) throws InterruptedException, ExecutionException {
        return memberService.updateMemberDetails(member);
    }

    @DeleteMapping("/deleteMember")
    public String deleteMember(@RequestParam String name){
        return memberService.deleteMember(name);
    }
*/
}