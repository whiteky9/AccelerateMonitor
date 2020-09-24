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

    @PostMapping("/addMember")
    public void addMember(@RequestBody Member member) { memberService.addMember(member); }

    @GetMapping("/getAllMembers")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping(path = "/getMember/{id}")
    public Member getMember(@PathVariable("id") int id){ return memberService.getMember(id); }

    @DeleteMapping(path = "/deleteMember/{id}")
    public void deleteMember(@PathVariable("id") int id){ memberService.deleteMember(id); }

    @PutMapping(path = "/updateMember/{id}")
    public void updateMember(@PathVariable("id") int id, @RequestBody Member member) { memberService.updateMember(id, member);}

}