import com.datou.dto.UserDTO;
import com.datou.utils.JwtUtils;
import org.junit.Test;

public class TokenTest {

    @Test
    public void test1(){
        String token = JwtUtils.createToken("55", "aa");
        System.out.println(token);
        Boolean aBoolean = JwtUtils.checkToken(token + "1");
        System.out.println(aBoolean);
        UserDTO s = JwtUtils.parseToken(token);
        System.out.println(s);
    }

}
