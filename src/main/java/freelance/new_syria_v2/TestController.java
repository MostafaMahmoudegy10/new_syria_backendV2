package freelance.new_syria_v2;
import freelance.new_syria_v2.article.dto.LatestNewsDto;
import freelance.new_syria_v2.article.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
public class TestController {
    @Autowired
    private ArticleRepository  articleRepository;
	private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/my-ip")
    public String getServerIp() {
        log.info("🚀 hellllllllllllo"); // استخدم Logger بدل println
        try {
            URL url = new URL("https://api.ipify.org");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = br.readLine();
            return "Server public IP: " + ip;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
//    @GetMapping("/test")
//    public Page<LatestNewsDto> test() {
//        Pageable pageable=PageRequest.of(0, 10);
////        return this.articleRepository.getArticleWithCommentCount(pageable);
//    }

}