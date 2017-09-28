package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    BookmarkClient bookmarkClient;

    @RequestMapping(path = "/ribben", method = RequestMethod.GET)
    public String ribben(@RequestParam(value="name", required=false, defaultValue="Jack") String name) {
        System.out.println(" ribben Get method ");
        return bookmarkClient.getBookmarks(name);
    }

}
