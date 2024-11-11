package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MethodTest extends NsTest {
    @Test
    public void testWelcome() {
        InputView.welcome();
        assertThat(output()).contains(
                "안녕하세요. W편의점입니다.",
                "현재 보유하고 있는 상품입니다.",
                "- 콜라 1,000원 10개 탄산2+1",
                "- 콜라 1,000원 10개",
                "- 사이다 1,000원 8개 탄산2+1",
                "- 사이다 1,000원 7개",
                "- 오렌지주스 1,800원 9개 MD추천상품",
                "- 탄산수 1,200원 5개 탄산2+1",
                "- 물 500원 10개",
                "- 비타민워터 1,500원 6개",
                "- 감자칩 1,500원 5개 반짝할인",
                "- 감자칩 1,500원 5개",
                "- 초코바 1,200원 5개 MD추천상품",
                "- 초코바 1,200원 5개",
                "- 에너지바 2,000원 5개",
                "- 정식도시락 6,400원 8개",
                "- 컵라면 1,700원 1개 MD추천상품",
                "- 컵라면 1,700원 10개");
    }

    @Test
    public void testReadItem() { // using with System.out.println(item); in Application.java
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]");
            Assertions.assertThat(output().replaceAll("\\s", "")).contains("[비타민워터-3],[물-2],[정식도시락-2]");
        });
    }


    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
